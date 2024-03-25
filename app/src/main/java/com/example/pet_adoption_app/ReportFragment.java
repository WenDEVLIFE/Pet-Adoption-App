package com.example.pet_adoption_app;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String username, name;

    EditText email, phone, dogOwner, descriptions;

    Button addReport;

    ImageView imageView;
    private Uri imageUri; // Add this line at the top of your class


    ProgressDialog progressDialog;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final int PICK_IMAGE_REQUEST = 1;

    public ReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
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
        View rootview = inflater.inflate(R.layout.fragment_report, container, false);

        // Initialize the edit text fields and the image view
        email = rootview.findViewById(R.id.email);
        phone = rootview.findViewById(R.id.phonenum);
        dogOwner = rootview.findViewById(R.id.petowner);
        dogOwner.setEnabled(false);
        dogOwner.setText(name);
        descriptions = rootview.findViewById(R.id.petdescription);
        imageView = rootview.findViewById(R.id.imageView4);

        // Set the on click listener for the import button
        Button ImportButton = rootview.findViewById(R.id.importimage);
        ImportButton.setOnClickListener(v->{

            // call the openFileChooser method
            openFileChooser();

        });


        // Get the add button and set the on click listener
        addReport = rootview.findViewById(R.id.AddButton);
        addReport.setOnClickListener(v->{

            // Get the values from the edit text fields
            int phone_number = Integer.parseInt(phone.getText().toString());
            String email_address = email.getText().toString();
            String dog_owner = dogOwner.getText().toString();
            String description = descriptions.getText().toString();


            // Check if the fields are empty
            if(phone_number == 0 || email_address.equals("") || dog_owner.equals("") || description.equals("") || imageUri == null) {
                Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
            else {

                //  Call the InsertReport method
                InsertReport(phone_number, email_address, dog_owner, description, imageUri);


            }

        });



    return rootview;
    }

    private void InsertReport(int phoneNumber, String emailAddress, String dogOwner, String description, Uri imageUri) {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        // Create a storage reference
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("uploads");

        // Create a reference to the file to be uploaded
        StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

        fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            fileReference.getDownloadUrl().addOnSuccessListener(uri -> {

                // Create a hashmap to store the report
                HashMap<String, Object> report = new HashMap<>();
                report.put("email", emailAddress);
                report.put("phone", phoneNumber);
                report.put("dogOwner", dogOwner);
                report.put("description", description);
                report.put("image", uri.toString());

                // Add the report to the database
                db.collection("Reports").add(report).addOnSuccessListener(documentReference -> {
                    // Dismiss the progress dialog
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Report Added", Toast.LENGTH_SHORT).show();

                    // Clear the edit text fields
                    email.setText("");
                    phone.setText("");
                    descriptions.setText("");
                }).addOnFailureListener(e -> {

                    // Dismiss the progress dialog
                    progressDialog.dismiss();

                    // Show a toast message
                    Toast.makeText(getContext(), "Failed to add report", Toast.LENGTH_SHORT).show();
                });
            });
        }).addOnFailureListener(e -> {

            // Dismiss the progress dialog
            progressDialog.dismiss();

            // Show a toast message
            Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
        });

    }


    // Open the file chooser
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

    // Get the file extension of the image
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


}