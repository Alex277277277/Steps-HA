package com.stepsha.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.stepsha.R
import kotlinx.android.synthetic.main.fragment_comments.*

class CommentsFragment : Fragment() {

    private val vm: CommentsVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_comments, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btLoadInitialData.setOnClickListener { vm.loadInitialComments() }

        val adapter = CommentsAdapter()

        commentsList.layoutManager = LinearLayoutManager(context)
        commentsList.adapter = adapter

        vm.comments.observe(viewLifecycleOwner, Observer(adapter::submitList))
        vm.loading().observe(viewLifecycleOwner, Observer(::renderLoading))
        vm.initialLoadingState().observe(viewLifecycleOwner, Observer(::renderState))
        vm.initialize(arguments)
    }

    private fun renderLoading(isLoading: Boolean) {
        loading.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun renderState(isInitialState: Boolean) {
        if (isInitialState) {
            commentsList.visibility = View.INVISIBLE
            btLoadInitialData.visibility = View.VISIBLE
        } else {
            commentsList.visibility = View.VISIBLE
            btLoadInitialData.visibility = View.INVISIBLE
        }
    }

}