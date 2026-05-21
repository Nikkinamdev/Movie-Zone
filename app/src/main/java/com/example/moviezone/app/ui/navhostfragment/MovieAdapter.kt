package com.example.moviezone.app.ui.navhostfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviezone.R
import com.example.moviezone.app.data.model.Movies

class MovieAdapter(
    private var list: List<Movies>,
    private val onFavClick: (Movies, Boolean) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    // ⭐ store favorite IDs
    private var favIds = setOf<Int>()

    private var filteredList = ArrayList(list)
    fun updateFavorites(newFavIds: Set<Int>) {
        favIds = newFavIds
        notifyDataSetChanged()
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title: TextView = view.findViewById(R.id.movieTitle)
        val rating: TextView = view.findViewById(R.id.movieRating)
        val image: ImageView = view.findViewById(R.id.movieImage)
        val favBtn: ImageView = view.findViewById(R.id.favbtn)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_movies_item, parent, false)

        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int
    ) {

        val item = list[position]

        holder.title.text = item.title
        holder.rating.text = "⭐ ${item.vote_average}"

        val imageUrl = "https://image.tmdb.org/t/p/w500${item.poster_path}"

        Glide.with(holder.image.context)
            .load(imageUrl)
            .into(holder.image)

        // ⭐ CHECK FAVORITE STATE
        val isFav = favIds.contains(item.id)

        if (isFav) {
            holder.favBtn.setImageResource(R.drawable.fav_filled) // RED HEART
        } else {
            holder.favBtn.setImageResource(R.drawable.fav)
        }

        holder.favBtn.setOnClickListener {

            val newState = !favIds.contains(item.id)

            onFavClick(item, newState)
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<Movies>) {
        list = newList
        notifyDataSetChanged()

    }
    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            ArrayList(list)
        } else {
            val result = list.filter {
                it.title.contains(query, ignoreCase = true)
            }
            ArrayList(result)
        }
        notifyDataSetChanged()
    }
}