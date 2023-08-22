package com.musthave0145.mochelins.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.ReviewDetailActivity;
import com.musthave0145.mochelins.model.Review;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

    Context context;
    ArrayList<Review> reviewArrayList;

    public ReviewAdapter(Context context, ArrayList<Review> reviewArrayList) {
        this.context = context;
        this.reviewArrayList = reviewArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_row, parent, false);

        return new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviewArrayList.get(position);

        // API가 완성되면 다시 수정하자!


        // 작성자의 프로필 사진 표시(없으면 기본 이미지, 있으면 올린 사진으로), 이름과 작성 시간을 표시하자!

        if (review.personUrl == null) {
            return;
        } else {
            Glide.with(context).load(review.personUrl).into(holder.imgPerson);
            holder.txtPerson.setText(review.person);
            holder.txtTime.setText(review.time);
        }


        // 좋아요 표시 (내가 좋아요를 눌렀을 때는 빨간 하트로, 내가 좋아요를 누르지 않았을 때는 빈하트 반영)
        if( review.isLike == 1){
            holder.imgLike.setImageResource(R.drawable.baseline_favorite_24);
        }else {
            holder.imgLike.setImageResource(R.drawable.outline_favorite_border_24);
        }

        // 가게가 위치하는 동네 이름과 내 기준 거리를 보여주자! (ex, 숭의동 (10.9km))
        holder.txtTown.setText(review.town);
        holder.txtDistance.setText(review.distance);

        // 평점과 상호명을 보여주자!!
        holder.txtRating.setText(review.rating);
        holder.txtName.setText(review.name);

        // 태그를 보여주는데, 선택된 태그가 없으면 가리고 보여주자!

        if (review.tags.size() ==0 ) {
            return;
        } else if (review.tags.size() == 1) {
            holder.txtTag1.setText(review.tags.get(0));
        } else if (review.tags.size() == 2) {
            holder.txtTag1.setText(review.tags.get(0));
            holder.txtTag2.setText(review.tags.get(1));
        } else if (review.tags.size() >= 3) {
            holder.txtTag1.setText(review.tags.get(0));
            holder.txtTag2.setText(review.tags.get(1));
            holder.txtTag3.setText(review.tags.get(2));
        }


        // 숫자들 형변환을 시켜주자!
        String strViews = "조회수 " + review.views;
        String strComments = "댓글수 " + review.comments;
        String strLikes = "좋아요 " + review.likes;

        // 조회수, 댓글수, 좋아요수를 보여주자!!
        holder.txtViews.setText(strViews);
        holder.txtComments.setText(strComments);
        holder.txtLikes.setText(strLikes);


    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        CardView cardView;
        CircleImageView imgPerson;
        TextView txtPerson;
        TextView txtTime;

        ImageView imgLike;
        TextView txtTown;
        TextView txtDistance;

        TextView txtRating;
        TextView txtName;

        TextView txtTag1;
        TextView txtTag2;
        TextView txtTag3;

        TextView txtViews;
        TextView txtComments;
        TextView txtLikes;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);

            imgPerson = itemView.findViewById(R.id.imgPerson);
            txtPerson = itemView.findViewById(R.id.txtPerson);
            txtTime = itemView.findViewById(R.id.txtTime);

            imgLike = itemView.findViewById(R.id.imgLike);
            txtTown = itemView.findViewById(R.id.txtTown);
            txtDistance = itemView.findViewById(R.id.txtDistance);

            txtRating = itemView.findViewById(R.id.txtRating);
            txtName = itemView.findViewById(R.id.txtStoreName);

            txtTag1 = itemView.findViewById(R.id.txtTag1);
            txtTag2 = itemView.findViewById(R.id.txtTag2);
            txtTag3 = itemView.findViewById(R.id.txtTag3);

            txtViews = itemView.findViewById(R.id.txtViews);
            txtComments = itemView.findViewById(R.id.txtComments);
            txtLikes = itemView.findViewById(R.id.txtLikes);


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    Review review = reviewArrayList.get(index);

                    // TODO; 여기에 유저가 누른 리뷰를 상세하게 보여주도록, 상세리뷰 액티비티 띠우고, 그 리뷰의 정보도 보내준다.

                    Intent intent = new Intent(context, ReviewDetailActivity.class);
                    intent.putExtra("review", review);
                    context.startActivity(intent);
                }
            });



        }
    }
}
