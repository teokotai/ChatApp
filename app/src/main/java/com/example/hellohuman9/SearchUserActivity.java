package com.example.hellohuman9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.hellohuman9.adapter.PlaceAutoSuggestAdapter;
import com.example.hellohuman9.adapter.SearchUserRecycleAdapter;
import com.example.hellohuman9.model.UserModel;
import com.example.hellohuman9.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class SearchUserActivity extends AppCompatActivity {

    EditText ageMinInput;
    EditText ageMaxInput;
    EditText locationInput;
    EditText searchInput;
    ImageButton searchButton;
    ImageButton backButton;
    RecyclerView recyclerView;
    SearchUserRecycleAdapter adapter;
    Spinner dropdownGender;
    ArrayAdapter<String> genderAdapter;
    Spinner dropdownExperience;
    ArrayAdapter<String> experienceAdapter;
    Spinner dropdownOwnedPets;
    ArrayAdapter<String> ownedPetsAdapter;

    Spinner dropdownPosition;
    ArrayAdapter<String> positionAdapter;
    AutoCompleteTextView locationSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        searchInput = findViewById(R.id.search_username_input);
        searchButton = findViewById(R.id.search_user_btn);
        backButton = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.search_user_recycle_view);

        locationSearch = findViewById(R.id.search_location);
        locationSearch.setAdapter(new PlaceAutoSuggestAdapter(SearchUserActivity.this, android.R.layout.simple_list_item_1));

        ageMinInput = findViewById(R.id.search_age_min);
        ageMaxInput = findViewById(R.id.search_age_max);

        ageMinInput.setText(String.valueOf(16));
        ageMaxInput.setText(String.valueOf(30));

        dropdownPosition = findViewById(R.id.search_position);
        String[] positionItems = new String[] {"Caretaker", "Pet Owner"};
        positionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, positionItems);
        dropdownPosition.setAdapter(positionAdapter);

        dropdownGender = findViewById(R.id.search_gender);
        String[] genderItems = new String[] {"Female", "Male"};
        genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genderItems);
        dropdownGender.setAdapter(genderAdapter);

        dropdownExperience = findViewById(R.id.search_experience);
        String[] experienceItems = new String[] {"Beginner", "Intermediate", "Professional"};
        experienceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, experienceItems);
        dropdownExperience.setAdapter(experienceAdapter);

        dropdownOwnedPets = findViewById(R.id.search_owned_pets);
        String[] ownedPetsItems = new String[] {"None", "Dog", "Cat", "Parrot", "Hamster", "Fish", "Others"};
        ownedPetsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ownedPetsItems);
        dropdownOwnedPets.setAdapter(ownedPetsAdapter);

        searchInput.requestFocus();

        backButton.setOnClickListener((v) ->{
            onBackPressed();
        });

        searchButton.setOnClickListener((v) ->{
            String searchTerm = searchInput.getText().toString();
            setupSearchRecycleView(searchTerm);
        });
    }

    void setupSearchRecycleView(String searchTerm){

        Query query;

        if (!searchTerm.isEmpty()){
            query = FirebaseUtil.allUserCollectionReference()
                    .whereGreaterThanOrEqualTo("username",searchTerm)
                    .whereLessThanOrEqualTo("username",searchTerm+'\uf8ff');
            }else {
            query = FirebaseUtil.allUserCollectionReference()
                    .whereGreaterThanOrEqualTo("username", searchTerm)
                    .whereLessThanOrEqualTo("username", searchTerm + '\uf8ff')
                    .whereEqualTo("location", locationSearch.getText().toString())
                    .whereGreaterThanOrEqualTo("age", Integer.parseInt(ageMinInput.getText().toString()))
                    .whereLessThanOrEqualTo("age", Integer.parseInt(ageMaxInput.getText().toString()))
                    .whereEqualTo("position",dropdownPosition.getSelectedItem().toString())
                    .whereEqualTo("experience", dropdownExperience.getSelectedItem().toString())
                    .whereEqualTo("ownedPets", dropdownOwnedPets.getSelectedItem().toString())
                    .whereEqualTo("gender", dropdownGender.getSelectedItem().toString())
                    .whereGreaterThanOrEqualTo("location", locationSearch.getText().toString())
                    .whereLessThanOrEqualTo("location", locationSearch.getText().toString() + '\uf8ff');
        }

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query,UserModel.class).build();

        adapter = new SearchUserRecycleAdapter(options,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    protected void onStart(){
        super.onStart();
        if (adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        if (adapter!=null){
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter!=null){
            adapter.startListening();
        }
    }
}