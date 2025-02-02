//package com.example.buy4all4;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.databinding.DataBindingUtil;
//import androidx.lifecycle.ViewModelProvider;
//import com.example.buy4all4.databinding.ActivityPasswordForUpdateBinding;
//
//public class PasswordForUpdate extends AppCompatActivity {
//
//    private ActivityPasswordForUpdateBinding binding;
//    private PasswordViewModel passwordViewModel;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//
//        passwordViewModel = new ViewModelProvider(this).get(PasswordViewModel.class);
//
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_password_for_update);
//
//        binding.setViewModel(passwordViewModel);
//        binding.setLifecycleOwner(this);
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
//
//    // This method is triggered when the submit button is clicked
//    public void submitPassword(View view) {
//        String password = passwordViewModel.password;
//
//        // Handle the password (e.g., validate, update profile, etc.)
//        if (password.isEmpty()) {
//            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
//        } else {
//            // You can add real validation logic here (e.g., check if the password is correct)
//            // For now, we'll assume it's correct and navigate to the Update activity.
//
//            // Navigate to Update activity
//            Intent intent = new Intent(PasswordForUpdate.this, Update.class);
//            startActivity(intent);
//            finish();  // Close the PasswordForUpdate activity
//        }
//    }
//}
