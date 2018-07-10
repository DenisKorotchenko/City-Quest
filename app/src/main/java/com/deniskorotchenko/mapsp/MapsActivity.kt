package com.deniskorotchenko.mapsp


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import android.net.ConnectivityManager


import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapView: View
    private val singleton = Singleton.instance


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapView = mapFragment.view!!
        mapFragment.getMapAsync(this)

        supportActionBar!!.hide() // убираем полоску сверху

        if (isOnline(this)){
            Toast.makeText(this, "Вы подключены к интернету", Toast.LENGTH_LONG).show()
        }
        else {
            Toast.makeText(this, "Ошибка", Toast.LENGTH_LONG).show()
        } // Это проверка подключения к интернету


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnInfoWindowClickListener(GoogleMap.OnInfoWindowClickListener {
            singleton.curentTableQuest = AllQuestsDataBase(this).getTableQuestById(it.tag as Int)
            val intent = Intent(this, QuestMapActivity::class.java)
            startActivity(intent)
        }) // при нажатии на инфо окно осуществляется переход к заданию

        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        } // Отоброжение моего местоположения

        val center = LatLng(59.910653, 30.121128)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 9.5F)) /* настраиваем начальное
        положение камеры */

        initFromDataBase()


        //Эта штука переносит кнопку "Моё местоположение" в правый нижний угол
        val locationButton = (mapView.findViewById<View>(Integer.parseInt("1")).parent as
                View).findViewById<View>(Integer.parseInt("2"))
        val rlp = locationButton.layoutParams as (RelativeLayout.LayoutParams)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rlp.setMargins(0, 0, 30, 60)


        mMap.uiSettings.isCompassEnabled = false // Отключаем компас
        mMap.uiSettings.isMapToolbarEnabled = false // Отключаем прочие лишние кнопки
    }

    private fun initFromDataBase() {
        val markers = AllQuestsDataBase(this).getAllMarkers()
        Log.v("MapsActivity", markers.size.toString())
        for (marker: MarkerInAll in markers) {
            var newMarker = mMap.addMarker(MarkerOptions()
                    .position(marker.coordinats)
                    .snippet("Нажмите на это окно один раз для старта")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .title(marker.startText)
            )
            newMarker.tag = marker.questID
        }
    }

    private fun isOnline(context: Context): Boolean { // Проверка подключени к интеренету (нагуглил)
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as
                ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return (netInfo != null && netInfo.isConnectedOrConnecting)
    }
}
