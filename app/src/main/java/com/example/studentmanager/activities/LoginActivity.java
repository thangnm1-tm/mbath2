package com.example.studentmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmanager.MainActivity;
import com.example.studentmanager.dal.DBHelper;
import com.example.studentmanager.databinding.ActivityLoginBinding;
import com.example.studentmanager.models.Account;
import com.example.studentmanager.models.Student;
import com.example.studentmanager.models.Teacher;
import com.example.studentmanager.utils.SharedPrefManager;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private DBHelper dbHelper;
    private SharedPrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DBHelper(this);
        prefManager = SharedPrefManager.getInstance(this);

        // Auto-login check
        if (prefManager.isLoggedIn() && prefManager.isRememberMe()) {
            navigateToMain();
            return;
        }

        // Restore remembered username if available
        if (prefManager.isRememberMe()) {
            binding.etUsername.setText(prefManager.getUsername());
            binding.cbRememberMe.setChecked(true);
        }

        setupQuickFillButtons();
        setupLoginButton();
    }

    private void setupQuickFillButtons() {
        binding.btnQuickTeacher.setOnClickListener(v -> {
            binding.etUsername.setText("teacher");
            binding.etPassword.setText("123456");
            binding.cbRememberMe.setChecked(true);
        });

        binding.btnQuickStudent.setOnClickListener(v -> {
            binding.etUsername.setText("student");
            binding.etPassword.setText("123456");
            binding.cbRememberMe.setChecked(true);
        });
    }

    private void setupLoginButton() {
        binding.btnLogin.setOnClickListener(v -> {
            String username = binding.etUsername.getText() != null ? binding.etUsername.getText().toString().trim() : "";
            String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";
            boolean rememberMe = binding.cbRememberMe.isChecked();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!", Toast.LENGTH_SHORT).show();
                return;
            }

            Account account = dbHelper.checkLogin(username, password);
            if (account != null) {
                String role = account.getRole();
                String userCode = "";
                String fullname = "";

                if ("TEACHER".equalsIgnoreCase(role)) {
                    Teacher teacher = dbHelper.getTeacherByAccountId(account.getId());
                    if (teacher != null) {
                        fullname = teacher.getFullname();
                        userCode = "GV01";
                    }
                } else {
                    Student student = dbHelper.getStudentByAccountId(account.getId());
                    if (student != null) {
                        fullname = student.getFullname();
                        userCode = student.getCode();
                    }
                }

                // Save session in SharedPreferences
                prefManager.saveLoginSession(account.getId(), account.getUsername(), role, userCode, fullname, rememberMe);

                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                navigateToMain();
            } else {
                Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
