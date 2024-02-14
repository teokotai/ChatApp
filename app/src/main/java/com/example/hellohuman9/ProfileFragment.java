package com.example.hellohuman9;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hellohuman9.adapter.PlaceAutoSuggestAdapter;
import com.example.hellohuman9.model.UserModel;
import com.example.hellohuman9.utils.AndroidUtil;
import com.example.hellohuman9.utils.FirebaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class ProfileFragment extends Fragment {

    ImageView profilePic;
    EditText usernameInput;
    EditText phoneInput;
    Button updateProfileBtn;
    ProgressBar progressBar;
    TextView logoutBtn;
    UserModel currentUserModel;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;
    Spinner dropdownGender;
    ArrayAdapter<String> genderAdapter;
    Spinner dropdownExperience;
    ArrayAdapter<String> experienceAdapter;
    Spinner dropdownOwnedPets;
    ArrayAdapter<String> ownedPetsAdapter;
    Spinner dropdownPosition;
    ArrayAdapter<String> positionAdapter;
    EditText ageInput;
    AutoCompleteTextView locationInput;
    EditText descriptionInput;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        if (data!=null && data.getData()!=null){
                            selectedImageUri = data.getData();
                            AndroidUtil.setProfilePic(getContext(),selectedImageUri,profilePic);
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profilePic = view.findViewById(R.id.profile_image_view);
        usernameInput = view.findViewById(R.id.profile_username);
        phoneInput = view.findViewById(R.id.profile_phone);
        updateProfileBtn = view.findViewById(R.id.profile_update_btn);
        progressBar = view.findViewById(R.id.profile_progress_bar);
        logoutBtn = view.findViewById(R.id.logout_btn);
        ageInput = view.findViewById(R.id.profile_age);
        descriptionInput = view.findViewById(R.id.profile_description);

        locationInput = view.findViewById(R.id.profile_location);
        locationInput.setAdapter(new PlaceAutoSuggestAdapter(ProfileFragment.this, android.R.layout.simple_list_item_1));

        dropdownGender = view.findViewById(R.id.profile_gender);
        String[] genderItems = new String[] {"Female", "Male"};
        genderAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, genderItems);
        dropdownGender.setAdapter(genderAdapter);

        dropdownPosition = view.findViewById(R.id.profile_position);
        String[] positionItems = new String[] {"Caretaker", "Pet Owner"};
        positionAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, positionItems);
        dropdownPosition.setAdapter(positionAdapter);

        dropdownExperience = view.findViewById(R.id.profile_experience);
        String[] experienceItems = new String[] {"Beginner", "Intermediate", "Professional"};
        experienceAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, experienceItems);
        dropdownExperience.setAdapter(experienceAdapter);

        dropdownOwnedPets = view.findViewById(R.id.profile_owned_pets);
        String[] ownedPetsItems = new String[] {"None", "Dog", "Cat", "Parrot", "Hamster", "Fish", "Others"};
        ownedPetsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, ownedPetsItems);
        dropdownOwnedPets.setAdapter(ownedPetsAdapter);

        getUserData();
        updateProfileBtn.setOnClickListener((v -> {
            updateBtnClick();
        }));

        logoutBtn.setOnClickListener((v) ->{
            FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        FirebaseUtil.logout();
                        Intent intent = new Intent(getContext(), SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            });

        });

        profilePic.setOnClickListener((v) ->{
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
        });
        return view;
    }


    void updateBtnClick(){
        String newUsername = usernameInput.getText().toString();
        if (newUsername.isEmpty() || newUsername.length()<3){
            usernameInput.setError("Username length should be at least 3 characters");
            return;
        }
        currentUserModel.setUsername(newUsername);
        currentUserModel.setAge(Integer.parseInt(ageInput.getText().toString()));
        currentUserModel.setLocation(locationInput.getText().toString());
        currentUserModel.setDescription(descriptionInput.getText().toString());
        currentUserModel.setGender(dropdownGender.getSelectedItem().toString());
        currentUserModel.setOwnedPets(dropdownOwnedPets.getSelectedItem().toString());
        currentUserModel.setExperience(dropdownExperience.getSelectedItem().toString());
        currentUserModel.setPosition(dropdownPosition.getSelectedItem().toString());
        setInProgress(true);
        if (selectedImageUri!=null){
            FirebaseUtil.getCurrentProfilePicStorageRef().putFile(selectedImageUri)
                    .addOnCompleteListener(task -> {
                        updateToFirestore();
                    });
        } else {
            updateToFirestore();
        }

    }

    void updateToFirestore(){
        FirebaseUtil.currentUserDetails().set(currentUserModel)
                .addOnCompleteListener(task -> {
                    setInProgress(false);
                    if (task.isSuccessful()){
                        AndroidUtil.showToast(getContext(),"Updated successfully");
                    } else {
                        AndroidUtil.showToast(getContext(),"Updated failed");
                    }
                });
    }

    void getUserData(){
        setInProgress(true);
        FirebaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                Uri uri = task.getResult();
                                AndroidUtil.setProfilePic(getContext(),uri,profilePic);
                            }
                        });

        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            setInProgress(false);
            currentUserModel = task.getResult().toObject(UserModel.class);
            usernameInput.setText(currentUserModel.getUsername());
            phoneInput.setText(currentUserModel.getPhone());
            ageInput.setText(String.valueOf(currentUserModel.getAge()));
            locationInput.setText(currentUserModel.getLocation());
            descriptionInput.setText(currentUserModel.getDescription());
            dropdownGender.setSelection(genderAdapter.getPosition(currentUserModel.getGender()), true);
            dropdownOwnedPets.setSelection(ownedPetsAdapter.getPosition(currentUserModel.getOwnedPets()), true);
            dropdownExperience.setSelection(experienceAdapter.getPosition(currentUserModel.getExperience()), true);
            dropdownPosition.setSelection(positionAdapter.getPosition(currentUserModel.getPosition()),true);
        });
    }

    void setInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            updateProfileBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            updateProfileBtn.setVisibility(View.VISIBLE);
        }
    }

}
