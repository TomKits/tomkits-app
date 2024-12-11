package com.neohamzah.tomkitsapp.ui.scanDisease

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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.neohamzah.tomkitsapp.R
import com.neohamzah.tomkitsapp.ViewModelFactory
import com.neohamzah.tomkitsapp.databinding.FragmentScanDiseaseBinding
import com.neohamzah.tomkitsapp.ui.detailDisease.DetailDiseaseActivity
import com.neohamzah.tomkitsapp.utils.Result
import com.neohamzah.tomkitsapp.utils.getImageUri
import com.neohamzah.tomkitsapp.utils.isNetworkAvailable
import com.neohamzah.tomkitsapp.utils.reduceFileImage
import com.neohamzah.tomkitsapp.utils.uriToFile

class ScanDiseaseFragment : Fragment() {

    private var _binding: FragmentScanDiseaseBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScanDiseaseViewModel by viewModels {
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
        _binding = FragmentScanDiseaseBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        viewModel.currentImageUri?.let { binding.ivDisease.setImageURI(it) }

        binding.btnGalery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnDiagnose.setOnClickListener { startDiagnose() }
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
            binding.ivDisease.setImageURI(it)
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
                                        val imagePath = imageFile.absolutePath
                                        val intent = Intent(requireActivity(), DetailDiseaseActivity::class.java)
                                        intent.putExtra(DetailDiseaseActivity.EXTRA_CONFIDENCE, result.data.confidence)
                                        intent.putExtra(DetailDiseaseActivity.EXTRA_DESCRIPTION, result.data.description)
                                        intent.putExtra(DetailDiseaseActivity.EXTRA_DISEASE_NAME, result.data.diseaseName)
                                        intent.putExtra(DetailDiseaseActivity.EXTRA_IMAGE, imagePath)
//                                        intent.putExtra(DetailDiseaseActivity.EXTRA_PRODUCT_LIST, result.data.productList)
                                        intent.putExtra(DetailDiseaseActivity.EXTRA_SOLUTION, result.data.solution)
                                        startActivity(intent)
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