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
import com.musthave0145.mochelins.meeting.MeetingDetailActivity;
import com.musthave0145.mochelins.model.Meeting;
import com.musthave0145.mochelins.model.Store;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoreMeetingAdapter extends RecyclerView.Adapter<StoreMeetingAdapter.ViewHolder> {

    Context context;
    ArrayList<Meeting> meetingArrayList;

    public StoreMeetingAdapter(Context context, ArrayList<Meeting> meetingArrayList) {
        this.context = context;
        this.meetingArrayList = meetingArrayList;
    }

    @NonNull
    @Override
    public StoreMeetingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_meeting_row,
                parent, false);
        return new StoreMeetingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meeting meeting = meetingArrayList.get(position);
        // 모임 대표사진 셋팅
        Glide.with(context).load(meeting.photo).error(R.drawable.not_image).into(holder.storePhoto);
        holder.storePhoto.setClipToOutline(true);
        // 모일날짜 셋팅
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

        // 작성자 프로필사진,이름 셋팅
        Glide.with(context).load(meeting.profile).error(R.drawable.default_profile).into(holder.imgProfile);
        holder.txtName.setText(meeting.nickname);

        // 내용 셋팅
        holder.txtContent.setText(meeting.content);

        // 참가한 인원만큼 프로필사진을 보여주자!
        for(int i = 0; i < meeting.profiles.size(); i++){
            if(i >= holder.imgProfiles.length){
                break;
            }
            holder.imgProfileList[i].setVisibility(View.VISIBLE);
            Glide.with(context).load(meeting.profiles.get(i).profile).
                    fallback(R.drawable.default_profile).error(R.drawable.default_profile).into(holder.imgProfileList[i]);
        }

        // 총 정원과 현재 참가한 인원수를 붙여서 출력하자
        String strCurrentPerson = meeting.attend + " / " + meeting.maximum;
        holder.txtMeetCount.setText(strCurrentPerson);


    }

    @Override
    public int getItemCount() {
        return meetingArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView storePhoto;
        TextView txtMeetingDate;
        CircleImageView imgProfile;
        TextView txtName;
        TextView txtContent;
        TextView txtMeetCount;
        CardView cardView;

        Integer[] imgProfiles = {R.id.imgProfile1, R.id.imgProfile2, R.id.imgProfile3,
                                 R.id.imgProfile4, R.id.imgProfile5, R.id.imgProfile6,
                                 R.id.imgProfile7};
        CircleImageView[] imgProfileList = new CircleImageView[imgProfiles.length];

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            storePhoto = itemView.findViewById(R.id.storePhoto);
            txtMeetingDate = itemView.findViewById(R.id.txtMeetingDate);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            txtName = itemView.findViewById(R.id.txtName);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtMeetCount = itemView.findViewById(R.id.txtMeetCount);
            cardView = itemView.findViewById(R.id.cardView);

            for (int i = 0; i < imgProfiles.length; i++) {
                imgProfileList[i] = itemView.findViewById(imgProfiles[i]);
            }

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    Meeting meeting = meetingArrayList.get(index);

                    Intent intent = new Intent(context, MeetingDetailActivity.class);
                    intent.putExtra("meetingId", meeting.id);

                    context.startActivity(intent);

                }
            });
        }
    }
}
