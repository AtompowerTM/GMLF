package com.example.atom.gamylife;

import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

/**
 * Created by Atom on 21/04/2017.
 */

public class CalendarFragment extends Fragment implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;

    List<WeekViewEvent> events;
    private ArrayList<Skill> skillEntries;
    private ArrayList<Quest> questEntries;

    Context context;

    private int pencho = 0;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View layout = inflater.inflate(R.layout.activity_calendar_fragment, viewGroup, false);

        context = layout.getContext();

        //fetch questEntries and skillEntries
        skillEntries = ((MainActivity) getActivity()).skillEntries;
        questEntries = ((MainActivity) getActivity()).questEntries;

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) layout.findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);

        return layout;
    }

    /*@Override
    public void onResume() {
        super.onResume();

        Log.d("adding event", "Event 3");
        Calendar startTime = Calendar.getInstance();
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.MINUTE, 85);

        WeekViewEvent event = new WeekViewEvent(10, "added one", startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_03));

        events.add(event);

        mWeekView.notifyDatasetChanged();

    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_week_view);
        switch (id){
            case R.id.action_today:
                mWeekView.goToToday();
                return true;
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" d/M", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }
    protected String getEventTitle(int position) {
        return questEntries.get(position).getName();
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        //Toast.makeText(this, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
        Log.d("Clicked ", event.getName() + " with ID: " + event.getId());
        /*Intent intent = new Intent(context, QuestAdd.class);

        Bundle bundle = new Bundle();
        bundle.putInt("mode", 2); //mode 1 is add quest; mode 2 is edit quest
        bundle.putParcelableArrayList("quests", questEntries);
        bundle.putParcelableArrayList("skills", skillEntries);

        //Translate mainQuestEntries position to questEntries position
        Log.d("oldPosition", Integer.toString(position));
        position = questEntries.indexOf(mainQuestEntries.get(position));
        Log.d("newPosition", Integer.toString(position));

        bundle.putInt("questIndex", position);
        intent.putExtra("bundle", bundle);
        startActivityForResult(intent, 2);*/
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        //Toast.makeText(this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
        Log.d("Long pressed event: ", event.getName());
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        //Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
        //Log.d("Empty view long pressed", getEventTitle(time));
    }

    /*public WeekView getWeekView() {
        return mWeekView;
    }*/

    private void populateEvents(int newYear, int newMonth) {

        Calendar eventStart = Calendar.getInstance();
        Calendar eventEnd = Calendar.getInstance();
        WeekViewEvent event;
        int questMonth;
        int questYear;

        /*Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(10, "this is a generic event", startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        events.add(event);*/

        for(Quest quest : questEntries) {
            Log.d("LookingAtQuestName", quest.getName());


            if(!quest.getCompleted() && quest.getScheduled() != null) {
                Log.d("QuestPassed1IF", quest.getName());

                //Calendar scheduledFor = Calendar.getInstance();
                //scheduledFor.setTime(quest.getScheduled());
                eventStart = Calendar.getInstance();
                eventStart.setTime(quest.getScheduled());
               /* eventStart.set(Calendar.HOUR_OF_DAY, scheduledFor.get(Calendar.HOUR_OF_DAY));
                eventStart.set(Calendar.MINUTE, scheduledFor.get(Calendar.MINUTE));
                eventStart.set(Calendar.DATE, scheduledFor.get(Calendar.DATE));
                eventStart.set(Calendar.MONTH, scheduledFor.get(Calendar.MONTH));
                eventStart.set(Calendar.YEAR, scheduledFor.get(Calendar.YEAR));*/
                //eventStart.setTime(scheduledFor);
                eventEnd = ((Calendar)eventStart.clone());

                questMonth = eventStart.get(Calendar.MONTH);
                questYear = eventStart.get(Calendar.YEAR);

                if(questYear == (newYear) && questMonth == (newMonth - 1)) {
                    Log.d("QuestPassed2IF", quest.getName());
                    /*eventEnd.set(Calendar.HOUR_OF_DAY, eventStart.get(Calendar.HOUR_OF_DAY));
                    eventEnd.set(Calendar.MINUTE, eventStart.get(Calendar.MINUTE));
                    eventEnd.set(Calendar.DATE, eventStart.get(Calendar.DATE));
                    eventEnd.set(Calendar.MONTH, eventStart.get(Calendar.MONTH));
                    eventEnd.set(Calendar.YEAR, eventStart.get(Calendar.YEAR));*/
                    eventEnd.add(Calendar.MINUTE, quest.getDuration());

                    Log.d("EventEnd", "Event: " + quest.getName() + " with start: " + eventStart.getTime() + " and with end: " + eventEnd.getTime());
                    event = new WeekViewEvent(quest.getID(), quest.getName(), eventStart, eventEnd);
                    event.setColor(getResources().getColor(R.color.event_color_01));

                    events.add(event);
                }
            }
        }
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        Log.d("MakingList", "making list" + pencho);
        // Populate the week view with some events.
        events = new ArrayList<WeekViewEvent>();
        populateEvents(newYear, newMonth);

        Log.d("EventsSize", Integer.toString(events.size()));
        return events;
    }
}
