package com.souzaemerson.marvelproject.view.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.souzaemerson.marvelproject.data.model.Results
import com.souzaemerson.marvelproject.databinding.CharacterDetailsBinding

class DetailFragment : Fragment() {
    private lateinit var binding: CharacterDetailsBinding
    private lateinit var character: Results

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CharacterDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        character = arguments?.getSerializable("CHARACTER") as Results

        binding.run {

            getGlide(imgPoster)

            getGlide(imgDetail)

            txtTitleDetails.text = character.name

            txtDescriptionDetails.text = character.description

        }
    }

    private fun getGlide(image: AppCompatImageView) =
        Glide.with(this@DetailFragment)
            .load("${character.thumbnail.path}.${character.thumbnail.extension}")
            .centerCrop()
            .into(image)
}