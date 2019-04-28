package com.tutorial.rafagire.albayzinmaps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

//This imports only use if database in local

public class MainActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SearchFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Launch the SearchFragment
        Fragment frag = new SearchFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, frag).commit();

        //Hide the keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            //Creamos el Intent
            Intent intent = new Intent(MainActivity.this, Settings.class);

            //Creamos con el Bundle la información a pasar entre actividades
            //Bundle b = new Bundle();
            //b.putString("NOMBRE", texto.getText().toString());

            //Metemos el Bundle en el Intent y lanzamos el intent
            //intent.putExtras(b);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        //Check keys of SharedPreferences

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();

        if(!pref.contains(getString(R.string.first_access_key))){
            editor.putBoolean(getString(R.string.first_access_key), true);
            editor.putBoolean(getString(R.string.pref_database_restore_key), false);
            editor.putString(getString(R.string.pref_idiom_key), "0");
            editor.putStringSet(getString(R.string.own_sites_key), new HashSet<String>());
            editor.putStringSet(getString(R.string.favourite_sites_key), new HashSet<String>());
            editor.putStringSet(getString(R.string.pending_sites_key), new HashSet<String>());
            editor.putStringSet(getString(R.string.visited_sites_key), new HashSet<String>());

            editor.commit();
        }
        if(!pref.contains(getString(R.string.pref_database_restore_key))){
            editor.putBoolean(getString(R.string.pref_database_restore_key), false);
            editor.commit();
        }
        if(!pref.contains(getString(R.string.pref_idiom_key))) {
            editor.putString(getString(R.string.pref_idiom_key), "0");
            editor.commit();
        }
        if(!pref.contains(getString(R.string.own_sites_key))){
            editor.putStringSet(getString(R.string.own_sites_key), new HashSet<String>());
            editor.commit();
        }
        if(!pref.contains(getString(R.string.favourite_sites_key))){
            editor.putStringSet(getString(R.string.favourite_sites_key), new HashSet<String>());
            editor.commit();
        }
        if(!pref.contains(getString(R.string.pending_sites_key))){
            editor.putStringSet(getString(R.string.pending_sites_key), new HashSet<String>());
            editor.commit();
        }
        if(!pref.contains(getString(R.string.visited_sites_key))){
            editor.putStringSet(getString(R.string.visited_sites_key), new HashSet<String>());
            editor.commit();
        }



        if(pref.getBoolean(getString(R.string.first_access_key), false)){
            //Necessary actions for the first access
            DBAccess dbAccess = new DBLocal(getApplicationContext(), "DB_AM", null, 1);
            dbAccess.deleteTable();
            dbAccess.createTable();
            try {
                InputStream fraw = getResources().openRawResource(R.raw.initial_database_content);
                BufferedReader brin = new BufferedReader(new InputStreamReader(fraw));

                String line;
                JSONParser parser=new JSONParser();
                while ( (line = brin.readLine()) != null ) {
                    Object o=parser.parse(line);
                    JSONObject siteJSON=(JSONObject)o;

                    Site site = new Site();
                    site.id = ((Long) siteJSON.get("id")).intValue();
                    site.name = (String) siteJSON.get("name");
                    site.description = (String) siteJSON.get("description");
                    site.viewpoint = (boolean) siteJSON.get("viewpoint");
                    site.church = (boolean) siteJSON.get("church");
                    site.mosque = (boolean) siteJSON.get("mosque");
                    site.monument = (boolean) siteJSON.get("monument");
                    site.zambra = (boolean) siteJSON.get("zambra");
                    site.restaurant = (boolean) siteJSON.get("restaurant");
                    site.fountain = (boolean) siteJSON.get("fountain");
                    site.latitude = (double) siteJSON.get("latitude");
                    site.longitude = (double) siteJSON.get("longitude");

                    dbAccess.add(site);
                }
                fraw.close();
            }
            catch (Exception ex) {
                Log.d("DATABASE", ex.getMessage());
            }

            editor.putBoolean(getString(R.string.first_access_key), false);
            editor.commit();
        }

        super.onResume();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        boolean FragmentTransaction = false;
        Fragment frag = null;

        if (id == R.id.nav_search) {
            frag = new SearchFragment();
            FragmentTransaction = true;
        } else if (id == R.id.nav_map) {
            item.setChecked(false);

            Intent intent = new Intent(this, SearchActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("fromPending", true);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.nav_favourites) {
            item.setChecked(false);

            Intent intent = new Intent(this, SearchActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("fromFavourite", true);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            frag = new ProfileFragment();
            FragmentTransaction = true;
        }

        if(FragmentTransaction){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, frag).commit();

            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
