package ir.mahdighanbarpour.khwarazmiapp.model.data


import com.google.gson.annotations.SerializedName

data class SliderMainResult(
    @SerializedName("massage")
    val massage: String,
    @SerializedName("result")
    val result: SliderResult,
    @SerializedName("status")
    val status: Int
)

data class SliderResult(
    @SerializedName("items")
    val sliderItems: List<SliderItem>
)