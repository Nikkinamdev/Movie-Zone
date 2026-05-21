package com.example.moviezone.app.ui.navhostfragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezone.R
import com.example.moviezone.app.data.local.FavoriteMovie
import com.example.moviezone.app.viewmodel.HomeViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: MovieAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView = view.findViewById(R.id.searchView)
        progressBar = view.findViewById(R.id.progressBar)
        val recyclerView = view.findViewById<RecyclerView>(R.id.movieRecycler)

        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 1)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]


        adapter = MovieAdapter(emptyList()) { movie, isFav ->

            val favoriteMovie = FavoriteMovie(
                id = movie.id,
                title = movie.title,
                posterPath = movie.poster_path ?: "",
                releaseDate = movie.release_date,
                rating = movie.vote_average
            )

            if (isFav) {
                viewModel.saveFavorite(favoriteMovie)
                Toast.makeText(requireContext(), "Added ❤️", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.deleteFavorite(favoriteMovie)
                Toast.makeText(requireContext(), "Removed 💔", Toast.LENGTH_SHORT).show()
            }
        }

        recyclerView.adapter = adapter


        viewModel.movies.observe(viewLifecycleOwner) { list ->
            Log.d("UI_DEBUG", "List size: ${list.size}")
            adapter.updateData(list)
        }


        viewModel.favoriteMovies.observe(viewLifecycleOwner) { favList ->
            val favIds = favList.map { it.id }.toSet()
            adapter.updateFavorites(favIds)
        }


        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.loadMovies()

        view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
            .title = "Movies Hub"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText ?: "")
                return true
            }
        })
    }
}
