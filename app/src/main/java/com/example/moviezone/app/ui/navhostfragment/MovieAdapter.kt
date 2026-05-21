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
    private val onFavClick: (Movies, Boolean) -> Unit,
    private val onMovieClick: (Int) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var favIds = setOf<Int>()

    // THIS is the only list used for display
    private var filteredList = ArrayList(list)

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.movieTitle)
        val rating: TextView = view.findViewById(R.id.movieRating)
        val image: ImageView = view.findViewById(R.id.movieImage)
        val favBtn: ImageView = view.findViewById(R.id.favbtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_movies_item, parent, false)

        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        val item = filteredList[position]
        val rating = item.vote_average
        val truncated = (rating * 100).toInt() / 100.0

        holder.title.text = item.title
        holder.rating.text = "⭐ ${truncated}"

        val imageUrl = "https://image.tmdb.org/t/p/w500${item.poster_path}"

        Glide.with(holder.image.context)
            .load(imageUrl)
            .into(holder.image)

        val isFav = favIds.contains(item.id)

        holder.favBtn.setImageResource(
            if (isFav) R.drawable.fav_filled else R.drawable.fav
        )

        holder.favBtn.setOnClickListener {
            onFavClick(item, !isFav)
        }
        holder.itemView.setOnClickListener {
            onMovieClick(item.id)
        }
    }

    override fun getItemCount(): Int = filteredList.size

    fun updateData(newList: List<Movies>) {
        list = newList
        filteredList = ArrayList(newList)
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            ArrayList(list)
        } else {
            ArrayList(list.filter {
                it.title.contains(query, ignoreCase = true)
            })
        }
        notifyDataSetChanged()
    }

    fun updateFavorites(newFavIds: Set<Int>) {
        favIds = newFavIds
        notifyDataSetChanged()
    }
}