package com.learn.storyappbyoby.view.upload_story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.learn.storyappbyoby.R
import com.learn.storyappbyoby.data.ResultState
import com.learn.storyappbyoby.databinding.ActivityUploadStoryBinding
import com.learn.storyappbyoby.view.ViewModelFactory
import com.learn.storyappbyoby.view.custom.MyButton
import com.learn.storyappbyoby.view.custom.MyEditTextDescription
import com.learn.storyappbyoby.view.main.MainActivity
import com.learn.storyappbyoby.view.main.MainViewModel
import com.learn.storyappbyoby.view.utils.getImageUri
import com.learn.storyappbyoby.view.utils.reduceFileImage
import com.learn.storyappbyoby.view.utils.uriToFile
import com.learn.storyappbyoby.view.welcome.WelcomeActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class UploadStoryActvity : AppCompatActivity() {

    //private lateinit var binding: ActivityUploadStoryBinding

    private var _binding: ActivityUploadStoryBinding? = null
    private val binding get() = _binding

    private var currentImageUri : Uri? = null

    private lateinit var myButton: MyButton

    private lateinit var myEditTextDescription : MyEditTextDescription

//    private lateinit var uploadStoryViewModel : UploadStoryViewModel
        private val uploadStoryViewModel by viewModels<UploadStoryViewModel> { ViewModelFactory.getInstance(this) }



    //permisision toast
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, getString(R.string.permission_toast_denied), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, getString(R.string.permission_toast_granted), Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUploadStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        myButton = findViewById(R.id.uploadButton)
        myEditTextDescription = findViewById(R.id.description_edit_text)

        uploadStoryViewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
            else {
                binding?.uploadButton?.setOnClickListener{ uploadImage(user.token) }
            }
        }

       //galeri
        binding?.fabGaleryButtonUpload?.setOnClickListener { startGallery() }
        binding?.fabCameraButtonUpload?.setOnClickListener { startCamera() }

        //permission
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }




        //back button main
        binding?.fabBackMain?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            }

        setupView()
        setMyButtonEnable(myEditTextDescription)

//        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
//        uploadStoryViewModel = ViewModelProvider(this, factory)[UploadStoryViewModel::class.java]

        myEditTextDescription.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable(myEditTextDescription)
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })

//        //panggil fungsi dari view model
//        uploadStoryViewModel.uploadResponse.observe(this) {
//            when (it) {
//                is ResultState.Loading -> {
//                    showLoading(true)
//                }
//                is ResultState.Success -> {
//                    showLoading(false)
//                    AlertDialog.Builder(this).apply {
//                        setTitle(getString(R.string.expression_first))
//                        setMessage(getString(R.string.message_upload))
//                        setCancelable(false)
//                        setPositiveButton(getString(R.string.bottom_next_signup_login)) { _, _ ->
//                            val intent = Intent(context, MainActivity::class.java)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                            startActivity(intent)
//                            finish()
//                        }
//                        create()
//                        show()
//                    }
//                }
//                is ResultState.Error -> {
//                    showLoading(false)
//                }
//
//                else -> {
//                    showToast(message = "")
//
//                }
//            }
//        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    //tampilkan galeri
    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    //start camerea
    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    //action camerea
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    //action galeri
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", getString(R.string.toast_empty_image))
        }
    }

    //pengecekan uri
    private fun uploadImage(token: String) {
       /* val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        uploadStoryViewModel = ViewModelProvider(this, factory)[UploadStoryViewModel::class.java]*/

        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding?.descriptionEditText?.text.toString()

            showLoading(true)

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            uploadStoryViewModel.uploadStory(token,multipartBody,requestBody)
            uploadStoryViewModel.isLoading.observe(this){
                binding?.progressIndicator?.show()
            }


//            uploadStoryViewModel.getSession().observe(this){ user ->
//                token =user.token
//                uploadStoryViewModel.uploadStory(token,multipartBody,requestBody)
//            }
        } ?: showToast(getString(R.string.toast_empty_image))

        //panggil fungsi dari view model
        uploadStoryViewModel.uploadStory.observe(this) {
            when (it) {
                is ResultState.Loading -> {
                    showLoading(true)
                }
                is ResultState.Success -> {
                    showLoading(false)
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.expression_first))
                        setMessage(getString(R.string.message_upload_acc))
                        setCancelable(false)
                        setPositiveButton(getString(R.string.bottom_next_signup_login)) { _, _ ->
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                }
                is ResultState.Error -> {
                    showLoading(false)
                }

                else -> {
                    showToast(message = "")

                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressIndicator?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    //request permission
    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    //tampilkan hasil gambar
    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding?.imageUpload?.setImageURI(it)
        }
    }

    private fun setMyButtonEnable(editText: MyEditTextDescription) {
        val result = editText.text
        myButton.isEnabled = result != null && result.toString().isNotEmpty()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

}