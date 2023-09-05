package com.musthave0145.mochelins.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

        // 프로필 사진 넣기
        Glide.with(context).load(comment.profile).into(holder.imgProfile);
        // 이름과, 시간, 코멘트 나오게 하기
        holder.txtName.setText(comment.nickname + " · " + comment.createdAt);
        holder.txtComment.setText(comment.content);

        // TODO: 메뉴 버튼을 누르면, 서브 메뉴가 나오고, 수정과 삭제를 할 수 있도록 한다.
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imgProfile;
        TextView txtName;
        TextView txtComment;
        ImageButton imgMenu;
        

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            txtName = itemView.findViewById(R.id.txtName);
            txtComment = itemView.findViewById(R.id.txtComments);
            imgMenu = itemView.findViewById(R.id.imgMenu);

        }
    }


}
