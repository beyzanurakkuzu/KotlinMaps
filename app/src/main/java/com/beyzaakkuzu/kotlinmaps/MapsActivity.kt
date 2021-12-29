package com.beyzaakkuzu.kotlinmaps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.beyzaakkuzu.kotlinmaps.databinding.ActivityMapsBinding
import com.google.android.material.snackbar.Snackbar


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        registerLauncher()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        val ankara_castle = LatLng(39.9394467,32.8606141)
        mMap.addMarker(MarkerOptions().position(ankara_castle).title("Ankara Castle"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ankara_castle,15f))

        //casting
        locationManager= this.getSystemService(LOCATION_SERVICE) as LocationManager
        locationListener= object: LocationListener {
            override fun onLocationChanged(location: Location) {

            }
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

                Snackbar.make(binding.root,"Permission need for locasiton.", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                    //request permission
                }.show()
            }else{
                //request permission
            }
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)
        }

    }
    private fun registerLauncher(){
        permissionLauncher= registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
            if(result){
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED)){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)
            }
            }else {
                Toast.makeText(this@MapsActivity, "Permission needed.",Toast.LENGTH_LONG).show()
            }
        }
    }
}

