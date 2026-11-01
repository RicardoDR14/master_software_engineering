package pt.isec.a2022147797.quizappexperimento.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AlunoScreen(navController: NavController, quizCode: String?) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Entrando no Quiz: $quizCode")

        Button(onClick = {
            // Aqui você poderia adicionar lógica de resposta ao quiz
            navController.navigate("initialScreen")  // Voltar para a tela inicial
        }) {
            Text("Responder")
        }
    }
}
