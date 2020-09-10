package com.example.controlealimentar.gateway.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class UsuarioResponseGateway {
    var email: String = ""
    var nome: String = ""
    var id: String = ""
}