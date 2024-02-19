package com.test.audiobooktest.ui.textHighlight.view

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.test.audiobooktest.R
import com.test.audiobooktest.databinding.FragmentReadTextBinding
import com.test.audiobooktest.utilities.observe
import java.util.Locale

class ReadTextFragment : Fragment() {

    private lateinit var binding: FragmentReadTextBinding
    private lateinit var textToSpeech: TextToSpeech

    private var pausedPosition: Int = 0
    private var previousPause = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReadTextBinding.inflate(inflater, container, false)
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
    }

    private fun subscribeUi() {
        observe(binding.backBtn, findNavController()::navigateUp)
        observe(binding.playBtn) { startReading(binding.textToRead.text.toString()) }
        observe(binding.stopBtn, ::pauseReading)
    }

    private fun startReading(text: String) {
        val params = Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId")
        if (pausedPosition != 0) {
            val secondHalf = text.substring(pausedPosition)
            textToSpeech.speak(secondHalf, TextToSpeech.QUEUE_FLUSH, params,"utteranceId")
        } else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, params,"utteranceId")
        }
    }

    private fun setupTtsListener() {
        textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                // Highlight the first word or resume highlighting from paused position
                pausedPosition.let { highlightWord(it, it) }
            }

            override fun onDone(utteranceId: String?) {
                // Speech playback completed
                pausedPosition = 0
            }

            override fun onError(utteranceId: String?) {

            }

            override fun onStop(utteranceId: String?, interrupted: Boolean) {

            }

            override fun onRangeStart(utteranceId: String?, start: Int, end: Int, frame: Int) {
                // Highlight the word being read
                highlightWord(start + pausedPosition, end + pausedPosition)
                previousPause = end
            }
        })
    }

    private fun pauseReading() {
        textToSpeech.stop()
        pausedPosition += previousPause
    }

    private fun highlightWord(start: Int, end: Int) {
        val spannableString = SpannableString(binding.textToRead.text)
        val backgroundColorSpan =
            BackgroundColorSpan(ContextCompat.getColor(requireContext(), R.color.golden))
        // Clear previous highlighting
        val existingSpans = spannableString.getSpans(0, spannableString.length, Any::class.java)
        for (span in existingSpans) {
            spannableString.removeSpan(span)
        }
        // Highlight the word being read
        spannableString.setSpan(backgroundColorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.textToRead.text = spannableString
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}