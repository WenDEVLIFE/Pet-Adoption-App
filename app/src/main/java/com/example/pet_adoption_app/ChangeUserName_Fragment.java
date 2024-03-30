package com.example.pet_adoption_app;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangeUserName_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeUserName_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String username,  name;

    EditText CurrentUsername, NewUsername, Password, ConfirmPassword;

    CheckBox checkBox;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ProgressDialog progressDialog;

    public ChangeUserName_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * Use this factory method to create a new instance of
     * Use this factory method to create a new instance of
     * Use this factory method to create a new instance of
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangeUserName_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangeUserName_Fragment newInstance(String param1, String param2) {
        ChangeUserName_Fragment fragment = new ChangeUserName_Fragment();
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

        // Get the username and name from the bundle
        if (getArguments() != null) {
            username = getArguments().getString("username");
            name = getArguments().getString("name");
        }

        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_change_user_name_, container, false);
        ImageButton back = rootview.findViewById(R.id.buttonnback);
        back.setOnClickListener(v->{
            // This will go back to user preferences
           UserPreferences userPreferences = new UserPreferences();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("name", name);
            userPreferences.setArguments(bundle);
            replaceFragement(userPreferences);
        });

        // Find the id of edit text and set the current username to the edit text
         CurrentUsername=rootview.findViewById(R.id.oldusername);
         CurrentUsername.setText(username);
         CurrentUsername.setEnabled(false);
         NewUsername=rootview.findViewById(R.id.editTextText2);
         Password=rootview.findViewById(R.id.password);
         ConfirmPassword=rootview.findViewById(R.id.confirmpassword);

        Button ChangeUsername=rootview.findViewById(R.id.button);
        ChangeUsername.setOnClickListener(v->{
            // This will go back to user preferences
            String currentusername=CurrentUsername.getText().toString();
            String newusername=NewUsername.getText().toString();
            String password=Password.getText().toString();
            String confirmpassword=ConfirmPassword.getText().toString();
            if (newusername.isEmpty() || password.isEmpty() || confirmpassword.isEmpty())
            {
                CurrentUsername.setError("Please enter the current username");
                NewUsername.setError("Please enter the new username");
                Password.setError("Please enter the password");
                ConfirmPassword.setError("Please enter the confirm password");
            }
           else {
               progressDialog = new ProgressDialog(getContext());
               progressDialog.setTitle("Changing Username");
                progressDialog.show();


               ChangeCredentials( currentusername, newusername, password, confirmpassword);

            }





        });

        // This will show the password
        checkBox=rootview.findViewById(R.id.checkBox);
        checkBox.setOnClickListener(v->{
            if(checkBox.isChecked())
            {
                // Show the password
                Password.setTransformationMethod(null);
                ConfirmPassword.setTransformationMethod(null);
            }
            else
            {
                // Hide the password
                Password.setTransformationMethod(new PasswordTransformationMethod());
                ConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
        });




    return rootview;
    }

    private void ChangeCredentials(String currentusername, String newusername, String password, String confirmpassword) {
        // Query the Firestore database to find the document with the current username
        db.collection("users").whereEqualTo("Username", currentusername)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Update the username field of the document
                            String hashedPassword = document.getString("Password");
                            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);

                            // Check if the password is correct
                            if (result.verified) {
                                // Check if the new username is valid
                                if (newusername.length() < 6) {
                                    NewUsername.setError("Username must be at least 6 characters long");

                                    // Dismiss the progress dialog
                                    progressDialog.dismiss();
                                } else {
                                    // Update the username field of the document
                                    document.getReference().update("Username", newusername)
                                            .addOnSuccessListener(aVoid -> {
                                                // Username updated successfully
                                                Toast.makeText(getContext(), "Username updated successfully", Toast.LENGTH_SHORT).show();

                                                // Dismiss the progress dialog
                                                progressDialog.dismiss();

                                                username = newusername;
                                            })
                                            .addOnFailureListener(e -> {
                                                // Handle any errors here
                                                Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();

                                                // Dismiss the progress dialog
                                                progressDialog.dismiss();
                                            });
                                }
                            } else {
                                Password.setError("Incorrect password");
                                // Dismiss the progress dialog
                                progressDialog.dismiss();
                            }
                        }
                    } else {
                        // Handle any errors here
                        Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                        // Dismiss the progress dialog
                        progressDialog.dismiss();
                    }
                });
    }

    private void replaceFragement(Fragment fragment) {

        // Call the fragment manager and begin the transaction to replace the fragment
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }



}