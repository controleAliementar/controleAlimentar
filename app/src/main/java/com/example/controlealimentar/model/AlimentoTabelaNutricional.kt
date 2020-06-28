package com.example.controlealimentar.model

import android.os.Parcel
import android.os.Parcelable


data class AlimentoTabelaNutricional(
    var nomeAlimento: String = "",
    var calorias: Double = 0.0,
    var carboidratos: Double = 0.0,
    var proteinas: Double = 0.0,
    var gorduras: Double = 0.0,
    var porcao: Double = 0.0,
    var unidadePorcao: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nomeAlimento)
        parcel.writeDouble(calorias)
        parcel.writeDouble(carboidratos)
        parcel.writeDouble(proteinas)
        parcel.writeDouble(gorduras)
        parcel.writeDouble(porcao)
        parcel.writeString(unidadePorcao)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AlimentoTabelaNutricional> {
        override fun createFromParcel(parcel: Parcel): AlimentoTabelaNutricional {
            return AlimentoTabelaNutricional(parcel)
        }

        override fun newArray(size: Int): Array<AlimentoTabelaNutricional?> {
            return arrayOfNulls(size)
        }
    }
}