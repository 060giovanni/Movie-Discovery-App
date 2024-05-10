package com.project.moviediscovery.ui.search

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.project.moviediscovery.databinding.ActivitySearchBinding
import com.project.moviediscovery.ui.adapter.SearchMovieAdapter
import com.project.moviediscovery.ui.detail.DetailActivity
import com.project.moviediscovery.utils.Constants.alertDialogMessage
import org.koin.android.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private val viewModel: SearchViewModel by viewModel()
    private val searchMovieAdapter = SearchMovieAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()
        setSearchBar()
        setListeners()
        setRecyclerView()
    }

    private fun observeViewModel() {
        viewModel.apply {
            isLoading.observe(this@SearchActivity) {
                showLoading(it)
            }

            searchedMovies.observe(this@SearchActivity) {
                searchMovieAdapter.submitList(it)
                binding.emptyView.isVisible = it.isEmpty()
            }

            message.observe(this@SearchActivity) {
                alertDialogMessage(this@SearchActivity, it)
            }
        }
    }

    private fun setSearchBar() {
        binding.svAccounts.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchMovies(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setRecyclerView() {
        binding.rvMovies.apply {
            layoutManager =
                GridLayoutManager(this@SearchActivity, 2, GridLayoutManager.VERTICAL, false)
            adapter = searchMovieAdapter
        }
    }

    private fun setListeners() {
        binding.apply {
            searchMovieAdapter.onMovieClick = { id ->
                val iDetail = Intent(this@SearchActivity, DetailActivity::class.java)
                iDetail.putExtra(DetailActivity.EXTRA_MOVIE_ID, id)
                startActivity(iDetail)
            }

            btnBack.setOnClickListener { finish() }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressbar.isVisible = isLoading
    }
}