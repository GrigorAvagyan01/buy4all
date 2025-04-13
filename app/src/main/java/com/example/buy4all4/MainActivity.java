    package com.example.buy4all4;

    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.util.Log;
    import androidx.appcompat.app.AppCompatActivity;

    import com.example.buy4all4.databinding.ActivityMainBinding;
    import com.google.firebase.auth.FirebaseAuth;

    public class MainActivity extends AppCompatActivity {

        private ActivityMainBinding binding;
        private SharedPreferences sharedPreferences;
        private static final String PREFS_NAME = "LoginPrefs";
        private static final String KEY_EMAIL = "email";
        private static final String KEY_PASSWORD = "password";
        private FirebaseAuth mAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            mAuth = FirebaseAuth.getInstance();
            sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

            String savedEmail = sharedPreferences.getString(KEY_EMAIL, null);
            String savedPassword = sharedPreferences.getString(KEY_PASSWORD, null);

            if (savedEmail != null && savedPassword != null) {
                Log.d("MainActivity", "Found saved credentials, attempting auto-login");
                autoLogin(savedEmail, savedPassword);
            } else {
                setupClickListeners();
            }
        }

        private void autoLogin(String email, String password) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Log.d("MainActivity", "Auto-login successful, saving credentials and navigating to HomePage");
                            saveCredentials(email, password);
                            navigateToActivity(HomePage.class);
                        } else {
                            Log.d("MainActivity", "Auto-login failed, navigating to SignInActivity");
                            navigateToActivity(SignInActivity.class);
                        }
                    });
        }

        private void setupClickListeners() {
            binding.signin.setOnClickListener(v -> {
                Log.d("MainActivity", "Sign In button clicked");
                navigateToActivity(SignInActivity.class);
            });

            binding.signup.setOnClickListener(v -> {
                Log.d("MainActivity", "Sign Up button clicked");
                navigateToActivity(SignUpActivity.class);
            });

            binding.guestbut.setOnClickListener(v -> {
                Log.d("MainActivity", "Guest button clicked");
                navigateToActivity(HomePageGuest.class);
            });

            binding.testUserButton.setOnClickListener(v -> {
                Log.d("MainActivity", "Test User button clicked");
                autoLogin("testuser3@gmail.com", "Testuser3");
            });
        }

        private void navigateToActivity(Class<?> targetActivity) {
            Intent intent = new Intent(MainActivity.this, targetActivity);
            startActivity(intent);
            finish();
        }
        private void saveCredentials(String email, String password) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_EMAIL, email);
            editor.putString(KEY_PASSWORD, password);
            editor.apply();
        }

        private void onLoginComplete(String email, String password) {
            saveCredentials(email, password);
            navigateToActivity(HomePage.class);
        }
    }
