package com.example.healthtracker.data.local.database

import com.example.healthtracker.data.local.entity.ActivityEntity
import com.example.healthtracker.data.local.entity.FoodEntity

object DatabaseSeed {

    val foods: List<FoodEntity> = listOf(
        FoodEntity(name = "Cơm trắng", calories = 130.0, servingUnit = "1 chén (100g)"),
        FoodEntity(name = "Phở bò", calories = 350.0, servingUnit = "1 tô"),
        FoodEntity(name = "Bún bò Huế", calories = 430.0, servingUnit = "1 tô"),
        FoodEntity(name = "Bánh mì thịt", calories = 400.0, servingUnit = "1 ổ"),
        FoodEntity(name = "Cơm tấm sườn", calories = 620.0, servingUnit = "1 đĩa"),
        FoodEntity(name = "Bún chả", calories = 470.0, servingUnit = "1 suất"),
        FoodEntity(name = "Gỏi cuốn", calories = 80.0, servingUnit = "1 cuốn"),
        FoodEntity(name = "Chả giò", calories = 130.0, servingUnit = "1 cuốn"),
        FoodEntity(name = "Trứng gà luộc", calories = 78.0, servingUnit = "1 quả"),
        FoodEntity(name = "Ức gà luộc", calories = 165.0, servingUnit = "100g"),
        FoodEntity(name = "Thịt bò xào", calories = 250.0, servingUnit = "100g"),
        FoodEntity(name = "Cá hồi áp chảo", calories = 208.0, servingUnit = "100g"),
        FoodEntity(name = "Tôm luộc", calories = 99.0, servingUnit = "100g"),
        FoodEntity(name = "Đậu hũ", calories = 76.0, servingUnit = "100g"),
        FoodEntity(name = "Rau muống luộc", calories = 30.0, servingUnit = "100g"),
        FoodEntity(name = "Salad trộn", calories = 150.0, servingUnit = "1 phần"),
        FoodEntity(name = "Chuối", calories = 105.0, servingUnit = "1 quả"),
        FoodEntity(name = "Táo", calories = 95.0, servingUnit = "1 quả"),
        FoodEntity(name = "Cam", calories = 62.0, servingUnit = "1 quả"),
        FoodEntity(name = "Sữa tươi", calories = 150.0, servingUnit = "1 ly (240ml)"),
        FoodEntity(name = "Sữa chua", calories = 100.0, servingUnit = "1 hộp"),
        FoodEntity(name = "Cà phê sữa", calories = 90.0, servingUnit = "1 ly"),
        FoodEntity(name = "Trà sữa", calories = 340.0, servingUnit = "1 ly"),
        FoodEntity(name = "Nước ngọt có gas", calories = 140.0, servingUnit = "1 lon"),
        FoodEntity(name = "Bánh quy", calories = 50.0, servingUnit = "1 cái"),
        FoodEntity(name = "Khoai tây chiên", calories = 312.0, servingUnit = "1 phần vừa"),
        FoodEntity(name = "Pizza", calories = 285.0, servingUnit = "1 miếng"),
        FoodEntity(name = "Hamburger", calories = 354.0, servingUnit = "1 cái"),
        FoodEntity(name = "Mì gói", calories = 380.0, servingUnit = "1 gói"),
        FoodEntity(name = "Xôi", calories = 340.0, servingUnit = "1 phần"),
        FoodEntity(name = "Bánh chưng", calories = 450.0, servingUnit = "1 miếng"),
        FoodEntity(name = "Yến mạch", calories = 150.0, servingUnit = "40g khô"),
    )

    val activities: List<ActivityEntity> = listOf(
        ActivityEntity(name = "Đi bộ", met = 3.5),
        ActivityEntity(name = "Đi bộ nhanh", met = 4.3),
        ActivityEntity(name = "Chạy bộ", met = 9.8),
        ActivityEntity(name = "Đạp xe", met = 7.5),
        ActivityEntity(name = "Bơi lội", met = 8.0),
        ActivityEntity(name = "Nhảy dây", met = 11.0),
        ActivityEntity(name = "Yoga", met = 2.5),
        ActivityEntity(name = "Tập gym (tạ)", met = 6.0),
        ActivityEntity(name = "Đá bóng", met = 7.0),
        ActivityEntity(name = "Cầu lông", met = 5.5),
        ActivityEntity(name = "Bóng rổ", met = 6.5),
        ActivityEntity(name = "Tennis", met = 7.3),
        ActivityEntity(name = "Leo cầu thang", met = 8.8),
        ActivityEntity(name = "Khiêu vũ", met = 5.0),
        ActivityEntity(name = "Làm việc nhà", met = 3.0),
        ActivityEntity(name = "Aerobic", met = 7.3),
    )
}
