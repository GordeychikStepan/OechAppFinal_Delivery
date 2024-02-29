package com.example.oechAppFinal.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.oechAppFinal.R
import com.example.oechAppFinal.activity.ReceiptActivity
import com.example.oechAppFinal.databinding.FragmentTrackBinding
import com.example.oechAppFinal.model.Constances
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

class TrackFragment : Fragment() {

    private lateinit var track: FragmentTrackBinding
    private lateinit var mapView: MapView

    private lateinit var courierRequestedCheckBox: ImageView
    private lateinit var packageReadyCheckBox: ImageView
    private lateinit var packageInTransitCheckBox: ImageView
    private lateinit var packageDeliveredCheckBox: ImageView

    private lateinit var courierRequestedTextView: TextView
    private lateinit var packageReadyTextView: TextView
    private lateinit var packageInTransitTextView: TextView
    private lateinit var packageDeliveredTextView: TextView

    private lateinit var textView: TextView
    private lateinit var courierRequestedData1TextView: TextView
    private lateinit var courierRequestedData2TextView: TextView
    private lateinit var packageReadyData1TextView: TextView
    private lateinit var packageReadyData2TextView: TextView
    private lateinit var packageInTransitData1TextView: TextView
    private lateinit var packageInTransitData2TextView: TextView
    private lateinit var packageDeliveredData1TextView: TextView
    private lateinit var packageDeliveredData2TextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        track = FragmentTrackBinding.inflate(inflater, container, false)
        mapView = track.root.findViewById(R.id.mapview)

        textView = track.root.findViewById(R.id.textView)
        courierRequestedCheckBox = track.root.findViewById(R.id.courierRequestedCheckBox)
        packageReadyCheckBox = track.root.findViewById(R.id.packageReadyCheckBox)
        packageInTransitCheckBox = track.root.findViewById(R.id.packageInTransitCheckBox)
        packageDeliveredCheckBox = track.root.findViewById(R.id.packageDeliveredCheckBox)

        courierRequestedTextView = track.root.findViewById(R.id.courierRequestedTextView)
        packageReadyTextView = track.root.findViewById(R.id.packageReadyTextView)
        packageInTransitTextView = track.root.findViewById(R.id.packageInTransitReadyTextView)
        packageDeliveredTextView = track.root.findViewById(R.id.packageDeliveredTextView)

        courierRequestedData1TextView = track.root.findViewById(R.id.courierRequestedData1TextView)
        courierRequestedData2TextView = track.root.findViewById(R.id.courierRequestedData2TextView)
        packageReadyData1TextView = track.root.findViewById(R.id.packageReadyData1TextView)
        packageReadyData2TextView = track.root.findViewById(R.id.packageReadyData2TextView)
        packageInTransitData1TextView = track.root.findViewById(R.id.packageInTransitData1TextView)
        packageInTransitData2TextView = track.root.findViewById(R.id.packageInTransitData2TextView)
        packageDeliveredData1TextView = track.root.findViewById(R.id.packageDeliveredData1TextView)
        packageDeliveredData2TextView = track.root.findViewById(R.id.packageDeliveredData2TextView)

        return track.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        track.viewPackageInfoButton.setOnClickListener {
            val intent = Intent(this@TrackFragment.requireContext(), ReceiptActivity::class.java)
            intent.putExtra(Constances.CODE, Constances.TWO)
            startActivity(intent)
        }

        if (getSavedThemeState())
        {
            track.root.setBackgroundColor(resources.getColor(R.color.blue_primary_shade1))
            textView.setTextColor(resources.getColor(R.color.white))
        }

        //updateCourierRequestedCheckBox() // курьер
        //updatePackageReadyCheckBox() // готовность
        updatePackageInTransitCheckBox() // транзит
        //updatePackageDeliveredCheckBox() // заказ доставлен
        //updatePackageAllDeliveredCheckBox() // все чекбоксы
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Constances.isMapInitialized) {
            MapKitFactory.setApiKey("e6e7b570-be74-4502-9a79-da3c217e14a3")
            MapKitFactory.initialize(requireContext())
            Constances.isMapInitialized = true
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()

        val cameraPosition = CameraPosition(Point(56.838011, 60.597465),
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
        super.onDestroy()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    private fun updateCourierRequestedCheckBox() {
        courierRequestedCheckBox.setImageResource(R.drawable.checkbox_off)
        courierRequestedTextView.setTextAppearance(R.style.RobotoRegular14spPrimaryText)
        courierRequestedData1TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)
        courierRequestedData2TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)

        packageReadyCheckBox.setImageResource(R.drawable.checkbox_off_block)
        packageReadyTextView.setTextAppearance(R.style.RobotoRegular14spGrayText)
        packageReadyData1TextView.setTextAppearance(R.style.RobotoRegular12spGrayText)
        packageReadyData2TextView.setTextAppearance(R.style.RobotoRegular12spGrayText)

        packageInTransitCheckBox.setImageResource(R.drawable.checkbox_off_block)
        packageInTransitTextView.setTextAppearance(R.style.RobotoRegular14spGrayText)
        packageInTransitData1TextView.setTextAppearance(R.style.RobotoRegular12spGrayText)
        packageInTransitData2TextView.setTextAppearance(R.style.RobotoRegular12spGrayText)

        packageDeliveredCheckBox.setImageResource(R.drawable.checkbox_off_block)
        packageDeliveredTextView.setTextAppearance(R.style.RobotoRegular14spGrayText)
        packageDeliveredData1TextView.setTextAppearance(R.style.RobotoRegular12spGrayText)
        packageDeliveredData2TextView.setTextAppearance(R.style.RobotoRegular12spGrayText)
    }

    private fun updatePackageReadyCheckBox() {
        courierRequestedCheckBox.setImageResource(R.drawable.checkbox_on)
        courierRequestedTextView.setTextAppearance(R.style.RobotoRegular14spGrayText)
        courierRequestedData1TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)
        courierRequestedData2TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)

        packageReadyCheckBox.setImageResource(R.drawable.checkbox_off)
        packageReadyTextView.setTextAppearance(R.style.RobotoRegular14spPrimaryText)
        packageReadyData1TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)
        packageReadyData2TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)

        packageInTransitCheckBox.setImageResource(R.drawable.checkbox_off_block)
        packageInTransitTextView.setTextAppearance(R.style.RobotoRegular14spGrayText)
        packageInTransitData1TextView.setTextAppearance(R.style.RobotoRegular12spGrayText)
        packageInTransitData2TextView.setTextAppearance(R.style.RobotoRegular12spGrayText)

        packageDeliveredCheckBox.setImageResource(R.drawable.checkbox_off_block)
        packageDeliveredTextView.setTextAppearance(R.style.RobotoRegular14spGrayText)
        packageDeliveredData1TextView.setTextAppearance(R.style.RobotoRegular12spGrayText)
        packageDeliveredData2TextView.setTextAppearance(R.style.RobotoRegular12spGrayText)
    }

    private fun updatePackageInTransitCheckBox() {
        courierRequestedCheckBox.setImageResource(R.drawable.checkbox_on)
        courierRequestedTextView.setTextAppearance(R.style.RobotoRegular14spGrayText)
        courierRequestedData1TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)
        courierRequestedData2TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)

        packageReadyCheckBox.setImageResource(R.drawable.checkbox_on)
        packageReadyTextView.setTextAppearance(R.style.RobotoRegular14spGrayText)
        packageReadyData1TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)
        packageReadyData2TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)

        packageInTransitCheckBox.setImageResource(R.drawable.checkbox_off)
        packageInTransitTextView.setTextAppearance(R.style.RobotoRegular14spPrimaryText)
        packageInTransitData1TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)
        packageInTransitData2TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)

        packageDeliveredCheckBox.setImageResource(R.drawable.checkbox_off_block)
        packageDeliveredTextView.setTextAppearance(R.style.RobotoRegular14spGrayText)
        packageDeliveredData1TextView.setTextAppearance(R.style.RobotoRegular12spGrayText)
        packageDeliveredData2TextView.setTextAppearance(R.style.RobotoRegular12spGrayText)
    }

    private fun updatePackageDeliveredCheckBox() {
        courierRequestedCheckBox.setImageResource(R.drawable.checkbox_on)
        courierRequestedTextView.setTextAppearance(R.style.RobotoRegular14spGrayText)
        courierRequestedData1TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)
        courierRequestedData2TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)

        packageReadyCheckBox.setImageResource(R.drawable.checkbox_on)
        packageReadyTextView.setTextAppearance(R.style.RobotoRegular14spGrayText)
        packageReadyData1TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)
        packageReadyData2TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)

        packageInTransitCheckBox.setImageResource(R.drawable.checkbox_on)
        packageInTransitTextView.setTextAppearance(R.style.RobotoRegular14spGrayText)
        packageInTransitData1TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)
        packageInTransitData2TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)

        packageDeliveredCheckBox.setImageResource(R.drawable.checkbox_off)
        packageDeliveredTextView.setTextAppearance(R.style.RobotoRegular14spPrimaryText)
        packageDeliveredData1TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)
        packageDeliveredData2TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)
    }

    private fun updatePackageAllDeliveredCheckBox() {
        courierRequestedCheckBox.setImageResource(R.drawable.checkbox_on)
        courierRequestedTextView.setTextAppearance(R.style.RobotoRegular14spPrimaryText)
        courierRequestedData1TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)
        courierRequestedData2TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)

        packageReadyCheckBox.setImageResource(R.drawable.checkbox_on)
        packageReadyTextView.setTextAppearance(R.style.RobotoRegular14spPrimaryText)
        packageReadyData1TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)
        packageReadyData2TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)

        packageInTransitCheckBox.setImageResource(R.drawable.checkbox_on)
        packageInTransitTextView.setTextAppearance(R.style.RobotoRegular14spPrimaryText)
        packageInTransitData1TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)
        packageInTransitData2TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)

        packageDeliveredCheckBox.setImageResource(R.drawable.checkbox_on)
        packageDeliveredTextView.setTextAppearance(R.style.RobotoRegular14spPrimaryText)
        packageDeliveredData1TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)
        packageDeliveredData2TextView.setTextAppearance(R.style.RobotoRegular12spSecondaryText)
    }

    private fun getSavedThemeState(): Boolean {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences(Constances.NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Constances.KEY, false)
    }
}