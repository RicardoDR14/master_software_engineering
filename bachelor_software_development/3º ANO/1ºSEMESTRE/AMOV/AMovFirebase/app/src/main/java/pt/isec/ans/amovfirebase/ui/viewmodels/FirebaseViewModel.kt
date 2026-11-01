package pt.isec.ans.amovfirebase.ui.viewmodels

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import pt.isec.ans.amovfirebase.utils.FAuthUtil
import pt.isec.ans.amovfirebase.utils.FStorageUtil

data class User(val name: String, val email:String, val picture:String?)

fun FirebaseUser.toUser() : User {
    val displayName = this.displayName ?: ""
    val strEmail = this.email ?: "n.d."
    val picture = this.photoUrl?.toString()
    return User(displayName,strEmail,picture)
}

class FirebaseViewModel : ViewModel() {
    private val _user = mutableStateOf(FAuthUtil.currentUser?.toUser())
    val user : State<User?>
        get() = _user

    private val _error = mutableStateOf<String?>(null)
    val error : State<String?>
        get() = _error

    fun createUserWithEmail(email: String, password: String) {
        if (email.isBlank() || password.isBlank())
            return

        viewModelScope.launch {
            FAuthUtil.createUserWithEmail(email, password) { exception ->
                if (exception == null)
                    _user.value = FAuthUtil.currentUser?.toUser()
                _error.value = exception?.message
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        if (email.isBlank() || password.isBlank())
            return
        viewModelScope.launch {
            FAuthUtil.signInWithEmail(email, password) { exception ->
                if (exception == null)
                    _user.value = FAuthUtil.currentUser?.toUser()
                _error.value = exception?.message
            }
        }
    }

    fun signOut() {
        FAuthUtil.signOut()
        _user.value = null
        _error.value = null
    }

    // FIRESTORE AND STORAGE

    private val _nrGames = mutableLongStateOf(0L)
    val nrGames : State<Long>
        get() = _nrGames

    private val _topScore = mutableLongStateOf(0L)
    val topScore : State<Long>
        get() = _topScore

    fun addDataToFirestore() {
        viewModelScope.launch {
            FStorageUtil.addDataToFirestore { exception ->
                _error.value = exception?.message
            }
        }
    }

    fun updateDataInFirestore() {
        viewModelScope.launch {
            //FirebaseUtils.updateDataInFirestore()
            FStorageUtil.updateDataInFirestoreTrans { exception ->
                _error.value = exception?.message
            }
        }
    }

    fun removeDataFromFirestore() {
        viewModelScope.launch {
            FStorageUtil.removeDataFromFirestore { exception ->
                _error.value = exception?.message
            }
        }
    }

    fun startObserver() {
        viewModelScope.launch {
            FStorageUtil.startObserver { g, t ->
                _nrGames.longValue = g
                _topScore.longValue = t
            }
        }
    }

    fun stopObserver() {
        viewModelScope.launch {
            FStorageUtil.stopObserver()
        }
    }

    fun uploadToStorage(context : Context) {
        viewModelScope.launch {
            FStorageUtil.getFileFromAsset(context.assets,"images/img.png")?.let { inputStream ->
                FStorageUtil.uploadFile(inputStream,"image.png")
            }
        }
    }


}