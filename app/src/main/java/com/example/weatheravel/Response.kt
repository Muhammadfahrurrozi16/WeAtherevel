package com.example.weatheravel

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("Response")
	val response: List<ResponseItem?>? = null
)

data class ResponseItem(

	@field:SerializedName("Image_link")
	val imageLink: String? = null,

	@field:SerializedName("Description")
	val description: String? = null,

	@field:SerializedName("Category")
	val category: String? = null,

	@field:SerializedName("Place_Id")
	val placeId: Int? = null,

	@field:SerializedName("Place_Ratings")
	val placeRatings: String? = null,

	@field:SerializedName("Place_Name")
	val placeName: String? = null,

	@field:SerializedName("Price")
	val price: Int? = null,

	@field:SerializedName("Kode")
	val kode: String? = null,

	@field:SerializedName("Rating")
	val rating: String? = null,

	@field:SerializedName("City")
	val city: String? = null
)
