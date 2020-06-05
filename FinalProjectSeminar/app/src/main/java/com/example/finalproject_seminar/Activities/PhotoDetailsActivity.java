package com.example.finalproject_seminar.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.finalproject_seminar.Adapters.CommentAdapter;
import com.example.finalproject_seminar.Models.Comment;
import com.example.finalproject_seminar.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kc.unsplash.models.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PhotoDetailsActivity extends AppCompatActivity {

    private Photo photo;

    private ImageView photoImage;
    private TextView createdTextView;
    private TextView updatedTextView;
    private TextView counterLikesTextView;
     String uimg;

    ImageView imgCurrentUser;
    EditText editTextComment;
    Button btnAddComment;
    String PostKey;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    RecyclerView RvComment;
    CommentAdapter commentAdapter;
    List<Comment> listComment;
    static String COMMENT_KEY = "Comment" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);

        photo = (Photo) getIntent().getExtras().get("photo");

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference mUsersReference = firebaseDatabase.getReference().child("MUsers");

        mUsersReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uimg = dataSnapshot.child("profileImage").getValue().toString();
                Log.d("IMAGE URL in snapshot:",uimg);
                imgCurrentUser = findViewById(R.id.post_detail_currentuser_img);
                Picasso.get().load(uimg).into(imgCurrentUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        setUpUI();
        getPhotoDetails();

        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnAddComment.setVisibility(View.INVISIBLE);
                DatabaseReference commentReference = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey).push();
                String comment_content = editTextComment.getText().toString();
                String uid = firebaseUser.getUid();
                String uname = firebaseUser.getDisplayName();


                Comment comment = new Comment(comment_content,uid, uimg,uname);

                commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("comment added");
                        editTextComment.setText("");
                        btnAddComment.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("fail to add comment : "+e.getMessage());
                    }
                });



            }
        });

        // get post id
        PostKey = photo.getId();

        iniRvComment();


    }

    private void getPhotoDetails() {
        Picasso.get().load(photo.getUrls().getSmall()).into(photoImage);
        createdTextView.setText("Created at: " + photo.getCreatedAt());
        updatedTextView.setText("Updated at: " + photo.getUpdatedAt());
        counterLikesTextView.setText("Likes: " + photo.getLikes());

    }

    private void setUpUI() {

        photoImage = (ImageView) findViewById(R.id.photoImageDET);
        createdTextView = (TextView) findViewById(R.id.createdTextView);
        updatedTextView = (TextView) findViewById(R.id.updatedTextView);
        counterLikesTextView = (TextView) findViewById(R.id.counterLikesTextView);


        RvComment = findViewById(R.id.rv_comment);
//        imgPost =findViewById(R.id.post_detail_img);
//        imgUserPost = findViewById(R.id.post_detail_user_img);
//        imgCurrentUser = findViewById(R.id.post_detail_currentuser_img);
//        Picasso.get().load(uimg).into(imgCurrentUser);
//        Glide.with(this).load(uimg[0]).into(imgCurrentUser);

//        Log.d("IMAGE URL:",uimg);

//        txtPostTitle = findViewById(R.id.post_detail_title);
//        txtPostDesc = findViewById(R.id.post_detail_desc);
//        txtPostDateName = findViewById(R.id.post_detail_date_name);

        editTextComment = findViewById(R.id.post_detail_comment);
        btnAddComment = findViewById(R.id.post_detail_add_comment_btn);





    }

    private void iniRvComment() {

        RvComment.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentRef = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap:dataSnapshot.getChildren()) {

                    Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment) ;

                }

                commentAdapter = new CommentAdapter(getApplicationContext(),listComment);
                RvComment.setAdapter(commentAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void showMessage(String message) {

        Toast.makeText(this,message,Toast.LENGTH_LONG).show();

    }


    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy",calendar).toString();
        return date;


    }
}
