package pt.isec.a2022147797.quizappexperimento.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pt.isec.a2022147797.quizappexperimento.screens.ProfessorScreen
import pt.isec.a2022147797.quizappexperimento.screens.AlunoScreen
import pt.isec.a2022147797.quizappexperimento.screens.InitialScreen

@Composable
fun NavigationGraph(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = "initialScreen") {
        composable("initialScreen") {
            InitialScreen(navController = navHostController)
        }
        composable("professorScreen") {
            ProfessorScreen(navController = navHostController)
        }
        composable("alunoScreen/{quizCode}") { backStackEntry ->
            val quizCode = backStackEntry.arguments?.getString("quizCode")
            AlunoScreen(navController = navHostController, quizCode = quizCode)
        }
    }
}


