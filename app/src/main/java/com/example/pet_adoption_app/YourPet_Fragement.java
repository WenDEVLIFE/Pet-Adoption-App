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

import java.util.ArrayList;
import java.util.List;

import ClassPackage.Pets;
import adapter.YourPetAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YourPet_Fragement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YourPet_Fragement extends Fragment implements YourPetAdapter.onCancelListener {

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

    private YourPetAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public YourPet_Fragement() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YourPet_Fragement.
     */
    // TODO: Rename and change types and number of parameters
    public static YourPet_Fragement newInstance(String param1, String param2) {
        YourPet_Fragement fragment = new YourPet_Fragement();
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
        View rootView =  inflater.inflate(R.layout.fragment_your_pet__fragement, container, false);


        // Our search view
        SearchView searchView = rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // Convert the user input to lowercase
                String userInput = query.toLowerCase();

                // Create a new list
                List<Pets> newList = new ArrayList<>();
                for (Pets pet : petList) {

                    // Check if the pet name or breed contains the user input
                    if (pet.getName().toLowerCase().contains(userInput) || pet.getBreed().toLowerCase().contains(userInput)) {

                       // Add the pet to the new list
                        newList.add(pet);
                    }
                }

                // Call the searchPets method
                adapter.searchPets(newList);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // Convert the user input to lowercase
                String userInput = newText.toLowerCase();

                // Create a new list
                List<Pets> newList = new ArrayList<>();

                // Loop through the petList
                for (Pets pet : petList) {
                    if (pet.getName().toLowerCase().contains(userInput) || pet.getBreed().toLowerCase().contains(userInput)) {
                        // Add the pet to the new list
                        newList.add(pet);
                    }
                }

                // Call the searchPets method
                adapter.searchPets(newList);
                return true;
            }
        });


        ImageButton btnback = rootView.findViewById(R.id.buttonnback);
        btnback.setOnClickListener(v ->
                goHomeFragmenet());

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        petList = new ArrayList<>();
        adapter = new YourPetAdapter(petList);
        recyclerView.setAdapter(adapter);

        // Ensure MainActivity implements OnDeleteClickListener
        adapter.setOnCancelListener(this);
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
    public void goHomeFragmenet(){
        // This will call the home fragment
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("name", name);
        fragment.setArguments(bundle);
        replaceFragement(fragment);
    }

    // Load pets from Firestore
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

                    // Check if the owner is the same as the current user
                    String owner = doc.getString("owner");

                    // If the owner is the same as the current user
                    if (owner.equals(name)) {

                        // Get the data from Firestore
                        String name = doc.getString("name");
                        String breed = doc.getString("breed");
                        String description = doc.getString("description");
                        String image = doc.getString("image");
                        petList.add(new Pets(name, breed, owner, description, image));
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    // Implement onCancel method
    @Override
    public void onCancel(int position) {
     AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Pet");
        builder.setMessage("Are you sure you want to delete this pet?");
        builder.setPositiveButton("Yes", (dialog, which) -> {

            // Get the pet at the position
            Pets pet = petList.get(position);
            db.collection("Pets")
                    .whereEqualTo("name", pet.getName())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {

                        // Loop through the documents
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



                        }
                    });
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // Do nothing
        });
        builder.show();

    }
}