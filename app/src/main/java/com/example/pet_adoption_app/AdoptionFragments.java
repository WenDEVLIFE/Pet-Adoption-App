package com.example.pet_adoption_app;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ViewTreeObserver;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.PetAdapter;
import adapter.Pets;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdoptionFragments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdoptionFragments extends Fragment implements PetAdapter.onAdoptListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private TextView usernametext;

    String username, name;

    RecyclerView recyclerView;

    private List<Pets> petList;

    private PetAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AdoptionFragments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdoptionFragments newInstance(String param1, String param2) {
        AdoptionFragments fragment = new AdoptionFragments();
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

            // This is a test to get the value from the navbar and send it to the fragments.
            username = getArguments().getString("username");
            name = getArguments().getString("name");


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null) {

            // This is a test to get the value from the navbar and send it to the fragments.
            username = getArguments().getString("username");
            name = getArguments().getString("name");


        }
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_adopt, container, false);

        // Our search view
        SearchView searchView = rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // This will search the pet by name
                String userInput = query.toLowerCase();

                // filtered the value
                List<Pets> newList = new ArrayList<>();

                // This will search the pet by name
                for (Pets pet : petList) {

                    // then if the pet name contains the user input
                    if (pet.getName().toLowerCase().contains(userInput)) {

                        // then add the pet to the new list
                        newList.add(pet);
                    }
                }

                // then search the pet
                adapter.searchPets(newList);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // This will search the pet by name
                String userInput = newText.toLowerCase();

                // filtered the value
                List<Pets> newList = new ArrayList<>();

                // This will search the pet by name
                for (Pets pet : petList) {

                    // then if the pet name contains the user input
                    if (pet.getName().toLowerCase().contains(userInput)) {

                        // then add the pet to the new list
                        newList.add(pet);
                    }
                }

                // then search the pet
                adapter.searchPets(newList);
                return  true;
            }
        });

        // our floating button
        FloatingActionButton fab = rootView.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> {
            AddAdopt addAdopt = new AddAdopt();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("name", name);
            addAdopt.setArguments(bundle);
           replaceFragement(addAdopt);
        });


        // our back button image
        ImageButton back = rootView.findViewById(R.id.buttonnback);
        back.setOnClickListener(v ->
        {
            // This will go to Home Fragments
            HomeFragment homeFragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("name", name);
            homeFragment.setArguments(bundle);
            replaceFragement(homeFragment);
        });

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        petList = new ArrayList<>();
        adapter = new PetAdapter(petList);
        recyclerView.setAdapter(adapter);

        // Ensure MainActivity implements OnDeleteClickListener
        adapter.setOnAdoptListener(this);
        LoadPets();

        return rootView;

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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Adopt");
        builder.setMessage("Are you sure you want to adopt this pet?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Adopt the pet
            // Get the pet from the list
            Pets pet = petList.get(position);
            // Add the pet to the user's list of pets


                    HashMap<String, Object> data = new HashMap<>();
                    data.put("name", pet.getName());
                    data.put("breed", pet.getBreed());
                    data.put("description", pet.getDescription());
                    data.put("owner", pet.getOwner());
                    data.put("image", pet.getImageUrl());
                    data.put("adoption request", name);

                    db.collection("PendingAdoption").document().set(data);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        LocalDate date = LocalDate.now();
                        HashMap <String, Object> Notifications = new HashMap<>();
                        Notifications.put("Notifications details", "The person has pending request " + pet.getName());
                        Notifications.put("name", pet.getOwner());
                        Notifications.put("date", date.toString());
                        db.collection("Notifications").document().set(Notifications);
                    }
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setTitle("Adopt");
                    builder1.setMessage("You have successfully requested to adopt " + pet.getName());
                    builder1.setPositiveButton("Ok", (dialog1, which1) -> {
                        // Do nothing
                    })
                            .show();


        })
                .setNegativeButton("No", (dialog, which) -> {
                    // Do nothing
                })
                .show();
    }

    public void LoadPets() {
        // Retrieve the data from Firestore
        db.collection("Pets").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                petList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    if (doc.get("name") != null) {
                        String name = doc.getString("name");
                        String breed = doc.getString("breed");
                        String description = doc.getString("description");
                        String owner = doc.getString("owner");
                        String image = doc.getString("image");
                        petList.add(new Pets(name, breed, owner, description, image));
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

    }
}