package com.example.studentmanager.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanager.R;
import com.example.studentmanager.models.Score;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    public interface OnScoreActionListener {
        void onEdit(Score score);
        void onDelete(Score score);
    }

    private final Context context;
    private List<Score> scoreList = new ArrayList<>();
    private final boolean isTeacher;
    private final OnScoreActionListener listener;

    public ScoreAdapter(Context context, boolean isTeacher, OnScoreActionListener listener) {
        this.context = context;
        this.isTeacher = isTeacher;
        this.listener = listener;
    }

    public void updateData(List<Score> newList) {
        this.scoreList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        Score score = scoreList.get(position);

        holder.tvSubject.setText(score.getSubject());
        holder.tvStudentCode.setText("Mã SV: " + score.getStudentCode());
        holder.tvTeacherCode.setText("Giảng viên: " + score.getTeacherCode());

        double s = score.getScore();
        holder.tvScoreBadge.setText(String.format(Locale.US, "%.1f", s));

        // Color coding for score badge
        if (s >= 8.0) {
            holder.tvScoreBadge.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.grade_good_bg)));
            holder.tvScoreBadge.setTextColor(ContextCompat.getColor(context, R.color.grade_good));
        } else if (s >= 5.0) {
            holder.tvScoreBadge.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.grade_average_bg)));
            holder.tvScoreBadge.setTextColor(ContextCompat.getColor(context, R.color.grade_average));
        } else {
            holder.tvScoreBadge.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.grade_poor_bg)));
            holder.tvScoreBadge.setTextColor(ContextCompat.getColor(context, R.color.grade_poor));
        }

        // Only show edit / delete buttons for Teacher
        if (isTeacher) {
            holder.layoutActions.setVisibility(View.VISIBLE);
            holder.btnEditScore.setOnClickListener(v -> {
                if (listener != null) listener.onEdit(score);
            });
            holder.btnDeleteScore.setOnClickListener(v -> {
                if (listener != null) listener.onDelete(score);
            });
        } else {
            holder.layoutActions.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return scoreList != null ? scoreList.size() : 0;
    }

    static class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubject, tvStudentCode, tvTeacherCode, tvScoreBadge;
        LinearLayout layoutActions;
        ImageButton btnEditScore, btnDeleteScore;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvStudentCode = itemView.findViewById(R.id.tvStudentCode);
            tvTeacherCode = itemView.findViewById(R.id.tvTeacherCode);
            tvScoreBadge = itemView.findViewById(R.id.tvScoreBadge);
            layoutActions = itemView.findViewById(R.id.layoutActions);
            btnEditScore = itemView.findViewById(R.id.btnEditScore);
            btnDeleteScore = itemView.findViewById(R.id.btnDeleteScore);
        }
    }
}
