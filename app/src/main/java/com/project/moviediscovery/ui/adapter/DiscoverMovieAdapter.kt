package com.project.moviediscovery.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.project.moviediscovery.R
import com.project.moviediscovery.data.models.MovieItem
import com.project.moviediscovery.databinding.ItemMovieGridBinding
import com.project.moviediscovery.utils.Constants

class DiscoverMovieAdapter :
    PagingDataAdapter<MovieItem, DiscoverMovieAdapter.MovieViewHolder>(Diff()) {
    var onMovieClick: ((Int) -> Unit)? = null

    override fun onBindViewHolder(viewHolder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        movie?.let {
            viewHolder.binds(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(
            ItemMovieGridBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    inner class MovieViewHolder(private val binding: ItemMovieGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binds(movieItem: MovieItem) {
            binding.apply {
                Glide.with(root)
                    .load(Constants.BASE_POSTER_PATH + movieItem.posterPath)
                    .placeholder(ContextCompat.getDrawable(root.context, R.drawable.md_logo))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivMovie)
                tvMovie.text = movieItem.title

                movieCard.setOnClickListener {
                    onMovieClick?.invoke(movieItem.id)
                }
            }
        }
    }

    class Diff : DiffUtil.ItemCallback<MovieItem>() {
        override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: MovieItem,
            newItem: MovieItem
        ): Boolean =
            oldItem == newItem
    }
}
