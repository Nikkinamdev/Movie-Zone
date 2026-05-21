package com.example.moviezone.app.ui.movie_screen

import android.os.Bundle
import androidx.fragment.app.Fragment

import android.view.View

import com.example.moviezone.R


import android.util.Log
import android.widget.ImageView

import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

import com.example.moviezone.app.viewmodel.MovieDetailsViewModel
import com.example.moviezone.databinding.FragmentMovieDetailsBinding


class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private lateinit var viewModel: MovieDetailsViewModel

    private lateinit var progressBar: ProgressBar

    private lateinit var title: TextView
    private lateinit var tagline: TextView
    private lateinit var overview: TextView
    private lateinit var rating: TextView
    private lateinit var runtime: TextView
    private lateinit var releaseDate: TextView

    private lateinit var poster: ImageView
    private lateinit var binding: FragmentMovieDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.progressBar)
        title = view.findViewById(R.id.tvTitle)
        tagline = view.findViewById(R.id.tvTagline)
        overview = view.findViewById(R.id.tvOverview)
        rating = view.findViewById(R.id.tvRating)
        runtime = view.findViewById(R.id.tvRuntime)
        releaseDate = view.findViewById(R.id.tvRelease)

        poster = view.findViewById(R.id.ivPoster)
        binding = FragmentMovieDetailsBinding.bind(view)

        viewModel = ViewModelProvider(this)[MovieDetailsViewModel::class.java]

        val movieId = arguments?.getInt("movieId") ?: 0

        if (movieId == 0) {
            Toast.makeText(requireContext(), "Invalid Movie ID", Toast.LENGTH_SHORT).show()
            return
        }
        binding.toolbar.navigationIcon?.setTint(
            ContextCompat.getColor(requireContext(), android.R.color.white)
        )
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        viewModel.fetchMovie(movieId)
        observeData()
    }

    private fun observeData() {

        viewModel.movieDetails.observe(viewLifecycleOwner) { movie ->

            progressBar.visibility = View.GONE

            title.text = movie.title
            tagline.text = movie.tagline
            overview.text = movie.overview
            rating.text = "⭐ ${(movie.vote_average * 100).toInt() / 100.0}"
            runtime.text = "${movie.runtime} min"
            releaseDate.text = movie.release_date

            Glide.with(requireContext())
                .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
                .into(poster)


        }

        viewModel.error.observe(viewLifecycleOwner) {

            progressBar.visibility = View.GONE

            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }
}