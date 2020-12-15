package com.stepsha.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stepsha.R
import com.stepsha.entity.Comment

class CommentsAdapter() :
    PagedListAdapter<Comment, CommentsAdapter.CommentViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CommentViewHolder(parent)

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    class CommentViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.comment_item, parent, false)
    ) {

        private val id = itemView.findViewById<TextView>(R.id.tvId)
        private val name = itemView.findViewById<TextView>(R.id.tvName)
        private val email = itemView.findViewById<TextView>(R.id.tvEmail)
        private val body = itemView.findViewById<TextView>(R.id.tvBody)

        fun bindTo(comment : Comment?) {
            comment?.let {
                id.text = it.id.toString()
                name.text = it.name
                email.text = it.email
                body.text = it.body
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Comment>() {

            override fun areItemsTheSame(old: Comment, new: Comment) =
                old.id == new.id

            override fun areContentsTheSame(old: Comment, new: Comment) =
                old == new
        }
    }
}