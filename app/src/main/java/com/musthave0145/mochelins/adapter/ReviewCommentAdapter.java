package com.musthave0145.mochelins.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.model.Comment;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewCommentAdapter extends RecyclerView.Adapter<ReviewCommentAdapter.ViewHolder>  {

    Context context;
    ArrayList<Comment> commentArrayList;

    public ReviewCommentAdapter(Context context, ArrayList<Comment> commentArrayList) {
        this.context = context;
        this.commentArrayList = commentArrayList;
    }

    @NonNull
    @Override
    public ReviewCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_comment_row, parent, false);

        return new ReviewCommentAdapter.ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = commentArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imgProfile;
        

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}
