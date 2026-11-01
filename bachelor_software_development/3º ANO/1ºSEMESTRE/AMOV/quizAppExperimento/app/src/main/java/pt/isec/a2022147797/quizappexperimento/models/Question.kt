package pt.isec.a2022147797.quizappexperimento.models

data class Question(
    val questionText: String,  // O texto da pergunta
    val options: List<String>,  // As opções de resposta
    val correctAnswerIndex: Int  // O índice da opção correta
)