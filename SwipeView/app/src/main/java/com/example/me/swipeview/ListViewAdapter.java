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


        textView.setOnTouchListener(new View.OnTouchListener() {

            float dX;

            int lastAction;
            float startingMove;


            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if (mWidth == 0) {
                    mWidth = view.getMeasuredWidth();
                    midPoint = mWidth / 2.0f;
                }

                if (measuredFromLeft == 0) {


                    measuredFromRight = remove.getMeasuredWidth();
                    measuredFromLeft = measuredFromRight * 2;

                }
                //Log.d(TAG, "onTouch: " + event.getActionMasked());
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        //dY = view.getY() - event.getRawY();
                        lastAction = MotionEvent.ACTION_DOWN;
                        startingMove = view.getX();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // view.setY(event.getRawY() + dY);
                        view.setX(event.getRawX() + dX);
                        lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        if (lastAction == MotionEvent.ACTION_MOVE) {


                            Log.d(TAG, "onTouch: X-axis of view: " + view.getX());
                            if (view.getX() - startingMove > 0) {
                                // Log.d(TAG, "onTouch: swiped right");

                                if (view.getX() > midPoint) {
                                    Toast.makeText(context, "Full Swipe to Right", Toast.LENGTH_SHORT).show();
                                    restore(view);
                                } else if (CURRENT_STATE == RIGHT_OPTIONS_OPENED) {
                                    restore(view);
                                } else {
                                    toLeftOptions(view);
                                }
                            } else {


                                //Log.d(TAG, "onTouch: swiped left");
                                if (view.getX() * -1 > midPoint) {
                                    Toast.makeText(context, "Full Swipe to Left", Toast.LENGTH_SHORT).show();


                                } else if (CURRENT_STATE == LEFT_OPTIONS_OPENED) {

                                    restore(view);
                                } else {
                                    toRightOptions(view);
                                }
                            }


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
