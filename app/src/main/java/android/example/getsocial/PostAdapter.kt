package android.example.getsocial

import android.example.getsocial.R.drawable.ic_liked
import android.example.getsocial.R.drawable.ic_unliked
import android.example.getsocial.models.Post
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class PostAdapter(options:FirestoreRecyclerOptions<Post>, val listener:IPostAdapter): FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(options) {


    class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val userImage:ImageView=itemView.findViewById(R.id.userImage)
        val userName:TextView=itemView.findViewById(R.id.userName)
        val createdAt:TextView=itemView.findViewById(R.id.createdAt)
        val postTitle:TextView=itemView.findViewById(R.id.postTitle)
        val likeButton:ImageView=itemView.findViewById(R.id.likeButton)
        val likeCount:TextView=itemView.findViewById(R.id.likeCount)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val viewHolder= PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item,parent,false))
        viewHolder.likeButton.setOnClickListener{

            listener.likeCountListener(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        Glide.with(holder.userImage.context).load(model.createdBy.imageUrl).circleCrop().into(holder.userImage)
        holder.postTitle.text=model.text
        holder.userName.text=model.createdBy.displayName
        holder.createdAt.text=Utils.getTimeAgo(model.createdAt)
        holder.likeCount.text=model.likedBy.size.toString()


        val auth=Firebase.auth
        val currentUser=auth.currentUser!!.uid
        val isLiked=model.likedBy.contains(currentUser)
        if(isLiked){
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context,ic_liked))
        }else{
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, ic_unliked))
        }

    }
}
interface IPostAdapter{
    fun likeCountListener(postId:String)
}