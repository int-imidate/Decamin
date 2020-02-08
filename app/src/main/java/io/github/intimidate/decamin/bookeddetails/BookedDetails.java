package io.github.intimidate.decamin.bookeddetails;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.intimidate.decamin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookedDetails extends Fragment {

    TextView driverEmail,NumberOfSeats,CurrentStatus,Address;
    String driveremail,noOfSeats,address;
    int status;
    public BookedDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_booked_details, container, false);
        PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("token", -1);
        driveremail=PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("driverEmail", null);
        int x=PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("noOfSeats",0);
        noOfSeats=String.valueOf(x);
        status=PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("status",0);
        address=PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("destination",null);



        return v;
    }

}
