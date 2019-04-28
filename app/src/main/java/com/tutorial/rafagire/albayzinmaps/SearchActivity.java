package com.tutorial.rafagire.albayzinmaps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchActivity extends  AppCompatActivity implements AdapterView.OnItemClickListener {

    DBAccess dbAccess;
    SharedPreferences pref;
    boolean updateSite = false;
    ItemAdapter itemAdapter;

    List<Site> list_sites;
    boolean filtersIntersection;
    String name;
    boolean viewpoint;
    boolean church;
    boolean mosque;
    boolean monument;
    boolean zambra;
    boolean restaurant;
    boolean fountain;

    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dbAccess = new DBLocal(getApplicationContext(), "DB_AM", null, 1);
        list_sites = new ArrayList<Site>();
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Bundle bundle = this.getIntent().getExtras();



        if(bundle.getBoolean("fromUpdate")) {
            updateSite = true;
            list_sites = dbAccess.findAll();
            setTitle(getString(R.string.title_activity_search_fromUpdate));
        }
        else if(bundle.getBoolean("fromFavourite")){
            setTitle(getString(R.string.title_activity_search_fromFav));

            Set<String> set = pref.getStringSet(getString(R.string.favourite_sites_key), new HashSet<String>());
            if(!set.isEmpty()){
                for(String id : set){
                    list_sites.add(dbAccess.findById(Integer.parseInt(id)));
                }
            }
        }
        else if (bundle.getBoolean("fromPending")){
            setTitle(getString(R.string.title_activity_search_fromPend));

            Set<String> set = pref.getStringSet(getString(R.string.pending_sites_key), new HashSet<String>());
            if(!set.isEmpty()){
                for(String id : set){
                    list_sites.add(dbAccess.findById(Integer.parseInt(id)));
                }
            }
        }
        else{
            filtersIntersection = bundle.getBoolean("filtersIntersection");
            name = bundle.getString("name");
            viewpoint = bundle.getBoolean("viewpoint");
            church = bundle.getBoolean("church");
            mosque = bundle.getBoolean("mosque");
            monument = bundle.getBoolean("monument");
            zambra = bundle.getBoolean("zambra");
            restaurant = bundle.getBoolean("restaurant");
            fountain = bundle.getBoolean("fountain");

            list_sites = findFilterSites();
        }

        listView = (ListView) findViewById(R.id.listView_search);
        itemAdapter = new ItemAdapter(this, list_sites);
        listView.setAdapter(itemAdapter);

        listView.setOnItemClickListener(this);


        if(list_sites.isEmpty()){
            TextView tView_noSites = findViewById(R.id.tView_search_no_site);
            tView_noSites.setText(getString(R.string.tView_search_no_sites));
            tView_noSites.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_SearchResult_Subtitle);
            tView_noSites.setGravity(Gravity.CENTER);
        }

    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final int pos = position;
            Site s = (Site)itemAdapter.getItem(position);

            Intent intent;

            if(updateSite) {
                intent = new Intent(this, AddSiteActivity.class);
            }
            else{
                intent = new Intent(this, ShowSiteActivity.class);
            }

            Bundle bundle = new Bundle();
            bundle.putInt("id", s.id);

            intent.putExtras(bundle);
            startActivity(intent);
        }



    private List<Site> findFilterSites() {
        List<Site> list = new ArrayList<Site>();
        List<Integer> listID = new ArrayList<Integer>();

        if((name != null) && (!name.equals(""))) {
            List<Site> list_all = dbAccess.findAll();
            if (list_all != null) {
                //Compare if the names are exactly the same
                for (Site s : list_all) {
                    if(s.name.toLowerCase().equals(name.toLowerCase())) {
                        list.add(s);
                        listID.add(s.id);
                    }
                }
                //Compare if the names are similar
                String[] words = name.toLowerCase().split(" ");
                for (Site s : list_all) {
                    for(String word : words)
                        if(s.name.toLowerCase().contains(word))
                            if(!listID.contains(s.id)) {
                                list.add(s);
                                listID.add(s.id);
                            }
                }
            }
        }
        else if(!viewpoint && !church && !mosque && !monument && !zambra && !restaurant && !fountain){
            filtersIntersection = false;
            viewpoint = true;
            church = true;
            mosque = true;
            monument = true;
            zambra = true;
            restaurant = true;
            fountain = true;
        }

        if (filtersIntersection) {
            List<Site> aux = dbAccess.findByFilters(viewpoint, church, mosque, monument, zambra, restaurant, fountain);
            if (aux != null)
                for(Site s : aux)
                    if(!listID.contains(s.id)) {
                        list.add(s);
                        listID.add(s.id);
                    }
        }
        else {
            if (viewpoint) {
                List<Site> aux = dbAccess.findByFilters(viewpoint, false, false, false, false, false, false);
                if (aux != null)
                    for(Site s : aux)
                        if(!listID.contains(s.id)) {
                            list.add(s);
                            listID.add(s.id);
                        }
            }

            if (church) {
                List<Site> aux = dbAccess.findByFilters(false, church, false, false, false, false, false);
                if (aux != null)
                    for(Site s : aux)
                        if(!listID.contains(s.id)) {
                            list.add(s);
                            listID.add(s.id);
                        }
            }

            if (mosque) {
                List<Site> aux = dbAccess.findByFilters(false, false, mosque, false, false, false, false);
                if (aux != null)
                    for(Site s : aux)
                        if(!listID.contains(s.id)) {
                            list.add(s);
                            listID.add(s.id);
                        }
            }

            if (monument) {
                List<Site> aux = dbAccess.findByFilters(false, false, false, monument, false, false, false);
                if (aux != null)
                    for(Site s : aux)
                        if(!listID.contains(s.id)) {
                            list.add(s);
                            listID.add(s.id);
                        }
            }

            if (zambra) {
                List<Site> aux = dbAccess.findByFilters(false, false, false, false, zambra, false, false);
                if (aux != null)
                    for(Site s : aux)
                        if(!listID.contains(s.id)) {
                            list.add(s);
                            listID.add(s.id);
                        }
            }

            if (restaurant) {
                List<Site> aux = dbAccess.findByFilters(false, false, false, false, false, restaurant, false);
                if (aux != null)
                    for(Site s : aux)
                        if(!listID.contains(s.id)) {
                            list.add(s);
                            listID.add(s.id);
                        }
            }

            if (fountain) {
                List<Site> aux = dbAccess.findByFilters(false, false, false, false, false, false, fountain);
                if (aux != null)
                    for(Site s : aux)
                        if(!listID.contains(s.id)) {
                            list.add(s);
                            listID.add(s.id);
                        }
            }
        }

        return list;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private class ItemAdapter extends BaseAdapter {

        protected Activity activity;
        protected List<Site> items;


        public ItemAdapter (Activity activity, List<Site> items) {
            this.activity = activity;
            this.items = items;
        }

        public int getCount() {
            return items.size();
        }

        public void addAll(List<Site> sites) {
            for (int i = 0; i < sites.size(); i++) {
                items.add(sites.get(i));
            }
        }

        public Object getItem(int position) {
            return items.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (convertView == null) {
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.item_category, null);
            }


            Site site = items.get(position);

            TextView tView_name = (TextView) v.findViewById(R.id.tView_nameSite);
            tView_name.setText(site.name);

            ImageView iView_fav = (ImageView)v.findViewById(R.id.iView_Favourite);
            ImageView iView_pend = (ImageView)v.findViewById(R.id.iView_Pending);

            Set<String> set;
            set = pref.getStringSet(getString(R.string.favourite_sites_key), new HashSet<String>());
            if(set.contains(Integer.toString(site.id))){
                iView_fav.setImageDrawable(getDrawable(R.drawable.ic_favourite_selected));
            }
            else{
                iView_fav.setImageDrawable(getDrawable(R.drawable.ic_menu_favourites));
            }

            set = pref.getStringSet(getString(R.string.pending_sites_key), new HashSet<String>());
            if(set.contains(Integer.toString(site.id))){
                iView_pend.setImageDrawable(getDrawable(R.drawable.ic_map_selected));
            }
            else{
                iView_pend.setImageDrawable(getDrawable(R.drawable.ic_menu_map));
            }



            TextView tView_categorys = (TextView) v.findViewById(R.id.tView_categorys);
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

            return v;
        }
    }
}






