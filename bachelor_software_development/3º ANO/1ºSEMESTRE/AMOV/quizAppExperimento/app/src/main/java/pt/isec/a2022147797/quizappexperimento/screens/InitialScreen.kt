package pt.isec.a2022147797.quizappexperimento.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun InitialScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bem-vindo ao Quiz App!")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("professorScreen")
        }) {
            Text("Entrar como Professor")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("alunoScreen/quiz1")  // Exemplo de código do quiz
        }) {
            Text("Entrar como Aluno")
        }
    }
}


