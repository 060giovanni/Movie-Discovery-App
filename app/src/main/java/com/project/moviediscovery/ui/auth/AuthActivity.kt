package com.project.moviediscovery.ui.auth

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.project.moviediscovery.R
import com.project.moviediscovery.databinding.ActivityAuthBinding
import com.project.moviediscovery.ui.auth.login.LoginFragment
import com.project.moviediscovery.ui.main.MainActivity
import com.project.moviediscovery.ui.search.SearchViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeLogin()

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.auth_container, LoginFragment())
                .commit()

            setupPlayAnimation()
        }
    }

    private fun observeLogin() {
        viewModel.getIsLoggedIn().observe(this) {
            if (it) {
                finish()
                startActivity(Intent(this@AuthActivity, MainActivity::class.java))
            }
        }
    }

    @SuppressLint("Recycle")
    private fun setupPlayAnimation() {
        val authContainer: Animator =
            ObjectAnimator.ofFloat(binding.authContainer, View.ALPHA, 1f).setDuration(1500)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(authContainer)
        animatorSet.startDelay = 500
        animatorSet.start()
    }
}
