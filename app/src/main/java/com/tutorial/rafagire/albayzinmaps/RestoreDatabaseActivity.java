package com.tutorial.rafagire.albayzinmaps;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

public class RestoreDatabaseActivity extends AppCompatActivity {

    DBAccess dbAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_database);

        dbAccess = new DBLocal(getApplicationContext(), "DB_AM", null, 1);


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.dialog_title_restoreDatabase));
        alertDialog.setMessage(getString(R.string.dialog_message_restoreDatabase));

        alertDialog.setPositiveButton(getString(R.string.dialog_positiveButton_restoreDatabase),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        restoreDatabase();
                    }
                });

        alertDialog.setNegativeButton(getString(R.string.dialog_negativeButton_restoreDatabase),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        alertDialog.show();
    }

    private void restoreDatabase(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();

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
        editor.putStringSet(getString(R.string.own_sites_key), new HashSet<String>());
        editor.putStringSet(getString(R.string.pending_sites_key), new HashSet<String>());
        editor.putStringSet(getString(R.string.favourite_sites_key), new HashSet<String>());
        editor.putStringSet(getString(R.string.visited_sites_key), new HashSet<String>());
        editor.putBoolean(getString(R.string.pref_database_restore_key), false);
        editor.commit();

        finish();
    }
}
