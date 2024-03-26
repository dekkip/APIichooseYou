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
        val type1Text = findViewById<TextView>(R.id.type1)
        val type2Text = findViewById<TextView>(R.id.type2)
        getNextPkm(button, image, nameText, type1Text, type2Text)
    }

    private fun getPokemon(id: Int) {

        val client = AsyncHttpClient()
        val apiUrl = "https://pokeapi.co/api/v2/pokemon/$id"
        client[apiUrl, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d("Pokemon", "response successful$json")
                val sprites = json.jsonObject.getJSONObject("sprites")
                pokemonImageURL = sprites.getString("front_default")
                Log.d("Pokemon", "pkmn image URL set")

                name = json.jsonObject.getString("name")
                Log.d("Pokemon", "Pokemon Name: $name")

                val typesArray = json.jsonObject.getJSONArray("types")
                val typesStringBuilder = StringBuilder()

                val typeObject1 = typesArray.getJSONObject(0)
                type1 = typeObject1.getJSONObject("type").getString("name")

                val typeObject2 = if (typesArray.length() > 1) {
                    typesArray.getJSONObject(1)?.getJSONObject("type")
                } else { null }
                type2 = typeObject2?.getString("name") ?: "None"
                Log.d("Pokemon", "Pokemon Types: $type1")
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

    private fun getNextPkm(button: Button, imageView: ImageView, nameTextView: TextView, type1TextView: TextView, type2TextView: TextView){
        button.setOnClickListener{

            val choice = Random.nextInt(809)
            getPokemon(choice)

            Glide.with(this)
                .load(pokemonImageURL)
                .fitCenter()
                .into(imageView)

            nameTextView.text = name
            type1TextView.text = type1
            type2TextView.text = type2
        }
    }
}
