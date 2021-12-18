package com.example.androidtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {

    private AssetManager assetManager;
    private List<String> listImg;
    private Button btnShuffle, btnBack;
    private GestureDetector gestureDetector;
    private ConstraintLayout mainCard;
    private String cardUp = "src/cards/";
    private String cardBack = "src/cards back/bb.png";
    float x =0 , y = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShuffle = findViewById(R.id.shuffle);
        mainCard =  findViewById(R.id.main);
        btnBack =  findViewById(R.id.back);

        gestureDetector = new GestureDetector(this, new SingleTapConfirm());

        try {
            listImg = getImage(this.getApplicationContext());
            Collections.shuffle(listImg);
            for(int i = 0; i <= listImg.size() - 1; i++ ){
                ImageView imgCard = new ImageView(this.getApplicationContext());
                imgCard.setImageBitmap(setCardImage(this.getApplicationContext(), cardBack));
                mainCard.addView(imgCard);
                int finalI = i;
                imgCard.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (gestureDetector.onTouchEvent(event)) {

                            final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imgCard, "scaleX", 1f, 0f);
                            final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imgCard, "scaleX", 0f, 1f);
                            oa1.setInterpolator(new DecelerateInterpolator());
                            oa1.setDuration(200);
                            oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                            oa2.setDuration(200);
                            oa1.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    try {
                                        imgCard.setImageBitmap(setCardImage(v.getContext(), cardUp+ listImg.get(finalI)));
                                        oa2.start();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                            oa1.start();
                        } else {
                            switch (event.getActionMasked()){

                                case MotionEvent.ACTION_DOWN:
                                    x = event.getX();
                                    y = event.getY();
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    float dX, dY;
                                    dX = event.getX();
                                    dY = event.getY();

                                    float distanceX = dX - x;
                                    float distanceY = dY - y;

                                    imgCard.setX(imgCard.getX()+distanceX);
                                    imgCard.setY(imgCard.getY()+distanceY);

                                    break;
                            }
                        }

                        return true;
                    }
                });
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                overridePendingTransition( 0, 0);
                startActivity(intent);
                overridePendingTransition( 0, 0);
            }
        });

        btnBack.setOnClickListener(v ->{
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);
        });

    }

    public Bitmap setCardImage(Context context, String card_path) throws IOException {
        assetManager = context.getAssets();
        InputStream is = assetManager.open(card_path);
        return BitmapFactory.decodeStream(is);
    }

    private List<String> getImage(Context context) throws IOException {
        assetManager = context.getAssets();
        return Arrays.asList(assetManager.list("src/cards"));
    }

    private static class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }

}