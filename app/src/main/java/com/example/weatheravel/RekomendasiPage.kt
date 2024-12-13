package com.example.weatheravel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatheravel.ui.API.ApiClient
import com.example.weatheravel.ui.API.ApiService
import com.example.weatheravel.ui.API.Destination
import androidx.navigation.fragment.findNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RekomendasiPage : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var chipGroup: ChipGroup
    private lateinit var adapter: RekomendasiAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rekomendasi_page, container, false)

        // Inisialisasi RecyclerView dan ChipGroup
        recyclerView = view.findViewById(R.id.recycler_rekomendasi)
        chipGroup = view.findViewById(R.id.chip_group)

        // Set RecyclerView dengan adapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = RekomendasiAdapter { destinationName ->
            // Navigasi ke DetailFragment dan kirimkan nama destinasi
            val action = RekomendasiPageDirections.actionRekomendasiPageToDetailFragment(destinationName)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter

        // Menambahkan listener pada chip
        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedChip = view.findViewById<Chip>(checkedId)
            selectedChip?.let {
                val city = it.text.toString()
                // Panggil API untuk mendapatkan destinasi berdasarkan kota
                fetchDestinations(city)
            }
        }

        return view
    }

    private fun fetchDestinations(city: String) {
        val apiService = ApiClient.instance.create(ApiService::class.java)
        apiService.getDestinationsByCity(city).enqueue(object : Callback<List<Destination>> {
            override fun onResponse(call: Call<List<Destination>>, response: Response<List<Destination>>) {
                if (response.isSuccessful && response.body() != null) {
                    // Update RecyclerView dengan destinasi yang diterima dari API
                    val destinations = response.body()!!
                    val destinationNames = destinations.map { it.name } // Asumsikan `Destination` punya field `name`
                    adapter.updateDestinations(destinationNames)
                } else {
                    // Menangani kasus data kosong atau error lainnya
                    Toast.makeText(context, "No destinations found for $city", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Destination>>, t: Throwable) {
                // Menangani error jaringan atau lainnya
                Toast.makeText(context, "Failed to load data: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

private fun RekomendasiPageDirections.Companion.actionRekomendasiPageToDetailFragment(
    destinationName: String
): Any {
    TODO("Not yet implemented")
}
