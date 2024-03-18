package com.example.pet_adoption_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangeUserName_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeUserName_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChangeUserName_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * Use this factory method to create a new instance of
     * Use this factory method to create a new instance of
     * Use this factory method to create a new instance of
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangeUserName_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangeUserName_Fragment newInstance(String param1, String param2) {
        ChangeUserName_Fragment fragment = new ChangeUserName_Fragment();
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
        View rootview = inflater.inflate(R.layout.fragment_change_user_name_, container, false);
        ImageButton back = rootview.findViewById(R.id.buttonnback);
        back.setOnClickListener(v->{
            // This will go back to user preferences

            replaceFragement(new UserPreferences());
        });

        Button ChangeUsername=rootview.findViewById(R.id.button);
        ChangeUsername.setOnClickListener(v->{
            // This will go back to user preferences

            replaceFragement(new UserPreferences());

        });
    return rootview;
    }
    private void replaceFragement(Fragment fragment) {

        // Call the fragment manager and begin the transaction to replace the fragment
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}