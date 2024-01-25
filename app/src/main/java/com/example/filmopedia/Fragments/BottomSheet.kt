package com.example.filmopedia.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentResultListener
import com.bumptech.glide.Glide
import com.example.filmopedia.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BottomSheet : BottomSheetDialogFragment() {
    private lateinit var movieTitle : TextView
    private lateinit var releaseYear : TextView
    private lateinit var poster : ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.setFragmentResultListener("dataFromAdapter", this, object : FragmentResultListener {
            override fun onFragmentResult(requestKey: String, result: Bundle) {
                val title = result.getString("title")
                val year = result.getString("year")
                val posterLink = result.getString("poster")

                var link = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQlZSOLoJ-j_JMIlVZZAlQg3R6_y8Jvq4QNumJl1fbowA&s"
                if(posterLink != null){
                    link = posterLink
                    Log.i("posterLink", link)
                }

                movieTitle = view.findViewById(R.id.bsMovieTitle)
                releaseYear = view.findViewById(R.id.bsMovieYear)
                poster = view.findViewById(R.id.bsMoviePoster)

                movieTitle.text = title
                releaseYear.text = year
                Glide.with(requireContext())
                    .load(link)
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(poster)
            }
        })
    }
}