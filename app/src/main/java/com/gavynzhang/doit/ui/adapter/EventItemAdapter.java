package com.gavynzhang.doit.ui.adapter;

import android.content.Intent;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gavynzhang.doit.R;
import com.gavynzhang.doit.app.state.MyApplication;
import com.gavynzhang.doit.model.entities.Event;
import com.gavynzhang.doit.ui.activities.EventDetailsActivity;
import com.gavynzhang.doit.ui.activities.MainActivity;

import java.util.List;

/**
 * Created by GavynZhang on 2016/8/23 21:46.
 */
public class EventItemAdapter extends RecyclerView.Adapter {

    private List<Event> mEvents;

    public EventItemAdapter(List<Event> events){
        this.mEvents = events;
    }

    @Override
    public EventItemAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, null);
        EventViewHolder viewHolder = new EventViewHolder(view);

        return viewHolder;
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EventViewHolder vh = (EventViewHolder)holder;
        final Event eventData = mEvents.get(position);
        vh.getItemEventCheckBox().setEnabled(false);

        try {
            vh.getItemEventName().setText(eventData.getName());

            String startDateString = eventData.getStartTime().getDate();
            String endDateString = eventData.getEndTime().getDate();
            //如果是普通事件
            if (eventData.getMode().intValue() == 0) {
                if (startDateString.substring(0, 10).equals(endDateString.substring(0, 10))) {
                    vh.getItemEventTime().setText(startDateString.substring(11, 16) + " - " + endDateString.substring(11, 16));
                } else {
                    vh.getItemEventTime().setText(startDateString.substring(5, 16) + " —— " + endDateString.substring(5, 16));
                }
            }else{
                //截止时间
                vh.getItemEventTime().setText("截止时间："+endDateString.substring(5, 16));
            }

            boolean isFinish;
            if (eventData.getIsFinish().intValue() == 0){
                isFinish = false;
            }else {
                isFinish = true;
            }
            vh.getItemEventCheckBox().setChecked(isFinish);

            if (eventData.getTag().equals("")) {
                vh.getItemTagImg().setVisibility(View.GONE);
            } else {
                vh.getItemEventTag().setText(eventData.getTag());
                vh.getItemTagImg().setVisibility(View.VISIBLE);
            }

            if (eventData.getMode().intValue() == 1){
                vh.getEventItemLayout().setBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.cardview_bac));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        /**
         * 进入事件详情
         * */
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.sMainActivity, EventDetailsActivity.class);
                intent.putExtra("event", eventData);
                MainActivity.sMainActivity.startActivity(intent);

                if(eventData.getMode().intValue() == 0){

                }else{

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{

        private TextView itemEventName;
        private TextView itemEventTime;
        private TextView itemEventTag;
        private AppCompatCheckBox itemEventCheckBox;
        private RelativeLayout eventItemLayout;

        private ImageView itemTagImg;

        public EventViewHolder(View itemView) {
            super(itemView);

            itemEventName = (TextView)itemView.findViewById(R.id.item_event_name);
            itemEventTime = (TextView)itemView.findViewById(R.id.item_event_time);
            itemEventTag = (TextView)itemView.findViewById(R.id.item_event_tag);
            itemEventCheckBox = (AppCompatCheckBox)itemView.findViewById(R.id.item_checkbox);
            eventItemLayout = (RelativeLayout)itemView.findViewById(R.id.event_item_layout);
            itemTagImg = (ImageView)itemView.findViewById(R.id.event_item_tag_img);


        }

        public RelativeLayout getEventItemLayout() {
            return eventItemLayout;
        }

        public void setEventItemLayout(RelativeLayout eventItemLayout) {
            this.eventItemLayout = eventItemLayout;
        }

        public AppCompatCheckBox getItemEventCheckBox() {
            return itemEventCheckBox;
        }

        public void setItemEventCheckBox(AppCompatCheckBox itemEventCheckBox) {
            this.itemEventCheckBox = itemEventCheckBox;
        }

        public TextView getItemEventName() {
            return itemEventName;
        }

        public void setItemEventName(TextView itemEventName) {
            this.itemEventName = itemEventName;
        }

        public TextView getItemEventTag() {
            return itemEventTag;
        }

        public void setItemEventTag(TextView itemEventTag) {
            this.itemEventTag = itemEventTag;
        }

        public TextView getItemEventTime() {
            return itemEventTime;
        }

        public void setItemEventTime(TextView itemEventTime) {
            this.itemEventTime = itemEventTime;
        }

        public ImageView getItemTagImg() {
            return itemTagImg;
        }

        public void setItemTagImg(ImageView itemTagImg) {
            itemTagImg = itemTagImg;
        }
    }
}
