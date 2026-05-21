package com.example.moviezone.app.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviezone.R
import com.example.moviezone.app.data.local.FavoriteMovie

class FavoriteAdapter(
    private var list: List<FavoriteMovie>, private val onDeleteClick: (FavoriteMovie) -> Unit,
    private val onMovieClick: (Int) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavViewHolder>() {

    class FavViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val image: ImageView = view.findViewById(R.id.movieImage)
        val title: TextView = view.findViewById(R.id.movieTitle)
        val rating: TextView = view.findViewById(R.id.movieRating)
        val genre: TextView = view.findViewById(R.id.movieGenre)
        val favRemove: ImageView=view.findViewById(R.id.favbtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fav_movie_items, parent, false)

        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {

        val item = list[position]

        holder.title.text = item.title
        holder.rating.text = "⭐ ${item.rating}"
        holder.genre.text = "Saved Movie"

        val imageUrl = "https://image.tmdb.org/t/p/w500${item.posterPath}"

        Glide.with(holder.image.context)
            .load(imageUrl)
            .into(holder.image)
        holder.favRemove.setOnClickListener {
            onDeleteClick(item)
        }
        holder.itemView.setOnClickListener {
            onMovieClick(item.id)
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<FavoriteMovie>) {
        list = newList
        notifyDataSetChanged()
    }
}