package com.example.finalproject_seminar.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.finalproject_seminar.Activities.MainActivity;
import com.example.finalproject_seminar.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateActFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateActFragment extends Fragment {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private EditText idNumber;
    private EditText age;
    private EditText phoneNumber;
    private Button createAccountBtn;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private StorageReference mFirebaseStorage;
    FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    private ImageButton profilePic;
    private Uri mImageUri;
    private Uri resultUri = null;
    private final static int GALLERY_CODE = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateActFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateActFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateActFragment newInstance(String param1, String param2) {
        CreateActFragment fragment = new CreateActFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_act, container, false);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MUsers");

        mAuth = FirebaseAuth.getInstance();

        mFirebaseStorage = FirebaseStorage.getInstance().getReference().child("MBlog_Profile_Pics");

        mProgressDialog = new ProgressDialog(CreateActFragment.this.getContext());

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        firstName = (EditText) view.findViewById(R.id.firstNameAct);
        lastName = (EditText) view.findViewById(R.id.lastNameAct);
        email = (EditText) view.findViewById(R.id.emailAct);
        password = (EditText) view.findViewById(R.id.passwordAct);
        profilePic = (ImageButton) view.findViewById(R.id.profilePic);
        idNumber = view.findViewById(R.id.idNumberAct);
        age = view.findViewById(R.id.ageAct);
        phoneNumber = view.findViewById(R.id.phoneNumberAct);

        createAccountBtn = (Button) view.findViewById(R.id.createAccoutAct);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                getActivity().startActivityForResult(galleryIntent, GALLERY_CODE);

            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
                startPosting();
            }
        });
        return view;
    }

    private void startPosting() {

        final String firstNameVal = firstName.getText().toString().trim();
        final String lastNameVal = lastName.getText().toString().trim();
        final String idNumberVal = idNumber.getText().toString().trim();
        final String ageVal = age.getText().toString().trim();
        final String phoneNumVal = phoneNumber.getText().toString().trim();


        if (!TextUtils.isEmpty(firstNameVal) && !TextUtils.isEmpty(lastNameVal) && !TextUtils.isEmpty(idNumberVal) && !TextUtils.isEmpty(ageVal) && !TextUtils.isEmpty(phoneNumVal)
                && mImageUri != null) {
            //start the uploading...

            final StorageReference filepath = mFirebaseStorage.child("MProfile_images").
                    child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                    while (!downloadUrl.isComplete()) ;

                    DatabaseReference newUser = mDatabaseReference.push();


                    Map<String, String> dataToSave = new HashMap<>();
                    dataToSave.put("firstName", firstNameVal);
                    dataToSave.put("lastName", lastNameVal);
                    dataToSave.put("profileImage", downloadUrl.getResult().toString());
                    dataToSave.put("id_num", idNumberVal);
                    dataToSave.put("age", ageVal);
                    dataToSave.put("phoneNumber", phoneNumVal);
                    dataToSave.put("userid", mUser.getUid());
                    dataToSave.put("username", mUser.getEmail());

                    newUser.setValue(dataToSave);


                    //                   startActivity(new Intent(CreateAccountActivity.this, ProfileShowActivity.class));


                }
            });
        }
    }

    private void createNewAccount() {

        final String name = firstName.getText().toString().trim();
        final String lname = lastName.getText().toString().trim();
        final String idNum = idNumber.getText().toString().trim();
        final String ageNum = age.getText().toString().trim();
        final String phoneNum = phoneNumber.getText().toString().trim();
        String em = email.getText().toString().trim();
        String pwd = password.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(lname)
                && !TextUtils.isEmpty(em) && !TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(idNum) && !TextUtils.isEmpty(ageNum) && !TextUtils.isEmpty(phoneNum)) {

            mProgressDialog.setMessage("Creating Account...");
            mProgressDialog.show();

            mAuth.createUserWithEmailAndPassword(em, pwd)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if (authResult != null) {

                                StorageReference imagePath = mFirebaseStorage.child("MProfile_Profile_Pics")
                                        .child(resultUri.getLastPathSegment());

                                imagePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        String userid = mAuth.getCurrentUser().getUid();
                                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                        while (!uri.isComplete()) ;
                                        DatabaseReference currenUserDb = mDatabaseReference.child(userid);
                                        currenUserDb.child("firstName").setValue(name);
                                        currenUserDb.child("lastName").setValue(lname);
                                        currenUserDb.child("id_num").setValue(idNum);
                                        currenUserDb.child("age").setValue(ageNum);
                                        currenUserDb.child("phoneNumber").setValue(phoneNum);
                                        currenUserDb.child("profileImage").setValue(uri.getResult().toString());

                                        Toast.makeText(getContext(), "Image Upload Successful", Toast.LENGTH_LONG).show();

                                        mProgressDialog.dismiss();

                                        //send users to postList
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        transaction.replace(R.id.fragmentContainer,new PhotoGalleryFragment(),"PhotoGalleryFragment");
                                        transaction.addToBackStack(null);
                                        transaction.commit();

                                    }
                                });

                            }

                        }
                    });

        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("Qcode: ", requestCode+"");
        Log.d("Scode: ", resultCode+"");

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            Uri mImageUri = data.getData();

//            CropImage.activity(mImageUri)
//                    .setAspectRatio(1, 1)
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .start(getActivity());
//        }
//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                resultUri = result.getUri();
        resultUri=data.getData();
                profilePic.setImageURI(resultUri);

//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }
        }
    }

}
