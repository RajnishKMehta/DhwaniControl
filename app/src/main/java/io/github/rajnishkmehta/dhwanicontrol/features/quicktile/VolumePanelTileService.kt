package io.github.rajnishkmehta.dhwanicontrol.features.quicktile

import android.media.AudioManager
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import io.github.rajnishkmehta.dhwanicontrol.R

class VolumePanelTileService : TileService() {

    private val audioManager by lazy {
        getSystemService(AUDIO_SERVICE) as AudioManager
    }

    override fun onStartListening() {
        super.onStartListening()

        qsTile?.apply {
            label = getString(R.string.quick_tile_label)
            state = Tile.STATE_ACTIVE
            updateTile()
        }
    }

    override fun onClick() {
        super.onClick()

        unlockAndRun {
            runCatching {
                audioManager.adjustVolume(AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI)
            }
        }
    }
}
