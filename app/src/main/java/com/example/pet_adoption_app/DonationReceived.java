package com.example.pet_adoption_app;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ClassPackage.DonationReceive;
import ClassPackage.Pets;
import adapter.DonationReceivedAdapter;
import adapter.PetAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DonationReceived#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DonationReceived extends Fragment implements DonationReceivedAdapter.onAdoptListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String username, name;

    RecyclerView recyclerView;

    private List<DonationReceive> donationReceiveList;

    private DonationReceivedAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public DonationReceived() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YourDonations.
     */
    // TODO: Rename and change types and number of parameters
    public static DonationReceived newInstance(String param1, String param2) {
        DonationReceived fragment = new DonationReceived();
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

        // Get the username and name from the previous fragment
        if (getArguments() != null) {
            username = getArguments().getString("username");
            name = getArguments().getString("name");
        }

        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_donation_received, container, false);

        //  Set the back button to go back to the previous fragment
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

        recyclerView = rootview.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        donationReceiveList = new ArrayList<>();
        adapter = new DonationReceivedAdapter(donationReceiveList);
        recyclerView.setAdapter(adapter);

        // Ensure MainActivity implements OnDeleteClickListener
        adapter.setOnAdoptListener(this);
        LoadDonationReceive();

    return rootview;
    }

    private void LoadDonationReceive() {
        // Retrieve the data from Firestore
        db.collection("Donated").addSnapshotListener((value, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }

            donationReceiveList.clear();
            for (QueryDocumentSnapshot doc : value) {
               String donatedby = doc.getString("donatedName");
               if(donatedby.equals(name)){
                   String donatename = doc.getString("donateItemName");
                   String donateto = doc.getString("donateTo");
                   String description = doc.getString("donatedDescription");
                   String image = doc.getString("image");
                      DonationReceive donationReceive = new DonationReceive(donatename, donateto, donatedby, description, image);
                      donationReceiveList.add(donationReceive);

               }

            }
            adapter.notifyDataSetChanged();
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
    public void onAdopt(int position) {

    }
}