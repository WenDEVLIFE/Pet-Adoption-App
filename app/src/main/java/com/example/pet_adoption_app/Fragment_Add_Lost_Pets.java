package com.example.pet_adoption_app;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Add_Lost_Pets#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Add_Lost_Pets extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGE_REQUEST = 1;
   ImageView imageView;
    private Uri imageUri; // Add this line at the top of your class

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

     String username, name;

    EditText dogname;
    EditText DogBreed;
    EditText DogOwner;
    EditText descriptions;

    ProgressDialog progressDialog;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Fragment_Add_Lost_Pets() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Add_Lost_Pets.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Add_Lost_Pets newInstance(String param1, String param2) {
        Fragment_Add_Lost_Pets fragment = new Fragment_Add_Lost_Pets();
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
        View rootview = inflater.inflate(R.layout.fragment__add__lost__pets, container, false);

        // Get the back button and set the on click listener
        ImageButton btnback = rootview.findViewById(R.id.buttonnback);
        btnback.setOnClickListener(v->{

            // Call the replaceFragment method
            Fragments_Lost_Pets fragment = new Fragments_Lost_Pets();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("name", name);
            fragment.setArguments(bundle);
            replaceFragement(fragment);
        });

        // Get the image view and the import button
        imageView = rootview.findViewById(R.id.imageView4);

        // Set the on click listener for the import button
        Button ImportButton = rootview.findViewById(R.id.importimage);
        ImportButton.setOnClickListener(v->{

            // call the openFileChooser method
            openFileChooser();

        });

         // Get the edit text fields
        dogname = rootview.findViewById(R.id.editTextText);
        DogBreed = rootview.findViewById(R.id.dogbreed);
        DogOwner = rootview.findViewById(R.id.petowner);
        DogOwner.setEnabled(false);
        DogOwner.setText(name);
        descriptions = rootview.findViewById(R.id.petdescription);

        // Get the add button and set the on click listener
        Button btnadd = rootview.findViewById(R.id.AddButton);
        btnadd.setOnClickListener(v->{


            // Get the text from the edit text fields
            String DogName = dogname.getText().toString();
        String Breed = DogBreed.getText().toString();
        String Owner = DogOwner.getText().toString();
        String Description = descriptions.getText().toString();

            // Check if the fields are empty
            if(DogName.isEmpty() || Breed.isEmpty() || Owner.isEmpty() || Description.isEmpty()){

                // Show a toast message
                Toast.makeText(getActivity(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }else{

                // Show a progress dialog
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Uploading...");


                // Call the addLostPet method
               LostPets( DogName, Breed, Owner, Description, imageUri);
            }

        });


    return rootview;
    }

    private void LostPets(String dogName, String breed, String owner, String description, Uri dogImageUri) {
        // Create a storage reference
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("uploads");

        // Create a reference to the file to be uploaded
        StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(dogImageUri));

        // Show the ProgressDialog
        progressDialog.show();

        // Upload the file to Firebase Storage
        fileReference.putFile(dogImageUri).addOnSuccessListener(taskSnapshot -> {
            // Get the download URL of the uploaded file
            fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                // Create a new document for the dog
                Map<String, Object> dog = new HashMap<>();
                dog.put("name", dogName);
                dog.put("breed", breed);
                dog.put("description", description);
                dog.put("owner", owner);
                dog.put("image", uri.toString());

                dogname.setText("");
                DogBreed.setText("");
                descriptions.setText("");
                imageView.setImageResource(0);

                // Add the document to the Firestore collection
                db.collection("LostPets").add(dog)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(getActivity(), "Dog added", Toast.LENGTH_SHORT).show();
                            // Hide the ProgressDialog
                            progressDialog.dismiss();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Error adding dog", Toast.LENGTH_SHORT).show();
                            // Hide the ProgressDialog
                            progressDialog.dismiss();
                        });
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(getActivity(), "Upload failed", Toast.LENGTH_SHORT).show();
            // Hide the ProgressDialog
            progressDialog.dismiss();
        });
    }

    private void openFileChooser() {

        // Create an intent to open the file chooser and set the type to image files
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    // This method is called when the file chooser is closed
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the data is not null and the data is not null
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            // Get the image uri
            imageUri = data.getData();

            // Set the image uri to the image view
            imageView.setImageURI(imageUri);
            Toast.makeText(getActivity(), "Image Imported", Toast.LENGTH_SHORT).show();
        }
    }
    private void replaceFragement(Fragment fragment) {

        // Call the fragment manager and begin the transaction to replace the fragment
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}