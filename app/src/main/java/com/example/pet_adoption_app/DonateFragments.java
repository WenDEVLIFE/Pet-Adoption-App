package com.example.pet_adoption_app;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DonateFragments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DonateFragments extends Fragment {

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

    String username, name, donate_request;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText DonateItem, DonatedName, DonateTo, DonatedDescription;

    ProgressDialog progressDialog;

    public DonateFragments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DonateFragments.
     */
    // TODO: Rename and change types and number of parameters
    public static DonateFragments newInstance(String param1, String param2) {
        DonateFragments fragment = new DonateFragments();
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
            donate_request = getArguments().getString("donateName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get the username and name from the arguments
        if (getArguments() != null) {
            username = getArguments().getString("username");
            name = getArguments().getString("name");
            donate_request = getArguments().getString("donateName");
        }

        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_donate_fragments, container, false);

        // our image button code here
        ImageButton btnback = rootview.findViewById(R.id.buttonnback);
        btnback.setOnClickListener(v->{
            // This will go back to home fragments
            Ask_Donations Ask_Donations = new Ask_Donations();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("name", name);
            Ask_Donations.setArguments(bundle);
            replaceFragement(Ask_Donations);


        });

        // our EditText code here
        DonateItem = rootview.findViewById(R.id.donatename);
        DonatedName = rootview.findViewById(R.id.donatednames);

        // Set the value of the EditText to the name
        DonatedName.setText(name);
        DonateTo = rootview.findViewById(R.id.donatesended);

        // Set the value of the EditText to the donate request
        DonateTo.setText(donate_request);
        DonatedDescription = rootview.findViewById(R.id.description);


        //  our image view code here
        imageView = rootview.findViewById(R.id.imageView4);



        // import image button
        Button ImportButton = rootview.findViewById(R.id.importimage);
        ImportButton.setOnClickListener(v->{

            // call the open file chooser method
            openFileChooser();
        });


        // Add button code here
        Button Adopt = rootview.findViewById(R.id.AddButton);
        Adopt.setOnClickListener(v -> {

            // Get the values from the EditText fields
            String donateItemName = DonateItem.getText().toString();
            String donatedName = DonatedName.getText().toString();
            String donateTo = DonateTo.getText().toString();
            String donatedDescription = DonatedDescription.getText().toString();

            // Check if the fields are empty
            if (donateItemName.isEmpty() || donatedName.isEmpty() || donateTo.isEmpty() || donatedDescription.isEmpty() || imageUri == null) {
                Toast.makeText(getActivity(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            } else {

                // Call the InsertToFireStore method
                InsertToFireStore(donateItemName, donatedName, donateTo, donatedDescription, imageUri);

                // Show the ProgressDialog
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Sending donations...");


                // Show the ProgressDialog
                progressDialog.show();

            }
        });



        return rootview;
    }

    private void InsertToFireStore(String donateItemName, String donatedName, String donateTo, String donatedDescription, Uri imageUri) {
        // Create a storage reference
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("uploads");

        // Create a reference to the file to be uploaded
        StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

        // Upload the file to Firebase Storage
        fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            // Get the download URL of the uploaded file
            fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                // Create a new document for the dog
                HashMap<String, Object> donations = new HashMap<>();
                donations.put("donateItemName", donateItemName);
                donations.put("donatedName", donatedName);
                donations.put("donateTo", donateTo);
                donations.put("donatedDescription", donatedDescription);
                donations.put("image", uri.toString());

                // Add the document to the Firestore collection
                db.collection("Donated").add(donations)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(getActivity(), "Donations sent successfully", Toast.LENGTH_SHORT).show();
                            // Hide the ProgressDialog
                            progressDialog.dismiss();

                            if(isAdded()){
                                Toast.makeText(getContext(), "Sending Donations success", Toast.LENGTH_SHORT).show();
                            }

                            // Notify adapter about the change in data set
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                LocalDate date = LocalDate.now();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                String formattedDate = date.format(formatter);

                                // Create a new document for the notification
                                HashMap <String, Object> Notifications = new HashMap<>();
                                Notifications.put("Notifications details","The user "+ donatedName +" Successfully donated to " + donateTo + " on " + formattedDate + " Thank you for your donation");
                                Notifications.put("name", donateTo);
                                Notifications.put("date", formattedDate);
                                db.collection("Notifications").document().set(Notifications);

                                // Create a new transaction
                                HashMap<String, Object> transaction = new HashMap<>();
                                transaction.put("Transaction", "You sent a donation to " + donateTo + " on " + formattedDate + " Thank you for your donation");
                                transaction.put("name", donatedName);
                                transaction.put("date", formattedDate);

                                db.collection("Transaction").document().set(transaction);

                                // Clear the EditText fields
                                DonatedDescription.setText("");
                                DonateItem.setText("");
                                imageView.setImageResource(0);
                            }
                        })
                        .addOnFailureListener(e -> {
                            if(isAdded()){
                                Toast.makeText(getContext(), "Failed to send donations", Toast.LENGTH_SHORT).show();
                            }

                            Toast.makeText(getActivity(), "Error sending donations", Toast.LENGTH_SHORT).show();
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

    private void replaceFragement(Fragment fragment) {

        // Call the fragment manager and begin the transaction to replace the fragment
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    // Get the file extension of the image
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
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
}