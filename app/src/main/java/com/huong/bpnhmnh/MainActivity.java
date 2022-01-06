package com.huong.bpnhmnh;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.huong.bpnhmnh.activity.Login;
import com.huong.bpnhmnh.adapter.MyViewPagerAdapter;
import com.huong.bpnhmnh.receiver.ReminderReceiver;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 mViewPager2;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAlarm();
        mViewPager2 = findViewById(R.id.view_pager_2);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(this);
        mViewPager2.setAdapter(myViewPagerAdapter);

        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.bottom_home) {
                    mViewPager2.setCurrentItem(0);
                } else if (id == R.id.bottom_search) {
                    mViewPager2.setCurrentItem(1);
                } else if (id == R.id.bottom_favorite) {
                    mViewPager2.setCurrentItem(2);
                } else if (id == R.id.bottom_me) {
                    mViewPager2.setCurrentItem(3);
                }
                return true;
            }
        });

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.bottom_home).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.bottom_search).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.bottom_favorite).setChecked(true);
                        break;
                    case 3:
                        mBottomNavigationView.getMenu().findItem(R.id.bottom_me).setChecked(true);
                        break;
                }
            }
        });
    }


    private void setAlarm() {
        cancelAlarm();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderReceiver.class);
        int flag = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flag = PendingIntent.FLAG_IMMUTABLE;
        }
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 111, intent, flag);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 30);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderReceiver.class);
        int flag = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flag = PendingIntent.FLAG_IMMUTABLE;

        }
        PendingIntent pendingIntent = PendingIntent.getService(this, 111, intent, flag);
        alarmManager.cancel(pendingIntent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getUid() == null) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }
    }
}