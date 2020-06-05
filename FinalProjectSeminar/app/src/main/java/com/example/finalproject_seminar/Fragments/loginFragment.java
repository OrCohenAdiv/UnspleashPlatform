package com.example.finalproject_seminar.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject_seminar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link loginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class loginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;

    private Button loginButton;
    private EditText emailField;
    private EditText passwordField;

    private ProgressDialog mProgressDialog;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public loginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment loginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static loginFragment newInstance(String param1, String param2) {
        loginFragment fragment = new loginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mProgressDialog = new ProgressDialog(loginFragment.this.getContext());

        loginButton = (Button) view.findViewById(R.id.loginButton);
        emailField = (EditText) view.findViewById(R.id.loginEmailEt);
        passwordField = (EditText) view.findViewById(R.id.loginPasswoedEt);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                mUser = mAuth.getCurrentUser();

                if (mUser != null) {
                    Toast.makeText(loginFragment.this.getContext(), "You Signed In!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(loginFragment.this.getContext(), "You CAN'T Signed In :(", Toast.LENGTH_LONG).show();
                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(emailField.getText().toString()) && !TextUtils.isEmpty((passwordField.getText().toString()))) {

                    String email = emailField.getText().toString();
                    String pwd = passwordField.getText().toString();

                    login(email, pwd);

                } else {

                }
            }
        });

        return view;
    }

    private void login(String email, String pwd) {

        mProgressDialog.setMessage("Logging in...");
        mProgressDialog.show();


        mAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(loginFragment.this.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, Yay!! We're in!
                            Toast.makeText(loginFragment.this.getContext(), "Sign in", Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                            //send users to postList
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragmentContainer,new PhotoGalleryFragment(),"PhotoGalleryFragment");
                            transaction.addToBackStack(null);
                            transaction.commit();

                        } else {
                            // If sign in fails,Not in, display a message to the user.

                        }

                    }
                });


    }
}
