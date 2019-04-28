package com.tutorial.rafagire.albayzinmaps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ProfileFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    DBAccess dbAccess;
    List<Site> visited;
    int number_ownSites;
    String most_visited_category;

    TextView tView_sitesNumber;
    TextView tView_ownSitesNumber;
    TextView tView_Categorys;
    Button button_add;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        dbAccess = new DBLocal( v.getContext(), "DB_AM", null, 1);

        tView_sitesNumber = (TextView)v.findViewById(R.id.tView_profile_n_sites);
        tView_ownSitesNumber = (TextView)v.findViewById(R.id.tView_profile_n_own_sites);
        tView_Categorys = (TextView)v.findViewById(R.id.tView_profile_categorys);
        button_add = (Button)v.findViewById(R.id.button_profile_add);


        getSharedPreferencesInfo();
        most_visited_category = calculateMostVisitedCategory(visited);

        tView_sitesNumber.setText(getString(R.string.tView_profile_visited_sites) + " " + visited.size());
        tView_ownSitesNumber.setText(getString(R.string.tView_profile_own_sites) + " " + number_ownSites);
        tView_Categorys.setText(getString(R.string.tView_profile_most_visited_categorys) + " " + most_visited_category);

        button_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Create the Intent
                Intent intent = new Intent(getContext(), MapsActivity.class);

                startActivity(intent);
            }
        });



        return v;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void getSharedPreferencesInfo(){
        //Get the visited sites and the number of own sites from the SharedPreferences
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Set<String> set;
        //Visited sites
        set = pref.getStringSet(getString(R.string.visited_sites_key), new HashSet<String>());
        visited = new ArrayList<Site>();
        if(!set.isEmpty()) {
            for (String idString : set) {
                    int id = Integer.parseInt(idString);
                    Site s = dbAccess.findById(id);
                    if (s != null)
                        visited.add(s);
            }
        }
        //Number of own sites
        set = pref.getStringSet(getString(R.string.own_sites_key), new HashSet<String>());
        number_ownSites = set.size();
    }

    private String calculateMostVisitedCategory(List<Site> list){
        int[] array = new int[7];

        if(list.isEmpty())
            return "";

        for(Site s : list){
            if(s.viewpoint) array[0]++;
            if(s.church) array[1]++;
            if(s.mosque) array[2]++;
            if(s.monument) array[3]++;
            if(s.zambra) array[4]++;
            if(s.restaurant) array[5]++;
            if(s.fountain) array[6]++;
        }

        int max = 0;
        int pos = 0;
        int i = 0;
        for(i = 0; i < array.length; i++){
            if (array[i] > max){
                max = array[i];
                pos = i;
            }
        }

        switch(pos){
            case 0:
                return getString(R.string.checkBox_addSite_f1);
            case 1:
                return getString(R.string.checkBox_addSite_f2);
            case 2:
                return getString(R.string.checkBox_addSite_f3);
            case 3:
                return getString(R.string.checkBox_addSite_f4);
            case 4:
                return getString(R.string.checkBox_addSite_f5);
            case 5:
                return getString(R.string.checkBox_addSite_f6);
            case 6:
                return getString(R.string.checkBox_addSite_f7);
            default:
                return "";
        }

    }
}
