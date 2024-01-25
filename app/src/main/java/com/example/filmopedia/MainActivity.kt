package com.example.filmopedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import com.example.filmopedia.Fragments.Home
import com.example.filmopedia.Fragments.Search
import com.example.filmopedia.Fragments.Wishlist
import com.google.android.material.bottomnavigation.BottomNavigationView
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enableEdgeToEdge()
        val bottomView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout,Home())
            .commit()

        bottomView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceWithFragment(Home())
                R.id.search -> replaceWithFragment(Search())
                R.id.wishlist -> replaceWithFragment(Wishlist())
                else -> {}
            }
            true
        }
    }

    private fun replaceWithFragment(frag : Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, frag)
        fragmentTransaction.commit()
    }
}