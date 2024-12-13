import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatheravel.R
import com.example.weatheravel.ui.API.ApiClient
import com.example.weatheravel.ui.API.ApiService
import com.example.weatheravel.ui.API.Destination
import com.example.weatheravel.ui.DestinationAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DestinationFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout dari fragment_destination.xml
        val view = inflater.inflate(R.layout.fragment_destination, container, false)

        // Inisialisasi RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Panggil API untuk memuat data
        fetchDestinations()

        return view
    }

    private fun fetchDestinations() {
        val apiService = ApiClient.instance.create(ApiService::class.java)
        apiService.getDestinations().enqueue(object : Callback<List<Destination>> {
            override fun onResponse(
                call: Call<List<Destination>>,
                response: Response<List<Destination>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val destinations = response.body()!!
                    recyclerView.adapter = DestinationAdapter(destinations)
                } else {
                    Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Destination>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
