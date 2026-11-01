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
import pt.isec.a2022147797.quizappexperimento.models.Quiz
import pt.isec.a2022147797.quizappexperimento.models.getSampleQuizzes
import pt.isec.a2022147797.quizappexperimento.websockets.QuizWebSocketClient
import java.net.URI

@Composable
fun ProfessorScreen(navController: NavController) {
    val quizzes = getSampleQuizzes()  // Carrega os quizzes
    var selectedQuiz by remember { mutableStateOf<Quiz?>(null) }
    var quizCode by remember { mutableStateOf("") }

    // Criar conexão com o WebSocket (simulando a parte de servidor)
    val serverUri = URI("ws://10.0.2.2:8080")  // IP do servidor WebSocket no emulador
    val client = QuizWebSocketClient(serverUri)
    client.connect()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Selecione um Quiz:")

        quizzes.forEach { quiz ->
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    selectedQuiz = quiz
                    quizCode = quiz.id  // O código do quiz é o ID
                }
            ) {
                Text(quiz.title)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        selectedQuiz?.let {
            Text("Código do Quiz: $quizCode")
            Button(onClick = {
                client.send("Start quiz: $quizCode")
                navController.navigate("alunoScreen/$quizCode")  // Navegar para a tela do aluno
            }) {
                Text("Iniciar Quiz")
            }
        }
    }
}
