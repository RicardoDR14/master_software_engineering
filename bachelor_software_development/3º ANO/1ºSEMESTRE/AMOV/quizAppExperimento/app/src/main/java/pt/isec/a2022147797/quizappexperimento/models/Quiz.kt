package pt.isec.a2022147797.quizappexperimento.models

data class Quiz(
    val id: String,  // Código único do quiz
    val title: String,  // Título do quiz
    val questions: List<Question>  // Lista de perguntas que o quiz contém
)

fun getSampleQuizzes(): List<Quiz> {
    return listOf(
        Quiz(
            id = "quiz1",
            title = "Quiz de Matemática",
            questions = listOf(
                Question(
                    questionText = "Qual é a raiz quadrada de 16?",
                    options = listOf("2", "4", "8", "16"),
                    correctAnswerIndex = 1
                ),
                Question(
                    questionText = "Qual é a soma de 5 + 3?",
                    options = listOf("7", "8", "9", "10"),
                    correctAnswerIndex = 1
                )
            )
        ),
        Quiz(
            id = "quiz2",
            title = "Quiz de História",
            questions = listOf(
                Question(
                    questionText = "Quem foi o primeiro presidente de Portugal?",
                    options = listOf("Afonso Costa", "Teófilo Braga", "Egas Moniz", "Mário Soares"),
                    correctAnswerIndex = 1
                ),
                Question(
                    questionText = "Em que ano foi a Revolução dos Cravos?",
                    options = listOf("1974", "1968", "1982", "1900"),
                    correctAnswerIndex = 0
                )
            )
        )
    )
}
