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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.AdoptedAdapter;
import ClassPackage.PetsPending;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Adopted_pets#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Adopted_pets extends Fragment implements AdoptedAdapter.onCancelListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AdoptedAdapter adapter;

    private List<PetsPending> petList;

    RecyclerView recyclerView;
     String username, name;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Fragment_Adopted_pets() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Adopted_pets.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Adopted_pets newInstance(String param1, String param2) {
        Fragment_Adopted_pets fragment = new Fragment_Adopted_pets();
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
       View rootView = inflater.inflate(R.layout.fragment__adopted_pets, container, false);


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


       // ImageButton of the back button
        ImageButton btnback = rootView.findViewById(R.id.buttonnback);
        btnback.setOnClickListener(v ->
                goHomeFragmenet());

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        petList = new ArrayList<>();
        adapter = new AdoptedAdapter(petList);
        recyclerView.setAdapter(adapter);

        // Ensure MainActivity implements OnDeleteClickListener
        adapter.setOnCancelListener(this);

        LoadYourPets();




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
    public void onCancel(int position) {

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Cancel Adoption")
                .setMessage("Are you sure you want to delete this adoption?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    PetsPending pet = petList.get(position);
                    db.collection("AdoptedPets")
                            .whereEqualTo("name", pet.getName())
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
                                    petList.remove(position);
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
                                        transaction.put("Transaction", "The adoption request for " + pet.getName() + " has been cancelled");
                                        transaction.put("name", pet.getOwner());
                                        transaction.put("date", formattedDate);

                                        db.collection("Transaction").document().set(transaction);
                                    }

                                }
                            });
                })
                .setNegativeButton("No", null)
                .create();
        dialog.show();
    }

    private void LoadYourPets() {
        db.collection("AdoptedPets").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                petList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    String newoner = doc.getString("new owner");

                    if (newoner != null && newoner.equals(name)) {
                        String owner = doc.getString("previous owner");
                        String name = doc.getString("name");
                        String breed = doc.getString("breed");
                        String description = doc.getString("description");
                        String image = doc.getString("image");
                        PetsPending pet = new PetsPending(name, breed, newoner, description, image, owner);
                        petList.add(pet);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}