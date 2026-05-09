package io.github.rajnishkmehta.dhwanicontrol.info

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    private var hasSavedAvatar = false
    private var isClosed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        avatarStore = DeveloperAvatarStore(applicationContext)

        binding.backButton.setOnClickListener {
            finish()
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

        loadAvatar()
    }

    override fun onDestroy() {
        isClosed = true
        avatarExecutor.shutdownNow()
        super.onDestroy()
    }

    private fun loadAvatar() {
        val savedAvatar = avatarStore.loadSavedBitmap()
        hasSavedAvatar = savedAvatar != null

        if (savedAvatar != null) {
            binding.avatarImage.setImageBitmap(savedAvatar)
            binding.avatarStatusText.setText(R.string.app_info_avatar_refreshing)
        } else {
            binding.avatarImage.setImageResource(android.R.drawable.sym_def_app_icon)
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
                        binding.avatarStatusText.setText(R.string.app_info_avatar_updated)
                    },
                    onFailure = {
                        if (hasSavedAvatar) {
                            binding.avatarStatusText.setText(R.string.app_info_avatar_failed)
                        } else {
                            binding.avatarStatusText.setText(R.string.app_info_avatar_unavailable)
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
