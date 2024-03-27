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

import adapter.PendingPetsAdapter;
import ClassPackage.PetsPending;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Adopt_request#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Adopt_request extends Fragment implements PendingPetsAdapter.onAdoptListener, PendingPetsAdapter.onCancelListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String username, name;

    RecyclerView recyclerView;

    private List<PetsPending> petList;

    private PendingPetsAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public Fragment_Adopt_request() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Adopt_request.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Adopt_request newInstance(String param1, String param2) {
        Fragment_Adopt_request fragment = new Fragment_Adopt_request();
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

        // Inflate the layout for this fragment
        if (getArguments() != null) {

            // Get the username and name from the bundle
            username = getArguments().getString("username");
            name = getArguments().getString("name");
        }


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment__adopt_request, container, false);

        // Our search view
        SearchView searchView = rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // This will search the pet by name
                String userInput = query.toLowerCase();

                // filtered the value
                List <PetsPending > newList = new ArrayList<>();

                // For pet and petList
                for (PetsPending pet : petList) {

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
                List <PetsPending > newList = new ArrayList<>();

                // For pet and petList
                for (PetsPending pet : petList) {

                    // then if the pet name contains the user input
                    if (pet.getName().toLowerCase().contains(userInput)) {

                        // then add the pet to the new list
                        newList.add(pet);
                    }
                }

                // then search the pet
                adapter.searchPets(newList);
                return true;
            }
        });


        // This is our button back to home
        ImageButton btnback = rootView.findViewById(R.id.buttonnback);
        btnback.setOnClickListener(v ->
                goHomeFragmenet());

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        petList = new ArrayList<>();
        adapter = new PendingPetsAdapter(petList);
        recyclerView.setAdapter(adapter);

        // Ensure MainActivity implements OnDeleteClickListener
        adapter.setOnAdoptListener(this);
        adapter.setOnCancelListener(this);
        LoadPendingPets();


        return rootView;
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
    public void onAdopt(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Adopt Request");
        builder.setMessage("Are you sure you want to adopt this pet?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            PetsPending pet = petList.get(position);

            // Delete document from "PendingAdoption" collection
            db.collection("PendingAdoption")
                    .whereEqualTo("name", pet.getName())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            documentSnapshot.getReference().delete();
                        }
                    });

            // Delete document from "Pets" collection
            db.collection("Pets")
                    .whereEqualTo("name", pet.getName())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            documentSnapshot.getReference().delete();
                        }
                    });

            // Move pet details to "AdoptedPets" collection
            HashMap<String, Object> petMap = new HashMap<>();
            petMap.put("name", pet.getName());
            petMap.put("breed", pet.getBreed());
            petMap.put("previous owner", pet.getOwner());
            petMap.put("description", pet.getDescription());
            petMap.put("image", pet.getImageUrl());
            petMap.put("new owner", pet.getAdopt_requets());
            db.collection("AdoptedPets").document().set(petMap);

            // Remove pet from the list
            petList.remove(position);

            // Show success message
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            builder1.setTitle("Adopt Request");
            builder1.setMessage("You have successfully  approved to adopted this pet!");
            builder1.setPositiveButton("Ok", (dialog1, which1) -> dialog1.dismiss());
            builder1.show();

            // This will notify the user that the adoption request has been approved
            HashMap <String, Object> Notifications = new HashMap<>();
            Notifications.put("Notifications details", "The adoption request for " + pet.getName() + " has been approved");
            Notifications.put("name", pet.getAdopt_requets());
            db.collection("Notifications").document().set(Notifications);

            // Notify adapter about the change in data set
            adapter.notifyDataSetChanged();
        }).setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public void onCancel(int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Cancel Request");
        builder.setMessage("Are you sure you want to cancel this request?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            PetsPending pet = petList.get(position);
            db.collection("PendingAdoption").document(pet.getName()).delete();
            petList.remove(position);

            // Notify adapter about the change in data set
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDate date = LocalDate.now();
                HashMap <String, Object> Notifications = new HashMap<>();
                Notifications.put("Notifications details", "The adoption request for " + pet.getName() + " has been cancelled");
                Notifications.put("name", pet.getAdopt_requets());
                Notifications.put("date", date);
                db.collection("Notifications").document().set(Notifications);
            }

            adapter.notifyDataSetChanged();
        }).setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();


    }

    public void LoadPendingPets(){
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
                    String owner = doc.getString("owner");
                    if (owner != null && owner.equals(name)) {
                        String name = doc.getString("name");
                        String breed = doc.getString("breed");
                        String description = doc.getString("description");
                        String image = doc.getString("image");
                        String adoptRequest = doc.getString("adoption request");
                        PetsPending pet = new PetsPending(name, breed, owner, description, image, adoptRequest);
                        petList.add(pet);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}