package com.neohamzah.tomkitsapp.ui.scanDisease

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.neohamzah.tomkitsapp.ViewModelFactory
import com.neohamzah.tomkitsapp.databinding.FragmentScanDiseaseBinding
import com.neohamzah.tomkitsapp.utils.getImageUri

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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanDiseaseBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.btnGalery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
//        binding.btnDiagnose.setOnClickListener { startDiagnose() }
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
            Log.d("Image URI", "showImage: $it")
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

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}