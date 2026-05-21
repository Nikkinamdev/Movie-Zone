package com.example.moviezone.app.ui.navhostfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviezone.R
import com.example.moviezone.app.ui.adapter.FavoriteAdapter
import com.example.moviezone.app.viewmodel.HomeViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FavouriteFragment : Fragment(R.layout.fragment_favourite) {

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: FavoriteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.favmovieRecycler)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[HomeViewModel::class.java]

        adapter = FavoriteAdapter(emptyList()) { movie -> viewModel.deleteFavorite(movie) }
        recyclerView.adapter = adapter

        viewModel.favoriteMovies.observe(viewLifecycleOwner) { list ->
            adapter.updateData(list)
            
        }
    }
}