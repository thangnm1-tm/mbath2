package com.example.studentmanager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.studentmanager.activities.LoginActivity;
import com.example.studentmanager.activities.ScoreActivity;
import com.example.studentmanager.dal.DBHelper;
import com.example.studentmanager.databinding.ActivityMainBinding;
import com.example.studentmanager.models.Score;
import com.example.studentmanager.utils.SharedPrefManager;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPrefManager prefManager;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = SharedPrefManager.getInstance(this);
        if (!prefManager.isLoggedIn()) {
            redirectToLogin();
            return;
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DBHelper(this);

        setupUserInfo();
        setupActions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefManager.isLoggedIn()) {
            updateStatistics();
        }
    }

    private void setupUserInfo() {
        String role = prefManager.getRole();
        boolean isTeacher = "TEACHER".equalsIgnoreCase(role);

        binding.tvUserFullname.setText(prefManager.getFullname().isEmpty() ? prefManager.getUsername() : prefManager.getFullname());
        binding.tvUserCode.setText(prefManager.getUserCode().isEmpty() ? "N/A" : prefManager.getUserCode());

        if (isTeacher) {
            binding.tvRoleBadge.setText("GIẢNG VIÊN");
            binding.tvRoleBadge.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary_light)));
            binding.tvRoleBadge.setTextColor(ContextCompat.getColor(this, R.color.role_teacher));
            binding.tvUserEmail.setText("thaynv@ptit.edu.vn");
            binding.btnViewScores.setText("Quản Lý Bảng Điểm");
        } else {
            binding.tvRoleBadge.setText("SINH VIÊN");
            binding.tvRoleBadge.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.secondary_light)));
            binding.tvRoleBadge.setTextColor(ContextCompat.getColor(this, R.color.role_student));
            binding.tvUserEmail.setText(prefManager.getUserCode().toLowerCase() + "@ptit.edu.vn");
            binding.btnViewScores.setText("Xem Bảng Điểm Cá Nhân");
        }
    }

    private void updateStatistics() {
        String role = prefManager.getRole();
        boolean isTeacher = "TEACHER".equalsIgnoreCase(role);

        List<Score> scores = isTeacher ? dbHelper.getAllScores() : dbHelper.getScoresByStudentCode(prefManager.getUserCode());
        int count = scores.size();
        double sum = 0;
        for (Score s : scores) {
            sum += s.getScore();
        }
        double avg = (count > 0) ? (sum / count) : 0.0;

        binding.tvTotalScores.setText(String.valueOf(count));
        binding.tvAverageScore.setText(String.format(Locale.US, "%.1f", avg));
    }

    private void setupActions() {
        binding.btnViewScores.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
            startActivity(intent);
        });

        binding.btnLogout.setOnClickListener(v -> {
            prefManager.logout();
            redirectToLogin();
        });
    }

    private void redirectToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
