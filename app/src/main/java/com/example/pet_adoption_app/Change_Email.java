package com.example.pet_adoption_app;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Change_Email#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Change_Email extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String username, name, code_send;

    EditText Username, OldEmail, NewEmail, codetype;

    TextView Timer;

    long timers = 0;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ProgressDialog progressDialog;
    public Change_Email() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Change_Email.
     */
    // TODO: Rename and change types and number of parameters
    public static Change_Email newInstance(String param1, String param2) {
        Change_Email fragment = new Change_Email();
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

        // Get the username and name from the arguments
        if (getArguments() != null) {
            username = getArguments().getString("username");
            name = getArguments().getString("name");
        }

        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_change__email, container, false);

        // our image button code here
        ImageButton btnback = rootview.findViewById(R.id.buttonnback);
        btnback.setOnClickListener(v->{
            // This will go back to user preferences
            UserPreferences userPreferences = new UserPreferences();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("name", name);
            userPreferences.setArguments(bundle);
            replaceFragement(userPreferences);
        });

        // This is to send the code
        Username = rootview.findViewById(R.id.editTextText2);
        Username.setText(username); // This will set the username to the edit text
        OldEmail = rootview.findViewById(R.id.oldemail);
        NewEmail = rootview.findViewById(R.id.newemail);
        codetype = rootview.findViewById(R.id.codetext);

        Timer = rootview.findViewById(R.id.textView10);
        Timer.setText("");


        // This button will send code to the user email
        Button sendcode = rootview.findViewById(R.id.sendcode);
        sendcode.setOnClickListener(v->{
          if(timers == 0){

              // This will go to change email code fragments
              String email = OldEmail.getText().toString();
             if (email.endsWith( "@gmail.com") || email.endsWith("@yahoo.com") || email.endsWith("@hotmail.com") || email.endsWith("@outlook.com") || email.endsWith("@aol.com") || email.endsWith("@icloud.com") || email.endsWith("@protonmail.com") || email.endsWith("@zoho.com") || email.endsWith("@yandex.com") || email.endsWith("@gmx.com") || email.endsWith("@mail.com") || email.endsWith("@tutanota.com") || email.endsWith("@tutanota.com") || email.endsWith("@yopmail.com") || email.endsWith("@mailinator.com") || email.endsWith("@guerrillamail.com") || email.endsWith("@10minutemail.com") || email.endsWith("@temp-mail.org") || email.endsWith("@mohmal.com")){
                  code_send = generateCode();
                  SendMail(email, code_send);

                  // Start the countdown timer
                  startCountdownTimer();
              }
             else{
                 AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                 alertDialog.setTitle("Alert");
                 alertDialog.setMessage("Invalid email address");
                 alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                         (dialog, which) -> dialog.dismiss());
                 alertDialog.show();

             }
          }
          else{
              AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
              alertDialog.setTitle("Alert");
              alertDialog.setMessage("You can only send the code once the timer is done.");
              alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                      (dialog, which) -> dialog.dismiss());
              alertDialog.show();
          }

        });

        // This button will change the email
        Button changeEmail = rootview.findViewById(R.id.changeEmail);
        changeEmail.setOnClickListener(v->
        {
            // This will go to change email fragment
            String user = Username.getText().toString();
            String oldemail = OldEmail.getText().toString();
            String newemail = NewEmail.getText().toString();
            String code = codetype.getText().toString();

           if(newemail.endsWith( "@gmail.com") || newemail.endsWith("@yahoo.com") || newemail.endsWith("@hotmail.com") || newemail.endsWith("@outlook.com") || newemail.endsWith("@aol.com") || newemail.endsWith("@icloud.com") || newemail.endsWith("@protonmail.com") || newemail.endsWith("@zoho.com") || newemail.endsWith("@yandex.com") || newemail.endsWith("@gmx.com") || newemail.endsWith("@mail.com") || newemail.endsWith("@tutanota.com") || newemail.endsWith("@tutanota.com") || newemail.endsWith("@yopmail.com") || newemail.endsWith("@mailinator.com") || newemail.endsWith("@guerrillamail.com") || newemail.endsWith("@10minutemail.com") || newemail.endsWith("@temp-mail.org") || newemail.endsWith("@mohmal.com") ){
               if(code.equals(code_send)) {
                   // This will change the email
                   // Call the method to
                   ChangeEmailCredentials( user, oldemail, newemail);
                   progressDialog = new ProgressDialog(getContext());
                   progressDialog.setTitle("Changing Email");
                   progressDialog.show();
               } else{
                   Toast.makeText(getContext(), "Invalid code", Toast.LENGTH_SHORT).show();
                   progressDialog.dismiss();
               }
           }
           else{
               AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
               alertDialog.setTitle("Alert");
               alertDialog.setMessage("Invalid email address");
               alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                       (dialog, which) -> dialog.dismiss());
               alertDialog.show();
           }


        });



        return rootview;
    }

    private void ChangeEmailCredentials(String user, String oldemail, String newemail) {
    db.collection("users").whereEqualTo("Username", user).whereEqualTo("Email", oldemail).get().addOnCompleteListener(task -> {
        if(task.isSuccessful()){
            for(QueryDocumentSnapshot doc: task.getResult()){
                String id = doc.getId();
                db.collection("users").document(id).update("Email", newemail).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){
                        Toast.makeText(getContext(), "Email changed successfully", Toast.LENGTH_SHORT).show();

                        // Clear the edit text
                        OldEmail.setText("");
                        NewEmail.setText("");
                        codetype.setText("");

                        // Dismiss the progress dialog
                        progressDialog.dismiss();

                    }
                    else{
                        Toast.makeText(getContext(), "Email not changed", Toast.LENGTH_SHORT).show();

                        // Dismiss the progress dialog
                        progressDialog.dismiss();
                    }
                });
            }
        } else{
            Toast.makeText(getContext(), "Email not existed changed", Toast.LENGTH_SHORT).show();

            // Dismiss the progress dialog
            progressDialog.dismiss();
        }
    });

    }


    // This will start the countdown timer
    private void startCountdownTimer() {
        new CountDownTimer(60000, 1000) { // 60000 milliseconds = 60 seconds

            public void onTick(long millisUntilFinished) {
                Timer.setText("Time remaining: " + millisUntilFinished / 1000 + " seconds");
                timers = millisUntilFinished / 1000;



            }

            public void onFinish() {
                Timer.setText("");
                timers = 0;

                if(Timer.getText().toString().isEmpty()){
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("You can now resend the code");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            (dialog, which) -> dialog.dismiss());
                    alertDialog.show();
                }
                else{
                    Timer.setText("Resend code");
                }

            }
        }.start();
    }
    // This will call the fragments
    private void replaceFragement(Fragment fragment) {

        // Call the fragment manager and begin the transaction to replace the fragment
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void SendMail(String Email, String code){
        JavaMailAPI mailAPI = new JavaMailAPI(getContext(), Email, "The Verification Code for changing email", "The verification code is:" + code, code);
        Toast.makeText(getContext(), " Email Sended", Toast.LENGTH_SHORT).show();
        mailAPI.execute();
    }
    private String generateCode() {
        // Implement code generation logic here
        Random random = new Random();
        int code = random.nextInt(900000) + 100000; // This will generate a random 6-digit number
        return String.valueOf(code);
    }

}