# Student Manager - Mobile App Test 2

Dự án mẫu hoàn chỉnh cho **Bài Test 2 - Lập trình thiết bị di động (PTIT)**.

## 📌 Tính năng đã hoàn thiện

1. **Đăng nhập phân quyền (2 roles):**
   - **Giáo viên (`TEACHER`)**: Xem toàn bộ điểm, thêm điểm mới, sửa điểm, xóa điểm, thống kê điểm số.
   - **Sinh viên (`STUDENT`)**: Chỉ xem bảng điểm cá nhân theo mã sinh viên, xem thông tin sinh viên, không thể chỉnh sửa hoặc xóa điểm.
2. **Lưu phiên đăng nhập với `SharedPreferences`:**
   - Hỗ trợ lưu trạng thái đăng nhập, ghi nhớ tài khoản (Remember Me), tự động đăng nhập khi mở lại app, và chức năng Đăng xuất.
3. **Cơ sở dữ liệu SQLite (`DBHelper`):**
   - 4 bảng chuẩn theo đề bài:
     - `Account(id, username, password, role)`
     - `Student(id, code, fullname, email, accountId)`
     - `Teacher(id, fullname, email, accountId)`
     - `Score(id, studentCode, teacherCode, subject, score)`
   - Tự động nạp sẵn dữ liệu mẫu (`seedDefaultData`) ngay khi chạy lần đầu.
4. **Tìm kiếm thời gian thực:**
   - Tìm kiếm theo tên môn học, mã sinh viên, mã giáo viên.

---

## 🔑 Tài khoản mẫu Test nhanh

| Vai trò | Tên đăng nhập | Mật khẩu | Mã số | Ghi chú |
|---|---|---|---|---|
| **Giảng viên** | `teacher` | `123456` | `GV01` | Toàn quyền CRUD điểm số |
| **Sinh viên 1** | `student` | `123456` | `B21DCCN001` | Xem điểm cá nhân sinh viên 1 |
| **Sinh viên 2** | `student2` | `123456` | `B21DCCN002` | Xem điểm cá nhân sinh viên 2 |

*(Trên màn hình Đăng nhập đã có sẵn nút bấm nhanh tự điền tài khoản để chấm thi siêu tốc).*

---

## 📂 Cấu trúc mã nguồn

```text
com.example.studentmanager
│
├── activities/
│   ├── LoginActivity.java     # Đăng nhập, ghi nhớ mật khẩu, điều hướng theo role
│   ├── MainActivity.java      # Màn hình chính, tổng quan, thống kê điểm, thông tin user
│   └── ScoreActivity.java     # Quản lý / Xem danh sách điểm, tìm kiếm, CRUD dialog
│
├── adapters/
│   └── ScoreAdapter.java      # Custom RecyclerView Adapter hiển thị điểm & huy hiệu màu
│
├── dal/
│   └── DBHelper.java          # SQLiteOpenHelper, quản lý 4 bảng và các hàm truy vấn
│
├── models/
│   ├── Account.java           # Model tài khoản
│   ├── Student.java           # Model sinh viên
│   ├── Teacher.java           # Model giảng viên
│   └── Score.java             # Model điểm số
│
└── utils/
    └── SharedPrefManager.java # Quản lý SharedPreferences (Session, Remember Me)
```

---

## 🚀 Cách tùy biến nhanh khi có đề bài chính thức

1. **Đổi tên bảng / thêm cột:** Mở `dal/DBHelper.java` chỉnh sửa câu lệnh `CREATE TABLE` và các trường trong `seedDefaultData`.
2. **Thêm / đổi thuộc tính Model:** Mở thư mục `models/` cập nhật các trường tương ứng.
3. **Giao diện:** Mở `res/layout/` để thêm bớt các View nếu đề bài yêu cầu thêm trường dữ liệu khác.
