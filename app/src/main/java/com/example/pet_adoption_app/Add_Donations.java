package com.example.pet_adoption_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Add_Donations#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Add_Donations extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String username,name;


    EditText DonationName, DonatedOwner, Descriptions;

    Button AddDonations;
    public Add_Donations() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Add_Donations.
     */
    // TODO: Rename and change types and number of parameters
    public static Add_Donations newInstance(String param1, String param2) {
        Add_Donations fragment = new Add_Donations();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_add__donations, container, false);

        ImageButton btnback = rootview.findViewById(R.id.buttonnback);
        btnback.setOnClickListener(v->{
            // This will go back to home fragments
            Ask_Donations ask_donations = new Ask_Donations();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("name", name);
            ask_donations.setArguments(bundle);
            replaceFragement(ask_donations);

        });

        // This is button is used to go to Ask Donations fragments
        Button btnsubmit = rootview.findViewById(R.id.AddButton);
        btnsubmit.setOnClickListener(v->{
            // This will go to Ask Donations fragments

        });

        DonationName = rootview.findViewById(R.id.donationsName);
        DonatedOwner = rootview.findViewById(R.id.donateOwner);
        Descriptions = rootview.findViewById(R.id.donationsDescription);



        return rootview;

    }

    // This will call the fragments
    private void replaceFragement(Fragment fragment) {

        // Call the fragment manager and begin the transaction to replace the fragment
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}