package com.souzaemerson.marvelproject.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.souzaemerson.marvelproject.data.model.Favorites
import com.souzaemerson.marvelproject.data.model.Results
import com.souzaemerson.marvelproject.data.model.converterToFavorite
import com.souzaemerson.marvelproject.databinding.CharacterItemBinding

class CharacterAdapter(
    private val results: List<Results>,
    private val itemClick: ((item: Favorites) -> Unit),
    private val longClick: ((item: Favorites) -> Unit)? = null
) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = CharacterItemBinding.inflate(view, parent, false)
        return CharacterViewHolder(binding, itemClick, longClick)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val characters = results[position]
        holder.bindView(characters)
    }

    override fun getItemCount(): Int = results.count()

    class CharacterViewHolder(
        private val binding: CharacterItemBinding,
        private val itemClick: (item: Favorites) -> Unit,
        private val longClick: ((item: Favorites) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(character: Results) {
            binding.run {
                txtNameCharacterItem.text = character.name

                Glide.with(itemView)
                    .load("${character.thumbnail.path}.${character.thumbnail.extension}")
                    .centerCrop()
                    .into(imgCharacterItem)

                itemView.setOnClickListener {
                    itemClick.invoke(converterToFavorite(character))
                }

                itemView.setOnLongClickListener {
                    longClick?.invoke(converterToFavorite(character))
                    return@setOnLongClickListener true
                }
            }
        }
    }
}