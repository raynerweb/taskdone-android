package br.com.raynerweb.ipl.taskdone.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.raynerweb.ipl.taskdone.R
import br.com.raynerweb.ipl.taskdone.databinding.FragmentLoginBinding
import br.com.raynerweb.ipl.taskdone.ui.model.ValidationType
import br.com.raynerweb.ipl.taskdone.ui.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
            if (acct != null) {
                viewModel.googleLogin(
                    acct.givenName ?: "",
                    acct.email ?: "",
                    acct.photoUrl.toString()
                )
            }
        }

    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.fragment = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribe()
        setupViews()

    }

    private fun subscribe() {
        viewModel.showNameValidation.observe(viewLifecycleOwner) {
            when (it) {
                ValidationType.REQUIRED -> {
                    binding.tilEmail.error = getString(R.string.required_field)
                }
                else -> binding.tilEmail.error = null
            }
        }
        viewModel.showNameValidation.observe(viewLifecycleOwner) {
            when (it) {
                ValidationType.REQUIRED -> {
                    binding.tilName.error = getString(R.string.required_field)
                }
                else -> binding.tilName.error = null
            }
        }
        viewModel.loginSuccess.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }

    private fun setupViews() {
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.signInButton.setOnClickListener {
            GoogleSignIn.getLastSignedInAccount(requireActivity())?.let { acct ->
                viewModel.googleLogin(
                    acct.givenName ?: "",
                    acct.email ?: "",
                    acct.photoUrl.toString()
                )
            } ?: run {
                val signInIntent = mGoogleSignInClient.signInIntent
                resultLauncher.launch(signInIntent)
            }
        }
    }


    fun login(view: View) {
        viewModel.login()
    }

    companion object {
        const val TAG = "LOGIN"
    }
}