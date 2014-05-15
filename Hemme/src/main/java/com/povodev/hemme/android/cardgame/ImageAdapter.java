package com.povodev.hemme.android.cardgame;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.povodev.hemme.android.R;

/**
 * Adapter used to populate the game GridView
 * Created by Stefano on 16/04/14.
 */
public class ImageAdapter extends BaseAdapter {

    private int gridSize;

    private Drawable image;

    private CardList mCardList;

    public ImageAdapter(Context context, int gridSize, CardList cardList) {
        this.gridSize = gridSize;
        this.mCardList = cardList;

        image = context.getResources().getDrawable(R.drawable.card_back);
    }

    @Override
    public int getCount() {
        return gridSize;
    }

    @Override
    public Object getItem(int i) {
        return mCardList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mCardList.get(i).getId();
    }


    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = mCardList.get(i);
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setImageDrawable(image);
        } else {
            imageView = (ImageView) convertView;
        }

        return imageView;
    }
}
