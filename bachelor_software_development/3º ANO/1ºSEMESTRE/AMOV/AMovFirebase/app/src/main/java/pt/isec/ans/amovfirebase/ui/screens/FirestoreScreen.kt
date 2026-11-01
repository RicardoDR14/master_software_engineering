package pt.isec.ans.amovfirebase.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isec.ans.amovfirebase.R
import pt.isec.ans.amovfirebase.ui.viewmodels.FirebaseViewModel

@Composable
fun FirestoreScreen(
    viewModel: FirebaseViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (viewModel.error.value != null) {
            Text(
                text = "Error: ${viewModel.error.value}",
                modifier = Modifier
                    .background(Color(255, 0, 0))
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text("User: ${viewModel.user.value?.email ?: ""}")
        Text("Nr Games: ${viewModel.nrGames.value}")
        Text("Top Score: ${viewModel.topScore.value}")

        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.addDataToFirestore() }) {
            Text(stringResource(R.string.add_data))
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.updateDataInFirestore() }) {
            Text(stringResource(R.string.update_data))
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.removeDataFromFirestore() }) {
            Text(stringResource(R.string.remove_data))
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.startObserver() }) {
            Text(stringResource(R.string.start_observer))
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.stopObserver() }) {
            Text(stringResource(R.string.stop_observer))
        }
        Spacer(Modifier.height(32.dp))
        Button(onClick = { viewModel.uploadToStorage(context) }) {
            Text(stringResource(R.string.upload_to_storage))
        }
    }
}