package com.souzaemerson.marvelproject.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.souzaemerson.marvelproject.data.model.Results
import com.souzaemerson.marvelproject.databinding.CharacterItemBinding

class CharacterAdapter(private val characterList: MutableList<Results>) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    class CharacterViewHolder(private val binding: CharacterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(character: Results) {
            binding.run {
                txtNameCharacterItem.text = character.name
                val portrait = "/portrait_medium."
                Glide.with(itemView)
                    .load("${character.thumbnail.path}${portrait}${character.thumbnail.extension}")
                    .centerCrop()
                    .into(imgCharacterItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = CharacterItemBinding.inflate(view, parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val characters = characterList[position]
        holder.bindView(characters)
    }

    override fun getItemCount(): Int = characterList.count()
}