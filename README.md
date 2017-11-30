[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)


# LR-Swipes-ListView
Sample project for listview swipe animations.

![Demo gif](/Demo/demo.gif?raw=true "Demo")


## What's Happening in the Layout
We used FrameLayout as root so that we can acheive overlapping behaviour. 
See  ![List Item's Layout](https://github.com/talhahasanzia/LR-Swipes-ListView/blob/master/SwipeView/app/src/main/res/layout/item_list_view.xml) for more details.

## Whre the Magic Happens
The real code is in onTouch of ListAdapter class. Here is the sneak peek with comments to clarify code.
```
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
        }
```

## License

Copyright 2017 Talha Hasan Zia

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

See the [LICENSE.md](LICENSE.md) file for details
