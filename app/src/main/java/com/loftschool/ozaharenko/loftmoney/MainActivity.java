package com.loftschool.ozaharenko.loftmoney;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String EXPENSE = "expense";
    public static final String INCOME = "income";
    public static final String USER_ID = "ozaharenko";
    public static String TOKEN = "token";

    private Api mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tabs);

        final ViewPager viewPager = findViewById(R.id.viewpager);
        final BudgetPagerAdapter adapter = new BudgetPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPager.setAdapter(adapter);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final int activeFragmentIndex = viewPager.getCurrentItem();
                Fragment activeFragment = getSupportFragmentManager().getFragments().get(activeFragmentIndex);
                activeFragment.startActivityForResult(new Intent(MainActivity.this, AddItemActivity.class), BudgetFragment.REQUEST_CODE);
            }
        });

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText(R.string.expences);
        tabLayout.getTabAt(1).setText(R.string.income);

        mApi = ((LoftApp)getApplication()).getApi();

        final String token = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString(TOKEN, "");

        if (TextUtils.isEmpty(token)){

            Call<Status> auth = mApi.auth(USER_ID);
            auth.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response
                ) {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                    editor.putString(TOKEN, response.body().getToken());
                    editor.apply();

                    for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                        if (fragment instanceof BudgetFragment) {
                            ((BudgetFragment)fragment).loadItems();
                        }

                    };
                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {

                }

            });
        }
    }

    static class BudgetPagerAdapter extends FragmentPagerAdapter {

        public BudgetPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return BudgetFragment.newInstance(R.color.dark_sky_blue, EXPENSE);
                case 1:
                    return BudgetFragment.newInstance(R.color.apple_green, INCOME);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
