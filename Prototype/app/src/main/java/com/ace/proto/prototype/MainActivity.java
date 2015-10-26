package com.ace.proto.prototype;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class MainActivity extends AppCompatActivity {
    boolean rotated = false;
    boolean naturalScrolling = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void rotate(View v){
        ((MapViewGroup)findViewById(R.id.MapViewGroup)).rotate();
    }
    public void down(View v){
        if(naturalScrolling) {
            ((MapViewGroup) findViewById(R.id.MapViewGroup)).decrementActiveLayer();
        }else{
            ((MapViewGroup)findViewById(R.id.MapViewGroup)).incrementActiveLayer();
        }
    }
    public void up(View v){
        if(!naturalScrolling) {
            ((MapViewGroup) findViewById(R.id.MapViewGroup)).decrementActiveLayer();
        }else{
            ((MapViewGroup)findViewById(R.id.MapViewGroup)).incrementActiveLayer();
        }
    }
}
