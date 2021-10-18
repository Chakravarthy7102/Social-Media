package android.example.getsocial

import android.example.getsocial.dao.PostDao
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class PostActivity : AppCompatActivity() {
    private lateinit var editText:EditText
    private lateinit var post:Button
    private lateinit var postDao:PostDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        post=findViewById(R.id.postButton)
        editText=findViewById(R.id.editText)
        postDao=PostDao()

        post.setOnClickListener(View.OnClickListener {
            val postVal=editText.text.toString().trim()
            if (postVal.isNotEmpty()){
                postDao.addPost(postVal)
                finish()
            }

        })

    }
}