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

import ClassPackage.Pets;
import adapter.YourLostPetsAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragments_Lost_Pets#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragments_Lost_Pets extends Fragment implements  YourLostPetsAdapter.onCancelListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String username,name;

    private List<Pets> petList;

    private YourLostPetsAdapter adapter;

    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Fragments_Lost_Pets() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragments_Lost_Pets.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragments_Lost_Pets newInstance(String param1, String param2) {
        Fragments_Lost_Pets fragment = new Fragments_Lost_Pets();
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
        View rootview = inflater.inflate(R.layout.activity_your_lost_pets, container, false);

        // our back button image
        ImageButton back = rootview.findViewById(R.id.buttonnback);
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


        recyclerView = rootview.findViewById(R.id.lostpetrecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        petList = new ArrayList<>();
        adapter = new YourLostPetsAdapter(petList);
        recyclerView.setAdapter(adapter);

        adapter.setOnCancelListener(this);

        LoadPet();



    return rootview;
    }

    private void LoadPet() {
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
                    String owner = doc.getString("owner");
                     if (owner.equals(name)) {
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


    // This will call the fragments
    private void replaceFragement(Fragment fragment) {

        // Call the fragment manager and begin the transaction to replace the fragment
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onCancel(int position) {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Cancel Lost Pet Adoption")
                .setMessage("Are you sure you want to delete this Lost pet?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    Pets pet = petList.get(position);
                    db.collection("LostPets")
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
                                            .setTitle("Lost Pet Deleted")
                                            .setMessage("Adoption has been deleted successfully")
                                            .setPositiveButton("Ok", null)
                                            .create();
                                    dialog1.show();


                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        // Get the current date
                                        LocalDate date = LocalDate.now();
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                        String formattedDate = date.format(formatter);

                                       // This will insert the data to the database
                                        HashMap<String, Object> transaction = new HashMap<>();
                                        transaction.put("Transaction", "You Delete the Adoption of " + pet.getName() + " Pet from the Lost Pets List ");
                                        transaction.put("name", pet.getOwner());
                                        transaction.put("date", formattedDate);

                                        // This will insert the data to the database
                                        db.collection("Transaction").document().set(transaction);
                                    }

                                }
                            });
                })
                .setNegativeButton("No", null)
                .create();
        dialog.show();
    }

}