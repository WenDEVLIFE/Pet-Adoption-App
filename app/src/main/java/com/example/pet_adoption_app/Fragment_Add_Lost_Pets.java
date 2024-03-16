package com.example.pet_adoption_app;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment__add__lost__pets, container, false);

        // Get the back button and set the on click listener
        ImageButton btnback = rootview.findViewById(R.id.buttonnback);
        btnback.setOnClickListener(v->{

            // Call the replaceFragment method
            replaceFragement(new Fragment_Lost_Pets());
        });

        // Get the image view and the import button
        imageView = rootview.findViewById(R.id.imageView4);

        // Set the on click listener for the import button
        Button ImportButton = rootview.findViewById(R.id.importimage);
        ImportButton.setOnClickListener(v->{

            // call the openFileChooser method
            openFileChooser();

        });

        // Get the add button and set the on click listener
        Button btnadd = rootview.findViewById(R.id.AddButton);
        btnadd.setOnClickListener(v->{
            Toast.makeText(getActivity(), "Lost Pet Added", Toast.LENGTH_SHORT).show();
        });


    return rootview;
    }

    private void openFileChooser() {

        // Create an intent to open the file chooser and set the type to image files
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the request code is the same as the PICK_IMAGE_REQUEST
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