# Ứng dụng iNotes

Ứng dụng web quản lý ghi chú cá nhân, được xây dựng bằng Java Servlet/JSP và áp dụng **Strategy Pattern** để có thể chuyển đổi linh hoạt giữa hai tầng lưu trữ:

- MySQL thông qua JDBC.
- Tệp tin dạng `id|title|content|typeId`.

## Chức năng

- Thêm ghi chú mới.
- Hiển thị danh sách ghi chú.
- Tìm kiếm theo tiêu đề hoặc nội dung.
- Phân loại ghi chú: Cá nhân, Công việc, Học tập.
- Xóa ghi chú.
- Chuyển đổi tầng lưu trữ giữa MySQL và file.

## Công nghệ sử dụng

- Java 17.
- Maven.
- Jakarta Servlet 6.
- JSP.
- MySQL Connector/J.
- Apache Tomcat 10 trở lên.

## Cấu trúc chính

```text
src/main/java/com/codegym/inotes/
├── controller/
│   └── NoteServlet.java
├── model/
│   └── Note.java
├── service/
│   └── NoteManagement.java
└── strategy/
    ├── NoteStrategy.java
    ├── NoteDBStrategy.java
    └── NoteFileStrategy.java

src/main/webapp/
└── WEB-INF/
    ├── views/notes.jsp
    └── web.xml

schema.sql
pom.xml
```

## Khởi tạo cơ sở dữ liệu MySQL

Mở MySQL và chạy file:

```text
schema.sql
```

File này sẽ tạo database `inotes_db` cùng hai bảng:

- `note_type`
- `notes`

Thông tin kết nối mặc định trong `NoteServlet`:

```text
URL: jdbc:mysql://localhost:3306/inotes_db?useSSL=false&serverTimezone=UTC
User: root
Password: rỗng
```

Nếu MySQL sử dụng tài khoản hoặc mật khẩu khác, cập nhật phần khởi tạo `NoteDBStrategy` trong `NoteServlet`.

## Chọn tầng lưu trữ

### Lưu bằng MySQL

Mặc định ứng dụng sử dụng MySQL khi không khai báo `storage` hoặc khi giá trị khác `file`.

### Lưu bằng file

Để sử dụng `NoteFileStrategy`, thêm init parameter vào `web.xml`:

```xml
<context-param>
    <param-name>storage</param-name>
    <param-value>file</param-value>
</context-param>
```

Khi đó ghi chú sẽ được lưu vào file:

```text
WEB-INF/notes.txt
```

Mỗi dòng có định dạng:

```text
id|title|content|typeId
```

## Build và chạy

Yêu cầu đã cài JDK 17, Maven và Tomcat 10 trở lên.

Build file WAR:

```bash
mvn clean package
```

Sau khi build thành công, file WAR nằm tại:

```text
target/app-inotes.war
```

Copy file WAR vào thư mục `webapps` của Tomcat, khởi động Tomcat và truy cập:

```text
http://localhost:8080/app-inotes/notes
```

## Strategy Pattern trong ứng dụng

`NoteManagement` không phụ thuộc trực tiếp vào MySQL hoặc file. Lớp này chỉ làm việc với interface:

```java
private NoteStrategy strategy;
```

Có thể thay đổi strategy lúc chạy bằng:

```java
management.setStrategy(new NoteFileStrategy("notes.txt"));
```

hoặc:

```java
management.setStrategy(new NoteDBStrategy(url, user, password));
```

Nhờ vậy logic nghiệp vụ và giao diện không cần thay đổi khi chuyển tầng lưu trữ.
