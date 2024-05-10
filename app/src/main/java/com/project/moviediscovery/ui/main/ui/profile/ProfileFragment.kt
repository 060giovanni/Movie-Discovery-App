package com.project.moviediscovery.ui.main.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.EmailAuthProvider
import com.project.moviediscovery.R
import com.project.moviediscovery.databinding.FragmentProfileBinding
import com.project.moviediscovery.ui.auth.AuthActivity
import com.project.moviediscovery.utils.Constants.alertDialogMessage
import com.project.moviediscovery.utils.Result
import org.koin.android.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var loadingDialog: AlertDialog

    private val viewModel: ProfileViewModel by viewModel()

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val image = result.data?.data as Uri
            val inputStream = requireActivity().contentResolver.openInputStream(image)
            val byteArray = inputStream?.readBytes()

            byteArray?.let {
                viewModel.uploadProfilePic(
                    it
                ).observe(viewLifecycleOwner) { resultTask ->
                    when (resultTask) {
                        is Result.Loading -> {
                            loadingDialog.show()
                        }

                        is Result.Success -> {
                            loadingDialog.dismiss()
                            viewModel.profilePic.postValue(image)
                            viewModel.canDeleteProfilePic.postValue(true)
                            binding.ivProfile.setImageURI(image)
                        }

                        is Result.Error -> {
                            loadingDialog.dismiss()
                            alertDialogMessage(requireContext(), resultTask.error)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val inflaterLoad: LayoutInflater = layoutInflater
        val loadingAlert = AlertDialog.Builder(requireContext())
            .setView(inflaterLoad.inflate(R.layout.custom_loading_dialog, null))
            .setCancelable(true)
        loadingDialog = loadingAlert.create()

        observeViewModel()
        setListeners()

        return binding.root
    }

    private fun observeViewModel() {
        viewModel.apply {
            profilePic.observe(viewLifecycleOwner) {
                if (it != null) {
                    Glide.with(binding.root)
                        .load(it)
                        .placeholder(
                            ContextCompat.getDrawable(
                                binding.root.context,
                                R.drawable.ic_account
                            )
                        )
                        .into(binding.ivProfile)
                } else {
                    binding.ivProfile.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_account
                        )
                    )
                }
            }

            canDeleteProfilePic.observe(viewLifecycleOwner) {
                binding.btnDeletePic.isVisible = it
            }

            getEmail().observe(viewLifecycleOwner) {
                binding.tvEmail.text = it
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            btnChangeImage.setOnClickListener {
                val iGallery = Intent()
                iGallery.action = Intent.ACTION_GET_CONTENT
                iGallery.type = "image/*"
                galleryLauncher.launch(Intent.createChooser(iGallery, "Change Profile Picture"))
            }

            btnChangePass.setOnClickListener {
                val inflaterChangePass: LayoutInflater = layoutInflater
                val loadingAlert = AlertDialog.Builder(requireContext())
                    .setView(
                        inflaterChangePass.inflate(
                            R.layout.layout_change_password_dialog,
                            null
                        )
                    )
                val changePassDialog = loadingAlert.create()

                changePassDialog.setOnShowListener {
                    val oldPassword =
                        changePassDialog.findViewById<TextInputEditText>(R.id.ed_old_password) as TextInputEditText
                    val newPassword =
                        changePassDialog.findViewById<TextInputEditText>(R.id.ed_new_password) as TextInputEditText
                    val changePassButton =
                        changePassDialog.findViewById<MaterialButton>(R.id.btn_change_password) as MaterialButton

                    changePassButton.setOnClickListener {
                        if (oldPassword.text.isNullOrEmpty() || newPassword.text.isNullOrEmpty()) {
                            alertDialogMessage(requireContext(), "Fill the field correctly!")
                        } else if (oldPassword.text.toString() == newPassword.text.toString()) {
                            alertDialogMessage(requireContext(), "Password must not same!")
                        } else {
                            changePassDialog.dismiss()
                            loadingDialog.show()

                            val oldPass = oldPassword.text.toString()
                            val newPass = newPassword.text.toString()

                            viewModel.getFirebaseCurrentUser()?.reauthenticate(
                                EmailAuthProvider.getCredential(
                                    viewModel.email,
                                    oldPass
                                )
                            )?.addOnCompleteListener {
                                loadingDialog.dismiss()
                                if (it.isSuccessful) {
                                    viewModel.getFirebaseCurrentUser()!!.updatePassword(newPass)
                                        .addOnCompleteListener { task ->
                                            if (!task.isSuccessful) {
                                                alertDialogMessage(
                                                    requireContext(),
                                                    "Something went wrong. Please try again later"
                                                )
                                            } else {
                                                alertDialogMessage(
                                                    requireContext(),
                                                    "Password Successfully Changed!"
                                                )
                                            }
                                        }
                                } else {
                                    alertDialogMessage(
                                        requireContext(),
                                        "Your Old Password is Incorrect!"
                                    )
                                }
                            }
                        }
                    }

                }

                changePassDialog.show()
            }

            btnDeletePic.setOnClickListener {
                viewModel.deleteProfilePic()
            }

            btnLogout.setOnClickListener {
                viewModel.clearPreferences()
                requireActivity().apply {
                    finish()
                    startActivity(Intent(this, AuthActivity::class.java))
                }
            }
        }
    }
}