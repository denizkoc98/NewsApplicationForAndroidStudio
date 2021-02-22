package com.example.denizkochomework3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentsViewHolder>{


    List<CommentItem> commentItems;
    Context context;


    public CommentAdapter(List<CommentItem> commentItems, Context context) {
        this.commentItems = commentItems;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.comment_row_layout,parent, false);

        return new CommentsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, final int position) {

        holder.commentName.setText(commentItems.get(position).getName());
        holder.message.setText(commentItems.get(position).getMessage());

    }

    @Override
    public int getItemCount() {
        return commentItems.size();
    }
    
    public interface CommentItemClickListener{
        public void commentItemClicked(CommentItem selectedCommentItem);
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder{

        TextView commentName;
        TextView message;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);

            commentName=itemView.findViewById(R.id.comment_name);
            message=itemView.findViewById(R.id.comment_message);

        }
    }

    public void setComments(List<CommentItem> comments) {
        this.commentItems = comments;
    }
}
