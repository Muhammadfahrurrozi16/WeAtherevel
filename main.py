from fastapi import FastAPI
import requests
from fastapi.responses import JSONResponse
from datetime import datetime, timedelta
import joblib
import numpy as np

app = FastAPI()

# Load data dan model
similarity = joblib.load('cosine_sim.pkl')  # Matriks kesamaan
all_tourism_name = joblib.load('place.pkl')  # Data tempat wisata
model = joblib.load('weather_prediction_model.pkl')  # Model prediksi cuaca

# Fungsi rekomendasi dengan filter kota yang sama
def recommend(Place_id: int, rekomendasi: str = None, top_n: int = 5):
    try:
        # Mencari indeks tempat berdasarkan Place_Id
        idx = all_tourism_name.index[all_tourism_name['Place_Id'] == Place_id][0]
        
        # Menghitung skor kesamaan
        sim_scores = list(enumerate(similarity[idx]))
        sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)
        sim_scores = sim_scores[1:]  # Hindari tempat itu sendiri
        city_of_place = all_tourism_name.iloc[idx]['City']  # Ambil kota tempat wisata
        
        # Filter berdasarkan rekomendasi (jika ada) dan kota yang sama
        if rekomendasi:
            rekomendasi = rekomendasi.lower().replace(" ", "")
            sim_scores = [
                i for i in sim_scores
                if rekomendasi in all_tourism_name.iloc[i[0]]['rekomendasi'].lower() and 
                   all_tourism_name.iloc[i[0]]['City'] == city_of_place  # Filter berdasarkan kota yang sama
            ]
        else:
            # Filter tempat yang berada di kota yang sama
            sim_scores = [
                i for i in sim_scores
                if all_tourism_name.iloc[i[0]]['City'] == city_of_place  # Filter berdasarkan kota yang sama
            ]

        # Ambil top_n rekomendasi
        sim_scores = sim_scores[:top_n]

        # Ambil detail tempat wisata
        recommendations = []
        for i in sim_scores:
            place_data = all_tourism_name.iloc[i[0]]
            recommendations.append({
                'name': place_data['Place_Name'],
                'category': place_data['Category'],  
                'city': place_data['City'],  
            })

        return recommendations

    except IndexError:
        return []
    except ValueError:
        return []

@app.get("/prediksi-cuaca/{Place_id}/{kode}")
async def prediksi_suhu(Place_id: int, kode: str, top_n: int = 5):
    try:
        # URL API BMKG
        url = f"https://api.bmkg.go.id/publik/prakiraan-cuaca?adm4={kode}"
        
        # Lakukan request ke API
        response = requests.get(url)
        
        if response.status_code == 200:
            data = response.json()
            cuaca_data = data['data'][0]['cuaca']
            cuaca_flattened = [item for sublist in cuaca_data for item in sublist]
            
            # Waktu yang dipilih
            today = datetime.now().date()
            besok = today + timedelta(days=1)
            lusa = today + timedelta(days=2)
            
            hasil = {
                "hari_ini": {"suhu_kelembapan": []},
                "besok": {"suhu_kelembapan": []},
                "lusa": {"suhu_kelembapan": []}
            }
            
            # Filter data berdasarkan tanggal dan waktu
            for cuaca in cuaca_flattened:
                waktu = cuaca["local_datetime"]
                tanggal = waktu.split(" ")[0]  # Ambil hanya bagian tanggal
                tanggal_obj = datetime.strptime(tanggal, "%Y-%m-%d").date()
                
                # Ambil suhu dan kelembapan
                suhu = cuaca["t"]
                kelembapan = cuaca["hu"]
                
                # Menyusun data hasil
                if tanggal_obj == today:
                    hasil["hari_ini"]["suhu_kelembapan"].append({"suhu": suhu, "kelembapan": kelembapan})
                elif tanggal_obj == besok:
                    hasil["besok"]["suhu_kelembapan"].append({"suhu": suhu, "kelembapan": kelembapan})
                elif tanggal_obj == lusa:
                    hasil["lusa"]["suhu_kelembapan"].append({"suhu": suhu, "kelembapan": kelembapan})
            
            # Menghitung rata-rata suhu dan kelembapan untuk masing-masing kategori hari
            for key in hasil:
                suhu_values = [entry["suhu"] for entry in hasil[key]["suhu_kelembapan"]]
                kelembapan_values = [entry["kelembapan"] for entry in hasil[key]["suhu_kelembapan"]]
                
                if suhu_values:
                    rata_rata_suhu = sum(suhu_values) / len(suhu_values)
                else:
                    rata_rata_suhu = None

                if kelembapan_values:
                    rata_rata_kelembapan = sum(kelembapan_values) / len(kelembapan_values)
                else:
                    rata_rata_kelembapan = None

                # Menyimpan hasil rata-rata dalam array
                hasil[key]["suhu_kelembapan"] = [rata_rata_suhu, rata_rata_kelembapan]
            
            # Persiapkan data untuk prediksi
            new_data = []
            for key in hasil:
                suhu_kelembapan = hasil[key]["suhu_kelembapan"]
                if suhu_kelembapan[0] is not None and suhu_kelembapan[1] is not None:
                    # Menambahkan suhu dan kelembapan ke dalam data untuk prediksi
                    new_data.append([suhu_kelembapan[0], suhu_kelembapan[1]])
            
            # Prediksi dengan model
            predictions = []
            for data in new_data:
                predicted_class = model.predict([data])
                predictions.append(predicted_class[0])
            
            # Menambahkan prediksi dan rekomendasi ke hasil
            for idx, key in enumerate(hasil):
                if idx < len(predictions):
                    hasil[key]["prediksi"] = predictions[idx]
                    rekomendasi = recommend(
                        Place_id=Place_id,
                        rekomendasi=predictions[idx],  # Rekomendasi berdasarkan prediksi cuaca
                        top_n=top_n
                    )
                    hasil[key]["rekomendasi_tempat_wisata"] = rekomendasi

            # Mengembalikan hanya rekomendasi tempat wisata
            return JSONResponse(content={
                "hari_ini": hasil["hari_ini"]["rekomendasi_tempat_wisata"],
                "besok": hasil["besok"]["rekomendasi_tempat_wisata"],
                "lusa": hasil["lusa"]["rekomendasi_tempat_wisata"]
            })
        
        else:
            return JSONResponse(content={"error": "Gagal mengambil data dari API BMKG"}, status_code=500)
    
    except Exception as e:
        return JSONResponse(content={"error": str(e)}, status_code=500)
