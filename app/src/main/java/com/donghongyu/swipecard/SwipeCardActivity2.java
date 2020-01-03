package com.donghongyu.swipecard;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.donghongyu.swipecard.custom.SwipeContentView;
import com.donghongyu.swipecard.custom.SwipeContentView2;
import com.donghongyu.swipecard.custom.SwipeContentView3;
import com.donghongyu.swipecard.custom.SwipeContentView4;
import com.donghongyu.swipecard.view.SwipeCardLayout;

import java.util.ArrayList;
import java.util.List;


public class SwipeCardActivity2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_card2);
        SwipeCardLayout scl_layout = findViewById(R.id.scl_layout);

        List<View> data = new ArrayList<>();
        View view = new SwipeContentView(this);
        View view2 = new SwipeContentView2(this);
        View view3 = new SwipeContentView3(this);
        View view4 = new SwipeContentView4(this);

        data.add(view4);
        data.add(view3);
        data.add(view2);
        data.add(view);

        scl_layout.setAdapter(new SwipeCardLayout.CardAdapter(data));
        scl_layout.setOnSwipeListener(new SwipeCardLayout.OnSwipeListener() {
            @Override
            public void onSwipe(int type) {
                switch (type) {
                    case SwipeCardLayout.TYPE_RIGHT:
                        Toast.makeText(SwipeCardActivity2.this, "right", Toast.LENGTH_SHORT).show();

                        break;
                    case SwipeCardLayout.TYPE_LEFT:
                        Toast.makeText(SwipeCardActivity2.this, "left", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

}
