package org.tvolkov.rvc.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MainPagerActivity extends FragmentActivity {

    private MainPagerAdapter mainPagerAdapter;
    private ViewPager viewPager;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager_main);

    }
}
