package com.musthave0145.mochelins.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.musthave0145.mochelins.meeting.MeetingDetailActivity;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.model.Meeting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.ViewHolder>{

    Context context;
    ArrayList<Meeting> meetingArrayList;

    public MeetingAdapter(Context context, ArrayList<Meeting> meetingArrayList) {
        this.context = context;
        this.meetingArrayList = meetingArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meeting_row, parent, false);

        return new MeetingAdapter.ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meeting meeting = meetingArrayList.get(position);

//        Log.i("확인", meeting.content);


        // 미팅 사진이 없으면, 기본 이미지 표시 / 있으면, 그 사진을 표시하는 로직.
        // 원래는 null !!
        if (meeting.profile != null) {
            Glide.with(context).load(meeting.profile).into(holder.imgPhoto);
        }

        String strDis = String.format("%.2f",meeting.distance) + "km";
        holder.txtDistance.setText("📍 "+ strDis);
        holder.txtStoreName.setText("  "+meeting.storeName+"  ");
        holder.txtMeetName.setText(meeting.content);


        // TODO: 스토어ID로 주소를 가져와야 한다.
//        holder.txtStoreAddress.setText(meeting.);
        String newDate = "";

        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = inputDateFormat.parse(meeting.date);

            // 날짜 형식을 변경
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("M월 d일 (E) HH:mm", Locale.KOREA);

            // Calendar 객체를 사용하여 요일을 얻음
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.KOREA);

            // 변경된 날짜 형식 출력
            String formattedDate = outputDateFormat.format(date);
            newDate = formattedDate.replace("요일", dayOfWeek);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        holder.txtMeetingDate.setText(newDate);
        // TODO: imgProfile들에게 프로필 사진이 없으면 기본이미지, 있으면 해당이미지로 셋팅!
//        if (meeting.profiles != null) {
//            Glide.with(context).load(meeting.photo).into(holder.imgPhoto);
//        }

        // 총 정원과 현재 참가한 인원수를 붙여서 출력하자
        String strCurrentPerson = meeting.attend + " / " + meeting.maximum;
        holder.txtMeetCount.setText(strCurrentPerson);


    }

    @Override
    public int getItemCount() {
        return meetingArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView imgPhoto;
        TextView txtDistance;
        TextView txtStoreName;
        TextView txtMeetName;
        TextView txtStoreAddress;
        TextView txtMeetingDate;

        Integer[] imgProfileInteger = {R.id.imgProfile1, R.id.imgProfile2, R.id.imgProfile3,
                                        R.id.imgProfile4, R.id.imgProfile5, R.id.imgProfile6,
                                        R.id.imgProfile7};
        CircleImageView[] imgProfiles = new CircleImageView[imgProfileInteger.length];
//        CircleImageView imgProfile1;
//        CircleImageView imgProfile2;
//        CircleImageView imgProfile3;
//        CircleImageView imgProfile4;
//        CircleImageView imgProfile5;
//        CircleImageView imgProfile6;
//        CircleImageView imgProfile7;
        TextView txtMeetCount;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            txtDistance = itemView.findViewById(R.id.txtDistance);
            txtStoreName = itemView.findViewById(R.id.txtStoreName);
            txtMeetName = itemView.findViewById(R.id.txtMeetName);
            txtStoreAddress = itemView.findViewById(R.id.txtStoreAddress);
            txtMeetingDate = itemView.findViewById(R.id.txtMeetingDate);
            cardView = itemView.findViewById(R.id.cardView);

            for (int i = 0; i < imgProfileInteger.length ; i++) {
                imgProfiles[i] = itemView.findViewById(imgProfileInteger[i]);
            }
//            imgProfile1 = itemView.findViewById(R.id.imgProfile1);
//            imgProfile2 = itemView.findViewById(R.id.imgProfile2);
//            imgProfile3 = itemView.findViewById(R.id.imgProfile3);
//            imgProfile4 = itemView.findViewById(R.id.imgProfile4);
//            imgProfile5 = itemView.findViewById(R.id.imgProfile5);
//            imgProfile6 = itemView.findViewById(R.id.imgProfile6);
//            imgProfile7 = itemView.findViewById(R.id.imgProfile7);
            txtMeetCount = itemView.findViewById(R.id.txtMeetCount);



            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    Meeting meeting = meetingArrayList.get(index);

                    // TODO; 여기에 유저가 누른 모임을 상세하게 보여주도록, 상세모임 액티비티 띠우고, 해당 모임의 정보도 보내준다.

                    Intent intent = new Intent(context, MeetingDetailActivity.class);
//                    intent.putExtra("meeting", meeting);
                    intent.putExtra("meetingId", meeting.id);
                    context.startActivity(intent);

                }
            });


        }
    }
}
