package com.project.moviediscovery.ui.main.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.project.moviediscovery.data.models.FavoritesEntity
import com.project.moviediscovery.databinding.FragmentFavoriteBinding
import com.project.moviediscovery.ui.adapter.FavoriteMovieAdapter
import com.project.moviediscovery.ui.detail.DetailActivity
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModel()
    private val favoriteMovieAdapter = FavoriteMovieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        initRecyclerview()
        observeViewModel()

        return binding.root
    }

    private fun initRecyclerview() {
        binding.apply {
            favoriteMovieAdapter.onMovieClick = { id ->
                val iDetail = Intent(requireActivity(), DetailActivity::class.java)
                iDetail.putExtra(DetailActivity.EXTRA_MOVIE_ID, id)
                requireActivity().startActivity(iDetail)
            }

            rvMovies.apply {
                layoutManager =
                    GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
                adapter = favoriteMovieAdapter
            }
        }
    }

    private fun observeViewModel() {
        viewModel.getAllFavorite().observe(viewLifecycleOwner) {
            favoriteMovieAdapter.submitList(it as ArrayList<FavoritesEntity>)
            binding.emptyView.isVisible = it.isEmpty()
        }
    }
}