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
import android.widget.TextView;
import android.widget.Toast;

import com.group4.land_of_oz.domain.Label;
import com.group4.land_of_oz.domain.Location;
import com.group4.land_of_oz.domain.LocationStub;
import com.group4.land_of_oz.domain.Neighbor;
import com.group4.land_of_oz.navigation.Navigator;
import com.group4.land_of_oz.persistence.LabelDAO;
import com.group4.land_of_oz.persistence.LocationDAO;
import com.group4.land_of_oz.persistence.NeighborDAO;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    boolean naturalScrolling = false, fitness= false, handicap = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        final Switch switch1 = (Switch)findViewById(R.id.fitness_switch);
        final Switch switch2 = (Switch)findViewById(R.id.accessibility_switch);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Toast.makeText(getApplicationContext(), "ON", Toast.LENGTH_SHORT).show();
                    switch2.setChecked(false);
                    handicap = false;
                    fitness = true;
                } else {
                    //Toast.makeText(getApplicationContext(), "OFF", Toast.LENGTH_SHORT).show();
                    fitness = false;
                }
                if(fitness){
                    Toast.makeText(getApplicationContext(), "Fitness mode turned ON", Toast.LENGTH_SHORT).show();
                }else if(handicap){
                    Toast.makeText(getApplicationContext(), "Accessibility mode turned ON", Toast.LENGTH_SHORT).show();
                }

            }
        });
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Toast.makeText(getApplicationContext(), " ", Toast.LENGTH_SHORT).show();
                    switch1.setChecked(false);
                    fitness = false;
                    handicap = true;
                } else {
                    //Toast.makeText(getApplicationContext(), " ", Toast.LENGTH_SHORT).show();
                    handicap = false;
                }
                if(fitness){
                    Toast.makeText(getApplicationContext(), "Fitness mode turned ON", Toast.LENGTH_SHORT).show();
                }else if(handicap){
                    Toast.makeText(getApplicationContext(), "Accessibility mode turned ON", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showSettings(View v){
        Switch handicapSwitch = (Switch)findViewById(R.id.accessibility_switch);
        Switch fitnessSwitch = (Switch)findViewById(R.id.fitness_switch);
        if(handicapSwitch.getVisibility() == View.VISIBLE){
            handicapSwitch.setVisibility(View.INVISIBLE);
            fitnessSwitch.setVisibility(View.INVISIBLE);
        }else{
            handicapSwitch.setVisibility(View.VISIBLE);
            fitnessSwitch.setVisibility(View.VISIBLE);
        }
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
        Location l2 = locationDAO.findById(37);

        List<Location> locationList = navigator.getBestPath(l0, l2, Location.STAIRWAY);

        ((MapViewGroup)findViewById(R.id.MapViewGroup)).drawPath(locationList);
    }
    public void drawingTest(View v){
        ArrayList<Location> locations = new ArrayList<>();
        locations.add(new LocationStub(111, 80, 1));
        locations.add(new LocationStub(111, 163, 1));
        locations.add(new LocationStub(89, 163, 1));
        locations.add(new LocationStub(66, 162, 1));
        locations.add(new LocationStub(66, 192, 1));
        locations.add(new LocationStub(66, 223, 1));
        locations.add(new LocationStub(98, 228, 1));
        locations.add(new LocationStub(132, 265, 1));
        locations.add(new LocationStub(170, 300, 1));
        locations.add(new LocationStub(201, 313, 1));
        locations.add(new LocationStub(211, 318, 1));
        locations.add(new LocationStub(250, 225, 1));
        locations.add(new LocationStub(291, 124, 1));
        ((MapViewGroup)findViewById(R.id.MapViewGroup)).drawPath(locations);
    }
    public void illustrateGraph(View v){
        List<Neighbor>neighbors = new NeighborDAO(getApplicationContext()).findAll();
        for(Neighbor neighbor: neighbors){
            if(neighbor.getNeighbor() != null && neighbor.getNode()!=null)
            ((MapViewGroup)findViewById(R.id.MapViewGroup)).illustrateEdge(neighbor.getNode(), neighbor.getNeighbor());
        }
    }

    private void init(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new LabelDAO(this).findAll());
        ((AutoCompleteTextView)findViewById(R.id.autocomplete_destination)).setAdapter(adapter);
        ((AutoCompleteTextView)findViewById(R.id.autocomplete_startingPoint)).setAdapter(adapter);
    }
    public void go(View v){
        LabelDAO labelDAO = new LabelDAO(getApplicationContext());
        Navigator navigator = new Navigator(getApplicationContext());
        String originName = ((AutoCompleteTextView)findViewById(R.id.autocomplete_startingPoint)).getText().toString();
        String destinationName = ((AutoCompleteTextView)findViewById(R.id.autocomplete_destination)).getText().toString();

        Location origin = labelDAO.findByName(originName).getLocation();
        Location destination = labelDAO.findByName(destinationName).getLocation();

        List<Location> locationList = navigator.getBestPath(origin, destination, Location.ELEVATOR);

        ((MapViewGroup)findViewById(R.id.MapViewGroup)).drawPath(locationList);
    }
}
