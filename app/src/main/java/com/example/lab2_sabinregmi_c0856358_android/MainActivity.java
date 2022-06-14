package com.example.lab2_sabinregmi_c0856358_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
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
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productList = new ArrayList<>();
        tempProductList = new ArrayList<>();
        sqLiteDatabase = new DatabaseHelper(this);

        EditText theFilter = (EditText) findViewById(R.id.et_searchbar);
        productsListView = findViewById(R.id.lv_products);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View view2 = layoutInflater.inflate(R.layout.dialog_add_products, null);
                builder.setView(view2);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                final EditText etName = view2.findViewById(R.id.et_name);
                final EditText etPrice = view2.findViewById(R.id.et_price);
                final EditText etDesc = view2.findViewById(R.id.et_description);

                view2.findViewById(R.id.btn_add_employee).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = etName.getText().toString().trim();
                        String price = etPrice.getText().toString().trim();
                        String desc = etDesc.getText().toString();

                        if (name.isEmpty()) {
                            etName.setError("name field cannot be empty");
                            etName.requestFocus();
                            return;
                        }

                        if (desc.isEmpty()) {
                            etDesc.setError("name field cannot be empty");
                            etDesc.requestFocus();
                            return;
                        }

                        if (price.isEmpty()) {
                            etPrice.setError("Price cannot be empty");
                            etPrice.requestFocus();
                            return;
                        }

                        if (sqLiteDatabase.addProduct(name, desc, Double.parseDouble(price)))
                            loadProducts();
                        alertDialog.dismiss();
                    }
                });
            }
        });

        loadTempProducts();
        loadProducts();

        theFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (MainActivity.this).productAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void loadTempProducts() {
        tempProductList.add(new Product(1, "GoPro HERO10 Black","Revolutionary Processor: Faster. Smoother. Better. The powerful new GP2 engine changes the game—snappy performance, responsive touch controls and double the frame rate for amazingly smooth footage. Designed specifically for the demanding nature of the GoPro, the GP2 “system on a chip” is by far our fastest ever.",590.0));
        tempProductList.add(new Product(2, "Canon EOS M50","Dual Pixel CMOS AF for fast, accurate autofocus that helps you get the photo you want right as the moment happens",999.0));
        tempProductList.add(new Product(3, "Apple Magic Keyboard","The Magic Keyboard is the perfect companion for iPad Pro and iPad Air (4th generation).",390.0));
        tempProductList.add(new Product(4, "Gaming Mouse","Gaming mouse with 9 programmable buttons for creating custom configurations; 1 independently macro hot key to switch between 3 game modes",52.0));
        tempProductList.add(new Product(5, "Gaming Headset","2-way communication headset for exciting video game play",70.0));
        tempProductList.add(new Product(6, "Playstation Classic Console","PlayStation Classic comes with 20 preloaded games - Includes Final Fantasy VII; Jumping Flash; Ridge Racer Type 4; Tekken 3; Wild Arms; Battle Arena Toshinden; Cool Boarders 2; Destruction Derby; Grand Theft Auto; Intelligent Qube; Metal Gear Solid; Mr. Driller",197.0));
        tempProductList.add(new Product(7, "SEGA Genesis Mini","The iconic SEGA Genesis console that defined a generation of gaming returns in a slick, miniaturized unit.",230.0));
        tempProductList.add(new Product(8, "Smartphone Gimbal","Portable and Palm-Sized Gimbal Stabilizer for Smartphones - DJI OM 5 is a lightweight and versatile tool that unlocks the full potential of your smartphone. Enjoy flawless selfies, super-smooth video, automatic tracking, and so much more with this DJI phone gimbal.",179.0));
        tempProductList.add(new Product(9, "Universal Video Microphone","UNIVERSAL COMPATIBILITY: External microphone for iPhone, Androids, Cameras, Camcorders, Audio Recorders, Tablets, and Laptops. Use as iPhone microphone, DSLR microphone or camera mic.",49.0));
        tempProductList.add(new Product(10, "Charging Soft Light Panel","New Upgrade, More Smooth --- Upgrade soft light board based on old models, make supplement light softer and lither, not dazzling, perfect for vlogging.",29.0));

        Cursor cursor = sqLiteDatabase.getAllProducts();
        if (cursor.getCount() < 9){
            for (Product product : tempProductList) {
                if (productList.contains(product)) {
                    return;
                } else {
                    sqLiteDatabase.addProduct(product.getName(), product.getDescription(), product.getPrice());
                }
            }
        }
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

        // create an adapter to display the employees
        productAdapter = new ProductAdapter(this, R.layout.list_layout, productList, sqLiteDatabase);
        productsListView.setAdapter(productAdapter);
    }
}