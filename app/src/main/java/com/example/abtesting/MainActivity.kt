package com.example.abtesting

import android.graphics.Color
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.abtesting.databinding.ActivityMainBinding
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

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

        /**
         * Generate Instance Token
         */
//        FirebaseInstallations.getInstance().getToken(/* forceRefresh */ true)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Log.d("Installations", "Installation auth token: " + task.result?.token)
//                    } else {
//                        Log.e("Installations", "Unable to get Installation auth token")
//                    }
//                }

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
            binding.progressBar.visibility = VISIBLE
            firebaseRemoteConfig!!.fetch(0)
                    .addOnCompleteListener(this@MainActivity){ task ->
                        if(task.isSuccessful){
                            firebaseRemoteConfig!!.fetchAndActivate()
                            binding.progressBar.visibility = INVISIBLE
                            binding.textHead.text = firebaseRemoteConfig!!.getString("text")
                            binding.mainBackground.setBackgroundColor(Color.parseColor(firebaseRemoteConfig!!.getString("background")))
                            if (flag == 1) {
                                binding.myImage.setImageResource(R.drawable.img)
                                flag = 0
                            }else{
                                Glide.with(this).load(firebaseRemoteConfig!!.getString("image_link")).into(binding.myImage);
                                flag = 1
                            }

                        }

                    }
        }

        /**
         * JSON PARAMETER
         */
//        binding.changeImageButton.setOnClickListener{
//            binding.progressBar.visibility = VISIBLE
//            firebaseRemoteConfig!!.fetch(0)
//                    .addOnCompleteListener(this@MainActivity){task->
//                        if(task.isSuccessful){
//                            firebaseRemoteConfig!!.fetchAndActivate()
//                            binding.progressBar.visibility = INVISIBLE
//                            val model = Gson().fromJson<Model>(firebaseRemoteConfig!!.getString("update_data"), object:
//                                    TypeToken<Model>(){}.type)
//                            binding.textHead.text = model.text
//                            binding.mainBackground.setBackgroundColor(Color.parseColor(model.background.toString()))
//                            if (flag == 1) {
//                                binding.myImage.setImageResource(R.drawable.img)
//                                flag = 0
//                            }else{
//                                Glide.with(this).load(model.image_link).into(binding.myImage);
//                                flag = 1
//                            }
//
//                        }
//                    }
//        }


    }
}