package com.example.yemektariflerimuygulamasi

import android.os.Parcel
import android.os.Parcelable

class Tarifler(val ad:String, val id:Int, val image: ByteArray, val malz:String, val yapilis:String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.createByteArray()!!,
        parcel.readString()!!,
        parcel.readString()!!,
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ad)
        parcel.writeInt(id)
        parcel.writeByteArray(image)
        parcel.writeString(malz)
        parcel.writeString(yapilis)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Tarifler> {
        override fun createFromParcel(parcel: Parcel): Tarifler {
            return Tarifler(parcel)
        }

        override fun newArray(size: Int): Array<Tarifler?> {
            return arrayOfNulls(size)
        }
    }
}