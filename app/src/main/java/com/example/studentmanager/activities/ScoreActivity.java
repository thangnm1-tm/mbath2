package com.example.studentmanager.activities;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.studentmanager.R;
import com.example.studentmanager.adapters.ScoreAdapter;
import com.example.studentmanager.dal.DBHelper;
import com.example.studentmanager.databinding.ActivityScoreBinding;
import com.example.studentmanager.models.Score;
import com.example.studentmanager.utils.SharedPrefManager;

import java.util.List;
import java.util.Locale;

public class ScoreActivity extends AppCompatActivity implements ScoreAdapter.OnScoreActionListener {

    private ActivityScoreBinding binding;
    private DBHelper dbHelper;
    private SharedPrefManager prefManager;
    private ScoreAdapter adapter;
    private boolean isTeacher = false;
    private String studentCodeLimit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DBHelper(this);
        prefManager = SharedPrefManager.getInstance(this);

        isTeacher = "TEACHER".equalsIgnoreCase(prefManager.getRole());
        if (!isTeacher) {
            studentCodeLimit = prefManager.getUserCode();
        }

        setupToolbar();
        setupRecyclerView();
        setupSearch();
        setupAddButton();

        loadScores("");
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        if (!isTeacher) {
            binding.toolbar.setTitle("Điểm Cá Nhân (" + prefManager.getUserCode() + ")");
        }
    }

    private void setupRecyclerView() {
        adapter = new ScoreAdapter(this, isTeacher, this);
        binding.rvScores.setLayoutManager(new LinearLayoutManager(this));
        binding.rvScores.setAdapter(adapter);
    }

    private void setupSearch() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadScores(s != null ? s.toString().trim() : "");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupAddButton() {
        if (isTeacher) {
            binding.fabAddScore.setVisibility(View.VISIBLE);
            binding.fabAddScore.setOnClickListener(v -> showAddEditScoreDialog(null));
        } else {
            binding.fabAddScore.setVisibility(View.GONE);
        }
    }

    private void loadScores(String keyword) {
        List<Score> list;
        if (keyword.isEmpty()) {
            if (isTeacher) {
                list = dbHelper.getAllScores();
            } else {
                list = dbHelper.getScoresByStudentCode(studentCodeLimit);
            }
        } else {
            list = dbHelper.searchScores(keyword, studentCodeLimit);
        }

        adapter.updateData(list);
        if (list.isEmpty()) {
            binding.tvEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.tvEmpty.setVisibility(View.GONE);
        }
    }

    private void showAddEditScoreDialog(Score scoreToEdit) {
        boolean isEdit = (scoreToEdit != null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_score, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextView tvTitle = dialogView.findViewById(R.id.tvDialogTitle);
        EditText etStudentCode = dialogView.findViewById(R.id.etDialogStudentCode);
        EditText etSubject = dialogView.findViewById(R.id.etDialogSubject);
        EditText etTeacherCode = dialogView.findViewById(R.id.etDialogTeacherCode);
        EditText etScore = dialogView.findViewById(R.id.etDialogScore);
        View btnCancel = dialogView.findViewById(R.id.btnDialogCancel);
        View btnSave = dialogView.findViewById(R.id.btnDialogSave);

        if (isEdit) {
            tvTitle.setText(R.string.edit_score);
            etStudentCode.setText(scoreToEdit.getStudentCode());
            etSubject.setText(scoreToEdit.getSubject());
            etTeacherCode.setText(scoreToEdit.getTeacherCode());
            etScore.setText(String.format(Locale.US, "%.1f", scoreToEdit.getScore()));
        } else {
            tvTitle.setText(R.string.add_score);
            etTeacherCode.setText("GV01");
        }

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            String studentCode = etStudentCode.getText().toString().trim();
            String subject = etSubject.getText().toString().trim();
            String teacherCode = etTeacherCode.getText().toString().trim();
            String scoreStr = etScore.getText().toString().trim();

            if (studentCode.isEmpty() || subject.isEmpty() || teacherCode.isEmpty() || scoreStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ các trường!", Toast.LENGTH_SHORT).show();
                return;
            }

            double scoreVal;
            try {
                scoreVal = Double.parseDouble(scoreStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Điểm phải là số hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (scoreVal < 0 || scoreVal > 10) {
                Toast.makeText(this, R.string.invalid_score, Toast.LENGTH_SHORT).show();
                return;
            }

            if (isEdit) {
                scoreToEdit.setStudentCode(studentCode);
                scoreToEdit.setSubject(subject);
                scoreToEdit.setTeacherCode(teacherCode);
                scoreToEdit.setScore(scoreVal);
                dbHelper.updateScore(scoreToEdit);
                Toast.makeText(this, "Cập nhật điểm thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Score newScore = new Score(studentCode, teacherCode, subject, scoreVal);
                dbHelper.insertScore(newScore);
                Toast.makeText(this, "Thêm điểm thành công!", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
            loadScores(binding.etSearch.getText() != null ? binding.etSearch.getText().toString().trim() : "");
        });

        dialog.show();
    }

    @Override
    public void onEdit(Score score) {
        showAddEditScoreDialog(score);
    }

    @Override
    public void onDelete(Score score) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_confirm_title)
                .setMessage("Bạn có chắc chắn muốn xóa điểm môn \"" + score.getSubject() + "\" của sinh viên " + score.getStudentCode() + "?")
                .setPositiveButton(R.string.btn_delete, (dialog, which) -> {
                    dbHelper.deleteScore(score.getId());
                    Toast.makeText(this, "Đã xóa mục điểm thành công!", Toast.LENGTH_SHORT).show();
                    loadScores(binding.etSearch.getText() != null ? binding.etSearch.getText().toString().trim() : "");
                })
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }
}
