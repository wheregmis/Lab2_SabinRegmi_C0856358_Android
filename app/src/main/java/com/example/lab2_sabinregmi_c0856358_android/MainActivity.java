package com.example.lab2_sabinregmi_c0856358_android;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import com.example.lab2_sabinregmi_c0856358_android.model.Product;
import com.example.lab2_sabinregmi_c0856358_android.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //SQLiteDatabase sqLiteDatabase;

    // instance of DatabaseOpenHelper class
    DatabaseHelper sqLiteDatabase;

    List<Product> productList;
    ListView productsListView;
    List<Product> tempProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productsListView = findViewById(R.id.lv_products);
        productList = new ArrayList<>();
        tempProductList = new ArrayList<>();
        sqLiteDatabase = new DatabaseHelper(this);
        loadTempProducts();
        loadProducts();
    }

    private void loadTempProducts() {
        tempProductList.add(new Product(1, "GoPro HERO10 Black","Revolutionary Processor: Faster. Smoother. Better. The powerful new GP2 engine changes the game—snappy performance, responsive touch controls and double the frame rate for amazingly smooth footage. Designed specifically for the demanding nature of the GoPro, the GP2 “system on a chip” is by far our fastest ever.",590.0));
        tempProductList.add(new Product(2, "Canon EOS M50","Dual Pixel CMOS AF for fast, accurate autofocus that helps you get the photo you want right as the moment happens",999.0));
    }

    private void loadProducts() {
        Cursor cursor = sqLiteDatabase.getAllProducts();
        if (cursor.moveToFirst()) {
            do {
                // create an employee instance
                productList.add(new Product(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(4)
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (productList.containsAll(tempProductList)){
            return;
        }else{
            productList.addAll(tempProductList);
        }

        // create an adapter to display the employees
        ProductAdapter productAdapter = new ProductAdapter(this, R.layout.list_layout, productList);
        productsListView.setAdapter(productAdapter);
    }
}