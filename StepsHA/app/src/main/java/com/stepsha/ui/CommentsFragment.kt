package com.stepsha.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.stepsha.R
import kotlinx.android.synthetic.main.fragment_comments.*

class CommentsFragment : Fragment() {

    private val vm: CommentsVM by viewModels()

    private lateinit var adapter: CommentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_comments, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        observeVm()
    }

    private fun setupUi() {
        btLoadInitialData.setOnClickListener { vm.loadInitialComments() }
        btCancelLoading.setOnClickListener { vm.cancelLoading() }

        adapter = CommentsAdapter()
        with(commentsList) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@CommentsFragment.adapter
        }
    }

    private fun observeVm() {
        vm.comments.observe(viewLifecycleOwner, Observer(adapter::submitList))
        vm.loadingState().observe(viewLifecycleOwner, Observer(::renderLoadingState))
        vm.error().observe(viewLifecycleOwner, Observer(this::showError))
        vm.initialize(arguments)
    }

    private fun renderLoadingState(state: CommentsVM.LoadingState) {
        when(state) {
            CommentsVM.LoadingState.INITIALIZED -> {
                loading.visibility = View.INVISIBLE
                btLoadInitialData.visibility = View.VISIBLE
                btCancelLoading.visibility = View.INVISIBLE
                commentsList.visibility = View.INVISIBLE
            }
            CommentsVM.LoadingState.INITIAL_LOADING -> {
                loading.visibility = View.VISIBLE
                btLoadInitialData.visibility = View.INVISIBLE
                btCancelLoading.visibility = View.VISIBLE
                commentsList.visibility = View.INVISIBLE
            }
            CommentsVM.LoadingState.LOADING -> {
                loading.visibility = View.VISIBLE
                btLoadInitialData.visibility = View.INVISIBLE
                btCancelLoading.visibility = View.INVISIBLE
                commentsList.visibility = View.VISIBLE
            }
            CommentsVM.LoadingState.IDLE -> {
                loading.visibility = View.INVISIBLE
                btLoadInitialData.visibility = View.INVISIBLE
                btCancelLoading.visibility = View.INVISIBLE
                commentsList.visibility = View.VISIBLE
            }
        }
    }

    private fun showError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
    }

}