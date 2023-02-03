package com.example.turkmen_mehmetcan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentHolder> {

    List<Comment> commentArrayList;
    Context context;

    public CommentsAdapter(Context context, List<Comment> commentArrayList) {
        this.commentArrayList = commentArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.comments_row_layout, parent, false);
        CommentHolder holder = new CommentHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        holder.txtName.setText(commentArrayList.get(holder.getAdapterPosition()).getName());
        holder.txtMessage.setText(commentArrayList.get(holder.getAdapterPosition()).getMessage());
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtMessage;
        ConstraintLayout row;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
             txtName = itemView.findViewById(R.id.txtName);
             txtMessage = itemView.findViewById(R.id.txtMessage);
             row = itemView.findViewById(R.id.comment_row_list);
        }
    }
}
