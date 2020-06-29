package com.example.app

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            pickLocation()
        }
    }

    private fun pickLocation() {
        val p = Manifest.permission.ACCESS_COARSE_LOCATION
        if (ActivityCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(p), REQUEST_CODE)
            return
        }

        val context = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            createAttributionContext("getLocation")
        } else {
            this
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    Toast.makeText(this, "$latitude, $longitude", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "location is null", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                    pickLocation()
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE = 11
    }
}
