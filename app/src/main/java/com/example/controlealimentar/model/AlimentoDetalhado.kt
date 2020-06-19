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
    val unidadePorcao: String = "",
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
        parcel.readString()!!,
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        // TODO To change body of created functions use File | Settings | File Templates.
    }

    override fun describeContents(): Int {
        // TODO To change body of created functions use File | Settings | File Templates.
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