package com.example.apiichooseyou

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.apiichooseyou.ui.theme.APIIChooseYouTheme
import okhttp3.Headers
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    var pokemonImageURL = ""
    var name = ""
    var type1 = ""
    var type2 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button =findViewById<Button>(R.id.randomButton)
        val image = findViewById<ImageView>(R.id.imageView)
        val nameText = findViewById<TextView>(R.id.pkmname)
        getNextPkm(button, image, nameText)
    }

    private fun getPokemonImageURL(id: Int) {

        val client = AsyncHttpClient()
        val apiUrl = "https://pokeapi.co/api/v2/pokemon/$id"
        client[apiUrl, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d("Pokemon", "response successful$json")
                val sprites = json.jsonObject.getJSONObject("sprites")
                pokemonImageURL = sprites.getString("front_default")
                Log.d("Pokemon", "pkmn image URL set")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Pokemon Error", errorResponse)
            }
        }]
    }

    private fun getPokemonName(id: Int, nameText: TextView) {
        val client = AsyncHttpClient()
        val apiUrl = "https://pokeapi.co/api/v2/pokemon/$id"
        client[apiUrl, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d("Pokemon", "response successful$json")
                name = json.jsonObject.getString("name")
                Log.d("Pokemon", "Pokemon Name: $name")
                nameText.text = name;
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Pokemon Error", errorResponse)
            }
        }]
    }

    private fun getNextPkm(button: Button, imageView: ImageView, nameTextView: TextView){
        button.setOnClickListener{

            val choice = Random.nextInt(809)
            getPokemonImageURL(choice)
            getPokemonName(choice, nameTextView)

            Glide.with(this)
                .load(pokemonImageURL)
                .fitCenter()
                .into(imageView)

        }
    }

}
