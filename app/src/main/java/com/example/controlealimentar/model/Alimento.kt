package com.example.controlealimentar.model

import android.os.Parcel
import android.os.Parcelable


data class Alimento(
    val id: String = "",
    val nome: String = "",
    val calorias: Double = 0.0,
    val carboidratos: Double = 0.0,
    val proteinas: Double = 0.0,
    val gorduras: Double = 0.0,
    val porcao: Porcao
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        TODO("porcao")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nome)
        parcel.writeDouble(calorias)
        parcel.writeDouble(carboidratos)
        parcel.writeDouble(proteinas)
        parcel.writeDouble(gorduras)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Alimento> {
        override fun createFromParcel(parcel: Parcel): Alimento {
            return Alimento(parcel)
        }

        override fun newArray(size: Int): Array<Alimento?> {
            return arrayOfNulls(size)
        }
    }
}