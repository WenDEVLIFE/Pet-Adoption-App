package com.example.pet_adoption_app;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import adapter.PetAdapter;
import adapter.Pets;
import adapter.ReportAdapater;
import adapter.ReportPet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YourReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YourReportFragment extends Fragment implements ReportAdapater.onCancelListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String username, name;

    private List<ReportPet> reportlist;

    private ReportAdapater adapter;

    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public YourReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YourReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static YourReportFragment newInstance(String param1, String param2) {
        YourReportFragment fragment = new YourReportFragment();
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
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
            username = getArguments().getString("username");
            name = getArguments().getString("name");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get the username and name from the arguments
        if (getArguments() != null) {
            username = getArguments().getString("username");
            name = getArguments().getString("name");
        }

        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_your_report, container, false);

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

        // Our search view
        SearchView searchView = rootview.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return  true;
            }
        });

        recyclerView = rootview.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reportlist = new ArrayList<>();
        adapter = new ReportAdapater(reportlist);
        recyclerView.setAdapter(adapter);

        // Ensure MainActivity implements OnDeleteClickListener
        adapter.setOnCancelListener(this);

        LoadReport();


    return  rootview;
    }

    private void LoadReport() {
        // Load the report from the database
        // Add the report to the adapter
        // Notify the adapter that the data has changed
        db.collection("Reports").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                reportlist.clear();
                for (QueryDocumentSnapshot doc : value) {
                    if (doc.getString("dogOwner") != null && doc.getString("dogOwner").equals(name)) {
                        // Add the report to the adapter
                        Long phone = doc.getLong("phone");
                        String email = doc.getString("email");
                        String owner = doc.getString("dogOwner");
                        String description = doc.getString("description");
                        String imageUrl = doc.getString("image");
                        reportlist.add(new ReportPet(phone, email, owner, description, imageUrl));
                    }

                }
                adapter.notifyDataSetChanged();
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

    @Override
    public void onCancel(int position) {

    }
}