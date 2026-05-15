package io.github.rajnishkmehta.dhwanicontrol.info

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.databinding.ActivityAppInfoBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AppInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppInfoBinding
    private lateinit var avatarStore: DeveloperAvatarStore
    private val avatarExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())

    private var isClosed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        avatarStore = DeveloperAvatarStore(applicationContext)

        setupClickListeners()
        loadAvatar()
    }

    private fun setupClickListeners() {
        binding.openGithubRepoButton.setOnClickListener {
            openLink(getString(R.string.app_info_link_github_repo))
        }

        binding.openGitlabRepoButton.setOnClickListener {
            openLink(getString(R.string.app_info_link_gitlab_repo))
        }

        binding.openXButton.setOnClickListener {
            openLink(getString(R.string.app_info_link_x))
        }

        binding.openGithubButton.setOnClickListener {
            openLink(getString(R.string.app_info_link_github))
        }

        binding.openLinkedinButton.setOnClickListener {
            openLink(getString(R.string.app_info_link_linkedin))
        }

        binding.openDevToButton.setOnClickListener {
            openLink(getString(R.string.app_info_link_devto))
        }

        binding.appInfoHelpButton.setOnClickListener {
            openLink(getString(R.string.app_info_help_url))
        }
    }

    override fun onDestroy() {
        isClosed = true
        avatarExecutor.shutdownNow()
        super.onDestroy()
    }

    private fun loadAvatar() {
        val savedAvatar = avatarStore.loadSavedBitmap()

        if (savedAvatar != null) {
            binding.avatarImage.setImageBitmap(savedAvatar)
            binding.avatarStatusText.visibility = View.GONE
        } else {
            binding.avatarImage.setImageResource(android.R.drawable.sym_def_app_icon)
            binding.avatarStatusText.visibility = View.VISIBLE
            binding.avatarStatusText.setText(R.string.app_info_avatar_loading)
        }

        refreshAvatarInBackground()
    }

    private fun refreshAvatarInBackground() {
        val avatarUrl = getString(R.string.app_info_avatar_url)
        avatarExecutor.execute {
            val result = avatarStore.refreshAvatar(avatarUrl)

            mainHandler.post {
                if (isClosed || isFinishing || isDestroyed) {
                    return@post
                }

                result.fold(
                    onSuccess = { bitmap ->
                        binding.avatarImage.setImageBitmap(bitmap)
                        binding.avatarStatusText.visibility = View.GONE
                    },
                    onFailure = {
                        if (binding.avatarImage.drawable == null) {
                            binding.avatarStatusText.visibility = View.VISIBLE
                            binding.avatarStatusText.setText(R.string.app_info_avatar_failed)
                        } else {
                            binding.avatarStatusText.visibility = View.GONE
                        }
                    }
                )
            }
        }
    }

    private fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(this, R.string.app_info_open_link_failed, Toast.LENGTH_SHORT).show()
        }
    }
}
