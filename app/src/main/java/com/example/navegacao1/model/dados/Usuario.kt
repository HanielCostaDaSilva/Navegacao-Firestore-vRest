package com.example.navegacao1.model.dados

import com.google.firebase.firestore.DocumentId

class Usuario {
    @DocumentId
    var id: Int = 0
    var nome: String = ""
    var senha: String = ""

    constructor()

    constructor(id: Int, nome: String, senha: String) {
        this.id = id
        this.nome = nome
        this.senha = senha
    }
}