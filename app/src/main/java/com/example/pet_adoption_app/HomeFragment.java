package com.example.pet_adoption_app;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // our code here with image buttons
        ImageButton buttonPending = rootView.findViewById(R.id.imageButton_pending);
        ImageButton buttonMissing = rootView.findViewById(R.id.missingbutton);
        ImageButton buttonDonate = rootView.findViewById(R.id.donatebutton);
        ImageButton buttonAdopt = rootView.findViewById(R.id.buttonadopt);
        ImageButton buttonAdoptPet = rootView.findViewById(R.id.button_adopt_pet);
        ImageButton ButtonAdoptRequest = rootView.findViewById(R.id.adopt_request);
        ImageButton Transaction_History = rootView.findViewById(R.id.transaction_history);


        buttonPending.setOnClickListener(v -> {
            // Handle button pending click
            // This will go to pending adopt pets
            replaceFragement(new Pending_Adoption());
        });

        buttonMissing.setOnClickListener(v -> {
            // Handle button pending click
            // This will go to lost pets fragments
            replaceFragement(new Fragment_Lost_Pets());
        });

        buttonDonate.setOnClickListener(v -> {
            // Handle button pending click
            replaceFragement(new Fragment_Your_Donations());
        });

        buttonAdopt.setOnClickListener(v -> {
            // Handle button pending click
            // This will go to your pet fragments
            replaceFragement(new YourPet_Fragement());
        });

        buttonAdoptPet.setOnClickListener(v -> {
            // Handle button pending click
            // Go to fragment adopted pets
            replaceFragement(new Fragment_Adopted_pets());
        });

        ButtonAdoptRequest.setOnClickListener(v -> {
            // Handle button pending click
            replaceFragement(new Fragment_Adopt_request());
        });

        Transaction_History.setOnClickListener(v -> {
            // Handle button pending click
            replaceFragement(new Transaction_History());
        });

        return rootView;
    }

    private void replaceFragement(Fragment fragment) {

        // Call the fragment manager and begin the transaction to replace the fragment
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}