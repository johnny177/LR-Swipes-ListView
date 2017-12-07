package com.example.me.swipeview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by VD-Test on 29-Nov-17.
 */

public class ListViewAdapter extends ArrayAdapter {

    Context context;


    int CURRENT_STATE = 0;

    static final int LEFT_OPTIONS_OPENED = 1;
    static final int RIGHT_OPTIONS_OPENED = 2;


    float mWidth = 0;

    int measuredFromLeft = 0;
    int measuredFromRight = 0;

    String TAG = "ListViewAdapter";

    float midPoint;


    public ListViewAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 5;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_view, parent, false);
        }


        final ImageView remove = convertView.findViewById(R.id.remove_imageview);


        TextView textView = convertView.findViewById(R.id.list_item_text_view);


        textView.setText("item " + (position + 1));

        // this is where the real magic happens
        textView.setOnTouchListener(new View.OnTouchListener() {

            // to register distance between View's X and screen touch's X coordinate
            float dX;

            int lastAction; // last action to be tracked
            float startingMove; // starting X coodinates


            @Override
            public boolean onTouch(View view, MotionEvent event) {

                // assuming that the view to be swiped has full screen width
                // initialize width if it is not initialized yet
                if (mWidth == 0) {
                    mWidth = view.getMeasuredWidth();
                    midPoint = mWidth / 2.0f;
                }

                // this block saves width of the buttons/views where top view will settle after swipe
                if (measuredFromLeft == 0) {


                    measuredFromRight = remove.getMeasuredWidth(); // it will settle from a distance of 1 view unit from right
                    measuredFromLeft = measuredFromRight * 2;       // it will settle from a distance of 2 view unit from right

                }
                //Log.d(TAG, "onTouch: " + event.getActionMasked());
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX(); // calculate distance between pointer touch and view initials
                        // this is necessary other wise view will jump to whereeven the screen is Tapped

                        lastAction = MotionEvent.ACTION_DOWN; // set last action for future checks (in ACTION_UP)
                        startingMove = view.getX(); // starting X coordinates
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // while user drags move the view along
                        view.setX(event.getRawX() + dX); // with difference in views initial position to be covered
                        lastAction = MotionEvent.ACTION_MOVE; // update last action
                        break;

                    case MotionEvent.ACTION_CANCEL: // in case the pointer (Down) is canceled
                    case MotionEvent.ACTION_UP: {
                        // if this was after a drag, calculate position and states
                        if (lastAction == MotionEvent.ACTION_MOVE) {


                            //  Log.d(TAG, "onTouch: X-axis of view: " + view.getX());
                            if (view.getX() - startingMove > 0) {  // RIGHT SWIPE
                                // Log.d(TAG, "onTouch: swiped right");


                                // if swipe is as long as it crossed half the screen length
                                if (view.getX() > midPoint) {
                                    Toast.makeText(context, "Full Swipe to Right", Toast.LENGTH_SHORT).show();
                                    restore(view);
                                } else if (CURRENT_STATE == RIGHT_OPTIONS_OPENED) { // if right views were visible
                                    restore(view); // they should be hidden
                                } else {
                                    toLeftOptions(view); // no views were visible so right swipe should show left options
                                }
                            } else {

                                // if not right swipe then left swipe  (duh)
                                //Log.d(TAG, "onTouch: swiped left");
                                if (view.getX() * -1 > midPoint) { // check if swipe passed mid screen as full swipe
                                    Toast.makeText(context, "Full Swipe to Left", Toast.LENGTH_SHORT).show();
                                    restore(view);

                                } else if (CURRENT_STATE == LEFT_OPTIONS_OPENED) {
                                    // if left options (views) are visible restore the whole view
                                    restore(view);
                                } else { // if the list item was in initial position then show right options on left swipe
                                    toRightOptions(view);
                                }
                            }

                            // NOTE THAT ONLY ONE SIDE OPTIONS ARE VISIBLE AT ONCE
                        }
                    }
                    break;

                }

                //Log.d(TAG, "onTouch: " + mWidth);
                return true;
            }
        });
        return convertView;
    }

    public void toLeftOptions(View view) {

        CURRENT_STATE = LEFT_OPTIONS_OPENED;

        float measuredFromLeft = this.measuredFromLeft;


        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationX", measuredFromLeft);
        animation.setDuration(300);
        animation.start();


    }

    public void toRightOptions(View view) {

        CURRENT_STATE = RIGHT_OPTIONS_OPENED;

        float measured = this.measuredFromRight;


        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationX", -measured);
        animation.setDuration(300);
        animation.start();


    }


    public void restore(View view) {

        CURRENT_STATE = 0;


        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationX", 0);
        animation.setDuration(200);
        animation.start();


    }

}
