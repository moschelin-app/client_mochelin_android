package com.musthave0145.mochelins.adapter;

import static android.graphics.Color.LTGRAY;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.model.CalendarUtil;
import com.musthave0145.mochelins.model.OnItemListener;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    ArrayList<LocalDate> dayList;

    OnItemListener onItemListener;

    public CalendarAdapter(ArrayList<LocalDate> dayList){
        this.dayList = dayList;
    }

    @NonNull
    @Override
    public CalendarAdapter.CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.calendar_cell,parent,false);

        return new CalendarViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        //날짜 변수에 담기
        LocalDate day = dayList.get(position);

        if (day == null) {
            holder.dayText.setText("");
        } else {
            holder.dayText.setText(String.valueOf(day.getDayOfMonth()));

            //현재 날짜 색상 칠하기
            if (day.equals(CalendarUtil.selectedDate)) {
                holder.parentView.setBackgroundColor(LTGRAY);
            }
        }
        //날짜 적용

        if ((position + 1) % 7 == 0) { //토요일 파랑
            holder.dayText.setTextColor(Color.BLUE);
        } else if (position == 0 || position % 7 == 0) { //일요일 빨강
            holder.dayText.setTextColor(Color.RED);
        }
        //날짜 클릭 이벤트

    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    public class CalendarViewHolder extends RecyclerView.ViewHolder{
        //초기화
        TextView dayText;
        View parentView;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);

            dayText =itemView.findViewById(R.id.dayText);

            parentView = itemView.findViewById(R.id.parentView);

        }
    }
}