package com.example.controlealimentar.model

import android.os.Parcel
import android.os.Parcelable

class MetaDiariasHistorico() : Parcelable {
    var id: String = ""
    var processoId: String = ""
    var calorias: Double = 0.0
    var carboidratos: Double = 0.0
    var proteinas: Double = 0.0
    var gorduras: Double = 0.0
    var dataReferencia: String = ""
    var metaAtingida: Boolean = true
    var caloriasConsumidas: Double = 0.0
    var carboidratosConsumidas: Double = 0.0
    var proteinasConsumidas: Double = 0.0
    var gordurasConsumidas: Double = 0.0

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()!!
        processoId = parcel.readString()!!
        calorias = parcel.readDouble()
        carboidratos = parcel.readDouble()
        proteinas = parcel.readDouble()
        gorduras = parcel.readDouble()
        dataReferencia = parcel.readString()!!
        metaAtingida = parcel.readByte() != 0.toByte()
        caloriasConsumidas = parcel.readDouble()
        carboidratosConsumidas = parcel.readDouble()
        proteinasConsumidas = parcel.readDouble()
        gordurasConsumidas = parcel.readDouble()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(processoId)
        parcel.writeDouble(calorias)
        parcel.writeDouble(carboidratos)
        parcel.writeDouble(proteinas)
        parcel.writeDouble(gorduras)
        parcel.writeString(dataReferencia)
        parcel.writeByte(if (metaAtingida) 1 else 0)
        parcel.writeDouble(caloriasConsumidas)
        parcel.writeDouble(carboidratosConsumidas)
        parcel.writeDouble(proteinasConsumidas)
        parcel.writeDouble(gordurasConsumidas)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MetaDiariasHistorico> {
        override fun createFromParcel(parcel: Parcel): MetaDiariasHistorico {
            return MetaDiariasHistorico(parcel)
        }

        override fun newArray(size: Int): Array<MetaDiariasHistorico?> {
            return arrayOfNulls(size)
        }
    }
}