package android.example.getsocial.dao

import android.example.getsocial.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {
    private val db=FirebaseFirestore.getInstance()
    val userCollection=db.collection("users")

    fun addUser(user: User?){
        //above line checks for the nullability of the variable object user
        user?.let {
            GlobalScope.launch (Dispatchers.IO){
                userCollection.document(user.uid).set(it)
            }
        }

    }
    fun getUserById(uid:String): Task<DocumentSnapshot>{
        return userCollection.document(uid).get()
    }
}