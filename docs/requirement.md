Ứng dụng Quản lý Sức khỏe & Theo dõi Calo (HealthTracker)

Thời gian thực hiện: 3 tuần (Deadline: 24/Jul/2026)

Hình thức nộp bài: Source code (GitHub) + APK demo

1\. MÔ TẢ TỔNG QUAN

Xây dựng ứng dụng Android "HealthTracker" giúp người dùng theo dõi lượng calo

nạp vào và tiêu thụ mỗi ngày, từ đó hỗ trợ duy trì hoặc cải thiện sức khỏe.

Ứng dụng bao gồm các chức năng chính:

\- Thiết lập hồ sơ cá nhân và tính calo cần thiết mỗi ngày (TDEE)

\- Ghi nhận bữa ăn và tính calo nạp vào

\- Ghi nhận hoạt động thể chất và tính calo tiêu thụ

\- Hiển thị biểu đồ và thống kê theo ngày / tuần / tháng

\- Cài đặt ứng dụng: chỉnh sửa hồ sơ, ngôn ngữ (EN/VI), giao diện (Light/Dark)

2\. YÊU CẦU CHỨC NĂNG CHI TIẾT

2.1 Hồ sơ người dùng

\- Nhập thông tin lần đầu sử dụng:

\+ Họ tên

\+ Ngày sinh (tính tuổi tự động)

\+ Giới tính (Nam / Nữ)

\+ Cân nặng (kg)

\+ Chiều cao (cm)

\+ Mức độ hoạt động hàng ngày:

\- Ít vận động (ngồi nhiều, không tập)

\- Vận động nhẹ (1-3 buổi/tuần)

\- Vận động vừa (3-5 buổi/tuần)

\- Vận động nhiều (6-7 buổi/tuần)

\- Vận động rất nhiều (công việc thể chất nặng)

\+ Mục tiêu:

\- Giảm cân

\- Giữ cân

\- Tăng cân

\- Validate đầu vào (không để trống, kiểm tra giá trị hợp lệ)

\- Lưu thông tin vào local storage

\- Cho phép chỉnh sửa hồ sơ sau này trong phần Settings

2.2 Tính toán TDEE

Bước 1 - Tính BMR (Basal Metabolic Rate) theo công thức:

Nam : BMR = 10 × cân nặng(kg) + 6.25 × chiều cao(cm) - 5 × tuổi + 5

Nữ : BMR = 10 × cân nặng(kg) + 6.25 × chiều cao(cm) - 5 × tuổi - 161

Bước 2 - Nhân hệ số hoạt động để ra TDEE:

Mức 1: BMR × 1.2

Mức 2: BMR × 1.375

Mức 3: BMR × 1.55

Mức 4: BMR × 1.725

Mức 5: BMR × 1.9

Bước 3 - Điều chỉnh theo mục tiêu:

Giảm cân : TDEE - 500 kcal

Giữ cân : TDEE

Tăng cân : TDEE + 500 kcal

Kết quả cuối là mức calo mục tiêu mỗi ngày, hiển thị trên màn hình chính.

2.3 Nhật ký bữa ăn

\- Chọn ngày xem nhật ký (mặc định là hôm nay)

\- Hiển thị các bữa ăn: Bữa sáng / Bữa trưa / Bữa tối / Bữa phụ

\- Trong mỗi bữa:

\+ Thêm món ăn (tìm kiếm từ danh sách có sẵn hoặc tự nhập)

\+ Nhập số lượng / khẩu phần

\+ Hiển thị calo của từng món

\+ Xóa món ăn khỏi bữa

\- Tổng calo từng bữa và tổng calo trong ngày

\- Danh sách món ăn mẫu (tối thiểu 30 món) lưu sẵn trong database:

Ví dụ: Cơm trắng 100g = 130 kcal, Trứng gà luộc = 78 kcal, Phở bò tô nhỏ =

350 kcal, Bánh mì = 265 kcal...

\- Chức năng tìm kiếm món ăn theo tên

2.4 Nhật ký hoạt động

\- Thêm hoạt động thể chất trong ngày:

\+ Chọn loại hoạt động từ danh sách (tối thiểu 15 loại):

Đi bộ, Chạy bộ, Đạp xe, Bơi lội, Yoga, Gym,

Leo cầu thang, Nhảy dây, Cầu lông, Bóng đá...

\+ Nhập thời gian thực hiện (phút)

\- Tính calo tiêu thụ theo công thức:

Calo tiêu thụ = MET × cân nặng(kg) × thời gian(giờ)

(MET - Metabolic Equivalent of Task, mỗi hoạt động có giá trị MET khác

nhau)

Ví dụ MET: Đi bộ = 3.5, Chạy bộ = 9.8, Đạp xe = 7.5, Bơi lội = 8.0

\- Hiển thị danh sách hoạt động trong ngày và tổng calo đã tiêu thụ

\- Xóa hoạt động

2.5 Dashboard

\- Hiển thị ngày hiện tại

\- Vòng tròn tiến độ (Circular Progress) thể hiện:

\+ Calo mục tiêu trong ngày (TDEE)

\+ Calo đã nạp vào

\+ Calo đã tiêu thụ

\+ Calo còn lại (hoặc vượt quá)

\- Summary card:

\+ Tổng calo ăn vào hôm nay

\+ Tổng calo tiêu thụ hôm nay

\+ Cân bằng calo: Nạp vào - Tiêu thụ

\- Lời khuyên ngắn dựa trên tình trạng hiện tại:

VD: "Bạn đã ăn đủ calo hôm nay!" hoặc "Bạn còn thiếu 300 kcal"

\- Shortcut điều hướng nhanh đến: Thêm bữa ăn, Thêm hoạt động

2.6 Thống kê và biểu đồ

\- Biểu đồ cột (Bar Chart) hiển thị calo nạp vào 7 ngày gần nhất

\- Biểu đồ đường (Line Chart) hiển thị xu hướng calo theo tuần

\- Thống kê tuần:

\+ Trung bình calo nạp vào / ngày

\+ Trung bình calo tiêu thụ / ngày

\+ Số ngày đạt mục tiêu

2.7 Cài đặt profile

\- Hiển thị thông tin hiện tại của người dùng

\- Cho phép chỉnh sửa toàn bộ các trường:

\+ Họ tên

\+ Ngày sinh

\+ Giới tính (Nam / Nữ)

\+ Cân nặng (kg)

\+ Chiều cao (cm)

\+ Mức độ hoạt động hàng ngày (1-5)

\+ Mục tiêu (Giảm cân / Giữ cân / Tăng cân)

\- Sau khi lưu, TDEE được tính lại ngay lập tức và cập nhật Dashboard

\- Validate đầu vào trước khi lưu (tương tự Onboarding)

\- Hiển thị BMI hiện tại và phân loại dựa trên chiều cao/cân nặng:

BMI < 18.5 → Thiếu cân

18.5 ≤ BMI < 25 → Bình thường

25 ≤ BMI < 30 → Thừa cân

BMI ≥ 30 → Béo phì

2.8 Cài đặt ngôn ngữ

\- Tiếng Việt

\- Tiếng Anh

2.9 Cài đặt giao diện

\- Theme:

\- brightness: sáng / tối / hệ thống (mặc định)

\- color: xây dựng sẵn các bộ màu khác nhau, cung cấp cho người dùng lựa

chọn

\- Cỡ chữ: Nhỏ / Vừa (mặc định) / Lớn

2.10 Tính năng bổ sung

\- Nhắc nhở ghi nhật ký qua Notification (sáng 7h, trưa 12h, tối 19h)

\- Tính BMI và hiển thị phân loại (Gầy / Bình thường / Thừa cân / Béo phì)

\- Widget hiển thị tiến độ calo trên màn hình chính điện thoại

\- Xuất báo cáo tuần ra file PDF hoặc chia sẻ

3\. YÊU CẦU KỸ THUẬT

3.1 Kiến Trúc

\- Áp dụng kiến trúc MVVM hoặc MVI

\- Tách biệt rõ ràng các layer: UI / ViewModel / Repository / Database

3.2 Ngôn ngữ lập trình

\- Sử dụng Kotlin

\- Sử dụng Kotlin Coroutines

\- Sử dụng: Flow / StateFlow / SharedFlow

\- Sử dụng Kotlin: extension functions, scope functions

\- Sử dụng Design Patterns

3.3 UI Frameword:

\- Jetpack Compose

3.4 Database:

\- Room Database

\- DataStore

3.5 Dependencies Injection:

\- Hilt

\- Koin