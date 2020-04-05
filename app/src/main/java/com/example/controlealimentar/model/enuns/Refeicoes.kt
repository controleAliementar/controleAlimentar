package com.example.controlealimentar.model.enuns

enum class Refeicoes {
    CAFE_MANHA("1e9fa024-b39e-4a7d-a8a9-b61b3f26e7c0"),
    LANCHE_MANHA("6a34e5bb-8aa5-43c2-99d8-5fdf3ba25ca5"),
    ALMOCO("00dba9fb-5dd8-436e-b824-9651a186132c"),
    LANCHE_TARDE("6fbe6057-e54f-477c-8e66-c6c38dca9807"),
    JANTA("64441006-cf8a-44cc-a35a-82e6ed75c55c"),
    CHA_NOITE("2a74adc9-4ca1-416c-a8a9-e136cfa784ac"),
    ALIMENTOS_AVULSOS("6ab66802-e7e5-4fb9-ba9a-6e85f44771a8");

    var id: String

    constructor(id: String){
        this.id = id
    }

}