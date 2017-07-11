package com.java.io.practise;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.java.io.practise.view.CircleIndicatorView;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private CircleIndicatorView mCircleIndicatorView, mCircleIndicatorView1,
            mCircleIndicatorView2, mCircleIndicatorView3;
    private MyPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(mAdapter);

        mCircleIndicatorView = (CircleIndicatorView) findViewById(R.id.indicator_view);
        mCircleIndicatorView1 = (CircleIndicatorView) findViewById(R.id.indicator_view1);
        mCircleIndicatorView2 = (CircleIndicatorView) findViewById(R.id.indicator_view2);
        mCircleIndicatorView3 = (CircleIndicatorView) findViewById(R.id.indicator_view3);

        mCircleIndicatorView.setupWithViewPager(mViewPager);
        mCircleIndicatorView1.setupWithViewPager(mViewPager);
        mCircleIndicatorView2.setupWithViewPager(mViewPager);
        mCircleIndicatorView3.setupWithViewPager(mViewPager);
    }

    private class MyPagerAdapter extends PagerAdapter {

        private final int RES[] = new int[]{R.mipmap.ic_01, R.mipmap.ic_02, R.mipmap.ic_03, R.mipmap.ic_04};
        private ImageView[] mImageViews = new ImageView[RES.length];

        @Override
        public int getCount() {
            return RES.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.view_pager_item, null);
            ImageView image = (ImageView) view.findViewById(R.id.image_item);
            image.setImageResource(RES[position]);
            mImageViews[position] = image;
            container.addView(image);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImageViews[position]);
        }

    }
}
