package com.stepsha.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.stepsha.R

class CommentsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        supportActionBar?.hide()
    }

    companion object {
        const val ARG_BOUNDS = "ARG_BOUNDS"
    }
}
