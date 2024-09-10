package com.example.navegacao1.ui.telas

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.navegacao1.model.dados.Endereco
import com.example.navegacao1.model.dados.RetrofitClient
import com.example.navegacao1.model.dados.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun TelaPrincipal(modifier: Modifier = Modifier, onLogoffClick: () -> Unit) {
    val scope = rememberCoroutineScope()

    var usuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }
    var endereco by remember { mutableStateOf<Endereco>(Endereco()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier) {
        Text(text = "Tela Principal")

        // LaunchedEffect to load users on initial launch
        LaunchedEffect(Unit) {
            scope.launch {
                try {
                    usuarios = getUsuarios()
                    // endereco = getEndereco() // Uncomment if needed
                } catch (e: Exception) {
                    errorMessage = "Erro ao carregar dados: ${e.message}"
                }
            }
        }

        // Mostrar mensagem de erro, se houver
        errorMessage?.let {
            Text(text = it, color = androidx.compose.ui.graphics.Color.Red)
        }

        // Botão para recarregar a lista de usuários
        Button(onClick = {
            scope.launch {
                try {
                    usuarios = getUsuarios()
                } catch (e: Exception) {
                    errorMessage = "Erro ao recarregar usuários: ${e.message}"
                }
            }
        }) {
            Text("Recarregar")
        }

        // Botão de Logoff
        Button(onClick = { onLogoffClick() }) {
            Text("Sair")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            scope.launch {
                val novoUsuario = Usuario(id = 3, nome = "Novo Usuário", senha = "senha123")
                RetrofitClient.usuarioService.inserir(novoUsuario)
                usuarios = getUsuarios() // Recarrega a lista
            }
        }) {
            Text("Inserir Novo Usuário")
        }

        // Exibir a lista de usuários com cards aprimorados
        LazyColumn {
            items(usuarios) { usuario ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp) // Espaçamento entre os cards
                ) {
                    Column(modifier = Modifier.padding(16.dp)) { // Espaçamento interno do card
                        Text(text = "Nome: ${usuario.nome}")
                        Text(text = "ID: ${usuario.id}")
                        Button(
                            onClick = {
                                scope.launch {
                                    try {
                                        RetrofitClient.usuarioService.remover(usuario.id);
                                        usuarios = getUsuarios() // Atualiza a lista após a remoção
                                    } catch (e: Exception) {
                                        errorMessage = "Erro ao remover usuário: ${e.message}"
                                    }
                                }
                            }
                        ) {
                            Text(text = "X")
                        }
                    }
                }
            }
        }
    }
}

suspend fun getUsuarios(): List<Usuario> {
    return withContext(Dispatchers.IO) {
        RetrofitClient.usuarioService.listar()
    }
}

