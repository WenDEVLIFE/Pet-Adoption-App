package com.example.pet_adoption_app;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Add_Donations#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Add_Donations extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String username,name;


    EditText DonationName, DonatedOwner, Descriptions;

    Button AddDonations;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ProgressDialog progressDialog;
    public Add_Donations() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Add_Donations.
     */
    // TODO: Rename and change types and number of parameters
    public static Add_Donations newInstance(String param1, String param2) {
        Add_Donations fragment = new Add_Donations();
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

        // This will get the username and name from the previous fragments
        if (getArguments() != null) {
            username = getArguments().getString("username");
            name = getArguments().getString("name");
        }

        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_add__donations, container, false);

        ImageButton btnback = rootview.findViewById(R.id.buttonnback);
        btnback.setOnClickListener(v->{
            // This will go back to home fragments
            Ask_Donations ask_donations = new Ask_Donations();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("name", name);
            ask_donations.setArguments(bundle);
            replaceFragement(ask_donations);

        });

        // This is button is used to go to Ask Donations fragments
        Button btnsubmit = rootview.findViewById(R.id.AddButton);
        btnsubmit.setOnClickListener(v->{
            // This will go to Ask Donations fragments

        });

        // This is the code to add the donations
        DonationName = rootview.findViewById(R.id.donationsName);
        DonatedOwner = rootview.findViewById(R.id.donateOwner);
        DonatedOwner.setText(name);
        Descriptions = rootview.findViewById(R.id.donationsDescription);

        // Add Donations button code
        AddDonations = rootview.findViewById(R.id.AddButton);
        AddDonations.setOnClickListener(v->{


            // This is the code to add the donation
            String donate_name = DonationName.getText().toString();
            String donate_owner = DonatedOwner.getText().toString();
            String donate_description = Descriptions.getText().toString();

            // Check if all edittext is empty or not.
            if(donate_name.isEmpty() || donate_description.isEmpty() || donate_owner.isEmpty()){
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Error")
                        .setMessage("Please fill all the fields")
                        .setPositiveButton("OK", null)
                        .show();
                alertDialog.show();
            }
            else{

                // Pass the value to the other method
                InsertDatabase( donate_name, donate_owner, donate_description);
            }



        });



        return rootview;

    }

    private void InsertDatabase(String donateName, String donateOwner, String donateDescription) {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Adding Donation");
        progressDialog.show();

        // This will insert the data to the database
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            // This will get the current date
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = date.format(formatter);

            // This will insert the data to the database
            HashMap <String, Object> donations = new HashMap<>();
            donations.put("donateName", donateName);
            donations.put("donateOwner", donateOwner);
            donations.put("donateDescription", donateDescription);
            donations.put("date" , formattedDate);


            //  This will insert the data to the database
            db.collection("Donations").document().set(donations)
                    .addOnSuccessListener(aVoid -> {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                                .setTitle("Success")
                                .setMessage("Donation Added")
                                .setPositiveButton("OK", (dialog, which) -> {

                                    // This will clear the EditText on the Ui
                                    DonationName.setText("");
                                    Descriptions.setText("");
                                    progressDialog.dismiss();
                                })
                                .show();
                        alertDialog.show();
                    })
                    .addOnFailureListener(e -> {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                                .setTitle("Error")
                                .setMessage("Error: " + e.getMessage())
                                .setPositiveButton("OK", null)
                                .show();
                        alertDialog.show();
                        progressDialog.dismiss();
                    });


        }


    }

    // This will call the fragments
    private void replaceFragement(Fragment fragment) {

        // Call the fragment manager and begin the transaction to replace the fragment
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}