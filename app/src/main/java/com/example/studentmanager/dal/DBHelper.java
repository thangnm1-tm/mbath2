package com.example.studentmanager.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.studentmanager.models.Account;
import com.example.studentmanager.models.Score;
import com.example.studentmanager.models.Student;
import com.example.studentmanager.models.Teacher;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "student_manager.db";
    private static final int DATABASE_VERSION = 1;

    // Table Account
    public static final String TABLE_ACCOUNT = "Account";
    public static final String COL_ACCOUNT_ID = "id";
    public static final String COL_ACCOUNT_USERNAME = "username";
    public static final String COL_ACCOUNT_PASSWORD = "password";
    public static final String COL_ACCOUNT_ROLE = "role";

    // Table Student
    public static final String TABLE_STUDENT = "Student";
    public static final String COL_STUDENT_ID = "id";
    public static final String COL_STUDENT_CODE = "code";
    public static final String COL_STUDENT_FULLNAME = "fullname";
    public static final String COL_STUDENT_EMAIL = "email";
    public static final String COL_STUDENT_ACCOUNT_ID = "accountId";

    // Table Teacher
    public static final String TABLE_TEACHER = "Teacher";
    public static final String COL_TEACHER_ID = "id";
    public static final String COL_TEACHER_FULLNAME = "fullname";
    public static final String COL_TEACHER_EMAIL = "email";
    public static final String COL_TEACHER_ACCOUNT_ID = "accountId";

    // Table Score
    public static final String TABLE_SCORE = "Score";
    public static final String COL_SCORE_ID = "id";
    public static final String COL_SCORE_STUDENT_CODE = "studentCode";
    public static final String COL_SCORE_TEACHER_CODE = "teacherCode";
    public static final String COL_SCORE_SUBJECT = "subject";
    public static final String COL_SCORE_SCORE = "score";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAccountTable = "CREATE TABLE " + TABLE_ACCOUNT + " (" +
                COL_ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ACCOUNT_USERNAME + " TEXT UNIQUE, " +
                COL_ACCOUNT_PASSWORD + " TEXT, " +
                COL_ACCOUNT_ROLE + " TEXT)";

        String createStudentTable = "CREATE TABLE " + TABLE_STUDENT + " (" +
                COL_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_STUDENT_CODE + " TEXT UNIQUE, " +
                COL_STUDENT_FULLNAME + " TEXT, " +
                COL_STUDENT_EMAIL + " TEXT, " +
                COL_STUDENT_ACCOUNT_ID + " INTEGER)";

        String createTeacherTable = "CREATE TABLE " + TABLE_TEACHER + " (" +
                COL_TEACHER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TEACHER_FULLNAME + " TEXT, " +
                COL_TEACHER_EMAIL + " TEXT, " +
                COL_TEACHER_ACCOUNT_ID + " INTEGER)";

        String createScoreTable = "CREATE TABLE " + TABLE_SCORE + " (" +
                COL_SCORE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SCORE_STUDENT_CODE + " TEXT, " +
                COL_SCORE_TEACHER_CODE + " TEXT, " +
                COL_SCORE_SUBJECT + " TEXT, " +
                COL_SCORE_SCORE + " REAL)";

        db.execSQL(createAccountTable);
        db.execSQL(createStudentTable);
        db.execSQL(createTeacherTable);
        db.execSQL(createScoreTable);

        seedDefaultData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        onCreate(db);
    }

    private void seedDefaultData(SQLiteDatabase db) {
        // Teacher Account
        ContentValues cvAcc1 = new ContentValues();
        cvAcc1.put(COL_ACCOUNT_USERNAME, "teacher");
        cvAcc1.put(COL_ACCOUNT_PASSWORD, "123456");
        cvAcc1.put(COL_ACCOUNT_ROLE, "TEACHER");
        long tAccId = db.insert(TABLE_ACCOUNT, null, cvAcc1);

        ContentValues cvTeacher = new ContentValues();
        cvTeacher.put(COL_TEACHER_FULLNAME, "ThS. Nguyen Van Thay");
        cvTeacher.put(COL_TEACHER_EMAIL, "thaynv@ptit.edu.vn");
        cvTeacher.put(COL_TEACHER_ACCOUNT_ID, tAccId);
        db.insert(TABLE_TEACHER, null, cvTeacher);

        // Student 1 Account
        ContentValues cvAcc2 = new ContentValues();
        cvAcc2.put(COL_ACCOUNT_USERNAME, "student");
        cvAcc2.put(COL_ACCOUNT_PASSWORD, "123456");
        cvAcc2.put(COL_ACCOUNT_ROLE, "STUDENT");
        long sAccId1 = db.insert(TABLE_ACCOUNT, null, cvAcc2);

        ContentValues cvStudent1 = new ContentValues();
        cvStudent1.put(COL_STUDENT_CODE, "B21DCCN001");
        cvStudent1.put(COL_STUDENT_FULLNAME, "Tran Van An");
        cvStudent1.put(COL_STUDENT_EMAIL, "antv.b21@ptit.edu.vn");
        cvStudent1.put(COL_STUDENT_ACCOUNT_ID, sAccId1);
        db.insert(TABLE_STUDENT, null, cvStudent1);

        // Student 2 Account
        ContentValues cvAcc3 = new ContentValues();
        cvAcc3.put(COL_ACCOUNT_USERNAME, "student2");
        cvAcc3.put(COL_ACCOUNT_PASSWORD, "123456");
        cvAcc3.put(COL_ACCOUNT_ROLE, "STUDENT");
        long sAccId2 = db.insert(TABLE_ACCOUNT, null, cvAcc3);

        ContentValues cvStudent2 = new ContentValues();
        cvStudent2.put(COL_STUDENT_CODE, "B21DCCN002");
        cvStudent2.put(COL_STUDENT_FULLNAME, "Le Thi Binh");
        cvStudent2.put(COL_STUDENT_EMAIL, "binhlt.b21@ptit.edu.vn");
        cvStudent2.put(COL_STUDENT_ACCOUNT_ID, sAccId2);
        db.insert(TABLE_STUDENT, null, cvStudent2);

        // Scores
        insertDefaultScore(db, "B21DCCN001", "GV01", "Lap trinh Di dong", 9.5);
        insertDefaultScore(db, "B21DCCN001", "GV01", "Co so du lieu", 8.0);
        insertDefaultScore(db, "B21DCCN001", "GV01", "Mang may tinh", 7.0);
        insertDefaultScore(db, "B21DCCN002", "GV01", "Lap trinh Di dong", 8.5);
        insertDefaultScore(db, "B21DCCN002", "GV01", "Tri tue nhan tao", 9.0);
    }

    private void insertDefaultScore(SQLiteDatabase db, String studentCode, String teacherCode, String subject, double score) {
        ContentValues cv = new ContentValues();
        cv.put(COL_SCORE_STUDENT_CODE, studentCode);
        cv.put(COL_SCORE_TEACHER_CODE, teacherCode);
        cv.put(COL_SCORE_SUBJECT, subject);
        cv.put(COL_SCORE_SCORE, score);
        db.insert(TABLE_SCORE, null, cv);
    }

    // Authentication
    public Account checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ACCOUNT, null,
                COL_ACCOUNT_USERNAME + "=? AND " + COL_ACCOUNT_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);

        Account account = null;
        if (cursor != null && cursor.moveToFirst()) {
            account = new Account(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_ACCOUNT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_ACCOUNT_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_ACCOUNT_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_ACCOUNT_ROLE))
            );
            cursor.close();
        }
        return account;
    }

    public Student getStudentByAccountId(int accountId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENT, null,
                COL_STUDENT_ACCOUNT_ID + "=?",
                new String[]{String.valueOf(accountId)}, null, null, null);

        Student student = null;
        if (cursor != null && cursor.moveToFirst()) {
            student = new Student(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_STUDENT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_CODE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_FULLNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_EMAIL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_STUDENT_ACCOUNT_ID))
            );
            cursor.close();
        }
        return student;
    }

    public Teacher getTeacherByAccountId(int accountId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TEACHER, null,
                COL_TEACHER_ACCOUNT_ID + "=?",
                new String[]{String.valueOf(accountId)}, null, null, null);

        Teacher teacher = null;
        if (cursor != null && cursor.moveToFirst()) {
            teacher = new Teacher(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_TEACHER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_TEACHER_FULLNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_TEACHER_EMAIL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_TEACHER_ACCOUNT_ID))
            );
            cursor.close();
        }
        return teacher;
    }

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STUDENT, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Student s = new Student(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_STUDENT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_CODE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_FULLNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_EMAIL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_STUDENT_ACCOUNT_ID))
                );
                list.add(s);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    // Score CRUD
    public List<Score> getAllScores() {
        List<Score> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SCORE + " ORDER BY " + COL_SCORE_ID + " DESC", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Score s = new Score(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_SCORE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_SCORE_STUDENT_CODE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_SCORE_TEACHER_CODE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_SCORE_SUBJECT)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_SCORE_SCORE))
                );
                list.add(s);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public List<Score> getScoresByStudentCode(String studentCode) {
        List<Score> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SCORE, null,
                COL_SCORE_STUDENT_CODE + "=?",
                new String[]{studentCode}, null, null, COL_SCORE_ID + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Score s = new Score(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_SCORE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_SCORE_STUDENT_CODE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_SCORE_TEACHER_CODE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_SCORE_SUBJECT)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_SCORE_SCORE))
                );
                list.add(s);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public List<Score> searchScores(String keyword, String studentCodeLimit) {
        List<Score> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query;
        String[] args;

        if (studentCodeLimit != null && !studentCodeLimit.isEmpty()) {
            query = "SELECT * FROM " + TABLE_SCORE +
                    " WHERE " + COL_SCORE_STUDENT_CODE + " = ? AND (" +
                    COL_SCORE_SUBJECT + " LIKE ? OR " + COL_SCORE_TEACHER_CODE + " LIKE ?)" +
                    " ORDER BY " + COL_SCORE_ID + " DESC";
            args = new String[]{studentCodeLimit, "%" + keyword + "%", "%" + keyword + "%"};
        } else {
            query = "SELECT * FROM " + TABLE_SCORE +
                    " WHERE " + COL_SCORE_STUDENT_CODE + " LIKE ? OR " +
                    COL_SCORE_SUBJECT + " LIKE ? OR " +
                    COL_SCORE_TEACHER_CODE + " LIKE ?" +
                    " ORDER BY " + COL_SCORE_ID + " DESC";
            args = new String[]{"%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%"};
        }

        Cursor cursor = db.rawQuery(query, args);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Score s = new Score(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_SCORE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_SCORE_STUDENT_CODE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_SCORE_TEACHER_CODE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_SCORE_SUBJECT)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_SCORE_SCORE))
                );
                list.add(s);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public long insertScore(Score score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_SCORE_STUDENT_CODE, score.getStudentCode());
        cv.put(COL_SCORE_TEACHER_CODE, score.getTeacherCode());
        cv.put(COL_SCORE_SUBJECT, score.getSubject());
        cv.put(COL_SCORE_SCORE, score.getScore());
        return db.insert(TABLE_SCORE, null, cv);
    }

    public int updateScore(Score score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_SCORE_STUDENT_CODE, score.getStudentCode());
        cv.put(COL_SCORE_TEACHER_CODE, score.getTeacherCode());
        cv.put(COL_SCORE_SUBJECT, score.getSubject());
        cv.put(COL_SCORE_SCORE, score.getScore());
        return db.update(TABLE_SCORE, cv, COL_SCORE_ID + "=?", new String[]{String.valueOf(score.getId())});
    }

    public int deleteScore(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SCORE, COL_SCORE_ID + "=?", new String[]{String.valueOf(id)});
    }
}
