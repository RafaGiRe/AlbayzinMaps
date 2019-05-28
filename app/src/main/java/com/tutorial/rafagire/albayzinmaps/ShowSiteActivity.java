package com.tutorial.rafagire.albayzinmaps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class ShowSiteActivity extends AppCompatActivity implements View.OnClickListener {

    DBAccess dbAccess;
    SharedPreferences pref;
    Site site;

    TextView tView_name;
    TextView tView_categorys;
    TextView tView_description;
    ImageButton iButton_fav;
    ImageButton iButton_pend;
    ImageButton iButton_showInMap;
    ImageButton iButton_visited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_site);


        dbAccess = new DBLocal(getApplicationContext(), "DB_AM", null, 1);
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Bundle bundle = this.getIntent().getExtras();

        site = dbAccess.findById(bundle.getInt("id"));

        tView_name = (TextView)findViewById(R.id.tView_showSite_name);
        tView_categorys = (TextView)findViewById(R.id.tView_showSite_categorys);
        tView_description = (TextView)findViewById(R.id.tView_showSite_description);
        iButton_fav = (ImageButton)findViewById(R.id.iButton_showSite_fav);
        iButton_pend = (ImageButton)findViewById(R.id.iButton_showSite_pend);
        iButton_showInMap = (ImageButton)findViewById(R.id.iButton_showSite_showInMap);
        iButton_visited = (ImageButton)findViewById(R.id.iButton_showSite_visited);

        tView_name.setText(site.name);
        if((site.description != null) && (!site.description.equals("")))
            tView_description.setText(site.description);
        else
            tView_description.setText(getString(R.string.tView_showSite_no_description));



        boolean firstCategory = true;
        if(site.viewpoint){
            tView_categorys.setText(getString(R.string.checkBox_addSite_f1));
            firstCategory = false;
        }
        if(site.church)
            if(firstCategory){
                tView_categorys.setText(getString(R.string.checkBox_addSite_f2));
                firstCategory = false;
            }
            else
                tView_categorys.append(", " + getString(R.string.checkBox_addSite_f2));
        if(site.mosque)
            if(firstCategory){
                tView_categorys.setText(getString(R.string.checkBox_addSite_f3));
                firstCategory = false;
            }
            else
                tView_categorys.append(", " + getString(R.string.checkBox_addSite_f3));
        if(site.monument)
            if(firstCategory){
                tView_categorys.setText(getString(R.string.checkBox_addSite_f4));
                firstCategory = false;
            }
            else
                tView_categorys.append(", " + getString(R.string.checkBox_addSite_f4));
        if(site.zambra)
            if(firstCategory){
                tView_categorys.setText(getString(R.string.checkBox_addSite_f5));
                firstCategory = false;
            }
            else
                tView_categorys.append(", " + getString(R.string.checkBox_addSite_f5));
        if(site.restaurant)
            if(firstCategory){
                tView_categorys.setText(getString(R.string.checkBox_addSite_f6));
                firstCategory = false;
            }
            else
                tView_categorys.append(", " + getString(R.string.checkBox_addSite_f6));
        if(site.fountain)
            if(firstCategory){
                tView_categorys.setText(getString(R.string.checkBox_addSite_f7));
                firstCategory = false;
            }
            else
                tView_categorys.append(", " + getString(R.string.checkBox_addSite_f7));

        Set<String> set;
        set = pref.getStringSet(getString(R.string.favourite_sites_key), new HashSet<String>());
        if(set.contains(Integer.toString(site.id))){
            iButton_fav.setImageDrawable(getDrawable(R.drawable.ic_favourite_selected));
        }
        set = pref.getStringSet(getString(R.string.pending_sites_key), new HashSet<String>());
        if(set.contains(Integer.toString(site.id))){
            iButton_pend.setImageDrawable(getDrawable(R.drawable.ic_pending_selected));
        }
        set = pref.getStringSet(getString(R.string.visited_sites_key), new HashSet<String>());
        if(set.contains(Integer.toString(site.id))){
            iButton_visited.setImageDrawable(getDrawable(R.drawable.ic_check_selected));
        }

        iButton_fav.setOnClickListener(this);
        iButton_pend.setOnClickListener(this);
        iButton_showInMap.setOnClickListener(this);
        iButton_visited.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Set<String> set;
        SharedPreferences.Editor editor = pref.edit();
        switch(v.getId()){
            case (R.id.iButton_showSite_fav):
                set = pref.getStringSet(getString(R.string.favourite_sites_key), new HashSet<String>());
                if(set.contains(Integer.toString(site.id))){
                    set.remove(Integer.toString(site.id));
                    editor.putStringSet(getString(R.string.favourite_sites_key), set);
                    iButton_fav.setImageDrawable(getDrawable(R.drawable.ic_menu_favourites));
                }
                else{
                    set.add(Integer.toString(site.id));
                    editor.putStringSet(getString(R.string.favourite_sites_key), set);
                    iButton_fav.setImageDrawable(getDrawable(R.drawable.ic_favourite_selected));
                    Toast.makeText(v.getContext(), getString(R.string.toast_showSite_addFav), Toast.LENGTH_SHORT).show();
                }
                break;
            case (R.id.iButton_showSite_pend):
                set = pref.getStringSet(getString(R.string.pending_sites_key), new HashSet<String>());
                if(set.contains(Integer.toString(site.id))){
                    set.remove(Integer.toString(site.id));
                    editor.putStringSet(getString(R.string.pending_sites_key), set);
                    iButton_pend.setImageDrawable(getDrawable(R.drawable.ic_menu_pending));
                }
                else{
                    set.add(Integer.toString(site.id));
                    editor.putStringSet(getString(R.string.pending_sites_key), set);
                    iButton_pend.setImageDrawable(getDrawable(R.drawable.ic_pending_selected));
                    Toast.makeText(v.getContext(), getString(R.string.toast_showSite_addPend), Toast.LENGTH_SHORT).show();
                }
                break;
            case(R.id.iButton_showSite_showInMap):
                Intent intent = new Intent(this, MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("markerTitle", site.name);
                bundle.putDouble("latitude", site.latitude);
                bundle.putDouble("longitude", site.longitude);

                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case(R.id.iButton_showSite_visited):
                set = pref.getStringSet(getString(R.string.visited_sites_key), new HashSet<String>());
                set.add(Integer.toString(site.id));
                editor.putStringSet(getString(R.string.visited_sites_key), set);
                iButton_visited.setImageDrawable(getDrawable(R.drawable.ic_check_selected));
                Toast.makeText(v.getContext(), getString(R.string.toast_showSite_addVisited), Toast.LENGTH_SHORT).show();
                break;
        }
        editor.commit();
    }
}
