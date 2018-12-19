package com.leibown.demo;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.leibown.library.widget.status.StatusViewContainer;

public class OtherStatusViewContainer extends StatusViewContainer {

    private final ImageView imageView;

    public OtherStatusViewContainer(Context context) {
        super(context);
        imageView = new ImageView(context);
        imageView.setBackgroundResource(R.drawable.loading);
        addOtherStatusViews(imageView);
    }


    public void showOtherStatusView() {
        hideAllViews();
        imageView.setVisibility(View.VISIBLE);
    }

}
