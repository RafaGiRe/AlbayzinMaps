package com.tutorial.rafagire.albayzinmaps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashSet;
import java.util.Set;

public class AddSiteActivity extends AppCompatActivity implements View.OnClickListener {

    DBAccess dbAccess;
    Bundle bundle;
    boolean newSite;
    LatLng latLng;
    Site oldSite;

    TextView tView_info;
    EditText eText_name;
    EditText eText_description;
    CheckBox checkBox_viewpoint;
    CheckBox checkBox_church;
    CheckBox checkBox_mosque;
    CheckBox checkBox_monument;
    CheckBox checkBox_zambra;
    CheckBox checkBox_restaurant;
    CheckBox checkBox_fountain;
    Button button_addSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_site);


        dbAccess = new DBLocal(getApplicationContext(), "DB_AM", null, 1);
        bundle = this.getIntent().getExtras();
        newSite = bundle.getBoolean("newSite");

        tView_info = (TextView) findViewById(R.id.tView_addSite_info);
        eText_name = (EditText) findViewById(R.id.eText_addSite_name);
        eText_description = (EditText) findViewById(R.id.eText_addSite_description);
        checkBox_viewpoint = (CheckBox)findViewById(R.id.checkBox_addSite_f1);
        checkBox_church = (CheckBox)findViewById(R.id.checkBox_addSite_f2);
        checkBox_mosque = (CheckBox)findViewById(R.id.checkBox_addSite_f3);
        checkBox_monument = (CheckBox)findViewById(R.id.checkBox_addSite_f4);
        checkBox_zambra = (CheckBox)findViewById(R.id.checkBox_addSite_f5);
        checkBox_restaurant = (CheckBox)findViewById(R.id.checkBox_addSite_f6);
        checkBox_fountain = (CheckBox)findViewById(R.id.checkBox_addSite_f7);
        button_addSite = (Button)findViewById(R.id.button_addSite);


        if(newSite) {
            setTitle(R.string.title_activity_addSite);
            latLng = new LatLng(bundle.getDouble("latitude"), bundle.getDouble("longitude"));

            tView_info.setText(getString(R.string.tView_addSite_infoAdd));
        }
        else{
            setTitle(R.string.title_activity_updateSite);
            oldSite = dbAccess.findById(bundle.getInt("id"));
            latLng = new LatLng(oldSite.latitude, oldSite.longitude);

            tView_info.setText(getString(R.string.tView_addSite_infoUpdate));
            eText_name.setText(oldSite.name);
            eText_description.setText(oldSite.description);
            checkBox_viewpoint.setChecked(oldSite.viewpoint);
            checkBox_church.setChecked(oldSite.church);
            checkBox_mosque.setChecked(oldSite.mosque);
            checkBox_monument.setChecked(oldSite.monument);
            checkBox_zambra.setChecked(oldSite.zambra);
            checkBox_restaurant.setChecked(oldSite.restaurant);
            checkBox_fountain.setChecked(oldSite.fountain);
            button_addSite.setText(getString(R.string.button_addSite_update));
        }

        button_addSite.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        String name = eText_name.getText().toString();

        if((name == null) || (name.equals(""))){
            ShapeDrawable shape = new ShapeDrawable(new RectShape());
            shape.getPaint().setColor(Color.RED);
            shape.getPaint().setStyle(Paint.Style.STROKE);
            shape.getPaint().setStrokeWidth(3);

            eText_name.setBackground(shape);
            Toast.makeText(this, getString(R.string.toast_addSite_no_name), Toast.LENGTH_SHORT).show();
            return;
        }

        Site site;
        int id;
        if(newSite) {
            id = 0;
            //Make sure the ID for the new site is not already used
            do {
                id++;
                site = dbAccess.findById(id);
            } while (site != null);
        }
        else{
            id = oldSite.id;
        }

        site = new Site();
        site.id = id;
        site.name = name;
        site.description = eText_description.getText().toString();
        site.viewpoint = checkBox_viewpoint.isChecked();
        site.church = checkBox_church.isChecked();
        site.mosque = checkBox_mosque.isChecked();
        site.monument = checkBox_monument.isChecked();
        site.zambra = checkBox_zambra.isChecked();
        site.restaurant = checkBox_restaurant.isChecked();
        site.fountain = checkBox_fountain.isChecked();
        site.latitude = latLng.latitude;
        site.longitude = latLng.longitude;


        //Add the site to the database
        if(newSite) {
            dbAccess.add(site);

            //Add the new site to "Own Sites" and "Favourite Sites"
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = pref.edit();

            Set<String> setOwn = pref.getStringSet(getString(R.string.own_sites_key), new HashSet<String>());
            setOwn.add(Integer.toString(site.id));
            editor.putStringSet(getString(R.string.own_sites_key), setOwn);

            Set<String> setFavourite = pref.getStringSet(getString(R.string.favourite_sites_key), new HashSet<String>());
            setFavourite.add(Integer.toString(site.id));
            editor.putStringSet(getString(R.string.favourite_sites_key), setFavourite);

            editor.commit();

            Toast.makeText(this, getString(R.string.toast_addSite_added), Toast.LENGTH_SHORT).show();

        }
        else {
            dbAccess.update(site);
            Toast.makeText(this, getString(R.string.toast_addSite_updated), Toast.LENGTH_SHORT).show();
        }


        //Return to MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
