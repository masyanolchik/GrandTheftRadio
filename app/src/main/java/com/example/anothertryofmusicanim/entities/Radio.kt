package com.example.anothertryofmusicanim.entities

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.Serializable

@Serializable
data class Radio(
    val name: String,
    val link: String,
    val picLink: String,
    @ContextualSerialization
    var pic: Bitmap? = null
) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )



    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeString(link)
        dest?.writeString(picLink)
    }

    companion object CREATOR : Parcelable.Creator<Radio> {
        override fun createFromParcel(parcel: Parcel): Radio {
            return Radio(parcel)
        }

        override fun newArray(size: Int): Array<Radio?> {
            return arrayOfNulls(size)
        }
    }
}