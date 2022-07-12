package br.com.raynerweb.ipl.taskdone.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.com.raynerweb.ipl.taskdone.R
import br.com.raynerweb.ipl.taskdone.databinding.FragmentLoginBinding
import br.com.raynerweb.ipl.taskdone.ui.model.ValidationType
import br.com.raynerweb.ipl.taskdone.ui.viewmodel.LoginViewModel
import br.com.raynerweb.ipl.taskdone.ui.viewmodel.TaskFormViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("853153103473-sq4gqrds0j8astbc9veh6t14eru04a49.apps.googleusercontent.com")

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
    }

    private fun setupViews() {
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
            if (acct != null) {
                val personName = acct.displayName
                val personGivenName = acct.givenName
                val personFamilyName = acct.familyName
                val personEmail = acct.email
                val personId = acct.id
                val personPhoto: Uri? = acct.photoUrl
            }

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            task.result.account?.let { account ->
                Log.d(TAG, account.toString())
            }
//            when (result.resultCode) {
//                RESULT_OK -> {
//                    Log.d(TAG, result.toString())
//                }
//                else -> {
//                    Log.d(TAG, result.toString())
//                }
//            }
        }

    fun login(view: View) {
        viewModel.login()
    }

    fun loginGoogle(view: View) {
//        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
//        if (acct != null) {
//            val personName = acct.displayName
//            val personGivenName = acct.givenName
//            val personFamilyName = acct.familyName
//            val personEmail = acct.email
//            val personId = acct.id
//            val personPhoto: Uri? = acct.photoUrl
//        }
        val signInIntent = mGoogleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    companion object {
        const val TAG = "LOGIN"
    }
}