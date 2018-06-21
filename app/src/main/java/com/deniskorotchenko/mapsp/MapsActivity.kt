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
    private val singleton = Singleton.instance



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnInfoWindowClickListener(GoogleMap.OnInfoWindowClickListener {
            singleton.curentTableQuest = AllQuestsDataBase(this).getTableQuestById(it.tag as Int)
            val intent = Intent(this, QuestMapActivity::class.java)
            startActivity(intent)
        }) // при нажатии на инфо окно осуществляется переход к заданию

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        }

        val center = LatLng(59.9367364, 30.3096995)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 15F))

        initFromDataBase()
    }

    private fun initFromDataBase(){
        val markers = AllQuestsDataBase(this).getAllMarkers()
        Log.v("MapsActivity", markers.size.toString())
        for (marker : MarkerInAll in markers){
            var newMarker = mMap.addMarker(MarkerOptions()
                    .position(marker.coordinats)
                    .snippet("Нажмите на это окно один раз для старта")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .title(marker.startText)
            )
            newMarker.tag = marker.questID
        }
    }
}
