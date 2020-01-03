package com.donghongyu.swipecard;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.donghongyu.swipecard.view.SwipeCardLayout;
import com.donghongyu.swipecard.view.SwipeCardLayout2;

import java.util.LinkedList;
import java.util.Queue;


public class SwipeCardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_card);
        SwipeCardLayout2 scl_layout = findViewById(R.id.scl_layout);

        Queue<SwipeCardLayout2.CardEntity> data = new LinkedList<>();
        SwipeCardLayout2.CardEntity cardEntity1 = new SwipeCardLayout2.CardEntity(R.drawable.f1, "1");
        SwipeCardLayout2.CardEntity cardEntity2 = new SwipeCardLayout2.CardEntity(R.drawable.f2, "2");
        SwipeCardLayout2.CardEntity cardEntity3 = new SwipeCardLayout2.CardEntity(R.drawable.f3, "3");
        SwipeCardLayout2.CardEntity cardEntity4 = new SwipeCardLayout2.CardEntity(R.drawable.f4, "4");
        SwipeCardLayout2.CardEntity cardEntity5 = new SwipeCardLayout2.CardEntity(R.drawable.f5, "5");

        data.add(cardEntity5);
        data.add(cardEntity4);
        data.add(cardEntity3);
        data.add(cardEntity2);
        data.add(cardEntity1);

        scl_layout.setAdapter(new SwipeCardLayout2.CardAdapter<SwipeCardLayout2.CardEntity>(data) {
            @Override
            public View bindLayout() {
                return LayoutInflater.from(SwipeCardActivity.this).inflate(R.layout.card_layout, null);
            }

            @Override
            public void bindData(SwipeCardLayout2.CardEntity data, View convertView) {

                ImageView iv_card = (ImageView) convertView.findViewById(R.id.iv_card);
                TextView tv_card = (TextView) convertView.findViewById(R.id.tv_card);
                iv_card.setImageResource(data.resId);
                tv_card.setText(data.content);
            }
        });
        scl_layout.setOnSwipeListener(new SwipeCardLayout2.OnSwipeListener() {
            @Override
            public void onSwipe(int type) {
                switch (type) {
                    case SwipeCardLayout.TYPE_RIGHT:
                        Toast.makeText(SwipeCardActivity.this, "right", Toast.LENGTH_SHORT).show();

                        break;
                    case SwipeCardLayout.TYPE_LEFT:
                        Toast.makeText(SwipeCardActivity.this, "left", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

}
