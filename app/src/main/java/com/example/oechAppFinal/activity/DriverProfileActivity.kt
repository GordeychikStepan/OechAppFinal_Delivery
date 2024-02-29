package com.example.oechAppFinal.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oechAppFinal.R
import com.example.oechAppFinal.model.ChatDriver
import com.example.oechAppFinal.model.Constances
import com.example.oechAppFinal.model.CustomerReviews
import com.example.oechAppFinal.model.CustomerReviewsAdapter
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

class DriverProfileActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private var customerReviews: List<CustomerReviews> = emptyList()
    private lateinit var customerReviewsAdapter: CustomerReviewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Constances.isMapInitialized) {
            MapKitFactory.setApiKey("e6e7b570-be74-4502-9a79-da3c217e14a3")
            MapKitFactory.initialize(this)
            Constances.isMapInitialized = true
        }
        setContentView(R.layout.activity_driver_profile)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Rider profile"

        val chatDriver = intent.getSerializableExtra("chatDriver") as ChatDriver
        val profileImage = chatDriver.profileImage
        val name = chatDriver.name
        val registrationNumber = chatDriver.registrationNumber
        val stars = chatDriver.stars
        val carModel = chatDriver.carModel
        val gender = chatDriver.gender

        val profileImageView: ImageView = findViewById(R.id.profileImageView)
        val nameTextView: TextView = findViewById(R.id.nameTextView)
        val star1: ImageView = findViewById(R.id.driverStar1)
        val star2: ImageView = findViewById(R.id.driverStar2)
        val star3: ImageView = findViewById(R.id.driverStar3)
        val star4: ImageView = findViewById(R.id.driverStar4)
        val star5: ImageView = findViewById(R.id.driverStar5)
        val carModelTextView: TextView = findViewById(R.id.carModelTextView)
        val registrationNumberTextView: TextView = findViewById(R.id.registrationNumberTextView)
        val genderTextView: TextView = findViewById(R.id.genderTextView)
        val reviewsRecyclerView: RecyclerView = findViewById(R.id.reviewsRecyclerView)
        val buttonSendMessage: Button = findViewById(R.id.buttonSendMessage)
        val buttonCallRider: Button = findViewById(R.id.buttonCallRider)
        val text1: TextView = findViewById(R.id.text1)
        val text2: TextView = findViewById(R.id.text2)

        val rootLayout = findViewById<ConstraintLayout>(R.id.rootLayout)

        if (getSavedThemeState()) {
            rootLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_primary_shade1))
            nameTextView.setTextColor(resources.getColor(R.color.white))
            carModelTextView.setTextColor(resources.getColor(R.color.white))
            registrationNumberTextView.setTextColor(resources.getColor(R.color.white))
            genderTextView.setTextColor(resources.getColor(R.color.white))
            text1.setTextColor(resources.getColor(R.color.white))
            text2.setTextColor(resources.getColor(R.color.white))
        }

        val layoutManager = LinearLayoutManager(this)
        reviewsRecyclerView.layoutManager = layoutManager

        customerReviews = listOf(
            CustomerReviews(R.drawable.profile_avatar_customer_1, "Mrs Bimbo", "Right on time ", 4),
            CustomerReviews(R.drawable.profile_avatar_customer_2, "Gentle Jack", "Calls with update on location", 5),
            CustomerReviews(R.drawable.profile_avatar_3, "Raph Ron", "Thanks for your service", 5),
            CustomerReviews(R.drawable.profile_avatar_4, "Joy Ezekiel", "Thanks for your service", 0),
            CustomerReviews(R.drawable.profile_avatar_5, "Joy Ezekiel", "Thanks for your service", 1)
        )
        customerReviewsAdapter = if (getSavedThemeState()) {
            CustomerReviewsAdapter(customerReviews, true)
        } else {
            CustomerReviewsAdapter(customerReviews, false)
        }

        reviewsRecyclerView.adapter = customerReviewsAdapter


        val starViews = arrayOf(star1, star2, star3, star4, star5)
        for (i in starViews.indices) {
            if (i < stars) {
                starViews[i].setImageResource(R.drawable.ic_star_filled)
            } else {
                starViews[i].setImageResource(R.drawable.ic_star)
            }
        }

        profileImageView.setImageResource(profileImage)
        nameTextView.text = name
        carModelTextView.text = carModel
        registrationNumberTextView.text = registrationNumber
        genderTextView.text = gender

        buttonSendMessage.setOnClickListener {
            val intent = Intent(this, ChatsMessagesActivity::class.java)
            intent.putExtra("chatName", name)
            intent.putExtra("chatImage", profileImage.toString())
            startActivity(intent)
        }

        buttonCallRider.setOnClickListener {
            val intent = Intent(this, CallRiderActivity::class.java)
            intent.putExtra("chatName", name)
            intent.putExtra("chatImage", profileImage.toString())
            startActivity(intent)
        }

        mapView = findViewById(R.id.mapview)
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()

        val cameraPosition = CameraPosition(
            Point(56.838011, 60.597465),
            9f, 0f, 0f)
        mapView.map.move(
            cameraPosition
        )
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getSavedThemeState(): Boolean {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(Constances.NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Constances.KEY, false)
    }

}