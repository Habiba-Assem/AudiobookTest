package com.test.audiobooktest.ui.audioPlayer.view

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.test.audiobooktest.R
import com.test.audiobooktest.databinding.FragmentAudioPlayerBinding
import com.test.audiobooktest.utilities.observe
import java.util.Locale

class AudioPlayerFragment : Fragment() {

    private lateinit var binding: FragmentAudioPlayerBinding

    private lateinit var textToSpeech: TextToSpeech
    private var pausedPosition: Int = 0
    private var previousPause = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        subscribeUi()
    }

    private fun initUi() {
        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.US
            } else {
                Log.e("TextToSpeech", "Initialization failed")
            }
        }
        setupTtsListener()
        binding.seekBar.max = getString(R.string.dummy_text).length
    }

    private fun subscribeUi() {
        observe(binding.backBtn, findNavController()::navigateUp)
        observe(binding.playBtn) { startReading(getString(R.string.dummy_text)) }
        observe(binding.pauseBtn, ::pausePlayback)
    }

    private fun setupTtsListener() {
        textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
            }

            override fun onDone(utteranceId: String?) {
                // Speech playback completed
                pausedPosition = 0
                binding.seekBar.progress=0
            }

            override fun onError(utteranceId: String?) {

            }

            override fun onStop(utteranceId: String?, interrupted: Boolean) {

            }

            override fun onRangeStart(utteranceId: String?, start: Int, end: Int, frame: Int) {
                previousPause = end
                binding.seekBar.progress= pausedPosition + start
            }
        })
    }

    private fun startReading(text: String) {
        val params = hashMapOf(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID to "utteranceId")
        if (pausedPosition != 0) {
            val secondHalf = text.substring(pausedPosition)
            textToSpeech.speak(secondHalf, TextToSpeech.QUEUE_FLUSH, params)
        } else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, params)
        }
    }

    private fun pausePlayback() {
        textToSpeech.stop()
        pausedPosition += previousPause
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}