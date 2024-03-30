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

import java.time.LocalDate;
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

    String username, name, owner;

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
            owner = getArguments().getString("petowner");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null) {
            username = getArguments().getString("username");
            name = getArguments().getString("name");
            owner = getArguments().getString("petowner");
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


        // our image button code here
        ImageButton btnback = rootview.findViewById(R.id.buttonnback);
        btnback.setOnClickListener(v->{
            // This will go back to home fragments
            Fragment_Lost_Pets homeFragment = new Fragment_Lost_Pets();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("name", name);
            homeFragment.setArguments(bundle);
            replaceFragement(homeFragment);
        });


        // Get the add button and set the on click listener
        addReport = rootview.findViewById(R.id.AddButton);
        addReport.setOnClickListener(v->{

            // Get the values from the edit text fields
            String phoneNumberString = phone.getText().toString();
            long phoneNumber = 0;
            if (!phoneNumberString.isEmpty() && phoneNumberString.matches("[0-9]+")) {
                phoneNumber = Long.parseLong(phoneNumberString);
            } else {
                Toast.makeText(getContext(), "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                return;
            }
            String email_address = email.getText().toString();
            String dog_owner = dogOwner.getText().toString();
            String description = descriptions.getText().toString();


            // Check if the fields are empty
            if(email_address.equals("") || dog_owner.equals("") || description.equals("") || imageUri == null || phoneNumberString.length() != 12) {
                Toast.makeText(getContext(), "Please fill all the fields or check the phone number is 12 digit", Toast.LENGTH_SHORT).show();
            }
            else {

                // Check if the email address is valid
                if(email_address.endsWith( "@gmail.com") || email_address.endsWith( "@yahoo.com") || email_address.endsWith( "@hotmail.com") || email_address.endsWith( "@outlook.com") || email_address.endsWith( "@aol.com") || email_address.endsWith( "@protonmail.com") || email_address.endsWith( "@zoho.com") || email_address.endsWith( "@icloud.com") || email_address.endsWith( "@yandex.com") || email_address.endsWith( "@mail.com") || email_address.endsWith( "@gmx.com") || email_address.endsWith( "@tutanota.com") || email_address.endsWith( "@mail.ru") || email_address.endsWith( "@inbox.lv") || email_address.endsWith( "@yopmail.com") || email_address.endsWith( "@mailinator.com") || email_address.endsWith( "@guerrillamail.com") || email_address.endsWith( "@10minutemail.com") || email_address.endsWith( "@temp-mail.org") || email_address.endsWith( "@maildrop.cc") || email_address.endsWith( "@mailnesia.com") || email_address.endsWith( "@mailinator2.com") || email_address.endsWith( "@mailinator.net") || email_address.endsWith( "@mailinator.org") || email_address.endsWith( "@sogetthis.com") || email_address.endsWith( "@mailinator.com") || email_address.endsWith( "@mailinator.co.uk") || email_address.endsWith( "@mailinator.co") || email_address.endsWith( "@mailinator.biz") || email_address.endsWith( "@mailinator.info") || email_address.endsWith( "@mailinator.jp") || email_address.endsWith( "@mailinator.us") || email_address.endsWith( "@mailinator.ca") || email_address.endsWith( "@mailinator.net") || email_address.endsWith( "@mailinator.org") || email_address.endsWith( "@mailinator.info") || email_address.endsWith( "@mailinator.jp") || email_address.endsWith( "@mailinator.us") || email_address.endsWith( "@mailinator.ca") || email_address.endsWith( "@mailinator.net") || email_address.endsWith( "@mailinator.org") || email_address.endsWith( "@mailinator.info") || email_address.endsWith( "@mailinator.jp") || email_address.endsWith( "@mailinator.us")) {

                    // Check if the phone number is 10 digits
                    if(phoneNumberString.length() == 11) {

                        // Call the insert report method
                        InsertReport(phoneNumber, email_address, dog_owner, description, imageUri);
                    }

                } else{

                    Toast.makeText(getContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();

                }

            }

        });





        return rootview;
    }

    private void InsertReport(long phoneNumber, String emailAddress, String dogOwner, String description, Uri imageUri) {

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

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        LocalDate date = LocalDate.now();
                        HashMap <String, Object> Notifications = new HashMap<>();
                        Notifications.put("Notifications details", "The " + name + " has been reported as lost. Please contact the owner if found.");
                        Notifications.put("name", owner );
                        Notifications.put("date", date.toString());
                        db.collection("Notifications").document().set(Notifications);
                    }

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
    private void replaceFragement(Fragment fragment) {

        // Call the fragment manager and begin the transaction to replace the fragment
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

}