package com.example.pet_adoption_app;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Change_Password#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Change_Password extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String username, name, codesended;

    TextView Timer;

    long timers = 0;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText Usernametext, OldPasswordtext, NewPasswordtext, Emailtext, Codetext;

    CheckBox checkBox;

    ProgressDialog progressDialog;
    public Change_Password() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Change_Password.
     */
    // TODO: Rename and change types and number of parameters
    public static Change_Password newInstance(String param1, String param2) {
        Change_Password fragment = new Change_Password();
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
        View rootview =  inflater.inflate(R.layout.fragment_change__password, container, false);

        // Inflate the layout for this fragment
        ImageButton back = rootview.findViewById(R.id.buttonnback);
        back.setOnClickListener(v->{
            // This will go back to user preferences

            // This will go back to user preferences
            UserPreferences fragment = new UserPreferences();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("name", name);
            fragment.setArguments(bundle);
            replaceFragement(fragment);
        });

        Usernametext = rootview.findViewById(R.id.editTextText2);
        Usernametext.setText(username);
        Usernametext.setEnabled(false);
        OldPasswordtext = rootview.findViewById(R.id.password);
        NewPasswordtext = rootview.findViewById(R.id.confirmpassword);
        Emailtext = rootview.findViewById(R.id.emailtext);
        Codetext = rootview.findViewById(R.id.codetext);

        Timer = rootview.findViewById(R.id.textView10);
        Timer.setText("");



        // This is to send the code
        Button sendCode = rootview.findViewById(R.id.sendcode);
        sendCode.setOnClickListener(v->{

            // Get the email
            String email = Emailtext.getText().toString();

            // Check if the timer is 0
            if(timers == 0){
                db.collection("users").whereEqualTo( "Email", email).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        if(!task.getResult().isEmpty()){
                            // Generate a code
                            String code = generateCode();
                            SendMail(Emailtext.getText().toString(), code); // Send the code to the email
                            codesended = code; // Save the code that was sent
                            startCountdownTimer(); // Start the countdown timer
                        }
                        else{
                            Toast.makeText(getContext(), "The Email does not exist", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else{
                        Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();

                    }
                });

            }
            else{
                Toast.makeText(getContext(), "You can only send the code once every 60 seconds", Toast.LENGTH_SHORT).show();
            }
        });

        // This is change Password function
        Button Change_password = rootview.findViewById(R.id.button);
        Change_password.setOnClickListener(v->{
           String code = Codetext.getText().toString();
           String oldpassword = OldPasswordtext.getText().toString();
           String newpassword = NewPasswordtext.getText().toString();
           String username = Usernametext.getText().toString();
           if(code.equals(codesended)) {
               if (oldpassword.isEmpty() || newpassword.isEmpty() || username.isEmpty() || Emailtext.getText().toString().isEmpty() || Codetext.getText().toString().isEmpty()) {
                   Toast.makeText(getContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
               } else {
                   if(newpassword.length()<10){
                       Toast.makeText(getContext(), "Password must be at least 10 characters", Toast.LENGTH_SHORT).show();

                   } else{
                       Boolean hasSpecialCharacter = hasSpecialCharacters(newpassword);
                          Boolean hasUpperCase = HasUpperCase(newpassword);
                          if( hasSpecialCharacter && hasUpperCase){
                              ChangePasswordCredentials(username, oldpassword,newpassword);
                              progressDialog = new ProgressDialog(getContext());
                              progressDialog.setTitle("Changing Password");
                              progressDialog.setMessage("Please wait...");
                              progressDialog.show();
                          } else {
                              Toast.makeText(getContext(), "Password must contain a special character and an uppercase letter", Toast.LENGTH_SHORT).show();
                          }
                   }
               }
           }
           else{
               Toast.makeText(getContext(), "Code is incorrect", Toast.LENGTH_SHORT).show();
           }


        });

        // This will show the password
        checkBox=rootview.findViewById(R.id.checkBox);
        checkBox.setOnClickListener(v->{
            if(checkBox.isChecked())
            {
                // Show the password
                OldPasswordtext.setTransformationMethod(null);
                NewPasswordtext.setTransformationMethod(null);
            }
            else
            {
                // Hide the password
                OldPasswordtext.setTransformationMethod(new PasswordTransformationMethod());
                NewPasswordtext.setTransformationMethod(new PasswordTransformationMethod());
            }
        });



        return rootview;

    }

    private void ChangePasswordCredentials(String username, String oldpassword, String newpassword) {
   db.collection("users").whereEqualTo( "Username", username).get().addOnCompleteListener(task -> {
       if(task.isSuccessful()){
         if(!task.getResult().isEmpty()){
             DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
             String hashedPassword = documentSnapshot.getString("Password");
             BCrypt.Result result = BCrypt.verifyer().verify(oldpassword.toCharArray(), hashedPassword);
             if(result.verified){
                 for(QueryDocumentSnapshot doc: task.getResult()){
                     String id = doc.getId();
                     String bcryptPassHashing = BCrypt.withDefaults().hashToString(12, newpassword.toCharArray());
                     db.collection("users").document(id).update("Password", bcryptPassHashing).addOnCompleteListener(task1 -> {
                         if(task1.isSuccessful()){
                             Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();

                             // Clear the edit text
                             OldPasswordtext.setText("");
                             NewPasswordtext.setText("");
                             Codetext.setText("");

                             // Dismiss the progress dialog
                             progressDialog.dismiss();

                         }
                         else{
                             Toast.makeText(getContext(), "Password not changed", Toast.LENGTH_SHORT).show();

                             // Dismiss the progress dialog
                             progressDialog.dismiss();
                         }
                     });
                 }
             }
             else{
                 Toast.makeText(getContext(), "Password is incorrect", Toast.LENGTH_SHORT).show();
                 progressDialog.dismiss();
             }
         }
         else{
             Toast.makeText(getContext(), "User does not exist", Toast.LENGTH_SHORT).show();
             progressDialog.dismiss();
         }
       }
       else{
           Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
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
        JavaMailAPI mailAPI = new JavaMailAPI(getContext(), Email, "The Verification Code for changing password", "The verification code is:" + code, code);
        Toast.makeText(getContext(), " Email Sended", Toast.LENGTH_SHORT).show();
        mailAPI.execute();
    }
    private String generateCode() {
        // Implement code generation logic here
        Random random = new Random();
        int code = random.nextInt(900000) + 100000; // This will generate a random 6-digit number
        return String.valueOf(code);
    }

    // This will check if it has a special character
    public boolean hasSpecialCharacters(String password1) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(password1);
        return matcher.find();
    }


    // This will check if it has an uppercase letter
    public boolean HasUpperCase(String password1){
        Pattern uppercasePattern = Pattern.compile("[A-Z]");
        Matcher uppercaseMatcher = uppercasePattern.matcher(password1);
        boolean hasUppercase = uppercaseMatcher.find();

        return  hasUppercase;
    }
}