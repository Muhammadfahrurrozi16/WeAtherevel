package com.example.weatheravel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weatheravel.ui.API.ApiClient
import com.example.weatheravel.ui.API.ApiService
import com.example.weatheravel.ui.API.Destination
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailFragment : Fragment() {

    private var destinationId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil data ID destinasi dari Bundle atau ViewModel
        destinationId = arguments?.getInt("destination_id")

        if (destinationId != null) {
            // Panggil API untuk mengambil data destinasi
            fetchDestinationDetail(destinationId!!)
        }
    }

    private fun fetchDestinationDetail(id: Int) {
        val apiService = ApiClient.instance.create(ApiService::class.java)
        apiService.getDestinationDetail(id).enqueue(object : Callback<Destination> { // Pastikan menggunakan Destination
            override fun onResponse(
                call: Call<Destination>,
                response: Response<Destination>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val destination = response.body()!!
                    // Update UI dengan data yang diterima
                    updateUI(destination)
                } else {
                    Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Destination>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI(destination: Destination) {
        TODO("Not yet implemented")
    }


}
