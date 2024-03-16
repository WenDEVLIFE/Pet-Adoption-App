package com.example.pet_adoption_app;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.Toast;

import kotlin.Suppress;

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
    ImageView imageView;

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_adopt, container, false);

        EditText dogname = rootView.findViewById(R.id.editTextText);
        EditText dogage = rootView.findViewById(R.id.dogage);
        EditText dogbreed = rootView.findViewById(R.id.dogBreed);
        EditText dogOwner = rootView.findViewById(R.id.dog_owner);


        ImageButton back = rootView.findViewById(R.id.buttonnback);
        back.setOnClickListener(v -> replaceFragement(new AdoptionFragments()));

        Button Adopt = rootView.findViewById(R.id.AddButton);
        Adopt.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Adopt");
            alertDialog.setMessage("Adopted");
            alertDialog.show();

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
            Uri imageUri = data.getData();

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
}