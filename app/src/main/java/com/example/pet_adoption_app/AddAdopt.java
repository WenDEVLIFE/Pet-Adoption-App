package com.example.pet_adoption_app;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.HashMap;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAdopt#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAdopt extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri imageUri; // Add this line at the top of your class
    ImageView imageView;

    String username, name;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText dogname;
    EditText DogBreed;
    EditText DogOwner;
    EditText descriptions;

    public AddAdopt() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddAdopt.
     */
    // TODO: Rename and change types and number of parameters
    public static AddAdopt newInstance(String param1, String param2) {
        AddAdopt fragment = new AddAdopt();
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


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_adopt, container, false);

        dogname = rootView.findViewById(R.id.editTextText);
        DogBreed = rootView.findViewById(R.id.dogage);
        DogOwner = rootView.findViewById(R.id.dogBreed);
        DogOwner.setEnabled(false);
        descriptions = rootView.findViewById(R.id.dog_owner);

        if (getArguments() != null) {

            // This is a test to get the value from the navbar and send it to the fragments.
            username = getArguments().getString("username");
            name = getArguments().getString("name");
            DogOwner.setText(name);
        }
        // our back button image
        ImageButton back = rootView.findViewById(R.id.buttonnback);
        back.setOnClickListener(v ->
        {
            // This will go to Adoption Fragments
            AdoptionFragments Fragment = new AdoptionFragments();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("name", name);
            Fragment.setArguments(bundle);
            replaceFragement(Fragment);
        });


        Button Adopt = rootView.findViewById(R.id.AddButton);
        Adopt.setOnClickListener(v -> {
            String DogName = dogname.getText().toString();
            String DogAge = DogBreed.getText().toString();
            String Dog_owner = DogOwner.getText().toString();
            String DogDescription = descriptions.getText().toString();

            if (DogName.isEmpty() || DogAge.isEmpty() || DogDescription.isEmpty() || Dog_owner.isEmpty() || imageUri == null) {
                Toast.makeText(getActivity(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            } else {
                InsertToFireStore(DogName, DogAge, DogDescription, Dog_owner, imageUri);
            }
        });

        // This where we set the image
        imageView = rootView.findViewById(R.id.imageView4);

        // import image button
        Button ImportButton = rootView.findViewById(R.id.importimage);
        ImportButton.setOnClickListener(v->{

            // call the open file chooser method
            openFileChooser();
        });

    return  rootView;
    }

    @SuppressLint("startActivityForResult")
    private void openFileChooser() {

        // Create an intent to open the file chooser
        Intent intent = new Intent();

        // Set the type of file to be selected
        intent.setType("image/*");

        // Set the action to get content
        intent.setAction(Intent.ACTION_GET_CONTENT);

        // Start the activity for result

        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // This method is called when the file chooser is closed

    @SuppressLint("ActivityResult")
    @Override

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

    public void InsertToFireStore(String dogName, String dogAge, String dogBreed, String dogOwner, Uri dogImageUri){
        // Create a storage reference
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("uploads");

        // Create a reference to the file to be uploaded
        StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(dogImageUri));

        // Upload the file to Firebase Storage
        fileReference.putFile(dogImageUri).addOnSuccessListener(taskSnapshot -> {
            // Get the download URL of the uploaded file
            fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                // Create a new document for the dog
                Map<String, Object> dog = new HashMap<>();
                dog.put("name", dogName);
                dog.put("breed", dogAge);
                dog.put("description", dogBreed);
                dog.put("owner", dogOwner);
                dog.put("image", uri.toString());

                dogname.setText("");
                DogBreed.setText("");
                descriptions.setText("");
                imageView.setImageResource(0);




                // Add the document to the Firestore collection
                db.collection("Pets").add(dog)
                        .addOnSuccessListener(documentReference -> Toast.makeText(getActivity(), "Dog added", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(getActivity(), "Error adding dog", Toast.LENGTH_SHORT).show());
            });
        }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Upload failed", Toast.LENGTH_SHORT).show());
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}