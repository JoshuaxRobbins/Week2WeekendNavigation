package com.example.josh.week2weekend_navigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Context context;
    private DrawerLayout mDrawerLayout;
    private TextView tvTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        mDrawerLayout = findViewById(R.id.drawer_layout);
        tvTest = findViewById(R.id.tvTest);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Test");
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);



        // Setting up Navigation window
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()){
                            case R.id.Calculator:
                                Intent intent = new Intent(context,Calculator.class);
                                startActivity(intent);
                                break;

                        }


                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String textSize;
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_txtUp:
                textSize = textEdit(true,false);
                setTextSize(textSize);
                return true;

            case R.id.action_txtDown:
                textSize = textEdit(false,false);
                setTextSize(textSize);
                return true;
            case R.id.action_clear:
                textSize = textEdit(false,true);
                setTextSize(textSize);
                return true;

            case R.id.action_back:
                finish();
                return true;


        }
        return super.onOptionsItemSelected(item);

    }

    public String textEdit(boolean way,boolean reset) {

        SharedPreferences sharedPreferences = getSharedPreferences("saved_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!sharedPreferences.contains("TEXT_SIZE")) {
            editor.putString("TEXT_SIZE", "25dp");
        }
        if (reset){
            editor.putString("TEXT_SIZE","25dp");
            editor.apply();
            return "25dp";
        }
        String textSize = sharedPreferences.getString("TEXT_SIZE", "25dp");
        int numSize = Integer.valueOf(textSize.substring(0, 2));
        if (numSize < 30 && way) {
            numSize++;
            editor.putString("TEXT_SIZE", numSize + "sp");
            editor.apply();
            return numSize + "sp";
        } else if (numSize > 20 && !way) {
            numSize--;
            editor.putString("TEXT_SIZE", numSize + "sp");
            editor.apply();
            return numSize + "sp";
        } else
            return textSize;
    }

    public void setTextSize(String size){
        float textSize = Float.valueOf(size.substring(0,2));
        tvTest.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
    }
}
