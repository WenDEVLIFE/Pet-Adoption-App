package com.example.pet_adoption_app;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
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
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ClassPackage.Donation;
import ClassPackage.PetsPending;
import adapter.DonationAdapter;
import adapter.DonationRequestAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Your_Donations#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Your_Donations extends Fragment implements DonationRequestAdapter.onCancelListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

     String username, name;

    RecyclerView recyclerView;

    private List<Donation> donationList;

    private DonationRequestAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ProgressDialog progressDialog;

    public Fragment_Your_Donations() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Your_Donations.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Your_Donations newInstance(String param1, String param2) {
        Fragment_Your_Donations fragment = new Fragment_Your_Donations();
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
        View rootview = inflater.inflate(R.layout.fragment__your__donations, container, false);

        // Our search view
        SearchView searchView = rootview.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // This will search the donation list
                String userInput = query.toLowerCase();

                // This will search the donation list
                ArrayList<Donation> searchList = new ArrayList<>();
                for (Donation donation : donationList) {

                    // This will search the donation list
                    if (donation.getDonationName().toLowerCase().contains(userInput)) {
                        searchList.add(donation);
                    }

                    else{

                    }
                }

                // This will search the donation list
                adapter.searchList(searchList);


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // This will search the donation list
               String userInput = newText.toLowerCase();

               // This will search the donation list
                ArrayList<Donation> searchList = new ArrayList<>();

                // This will search the donation list
                for (Donation donation : donationList) {

                    // This will search the donation list
                    if (donation.getDonationName().toLowerCase().contains(userInput)) {
                        searchList.add(donation);
                    }

                    else{
                        AlertDialog dialog = new AlertDialog.Builder(getContext())
                                .setTitle("No Item Found")
                                .setMessage("No item found with the name " + newText)
                                .setPositiveButton("Ok", null)
                                .create();
                        dialog.show();
                    }
                }

                // This will search the donation list
                adapter.searchList(searchList);


                return true;
            }
        });


        // This is our button back to home
        ImageButton btnback = rootview.findViewById(R.id.buttonnback);
        btnback.setOnClickListener(v ->
                goHomeFragmenet());

        recyclerView = rootview.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        donationList = new ArrayList<>();
        adapter = new DonationRequestAdapter(donationList);
        recyclerView.setAdapter(adapter);

        adapter.setOnCancelListener(this);
        LoadDonations();

    return rootview;
    }

    private void replaceFragement(Fragment fragment) {

        // Call the fragment manager and begin the transaction to replace the fragment
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
    public void goHomeFragmenet(){
        // This will call the home fragment
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("name", name);
        fragment.setArguments(bundle);
        replaceFragement(fragment);
    }
    private void LoadDonations() {
        // Retrieve the data from Firestore
        db.collection("Donations").addSnapshotListener((value, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }

            donationList.clear();
            for (QueryDocumentSnapshot doc : value) {

            String donateOwner = doc.getString("donateOwner");
            if (donateOwner.equals(name)) {
                Donation donation = new Donation(doc.getString("donateName"), doc.getString("donateOwner"), doc.getString("donateDescription"));
                donationList.add(donation);
            }

            }
            adapter.notifyDataSetChanged();
        });

    }
    @Override
    public void onCancel(int position) {
        AlertDialog deletedialog = new AlertDialog.Builder(getContext())
                .setTitle("Delete Donation")
                .setMessage("Are you sure you want to delete this donation?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Donation donations = donationList.get(position);

                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Deleting Donation");
                    progressDialog.show();


                    // Fetch the donation
                    db.collection("Donations")
                            .whereEqualTo("donateName", donations.getDonationName())
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Delete the donation
                                        db.collection("Donations").document(document.getId()).delete();
                                        Toast.makeText(getContext(), "Donation deleted", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            });
                    donationList.remove(position);
                    adapter.notifyDataSetChanged();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        // Get the current date
                        LocalDate date = LocalDate.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String formattedDate = date.format(formatter);

                        // Create a new transaction
                        HashMap<String, Object> transaction = new HashMap<>();
                        transaction.put("Transaction", "You have deleted an adoption request for " + donations.getDogOwner() + " on " + formattedDate);
                        transaction.put("name", name);
                        transaction.put("date", formattedDate);

                        db.collection("Transaction").document().set(transaction);
                    }
                })
                .setNegativeButton("No", null)
                .create();
        deletedialog.show();
        deletedialog.show();



    }


}