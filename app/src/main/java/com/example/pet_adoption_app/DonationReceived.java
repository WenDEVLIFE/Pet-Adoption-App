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
import android.widget.ImageButton;

import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ClassPackage.DonationReceive;
import ClassPackage.Pets;
import ClassPackage.PetsPending;
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

            // Create a new instance of the HomeFragment
            HomeFragment homeFragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("name", name);
            homeFragment.setArguments(bundle);
            replaceFragement(homeFragment);


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

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Receive Donation")
                .setMessage("Are you sure this item is receive?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    DonationReceive donationReceive = donationReceiveList.get(position);
                    db.collection("Donated")
                            .whereEqualTo("donateItemName", donationReceive.getDonateItemName())
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    // Get the image URL from the document
                                    String imageUrl = documentSnapshot.getString("image");

                                    // Create a storage reference from our app
                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference storageRef = storage.getReferenceFromUrl(imageUrl);

                                    // Delete the file
                                    storageRef.delete().addOnSuccessListener(aVoid -> {
                                        // File deleted successfully
                                        Log.d(TAG, "onSuccess: deleted file");
                                    }).addOnFailureListener(exception -> {
                                        // Uh-oh, an error occurred!
                                        Log.d(TAG, "onFailure: did not delete file");
                                    });

                                    documentSnapshot.getReference().delete();
                                    donationReceiveList.remove(position);
                                    adapter.notifyDataSetChanged();

                                    AlertDialog dialog1 = new AlertDialog.Builder(getContext())
                                            .setTitle("Adoption Deleted")
                                            .setMessage("Adoption has been deleted successfully")
                                            .setPositiveButton("Ok", null)
                                            .create();
                                    dialog1.show();


                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        // Get the current date
                                        LocalDate date = LocalDate.now();
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                        String formattedDate = date.format(formatter);

                                        // Create a new transaction
                                        HashMap<String, Object> transaction = new HashMap<>();
                                        transaction.put("Transaction", "You  have received a donation from " + donationReceive.getDonateName() + " on " + formattedDate + " for " + donationReceive.getDonateItemName() + " to " + donationReceive.getDonateTo());
                                        transaction.put("name", donationReceive.getDonateName());
                                        transaction.put("date", formattedDate);

                                        // This will notify the user that the donation request has been approved and received
                                        HashMap <String, Object> Notifications = new HashMap<>();
                                        Notifications.put("Notifications details", " The donate has been received a donation from " + donationReceive.getDonateName() + " for " + donationReceive.getDonateItemName() + " to " + donationReceive.getDonateTo() + " on " + formattedDate + "");
                                        Notifications.put("name", donationReceive.getDonateTo());
                                        db.collection("Notifications").document().set(Notifications);

                                    }

                                }
                            });
                })
                .setNegativeButton("No", null)
                .create();
        dialog.show();

    }
}