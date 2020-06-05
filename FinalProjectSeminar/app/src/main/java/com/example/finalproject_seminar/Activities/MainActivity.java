package com.example.finalproject_seminar.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.finalproject_seminar.Fragments.MainFragment;
import com.example.finalproject_seminar.Fragments.loginFragment;
import com.example.finalproject_seminar.R;
import com.kc.unsplash.Unsplash;
import com.kc.unsplash.models.Photo;
import com.kc.unsplash.models.SearchResults;

import java.util.List;

import static com.example.finalproject_seminar.R.id.constraintLayout;

public class MainActivity extends AppCompatActivity {
    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

        Unsplash unsplash = new Unsplash("qEU9gC8EU7f-4_X9f-9mVbWIS-5IMFwfQA5QOgEM6JQ");

        unsplash.searchPhotos("london", new Unsplash.OnSearchCompleteListener() {
            @Override
            public void onComplete(SearchResults results) {
                Log.d("Photos", "Total Results Found " + results.getTotal());
                List<Photo> photos = results.getResults();
            }

            @Override
            public void onError(String error) {
                Log.d("Unsplash", error);
            }
        });

        if(fragment == null )
        {
            fragment = new MainFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragmentContainer , fragment).commit();

        }

        Button loginBtn = (Button) findViewById(R.id.loginButton);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        fragment.onActivityResult(requestCode, resultCode, data);


    }
}
