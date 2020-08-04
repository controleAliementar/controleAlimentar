package com.example.controlealimentar.model

import android.os.Parcel
import android.os.Parcelable


data class AlimentoUsuario(
    var porcaoConsumida: Double = 0.0,
    var nomeAlimento: String = "",
    var calorias: Double = 0.0,
    var porcaoAlimento: Double = 0.0,
    var caloriaPorcao: Double = 0.0,
    var carboidratos: Double = 0.0,
    var carboidratoPorcao: Double = 0.0,
    var proteinas: Double = 0.0,
    var proteinaPorcao: Double = 0.0,
    var gorduras: Double = 0.0,
    var gorduraPorcao: Double = 0.0,
    var alimentoIngerido : Boolean = false,
    var unidadePorcao : String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(porcaoConsumida)
        parcel.writeString(nomeAlimento)
        parcel.writeDouble(calorias)
        parcel.writeDouble(porcaoAlimento)
        parcel.writeDouble(caloriaPorcao)
        parcel.writeDouble(carboidratos)
        parcel.writeDouble(carboidratoPorcao)
        parcel.writeDouble(proteinas)
        parcel.writeDouble(proteinaPorcao)
        parcel.writeDouble(gorduras)
        parcel.writeDouble(gorduraPorcao)
        parcel.writeByte(if (alimentoIngerido) 1 else 0)
        parcel.writeString(unidadePorcao)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AlimentoUsuario> {
        override fun createFromParcel(parcel: Parcel): AlimentoUsuario {
            return AlimentoUsuario(parcel)
        }

        override fun newArray(size: Int): Array<AlimentoUsuario?> {
            return arrayOfNulls(size)
        }
    }
}