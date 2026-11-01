package pt.isec.ans.amovfirebase.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pt.isec.ans.amovfirebase.ui.viewmodels.FirebaseViewModel

const val FIRESTORE_SCREEN = "Firestore"
const val INTENT_SCREEN = "Intent"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: FirebaseViewModel,
    onSignOut: () -> Unit,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val currentScreen by navController.currentBackStackEntryAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Firebase App")
                },
                actions = {
                    IconButton(
                        onClick = {
                            onSignOut()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Sign out"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            navController.navigate("firestore") {
                                popUpTo("firestore") {
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = FIRESTORE_SCREEN,
                            tint =  if (currentScreen?.destination?.route == FIRESTORE_SCREEN)
                                        Color(0,128,0)
                                    else
                                        Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            navController.navigate(INTENT_SCREEN) {
                                popUpTo(INTENT_SCREEN) {
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = INTENT_SCREEN,
                            tint =  if (currentScreen?.destination?.route == INTENT_SCREEN)
                                        Color(0,128,0)
                                    else
                                        Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = FIRESTORE_SCREEN,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = FIRESTORE_SCREEN) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(64, 160, 255))
                ) {
                    FirestoreScreen(
                        viewModel = viewModel
                    )
                }
            }
            composable(route = INTENT_SCREEN) {
                val intent = (context as? Activity)?.intent
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(64, 224, 128))
                        .padding(16.dp)
                ) {
                    intent?.let {
                        LazyColumn {
                            it.extras?.apply {
                                for (k in keySet()) {
                                    item {
                                        Text(
                                            text = "$k : ${get(k)}"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

