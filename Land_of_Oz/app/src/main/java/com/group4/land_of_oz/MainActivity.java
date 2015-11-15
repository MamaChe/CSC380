package com.group4.land_of_oz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.group4.land_of_oz.domain.Location;
import com.group4.land_of_oz.navigation.Navigator;
import com.group4.land_of_oz.persistence.LabelDAO;
import com.group4.land_of_oz.persistence.LocationDAO;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    boolean naturalScrolling = false, fitness= false, handicap = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        Switch switch1 = (Switch)findViewById(R.id.fitness_switch);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "ON", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "OFF", Toast.LENGTH_LONG).show();
                }
            }
        });
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
        LocationDAO locationDAO = new LocationDAO(getApplicationContext());
        Navigator navigator = new Navigator(getApplicationContext());
        Location l0 = locationDAO.findById(0);
        Location l2 = locationDAO.findById(1);

        List<Location> locationList = navigator.getBestPath(l0, l2, com.group4.land_of_oz.domain.Location.ELEVATOR);

        ((MapViewGroup)findViewById(R.id.MapViewGroup)).drawPath(locationList);
    }
    private void init(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new LabelDAO(this).findAll());
        ((AutoCompleteTextView)findViewById(R.id.autocomplete_destination)).setAdapter(adapter);
        ((AutoCompleteTextView)findViewById(R.id.autocomplete_startingPoint)).setAdapter(adapter);
    }
}
