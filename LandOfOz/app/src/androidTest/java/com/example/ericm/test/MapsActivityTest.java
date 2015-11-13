package com.example.ericm.test;

import android.test.ActivityInstrumentationTestCase2;

import com.example.ericm.gui.MapsActivity;
import com.landofoz.commonland.domain.Floor;
import com.landofoz.commonland.domain.GraphNode;
import com.landofoz.commonland.domain.Label;
import com.landofoz.commonland.domain.Location;
import com.landofoz.commonland.navigation.Navigator;
import com.landofoz.commonland.persistence.LabelDAO;
import com.landofoz.commonland.persistence.LocationDAO;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ericm on 10/21/2015.
 */
public class MapsActivityTest extends ActivityInstrumentationTestCase2<MapsActivity> {

    public MapsActivityTest() {
        super(MapsActivity.class);
    }

    @Test
    public void testLocationDAO() {
        LocationDAO dao = new LocationDAO(getActivity().getApplicationContext());
        Location l = new Location();
        l.setLatitude(12);
        l.setLongitude(12);
        l.setFloor(new Floor());
        l.setType(0);
        long id = dao.insert(l);
        assertTrue(id != -1);
        l.setId(id);
        assertTrue(dao.findById(id) != null);
        l.setType(1);
        assertTrue(dao.update(l));
        assertTrue(dao.findByType(1).size() > 0);
        assertTrue(dao.remove(id));
        assertTrue(dao.findById(id) == null);
    }

    @Test
    public void testLabelDAO() {
        LabelDAO dao = new LabelDAO(getActivity().getApplicationContext());

        Label label = new Label();
        label.setName("name");
        Location location = new Location();
        location.setId(1);
        label.setLocation(location);

        long id = dao.insert(label);

        assertTrue(dao.findById(id) != null);

        label.setId(id);
        label.setName("updatedName");
        dao.update(label);

        assertTrue(dao.findById(id).getName().equals("updatedName"));

        dao.remove(id);
        assertTrue(dao.findById(id) == null);
    }

    @Test
    public void testNavigator() {
        ArrayList<Location> l = new ArrayList<>();
        for (int i=0; i<5;i++){
            l.add(new Location());
            l.get(i).setType(1);
            l.get(i).setLongitude(1);
            l.get(i).setLatitude(1);
        }
        l.get(0).setLatitude(0);
        l.get(l.size()-1).setLatitude(2);

        ArrayList<GraphNode> gs = new ArrayList<>();
        for (int i=0; i<l.size();i++){
            gs.add(new GraphNode());
            gs.get(i).setLocation(l.get(i));
        }

        for (int i=0; i<l.size()-1;i++){
            gs.get(i).getNeighbors().add(gs.get(i+1));
        }

        Navigator n = new Navigator(gs.get(0));
        List<Location> response = null;

        response = n.getBestPath(l.get(0),l.get(l.size()-1),1);

        Assert.assertTrue(response!=null && response.size()>l.size());

    }


}
