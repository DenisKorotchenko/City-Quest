package com.deniskorotchenko.mapsp

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        init() // в этой функции объявляем все маркеры и прочую дичь

        mMap.setOnInfoWindowClickListener(GoogleMap.OnInfoWindowClickListener {
            val intent = Intent(this, QuestMapActivity::class.java)

            startActivity(intent)
        }) // при нажатии на инфо окно осуществляется переход к заданию


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        }
    }

    fun init() {
        // Добавил маркер на Крестовский остров
        val firstQuest = LatLng(59.971944, 30.241794)
        val markerKrestovskiy = mMap.addMarker(MarkerOptions()
                .position(firstQuest)
                .snippet("Нажмите на это окно один раз для старта")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title("Крестовский остров")
        )

        // Добавил маркер на стадион
        val stadium = LatLng(59.972959, 30.220219)
        val markerStadium = mMap.addMarker(MarkerOptions()
                .title("Стадион \"Санкт-Петербург\"")
                .position(stadium)
                .snippet("Нажмите на это окно один раз для старта")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
        )

        val center = LatLng(59.9367364, 30.3096995)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 11.2F)) /* переводит камеру в СПб
        при открытии и совершает масштабирование карты */

        // Добавил маркер перед Адмиралтейством
        val markerCenter = mMap.addMarker(MarkerOptions()
                .position(center)
                .title("Адмиралтейство")
                .snippet("Нажмите на это окно один раз для старта")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )

        // Добавил маркер Новая Голландия
        val newHolland = LatLng(59.929470, 30.289756)
        val markerNewHolland = mMap.addMarker(MarkerOptions()
                .position(newHolland)
                .snippet("Нажмите на это окно один раз для старта")
                .title("Новая Голландия")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )


    }


}
