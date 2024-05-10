package com.project.moviediscovery.ui.main.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.project.moviediscovery.databinding.FragmentHomeBinding
import com.project.moviediscovery.ui.adapter.DiscoverMovieAdapter
import com.project.moviediscovery.ui.adapter.LoaderStateAdapter
import com.project.moviediscovery.ui.detail.DetailActivity
import com.project.moviediscovery.ui.detail.DetailActivity.Companion.EXTRA_MOVIE_ID
import com.project.moviediscovery.ui.search.SearchActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModel()

    private val movieAdapter = DiscoverMovieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setListeners()
        initRecyclerview()
        observeViewModel()

        return binding.root
    }

    private fun setListeners() {
        binding.apply {
            movieAdapter.onMovieClick = { id ->
                val iDetail = Intent(requireActivity(), DetailActivity::class.java)
                iDetail.putExtra(EXTRA_MOVIE_ID, id)
                requireActivity().startActivity(iDetail)
            }

            searchBar.setOnClickListener {
                startActivity(
                    Intent(
                        requireActivity(),
                        SearchActivity::class.java
                    )
                )
            }
        }
    }

    private fun observeViewModel() {
        viewModel.listDiscoverMovies.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                movieAdapter.submitData(it)
            }
        }
    }

    private fun initRecyclerview() {
        binding.apply {
            rvMovies.apply {
                layoutManager =
                    GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
                adapter = movieAdapter.withLoadStateHeaderAndFooter(
                    header = LoaderStateAdapter { movieAdapter::retry },
                    footer = LoaderStateAdapter { movieAdapter::retry }
                )
            }
        }
    }
}