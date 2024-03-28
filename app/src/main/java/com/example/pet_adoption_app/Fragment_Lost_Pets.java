package com.example.pet_adoption_app;

import static android.content.ContentValues.TAG;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import adapter.LostAdapter;
import ClassPackage.Pets;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Lost_Pets#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Lost_Pets extends Fragment implements  LostAdapter.onCancelListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String username, name;

    RecyclerView recyclerView;

    private List<Pets> petList;

    private LostAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public Fragment_Lost_Pets() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Lost_Pets.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Lost_Pets newInstance(String param1, String param2) {
        Fragment_Lost_Pets fragment = new Fragment_Lost_Pets();
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
        View rootview = inflater.inflate(R.layout.fragment_lost_pets, container, false);

        // Our search view
        SearchView searchView = rootview.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // This will filter the list as the user types
                String userInput = query.toLowerCase();

                // Create a new list to store the filtered data
                List<Pets> newList = new ArrayList<>();

                // Loop through the list of pets and add the ones that match the search query
                for (Pets pet : petList) {

                    // Check if the pet name or breed contains the search query
                    if (pet.getName().toLowerCase().contains(userInput) || pet.getBreed().toLowerCase().contains(userInput)) {
                        newList.add(pet);
                    } else{
                        // Check if the pet name or breed contains the search query
                        if (pet.getName().toLowerCase().contains(userInput) || pet.getBreed().toLowerCase().contains(userInput)) {
                            newList.add(pet);

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Search Results");
                            builder.setMessage("No results found");
                            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                            builder.show();
                        }
                    }
                }

                // Update the adapter with the new list
                adapter.searchPets(newList);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // This will filter the list as the user types
                String userInput = newText.toLowerCase();

                // Create a new list to store the filtered data
                List<Pets> newList = new ArrayList<>();

                // Loop through the list of pets and add the ones that match the search query
                for (Pets pet : petList) {

                    // Check if the pet name or breed contains the search query
                    if (pet.getName().toLowerCase().contains(userInput) || pet.getBreed().toLowerCase().contains(userInput)) {
                        newList.add(pet);
                    }
                    else {
                        // Check if the pet name or breed contains the search query
                        if (pet.getName().toLowerCase().contains(userInput) || pet.getBreed().toLowerCase().contains(userInput)) {
                            newList.add(pet);

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Search Results");
                            builder.setMessage("No results found");
                            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                            builder.show();
                        }
                    }
                }

                // Update the adapter with the new list
                adapter.searchPets(newList);
                return true;
            }
        });

        // This button serves as a floating action button to add lost pets
        FloatingActionButton buttonAdd = rootview.findViewById(R.id.floatingActionButton);
        buttonAdd.setOnClickListener(v->{

            // Throw the values on the next fragment
            Fragment_Add_Lost_Pets fragment = new Fragment_Add_Lost_Pets();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("name", name);
            fragment.setArguments(bundle);
            replaceFragement(fragment);

        });

        // this for button back image button
        ImageButton btnback = rootview.findViewById(R.id.buttonnback);
        btnback.setOnClickListener(v ->
                goHomeFragmenet());

        recyclerView = rootview.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        petList = new ArrayList<>();
        adapter = new LostAdapter(petList);
        recyclerView.setAdapter(adapter);

        // Ensure MainActivity implements OnDeleteClickListener
        adapter.setOnCancelListener(this);
        LoadLostPets();

        return rootview;

    }

    private void LoadLostPets() {
        // Retrieve the data from Firestore
        db.collection("LostPets").addSnapshotListener(new EventListener<QuerySnapshot>() {
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

        // get the pet object from the list
        Pets pet = petList.get(position);

        // This will call the report fragment
        ReportFragment fragment = new ReportFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("name", name);
        bundle.putString("petowner", pet.getOwner());
        fragment.setArguments(bundle);
        replaceFragement(fragment);

    }
}