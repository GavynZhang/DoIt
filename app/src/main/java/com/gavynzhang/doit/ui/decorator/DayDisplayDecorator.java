package com.gavynzhang.doit.ui.decorator;

import com.gavynzhang.doit.R;
import com.gavynzhang.doit.app.state.MyApplication;
import com.gavynzhang.doit.model.entities.Event;
import com.gavynzhang.doit.utils.LogUtils;
import com.gavynzhang.doit.utils.TimeUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.List;

/**
 * Created by GavynZhang on 2016/8/24 13:26.
 */
public class DayDisplayDecorator implements DayViewDecorator {

    List<Event> mEvents;

    public DayDisplayDecorator(List<Event> events){
        this.mEvents = events;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
//        if (TimeUtils.string2Milliseconds(day.getDate().toString()) > )
        String dayString = day.getYear()+"-"+day.getMonth()+"-"+day.getDay()+" "+"00:00:00";
        long nowDayBeginMill = TimeUtils.string2Milliseconds(dayString);
        long nowDayEndMill = nowDayBeginMill + 86400 * 1000;

        //LogUtils.d("DayDisplayDecorator", "dayString: "+dayString+" dayMill: "+TimeUtils.string2Milliseconds(dayString));
        //LogUtils.d("DayDisplayDector", "CalendarDay day : "+day.getDate().toString()+" getDay: "+day.getDay()+" getMonth: "+day.getMonth()+" getYear: "+day.getYear());

        boolean isDecorator = false;

        for (int i = 0; i < mEvents.size(); i++){
            Event event = mEvents.get(i);

            long eventStartTimeMill = TimeUtils.string2Milliseconds(event.getStartTime().getDate());
            long eventEndTimeMill = TimeUtils.string2Milliseconds(event.getEndTime().getDate());

            if (nowDayBeginMill < eventStartTimeMill && eventStartTimeMill < nowDayEndMill){
                isDecorator = true;
                break;
            }else if (nowDayBeginMill < eventEndTimeMill && nowDayBeginMill > eventStartTimeMill){
                isDecorator = true;
                break;
            }
            else
                isDecorator = false;
        }

        LogUtils.d("DayDisplayDecorator", "isDecorator: "+isDecorator);
        return isDecorator;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(MyApplication.getContext().getResources().getDrawable(R.drawable.circle));
    }

}
