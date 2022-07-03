package com.souzaemerson.marvelproject.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.souzaemerson.marvelproject.data.model.Results
import com.souzaemerson.marvelproject.databinding.CharacterItemBinding

class CharacterAdapter(
    private val characterList: List<Results>,
    private val itemClick: ((item: Results) -> Unit),
    private val longClick: ((item: Results) -> Unit)? = null
) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = CharacterItemBinding.inflate(view, parent, false)
        return CharacterViewHolder(binding, itemClick, longClick)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val characters = characterList[position]
        holder.bindView(characters)
    }

    override fun getItemCount(): Int = characterList.count()

    class CharacterViewHolder(
        private val binding: CharacterItemBinding,
        private val itemClick: (item: Results) -> Unit,
        private val longClick: ((item: Results) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(character: Results) {
            binding.run {
                txtNameCharacterItem.text = character.name

                Glide.with(itemView)
                    .load("${character.thumbnail.path}.${character.thumbnail.extension}")
                    .centerCrop()
                    .into(imgCharacterItem)

                itemView.setOnClickListener {
                    itemClick.invoke(character)
                }

                itemView.setOnLongClickListener {
                    longClick?.invoke(character)
                    return@setOnLongClickListener true
                }
            }
        }
    }
}