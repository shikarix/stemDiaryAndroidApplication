package com.coistem.stemdiary.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.coistem.stemdiary.R;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {
    public static String image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        setTitle("");
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TouchImageView img = findViewById(R.id.bigImageNews);
        Picasso.with(this).load(image).error(R.drawable.ic_warning).into(img);
        img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        img.setScaleType(ImageView.ScaleType.CENTER);
    }

    protected void setImage() {

    }
}
