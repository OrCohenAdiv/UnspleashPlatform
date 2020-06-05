package com.example.finalproject_seminar.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.example.finalproject_seminar.Adapters.PhotoRecyclerViewAdapter;
import com.example.finalproject_seminar.R;
import com.kc.unsplash.Unsplash;
import com.kc.unsplash.api.Order;
import com.kc.unsplash.models.Photo;
import com.kc.unsplash.models.SearchResults;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoGalleryFragment extends Fragment {

    private final String CLIENT_ID = "qEU9gC8EU7f-4_X9f-9mVbWIS-5IMFwfQA5QOgEM6JQ";
    private Unsplash unsplash;
    private PhotoRecyclerViewAdapter adapter;
    private Button searchButton;
    private  EditText editText;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PhotoGalleryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotoGalleryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoGalleryFragment newInstance(String param1, String param2) {
        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
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
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);


        unsplash = new Unsplash(CLIENT_ID);

        editText = view.findViewById(R.id.editText);
        searchButton = (Button) view.findViewById(R.id.button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(v);
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(PhotoGalleryFragment.this.getContext(), 2));

        adapter = new PhotoRecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);

        unsplash.getPhotos(1, 10, Order.LATEST, new Unsplash.OnPhotosLoadedListener() {
            @Override
            public void onComplete(List<Photo> photos) {
                adapter.setPhotos(photos);
            }

            @Override
            public void onError(String error) {

            }
        });
        return view;
    }

    public void search(View view){

        String query = editText.getText().toString();

        unsplash.searchPhotos(query, new Unsplash.OnSearchCompleteListener() {
            @Override
            public void onComplete(SearchResults results) {
                Log.d("Photos", "Total Results Found " + results.getTotal());
                List<Photo> photos = results.getResults();
                adapter.setPhotos(photos);
            }

            @Override
            public void onError(String error) {
                Log.d("Unsplash", error);
            }
        });
    }

}
