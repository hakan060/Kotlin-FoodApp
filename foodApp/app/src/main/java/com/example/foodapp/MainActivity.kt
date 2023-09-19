package com.example.foodapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
}

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // menu binding section
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.food_add_menu,menu)
        // menu binding section finish
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.food_add_item) {
            val action = ListFragmentDirections.actionListFragmentToRecipeFragment()
            Navigation.findNavController(this,R.id.nav_host_fragment).navigate(action)
        }

        return super.onOptionsItemSelected(item)
    }

}

