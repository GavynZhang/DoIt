package com.gavynzhang.doit.ui.adapter;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gavynzhang.doit.R;
import com.gavynzhang.doit.model.entities.Event;

import java.util.List;

/**
 * Created by GavynZhang on 2016/8/23 21:46.
 */
public class EventItemAdapter extends RecyclerView.Adapter {

    private List<Event> mEvents;

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

        try {
            vh.getItemEventName().setText(eventData.getName());
            vh.getItemEventTime().setText(eventData.getStartTime().getDate() + "-" + eventData.getEndTime().getDate());
            boolean isFinish;
            if (eventData.getFinish().intValue() == 0){
                isFinish = false;
            }else {
                isFinish = true;
            }
            vh.getItemEventCheckBox().setChecked(isFinish);
            if (eventData.getTag() != null) {
                vh.getItemEventTag().setText(eventData.getTag());
            } else {
                vh.getItemTagImg().setVisibility(View.GONE);
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

        private ImageView itemTagImg;

        public EventViewHolder(View itemView) {
            super(itemView);

            itemEventName = (TextView)itemView.findViewById(R.id.item_event_name);
            itemEventTime = (TextView)itemView.findViewById(R.id.item_event_time);
            itemEventTag = (TextView)itemView.findViewById(R.id.item_event_tag);
            itemEventCheckBox = (AppCompatCheckBox)itemView.findViewById(R.id.item_checkbox);
            itemTagImg = (ImageView)itemView.findViewById(R.id.event_item_tag_img);


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
