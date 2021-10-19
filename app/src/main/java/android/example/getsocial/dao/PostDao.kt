package android.example.getsocial.dao

import android.example.getsocial.models.Post
import android.example.getsocial.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {
    val db=FirebaseFirestore.getInstance()
    val postCollection=db.collection("posts")
    val auth= Firebase.auth
    fun addPost(text:String){
        val currentUser=auth.currentUser!!.uid
        GlobalScope.launch(Dispatchers.IO){
            val userDao=UserDao()
            val user=userDao.getUserById(currentUser).await().toObject(User::class.java)!!
            val currentTime=System.currentTimeMillis()
            val post= Post(text,user,currentTime)
            postCollection.document().set(post)
        }
    }
     fun updateLikes(postId:String){
        val currentUser=auth.currentUser!!.uid
         GlobalScope.launch (Dispatchers.IO){
             val post =getPostById(postId).await().toObject(Post::class.java)
             val isLiked= post!!.likedBy.contains(currentUser)
             if(isLiked){
                 post.likedBy.remove(currentUser)
             }else{
                 post.likedBy.add(currentUser)
             }
             postCollection.document(postId).set(post)
         }
    }

    private fun getPostById(postId: String): Task<DocumentSnapshot> {
        return postCollection.document(postId).get()

    }


}