package com.neohamzah.tomkitsapp.ui.scanQuality

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.neohamzah.tomkitsapp.R
import com.neohamzah.tomkitsapp.ViewModelFactory
import com.neohamzah.tomkitsapp.data.remote.response.UploadQualityResponse
import com.neohamzah.tomkitsapp.databinding.FragmentScanQualityBinding
import com.neohamzah.tomkitsapp.ui.detailDisease.DetailDiseaseActivity
import com.neohamzah.tomkitsapp.utils.Result
import com.neohamzah.tomkitsapp.utils.dpToPx
import com.neohamzah.tomkitsapp.utils.getImageUri
import com.neohamzah.tomkitsapp.utils.isNetworkAvailable
import com.neohamzah.tomkitsapp.utils.reduceFileImage
import com.neohamzah.tomkitsapp.utils.uriToFile

class ScanQualityFragment : Fragment() {

    private var _binding: FragmentScanQualityBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScanQualityViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("currentImageUri", viewModel.currentImageUri)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.currentImageUri = savedInstanceState?.getParcelable("currentImageUri")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanQualityBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        viewModel.currentImageUri?.let { binding.ivQuality.setImageURI(it) }

        binding.btnGalery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnDiagnose.setOnClickListener { startDiagnose() }

        viewModel.data.observe(viewLifecycleOwner) { result ->
            result?.let {
                updateUI(it)
            }
        }
    }

    private fun updateUI(result: UploadQualityResponse) {
        val params = binding.tvResultType.layoutParams as ViewGroup.MarginLayoutParams
        params.bottomMargin = dpToPx(36, requireContext())
        binding.tvResultType.layoutParams = params
        binding.tvResultQuality.visibility = View.VISIBLE
        binding.tvResultType.visibility = View.VISIBLE
        binding.tvDetailQuality.text = result.quality?.classQuality
        binding.tvDetailQualityConfidence.text = result.quality?.confidenceQuality
        binding.tvDetailType.text = result.type?.classType
        binding.tvDetailTypeConfidence.text = result.type?.confidenceType
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "Tidak Ada Media Dipilih")
        }
    }

    private fun showImage() {
        viewModel.currentImageUri?.let {
//            Log.d("Image URI", "showImage: $it")
            binding.ivQuality.setImageURI(it)
        }
    }

    private fun startCamera() {
        viewModel.currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(viewModel.currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            viewModel.currentImageUri = null
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun startDiagnose() {
        viewModel.currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
//            Log.d("Image File", "showImage: ${imageFile.path}")

            if (!isNetworkAvailable(requireContext())) {
                Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.getSession().observe(viewLifecycleOwner) {session ->
                    if (session.token.isNotEmpty()) {
                        viewModel.imageUpload(imageFile, session.token).observe(viewLifecycleOwner) { result ->
                            if (result != null) {
                                when (result) {
                                    is Result.Error -> {
                                        showLoading(false)
                                        showToast(result.error)
                                    }
                                    Result.Loading -> {
                                        showLoading(true)
                                    }
                                    is Result.Success -> {
                                        showLoading(false)
                                        showToast("Scan Berhasil!")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } ?: showToast(getString(R.string.app_name)) // image warning harusnya
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}