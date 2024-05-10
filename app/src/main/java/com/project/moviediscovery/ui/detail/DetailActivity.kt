package com.project.moviediscovery.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.project.moviediscovery.R
import com.project.moviediscovery.databinding.ActivityDetailBinding
import com.project.moviediscovery.ui.adapter.DiscoverMovieAdapter
import com.project.moviediscovery.ui.adapter.LoaderStateAdapter
import com.project.moviediscovery.utils.Constants
import com.project.moviediscovery.utils.Constants.alertDialogMessage
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val movieId by lazy { intent.getIntExtra(EXTRA_MOVIE_ID, -1) }
    private val viewModel: DetailViewModel by viewModel {
        parametersOf(
            movieId
        )
    }

    private val movieAdapter = DiscoverMovieAdapter()
    private lateinit var loadingDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val inflaterLoad: LayoutInflater = layoutInflater
        val loadingAlert = AlertDialog.Builder(this)
            .setView(inflaterLoad.inflate(R.layout.custom_loading_dialog, null))
            .setCancelable(true)
        loadingDialog = loadingAlert.create()

        setListeners()
        observeViewModel()
        initRecyclerview()
    }

    private fun setListeners() {
        binding.apply {
            btnBack.setOnClickListener { finish() }
        }
    }

    private fun observeViewModel() {
        viewModel.apply {
            isFavorite.observe(this@DetailActivity) {
                if (it) {
                    binding.btnFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@DetailActivity,
                            R.drawable.ic_fav
                        )
                    )
                } else {
                    binding.btnFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@DetailActivity,
                            R.drawable.ic_fav_border
                        )
                    )
                }
            }

            isLoading.observe(this@DetailActivity) {
                if (it) {
                    loadingDialog.show()
                } else {
                    loadingDialog.dismiss()
                }
            }

            message.observe(this@DetailActivity) {
                val builder = AlertDialog.Builder(this@DetailActivity)
                builder.setCancelable(false)

                with(builder)
                {
                    setTitle("Gagal Mendapatkan Detail")
                    setMessage(it)
                    setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        this@DetailActivity.finish()
                    }
                    show()
                }
            }

            detailMovie.observe(this@DetailActivity) { movieDetail ->
                binding.apply {
                    Glide.with(root)
                        .load(Constants.BASE_BACKDROP_PATH + movieDetail.backdropPath)
                        .placeholder(ContextCompat.getDrawable(root.context, R.drawable.md_logo))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivBackdrop)

                    Glide.with(root)
                        .load(Constants.BASE_POSTER_PATH + movieDetail.posterPath)
                        .placeholder(ContextCompat.getDrawable(root.context, R.drawable.md_logo))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivPoster)

                    tvTitle.text = movieDetail.title
                    tvRating.text = movieDetail.voteAverage.toString()
                    tvReleaseDate.text = movieDetail.releaseDate.toString()
                    tvOverview.text = movieDetail.overview.toString()

                    btnFavorite.isVisible = true
                    btnFavorite.apply {
                        setOnClickListener {
                            viewModel.isFavorite.value?.let {
                                if (it) {
                                    viewModel.deleteFavorite()
                                    alertDialogMessage(
                                        this@DetailActivity,
                                        "Movie Dihapus Dari Favorites!"
                                    )
                                } else {
                                    viewModel.insertFavorite(
                                        movieDetail.posterPath!!,
                                        movieDetail.title!!
                                    )
                                    alertDialogMessage(
                                        this@DetailActivity,
                                        "Movie Ditambahkan Ke Favorites!"
                                    )
                                }
                            }
                        }
                    }

                    listSimilarMovies.observe(this@DetailActivity) {
                        lifecycleScope.launch {
                            movieAdapter.submitData(it)
                        }
                    }
                }
            }
        }
    }

    private fun initRecyclerview() {
        binding.apply {
            movieAdapter.onMovieClick = { id ->
                val iDetail = Intent(this@DetailActivity, DetailActivity::class.java)
                iDetail.putExtra(EXTRA_MOVIE_ID, id)
                startActivity(iDetail)
            }

            rvMovies.apply {
                layoutManager =
                    GridLayoutManager(this@DetailActivity, 2, GridLayoutManager.VERTICAL, false)
                adapter = movieAdapter.withLoadStateHeaderAndFooter(
                    header = LoaderStateAdapter { movieAdapter::retry },
                    footer = LoaderStateAdapter { movieAdapter::retry }
                )
            }
        }
    }

    companion object {
        const val EXTRA_MOVIE_ID = "extra_movie_id"
    }
}