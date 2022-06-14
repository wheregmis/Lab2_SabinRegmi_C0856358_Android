package com.example.lab2_sabinregmi_c0856358_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.lab2_sabinregmi_c0856358_android.model.Product;
import com.example.lab2_sabinregmi_c0856358_android.utils.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    // instance of DatabaseOpenHelper class
    DatabaseHelper sqLiteDatabase;

    List<Product> productList;
    ListView productsListView;
    List<Product> tempProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productList = new ArrayList<>();
        tempProductList = new ArrayList<>();
        sqLiteDatabase = new DatabaseHelper(this);

        productsListView = findViewById(R.id.lv_products);
        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
//                View view2 = layoutInflater.inflate(R.layout.dialog_add_products, null);
//                builder.setView(view2);
//                final AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//
//                final EditText etName = view2.findViewById(R.id.et_name);
//                final EditText etPrice = view2.findViewById(R.id.et_price);
//                final EditText etDesc = view2.findViewById(R.id.et_description);
//
//                view2.findViewById(R.id.btn_add_employee).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String name = etName.getText().toString().trim();
//                        String price = etPrice.getText().toString().trim();
//                        String desc = etDesc.getText().toString();
//
//                        if (name.isEmpty()) {
//                            etName.setError("name field cannot be empty");
//                            etName.requestFocus();
//                            return;
//                        }
//
//                        if (desc.isEmpty()) {
//                            etDesc.setError("name field cannot be empty");
//                            etDesc.requestFocus();
//                            return;
//                        }
//
//                        if (price.isEmpty()) {
//                            etPrice.setError("Price cannot be empty");
//                            etPrice.requestFocus();
//                            return;
//                        }
//
////                        if (sqLiteDatabase.addProduct(name, desc, Double.parseDouble(price)))
////                            loadProducts();
//                        alertDialog.dismiss();
//                    }
//                });
//            }
//        });

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
                        cursor.getDouble(3)
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