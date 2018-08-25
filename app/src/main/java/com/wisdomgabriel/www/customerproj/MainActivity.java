package com.wisdomgabriel.www.customerproj;

import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.wisdomgabriel.www.customerproj.DatabaseForCart.CartDatabase;
import com.wisdomgabriel.www.customerproj.Registration.SignInActivity;
import com.wisdomgabriel.www.customerproj.count.Utils;

public class MainActivity extends AppCompatActivity {
    private int mNotificationsCount = 0;
    private CartDatabase cartDatabase;
    private ViewPager viewPager;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cartDatabase = new CartDatabase(getApplicationContext());
        viewPager = (ViewPager) findViewById(R.id.content);
        setUpViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(getApplicationContext(), CartActivity.class));
            }
        });

        new FetchCountTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shopping_menu, menu);
        MenuItem item = menu.findItem(R.id.action_notifications);
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable
        new FetchCountTask().execute();
        invalidateOptionsMenu();
        Utils.setBadgeCount(this, icon, mNotificationsCount);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notifications) {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
            return true;
        }if(id == R.id.action_logout){

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUpViewPager(ViewPager upViewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DrinksFragment(), "Drinks");
        adapter.addFragment(new FoodFragment(), "Foods");
        adapter.addFragment(new FruitFragment(), "Fruits");
        adapter.addFragment(new DesertFragment(), "Deserts");
        upViewPager.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new FetchCountTask().execute();
    }

    class FetchCountTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {


            int counts = (int) cartDatabase.getProfilesCount();

            return counts;

        }
        private void updateNotificationsBadge(int count) {
            mNotificationsCount = count;

            // force the ActionBar to relayout its MenuItems.
            // onCreateOptionsMenu(Menu) will be called again.
            invalidateOptionsMenu();

        }

        @Override
        public void onPostExecute(Integer count) {
            updateNotificationsBadge(count);
        }
    }
}
