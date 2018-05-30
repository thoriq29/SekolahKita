package com.example.thoriq.sekolahkita.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.thoriq.sekolahkita.R;

/**
 * Created by Thoriq on 23/02/2018.
 */

public class ViewPagerAdapter extends PagerAdapter
{
    Context context;
    LayoutInflater layoutInflater;
    Integer [] images = {R.drawable.bannersmk1,R.drawable.logosmk2};

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View  view = layoutInflater.inflate(R.layout.layout_img,null );
        ImageView imageView = (ImageView)  view.findViewById(R.id.img);
        imageView.setImageResource(images[position]);
        ViewPager vp = (ViewPager) container;
        vp.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp =  (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
