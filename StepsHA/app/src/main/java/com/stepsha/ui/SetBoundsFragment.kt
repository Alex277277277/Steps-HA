package com.stepsha.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.stepsha.R
import com.stepsha.ui.route.RouteInfo
import kotlinx.android.synthetic.main.fragment_set_bounds.*

class SetBoundsFragment : Fragment() {

    private val vm: SetBoundsVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_set_bounds, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        observeVm()
    }

    private fun setupListeners() {
        btSetBounds.setOnClickListener { setBounds() }
        etEndCommentId.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                setBounds()
                true
            } else false
        }
    }

    private fun setBounds() {
        vm.setBounds(
            etStartCommentId.text.toString(),
            etEndCommentId.text.toString()
        )
    }

    private fun observeVm() {
        vm.error().observe(viewLifecycleOwner, Observer(this::showError))
        vm.router().observe(viewLifecycleOwner, Observer(this::navigate))
    }

    private fun navigate(routeInfo: RouteInfo) {
        findNavController().navigate(routeInfo.routeId, routeInfo.params, routeInfo.navOptions)
    }

    private fun showError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
    }

}