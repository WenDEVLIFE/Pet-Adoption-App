package com.example.pet_adoption_app;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ClassPackage.Donation;
import ClassPackage.Pets;
import adapter.DonationAdapter;
import adapter.PetAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ask_Donations#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ask_Donations extends Fragment implements  DonationAdapter.onCancelListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String username,name;
    RecyclerView recyclerView;

    private List<Donation> donationList;

    private DonationAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public Ask_Donations() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ask_Donations.
     */
    // TODO: Rename and change types and number of parameters
    public static Ask_Donations newInstance(String param1, String param2) {
        Ask_Donations fragment = new Ask_Donations();
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
            username = getArguments().getString("username");
            name = getArguments().getString("name");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null) {
            username = getArguments().getString("username");
            name = getArguments().getString("name");
        }

        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_ask__donations, container, false);

        // our image button code here
        ImageButton btnback = rootview.findViewById(R.id.buttonnback);
        btnback.setOnClickListener(v->{
            // This will go back to home fragments
            HomeFragment homeFragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("name", name);
            homeFragment.setArguments(bundle);
            replaceFragement(homeFragment);


        });

        // This is for floating action button add donation
        FloatingActionButton btnaskdonation = rootview.findViewById(R.id.floatingActionButton);
        btnaskdonation.setOnClickListener(v->{
            // This will go to ask donation form
            Add_Donations add_donations = new Add_Donations();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("name", name);
            add_donations.setArguments(bundle);
            replaceFragement(add_donations);

        });


        recyclerView = rootview.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        donationList = new ArrayList<>();
        adapter = new DonationAdapter(donationList);
        recyclerView.setAdapter(adapter);

        // Ensure MainActivity implements OnDeleteClickListener
        adapter.setOnCancelListener(this);
        LoadDonations();

    return rootview;
    }

    private void LoadDonations() {
        // Retrieve the data from Firestore
        db.collection("Donations").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                donationList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    if (doc.get("name") != null) {
                        Donation donation = new Donation(doc.getString("donateName"), doc.getString("donateOwner"), doc.getString("donateDescription"));
                        donationList.add(donation);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void replaceFragement(Fragment fragment) {

        // Call the fragment manager and begin the transaction to replace the fragment
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onCancel(int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Go TO Donation");
        builder.setMessage("Do you want to go to donation page?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // This will go to ask donation form
            Add_Donations add_donations = new Add_Donations();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("name", name);
            add_donations.setArguments(bundle);
            replaceFragement(add_donations);
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // Do nothing
        });
        builder.show();

    }
}