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

import java.util.ArrayList;
import java.util.List;

import adapter.Pending_PetsAdapter;
import ClassPackage.PetsPending;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Pending_Adoption#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Pending_Adoption extends Fragment implements Pending_PetsAdapter.onCancelListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String username, name;

    RecyclerView recyclerView;

    private List<PetsPending> petList;

    private Pending_PetsAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public Pending_Adoption() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Pending_Adoption.
     */
    // TODO: Rename and change types and number of parameters
    public static Pending_Adoption newInstance(String param1, String param2) {
        Pending_Adoption fragment = new Pending_Adoption();
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

        // Get the username and name from the bundle
        if (getArguments() != null) {
            username = getArguments().getString("username");
            name = getArguments().getString("name");

        }

        // Inflate the layout for this fragment
        View rootview= inflater.inflate(R.layout.fragment_pending__adoption, container, false);


        // ImageButton of the back button
        ImageButton btnback = rootview.findViewById(R.id.buttonnback);
        btnback.setOnClickListener(v ->
                goHomeFragmenet());

        recyclerView = rootview.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        petList = new ArrayList<>();
        adapter = new Pending_PetsAdapter(petList);
        recyclerView.setAdapter(adapter);

        // Ensure MainActivity implements OnDeleteClickListener
        adapter.setOnCancelListener(this);
        LoadPending();

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

    @Override
    public void onCancel(int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Cancel Adoption Request");
        builder.setMessage("Are you sure you want to cancel this adoption request?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            PetsPending pet = petList.get(position);
            db.collection("PendingAdoption").document(pet.getAdoptRequest()).delete();
            LoadPending();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // Do nothing
        });

    }

    public void LoadPending(){
        db.collection("PendingAdoption").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                petList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    String adoptRequest = doc.getString("adoption request");

                    if (adoptRequest != null && adoptRequest.equals(name)) {
                        String owner = doc.getString("owner");
                        String name = doc.getString("name");
                        String breed = doc.getString("breed");
                        String description = doc.getString("description");
                        String image = doc.getString("image");
                        PetsPending pet = new PetsPending(name, breed, owner, description, image, adoptRequest);
                        petList.add(pet);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}