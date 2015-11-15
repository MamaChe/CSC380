package com.group4.land_of_oz;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.group4.land_of_oz.persistence.LabelDAO;

import java.util.ArrayList;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    boolean naturalScrolling = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       // if (id == R.id.action_settings) {
            return true;
       // }

        //return super.onOptionsItemSelected(item);
    }
    public void rotate(View v){
        ((MapViewGroup)findViewById(R.id.MapViewGroup)).rotate();
        //((MapView)findViewById(R.id.MapView0)).invalidate();
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
    public void test(View v){
        ArrayList<Location> locations = new ArrayList<>();
        locations.add(new Location(100, 100, 0));
        locations.add(new Location(600, 100, 0));
        locations.add(new Location(600, 600, 0));
        locations.add(new Location(600, 600, 1));
        locations.add(new Location(100, 100, 1));
        locations.add(new Location(300, 100, 1));
        ((MapViewGroup)findViewById(R.id.MapViewGroup)).drawPath(locations);
    }
    private void init(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new LabelDAO(this).findAll());
        ((AutoCompleteTextView)findViewById(R.id.autocomplete_destination)).setAdapter(adapter);
        ((AutoCompleteTextView)findViewById(R.id.autocomplete_startingPoint)).setAdapter(adapter);
    }
}
