##### DEMO
[DEMO](https://github.com/MartinDong/SwipeCard/blob/master/screen/app-debug.apk)

##### 效果图
![效果图1](https://github.com/MartinDong/SwipeCard/blob/master/screen/device-2020-01-03-113510.png)
![效果图2](https://github.com/MartinDong/SwipeCard/blob/master/screen/device-2020-01-03-113531.png)
![效果图3](https://github.com/MartinDong/SwipeCard/blob/master/screen/device-2020-01-03-113555.png)


##### 使用说明
```xml

 <com.donghongyu.swipecard.view.SwipeCardLayout
        android:id="@+id/scl_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_centerInParent="true" />

```

```java
// 获取控件对象
SwipeCardLayout scl_layout = findViewById(R.id.scl_layout);

// 存储View
List<View> data = new ArrayList<>();
View view = new SwipeContentView(this);
View view2 = new SwipeContentView2(this);
View view3 = new SwipeContentView3(this);
View view4 = new SwipeContentView4(this);

data.add(view4);
data.add(view3);
data.add(view2);
data.add(view);

// 设置卡片容器的Adapter
scl_layout.setAdapter(new SwipeCardLayout.CardAdapter(data));
// 设置滑动的回调监听
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

```