package com.byteshaft.homemade.userActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.byteshaft.homemade.R;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by husnain on 8/17/17.
 */

public class ProductImageView extends AppCompatActivity {

    private ImageView imageView;
    private PhotoViewAttacher photoViewAttacher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_imageview);
        imageView = (ImageView) findViewById(R.id.product_image_view);
        photoViewAttacher = new PhotoViewAttacher(imageView);
        photoViewAttacher.update();
        Intent intent = getIntent();
        String imagePosition = intent.getStringExtra("url");
        Picasso.with(ProductImageView.this)
                .load(imagePosition)
                .resize(720, 1280)
                .into(imageView);
    }
}
