package com.project.moviediscovery.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.moviediscovery.databinding.LayoutErrorStateBinding

class LoaderStateAdapter (private val retry: () -> Unit) :
    LoadStateAdapter<LoaderStateAdapter.LoaderStateViewHolder>() {

    override fun onBindViewHolder(holder: LoaderStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoaderStateViewHolder {
        return LoaderStateViewHolder(
            LayoutErrorStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), retry
        )
    }

    class LoaderStateViewHolder(private val binding: LayoutErrorStateBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetry.setOnClickListener {
                retry()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                if (loadState is LoadState.Error) {
                    binding.errorTxt.text = loadState.error.localizedMessage
                }

                progressBar.isVisible = loadState is LoadState.Loading
                btnRetry.isVisible = loadState is LoadState.Error
                errorTxt.isVisible = loadState is LoadState.Error
            }
        }
    }

}