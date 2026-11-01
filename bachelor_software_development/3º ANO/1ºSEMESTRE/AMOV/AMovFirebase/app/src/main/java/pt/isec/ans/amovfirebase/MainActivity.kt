package pt.isec.ans.amovfirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.isec.ans.amovfirebase.ui.screens.LoginScreen
import pt.isec.ans.amovfirebase.ui.screens.MainScreen
import pt.isec.ans.amovfirebase.ui.theme.AMovFirebaseTheme
import pt.isec.ans.amovfirebase.ui.viewmodels.FirebaseViewModel

class MainActivity : ComponentActivity() {
    companion object {
        const val LOGIN_SCREEN = "Login"
        const val MAIN_SCREEN = "Main"
    }
    private val viewModel : FirebaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            AMovFirebaseTheme {
                Surface {
                    NavHost(
                        navController = navController,
                        startDestination = LOGIN_SCREEN,
                    ) {
                        composable(LOGIN_SCREEN) {
                            LoginScreen(
                                viewModel,
                                onSuccess = {
                                    navController.navigate(MAIN_SCREEN) {
                                        popUpTo(LOGIN_SCREEN) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(MAIN_SCREEN) {
                            MainScreen(
                                viewModel = viewModel,
                                onSignOut =  {
                                    viewModel.signOut()
                                    navController.navigate(LOGIN_SCREEN) {
                                        popUpTo(MAIN_SCREEN) { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }


}

