package com.loftschool.ozaharenko.loftmoney;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {

    private Api mApi;
    private Animation mFadeOutAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mApi = ((LoftApp)getApplication()).getApi();

        mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.auth_fadeout);

        final Button authButton = findViewById(R.id.enter_button);
        final ImageView mImageView = findViewById(R.id.auth_imageView);
        final TextView mTextView = findViewById(R.id.auth_textView);

        authButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mImageView.startAnimation(mFadeOutAnimation);
                mTextView.startAnimation(mFadeOutAnimation);
                authButton.startAnimation(mFadeOutAnimation);

                //Close authorization form; it should be accessible once.
                finish();

                //Start MainActivity (to process case when we press Back button from
                startActivity(new Intent(AuthActivity.this, MainActivity.class));
            }
        });

        final String token = PreferenceManager.getDefaultSharedPreferences(this).getString(MainActivity.TOKEN, "");

        if(TextUtils.isEmpty(token)) {
            //Receive token and save it
            auth();
        } else {
            //Close current Activity:
            finish();
            //Start MainActivity (because token exists on the device):
            startActivity(new Intent(this, MainActivity.class));
        }

    }

    private void auth() {
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            Call<Status> auth = mApi.auth(androidId);
            auth.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response
                ) {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AuthActivity.this).edit();
                    editor.putString(MainActivity.TOKEN, response.body().getToken());
                    editor.apply();
               }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {

                }

            });

    }
}
