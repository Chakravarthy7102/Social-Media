package android.example.getsocial

import android.content.Intent
import android.example.getsocial.dao.PostDao
import android.example.getsocial.models.Post
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), IPostAdapter {
    private lateinit var fab:FloatingActionButton
    private lateinit var recyclerView:RecyclerView
    private lateinit var adapter:PostAdapter
    private lateinit var postDao:PostDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        postDao= PostDao()
        fab=findViewById(R.id.floatingAction)
        recyclerView=findViewById(R.id.recyclerView)
        fireStoreAdapter()
        //changes the activity context
        fab.setOnClickListener(View.OnClickListener {
            val intent=Intent(this,PostActivity::class.java)
            startActivity(intent)
        })
    }

    private fun fireStoreAdapter() {
        val postCollection=postDao.postCollection
        val query=postCollection.orderBy("createdAt",Query.Direction.DESCENDING)
        val recyclerViewOptions=FirestoreRecyclerOptions.Builder<Post>().setQuery(query,Post::class.java).build()

        adapter= PostAdapter(recyclerViewOptions,this)


        recyclerView.adapter=adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.logout,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logoutMenu -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        Firebase.auth.signOut()
        val logout= Intent(this,LoginActivity::class.java)
        startActivity(logout)
        finish()
    }

    override fun likeCountListener(postId: String) {
        postDao.updateLikes(postId)
    }

}
