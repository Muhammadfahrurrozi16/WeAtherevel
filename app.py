from fastapi import FastAPI
import joblib
import pandas as pd
# from typing import List, Optional

# Inisialisasi aplikasi FastAPI
app = FastAPI()

# Memuat data dari file pickle
similarity = joblib.load('cosine_sim.pkl')  # Matriks kesamaan
all_tourism_name = joblib.load('place.pkl')  # Data tempat wisata

# Fungsi rekomendasi
def recommend(Place_id: int, rekomendasi: str, top_n: int = 5):
    try:
        # Mencari indeks tempat berdasarkan Place_Id
        idx = all_tourism_name.index[all_tourism_name['Place_Id'] == Place_id][0]
        
        # Menghitung skor kesamaan
        sim_scores = list(enumerate(similarity[idx]))
        sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)
        sim_scores = sim_scores[1:]  # Hindari tempat itu sendiri

        # Filter berdasarkan rekomendasi (jika ada)
        if rekomendasi:
            rekomendasi = rekomendasi.lower().replace(" ", "")
            sim_scores = [
                i for i in sim_scores
                if rekomendasi in all_tourism_name.iloc[i[0]]['rekomendasi'].lower()
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
                # 'description': place_data['Description'],  # Deskripsi
                # 'image': place_data['Image_URL'],  # Gambar (URL)
                'city': place_data['City'],  
            })

        return recommendations

    except IndexError:
        return {"error": "Place_Id not found in the dataset."}
    except ValueError as e:
        return {"error": str(e)}

# Endpoint untuk rekomendasi menggunakan GET
@app.get("/recommend/{Place_id}")
async def get_recommendations(
    Place_id: int, 
    rekomendasi: str = "Hujan Ringan", 
    top_n: int = 5
):
    """
    Endpoint untuk mendapatkan rekomendasi tempat wisata.
    - `id_place`: ID tempat wisata sebagai input utama.
    - `rekomendasi`: (Opsional) Filter rekomendasi berdasarkan kata kunci.
    - `top_n`: (Opsional) Jumlah rekomendasi yang diinginkan.
    """
    recommendations = recommend(
        Place_id=Place_id,
        rekomendasi=rekomendasi,
        top_n=top_n
    )
    return {"recommendations": recommendations}
