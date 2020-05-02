package com.example.controlealimentar.model

import android.os.Parcel
import android.os.Parcelable


data class AlimentoDetalhado(
    val idRegistro: String = "",
    val idAlimento: String = "",
    val nomeAlimento: String = "",
    val calorias: Double = 0.0,
    val carboidratos: Double = 0.0,
    val proteinas: Double = 0.0,
    val gorduras: Double = 0.0,
    val porcaoConsumida: Double = 0.0,
    val alimentoIngerido: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idRegistro)
        parcel.writeString(idAlimento)
        parcel.writeString(nomeAlimento)
        parcel.writeDouble(calorias)
        parcel.writeDouble(carboidratos)
        parcel.writeDouble(proteinas)
        parcel.writeDouble(gorduras)
        parcel.writeDouble(porcaoConsumida)
        parcel.writeByte(if (alimentoIngerido) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AlimentoDetalhado> {
        override fun createFromParcel(parcel: Parcel): AlimentoDetalhado {
            return AlimentoDetalhado(parcel)
        }

        override fun newArray(size: Int): Array<AlimentoDetalhado?> {
            return arrayOfNulls(size)
        }
    }
}