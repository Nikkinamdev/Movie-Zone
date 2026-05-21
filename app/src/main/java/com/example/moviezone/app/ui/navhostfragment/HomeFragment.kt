package com.example.moviezone.app.ui.navhostfragment

import android.text.Editable
import android.text.TextWatcher
import android.os.Bundle

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezone.R
import com.example.moviezone.app.data.local.FavoriteMovie
import com.example.moviezone.app.viewmodel.HomeViewModel
import com.google.android.material.textfield.TextInputEditText

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: MovieAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var searchContainer: CardView
    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchEditText = view.findViewById<AppCompatEditText>(R.id.searchEditText)
        progressBar = view.findViewById(R.id.progressBar)
        val recyclerView = view.findViewById<RecyclerView>(R.id.movieRecycler)

        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 1)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]


        adapter = MovieAdapter(
            emptyList(),

            onFavClick = { movie, isFav ->

                val favoriteMovie = FavoriteMovie(
                    id = movie.id,
                    title = movie.title,
                    posterPath = movie.poster_path ?: "",
                    releaseDate = movie.release_date,
                    rating = (movie.vote_average * 100).toInt() / 100.0
                )

                if (isFav) {
                    viewModel.saveFavorite(favoriteMovie)
                    Toast.makeText(requireContext(), "Added ❤️", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.deleteFavorite(favoriteMovie)
                    Toast.makeText(requireContext(), "Removed 💔", Toast.LENGTH_SHORT).show()
                }
            },

            onMovieClick = { movieId ->

                val bundle = Bundle()
                bundle.putInt("movieId", movieId)

                findNavController().navigate(
                    R.id.movieDetailsFragment,
                    bundle
                )
            }
        )

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
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adapter.filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}
