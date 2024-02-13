package com.test.audiobooktest.ui.textHighlight.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.test.passkeyapp.databinding.FragmentReadTextBinding
import com.test.passkeyapp.utilities.observe

class ReadTextFragment: Fragment() {

    private lateinit var binding : FragmentReadTextBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentReadTextBinding.inflate(inflater, container, false)
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
        observe(binding.backBtn, findNavController()::navigateUp)
    }


}