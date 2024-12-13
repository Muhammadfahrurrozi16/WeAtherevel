package com.example.weatheravel.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatheravel.R
import kotlinx.android.synthetic.main.fragment_hasil_rekomendasi.*

class HasilRekomendasiFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hasil_rekomendasi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Contoh pengaturan RecyclerView
        rv_hasil_rekomendasi_list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = HasilRekomendasiAdapter(getDummyData())
        }

        // Contoh menampilkan data placeholder
        iv_hasil_rekomendasi_image.setImageResource(R.drawable.sample_image)
        tv_hasil_rekomendasi_title.text = "Nama Tempat Wisata"
        tv_hasil_rekomendasi_location.text = "Lokasi: Yogyakarta"
        tv_hasil_rekomendasi_description.text = "Deskripsi singkat tentang tempat wisata yang dipilih."
    }

    // Fungsi untuk mendapatkan data dummy (nanti diganti dengan data asli)
    private fun getDummyData(): List<String> {
        return listOf(
            "Tempat Wisata 1",
            "Tempat Wisata 2",
            "Tempat Wisata 3",
            "Tempat Wisata 4",
            "Tempat Wisata 5"
        )
    }
}
