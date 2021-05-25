package com.example.abtesting

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.abtesting.databinding.ActivityMainBinding
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    var firebaseRemoteConfig: FirebaseRemoteConfig? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder().build()
        firebaseRemoteConfig!!.setConfigSettingsAsync(configSettings)

        val defaultValue = HashMap<String,Any>()
        defaultValue["update_data"] = "{\"text\":\"This is DEFAULT\",\"enabled\":false,\"image_link\":\"https://images.pexels.com/photos/1231265/pexels-photo-1231265.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260\"}"
        firebaseRemoteConfig!!.setDefaultsAsync(defaultValue)


        binding.myButton.setOnClickListener{
            val editText = binding.myEditText.text.toString()
            binding.plainText.text = editText
            if (editText.isNotEmpty()) {
                binding.plainText.setBackgroundColor(Color.parseColor("#80FFFFFF"))
            } else
                binding.plainText.setBackgroundColor(Color.parseColor("#00FFFFFF"))

        }

        var flag = 0

        binding.changeImageButton.setOnClickListener{
            firebaseRemoteConfig!!.fetch(0)
                .addOnCompleteListener(this@MainActivity){task->
                    if(task.isSuccessful){
                        firebaseRemoteConfig!!.fetchAndActivate()
                        val model = Gson().fromJson<Model>(firebaseRemoteConfig!!.getString("update_data"), object:
                            TypeToken<Model>(){}.type)
                        binding.textHead.text = model.text
                        Glide.with(this).load(model.image_link).into(binding.myImage);
                        if (flag == 1) {
                            binding.myImage.setImageResource(R.drawable.img)
                            flag = 0
                        }else{
                            Glide.with(this).load("https://images.pexels.com/photos/1231265/pexels-photo-1231265.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260").into(binding.myImage);
                            flag = 1
                        }

                    }
                }



        }


    }
}