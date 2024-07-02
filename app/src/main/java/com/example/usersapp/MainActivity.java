package com.example.usersapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ImageView userImage;
    private TextView firstName, lastName, age, email, city, country;
    private Button nextUserButton, addUserButton, viewCollectionButton;
    private ApiService apiService;
    private AppDatabase db;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userImage = findViewById(R.id.userImage);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        age = findViewById(R.id.age);
        email = findViewById(R.id.email);
        city = findViewById(R.id.city);
        country = findViewById(R.id.country);
        nextUserButton = findViewById(R.id.nextUserButton);
        addUserButton = findViewById(R.id.addUserButton);
        viewCollectionButton = findViewById(R.id.viewCollectionButton);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://randomuser.me/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").build();

        nextUserButton.setOnClickListener(v -> fetchUser());
        addUserButton.setOnClickListener(v -> addUserToDatabase());
        viewCollectionButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, UsersActivity.class)));

        fetchUser();
    }

    private void fetchUser() {
        toggleButtons(false);
        apiService.getRandomUser().enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse.Result result = response.body().results.get(0);
                    currentUser = new User();
                    currentUser.id = result.id.value != null ? result.id.value : ""; // Убедитесь, что ID не null
                    currentUser.firstName = result.name.first;
                    currentUser.lastName = result.name.last;
                    currentUser.age = result.dob.age;
                    currentUser.email = result.email;
                    currentUser.city = result.location.city;
                    currentUser.country = result.location.country;
                    currentUser.pictureUrl = result.picture.large;

                    updateUI();
                } else {
                    showError();
                }
                toggleButtons(true);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                showError();
                toggleButtons(true);
            }
        });
    }

    private void updateUI() {
        Log.d("MainActivity", "Image URL: " + currentUser.pictureUrl);

        Glide.with(this)
                .load(currentUser.pictureUrl)
                .error(R.drawable.error)
                .into(userImage);

        firstName.setText(currentUser.firstName);
        lastName.setText(currentUser.lastName);
        age.setText(String.valueOf(currentUser.age));
        email.setText(currentUser.email);
        city.setText(currentUser.city);
        country.setText(currentUser.country);
    }

    private void showError() {
        userImage.setImageResource(R.drawable.error);
        firstName.setText("error");
        lastName.setText("error");
        age.setText("error");
        email.setText("error");
        city.setText("error");
        country.setText("error");
    }

    private void addUserToDatabase() {
        if (currentUser == null || "error".equals(currentUser.firstName)) {
            Toast.makeText(this, "Cannot add user in error state", Toast.LENGTH_SHORT).show();
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            UserDao userDao = db.userDao();
            userDao.insert(currentUser);
            runOnUiThread(() -> Toast.makeText(MainActivity.this, "User added", Toast.LENGTH_SHORT).show());
        });
    }

    private void toggleButtons(boolean enabled) {
        nextUserButton.setEnabled(enabled);
        addUserButton.setEnabled(enabled);
        viewCollectionButton.setEnabled(enabled);
    }
}
