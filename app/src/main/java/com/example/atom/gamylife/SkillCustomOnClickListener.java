package com.example.atom.gamylife;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.atom.gamylife.Skills;

/**
 * Created by Atom on 04/04/2017.
 */

public class SkillCustomOnClickListener implements RecyclerView.OnItemTouchListener{

    //Interface to set on click actions
    public interface RecyclerViewItemListener {

        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    GestureDetector gestureDetector;
    private RecyclerViewItemListener clickListener;

    public SkillCustomOnClickListener(Context context, final RecyclerView recyclerView,
                                      RecyclerViewItemListener listener) {
        clickListener = listener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {

                return true;
            }

            //Find long pressed item via the press coords.
            @Override
            public void onLongPress(MotionEvent e) {

                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent e) {

        View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(child, recyclerView.getChildLayoutPosition(child));
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }



}
