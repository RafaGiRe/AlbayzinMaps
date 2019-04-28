package com.tutorial.rafagire.albayzinmaps;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SearchFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    View v;

    EditText eText_name;
    CheckBox checkBox_viewpoint;
    CheckBox checkBox_church;
    CheckBox checkBox_mosque;
    CheckBox checkBox_monument;
    CheckBox checkBox_zambra;
    CheckBox checkBox_restaurant;
    CheckBox checkBox_fountain;
    RadioButton rButton_and;
    Button button_search;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_search, container, false);

        eText_name = (EditText) v.findViewById(R.id.eText_search_name);
        checkBox_viewpoint = (CheckBox) v.findViewById(R.id.checkBox_search_f1);
        checkBox_church = (CheckBox) v.findViewById(R.id.checkBox_search_f2);
        checkBox_mosque = (CheckBox) v.findViewById(R.id.checkBox_search_f3);
        checkBox_monument = (CheckBox) v.findViewById(R.id.checkBox_search_f4);
        checkBox_zambra = (CheckBox) v.findViewById(R.id.checkBox_search_f5);
        checkBox_restaurant = (CheckBox) v.findViewById(R.id.checkBox_search_f6);
        checkBox_fountain = (CheckBox) v.findViewById(R.id.checkBox_search_f7);
        rButton_and = (RadioButton) v.findViewById(R.id.rButton_search_and);
        button_search = (Button) v.findViewById(R.id.button_search);

        button_search.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        Bundle bundle = new Bundle();

        bundle.putBoolean("filtersIntersection", rButton_and.isChecked());
        bundle.putString("name", eText_name.getText().toString());
        bundle.putBoolean("viewpoint", checkBox_viewpoint.isChecked());
        bundle.putBoolean("church", checkBox_church.isChecked());
        bundle.putBoolean("mosque", checkBox_mosque.isChecked());
        bundle.putBoolean("monument", checkBox_monument.isChecked());
        bundle.putBoolean("zambra", checkBox_zambra.isChecked());
        bundle.putBoolean("restaurant", checkBox_restaurant.isChecked());
        bundle.putBoolean("fountain", checkBox_fountain.isChecked());

        intent.putExtras(bundle);
        startActivity(intent);
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
}
