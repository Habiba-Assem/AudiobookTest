package com.test.audiobooktest.ui.startScreen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.test.passkeyapp.databinding.FragmentStartUpScreenBinding
import com.test.passkeyapp.utilities.observe

class StartUpScreenFragment : Fragment() {

    private lateinit var binding : FragmentStartUpScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStartUpScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        subscribeUi()
    }

    private fun initUi(){

    }

    private fun subscribeUi(){
        observe(binding.readTextBtn, ::navigateToReadText)
        observe(binding.playAudioBtn, ::navigateToPlayAudio)
    }

    //navigation
    private fun navigateToReadText(){
        findNavController().navigate(StartUpScreenFragmentDirections.actionStartUpScreenFragmentToReadTextFragment())
    }

    private fun navigateToPlayAudio(){
        findNavController().navigate(StartUpScreenFragmentDirections.actionStartUpScreenFragmentToAudioPlayerFragment())
    }
}