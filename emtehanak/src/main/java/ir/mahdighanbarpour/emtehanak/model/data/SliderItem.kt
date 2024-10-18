package ir.mahdighanbarpour.emtehanak.model.data

import com.google.gson.annotations.SerializedName

data class SliderItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("isForStudent")
    val isForStudent: Boolean,
    @SerializedName("title")
    val title: String
)