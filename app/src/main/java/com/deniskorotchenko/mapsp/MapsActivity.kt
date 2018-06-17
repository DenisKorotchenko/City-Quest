package com.deniskorotchenko.mapsp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        init() // в этой функции объявляем все маркеры и прочую дичь

        button1.setOnClickListener {
            val intent = Intent(this, QuestMapActivity::class.java)
            startActivity(intent)
        }

        mMap.setOnInfoWindowClickListener(GoogleMap.OnInfoWindowClickListener{
            magic(button1) }) // при нажатии на инфо окно появляется кнопка старта
    }

    fun init(){
        // Добавил маркер на Крестовский остров
        val firstQuest = LatLng(59.971944, 30.241794)
        val markerKrestovskiy = mMap.addMarker(MarkerOptions()
                .position(firstQuest)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title("Крестовский остров")
        )

        // Добавил маркер на стадион
        val stadium = LatLng(59.972959, 30.220219)
        val markerStadium =mMap.addMarker(MarkerOptions()
                .title("Стадион \"Санкт-Петербург\"")
                .position(stadium)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
        )

        val center = LatLng(59.9367364, 30.3096995)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center,11.2F)) /* переводит камеру в СПб
        при открытии и совершает масштабирование карты */

        // Добавил маркер перед Адмиралтейством
        val markerCenter = mMap.addMarker(MarkerOptions()
                .position(center)
                .title("Адмиралтейство")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )

        // Добавил маркер Новая Голландия
        val newHolland = LatLng(59.929470, 30.289756)
        val markerNewHolland = mMap.addMarker(MarkerOptions()
                .position(newHolland)
                .title("Новая Голландия")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )
    }

    fun magic(button: Button){
        button.visibility = View.VISIBLE
    }
}
