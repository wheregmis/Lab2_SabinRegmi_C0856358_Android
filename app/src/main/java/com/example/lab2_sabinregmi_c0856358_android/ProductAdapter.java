package com.example.lab2_sabinregmi_c0856358_android;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lab2_sabinregmi_c0856358_android.model.Product;
import com.example.lab2_sabinregmi_c0856358_android.utils.DatabaseHelper;

import java.util.List;

public class ProductAdapter extends ArrayAdapter {

    private static final String TAG = "productAdapter";
    int layoutRes;
    Context context;
    List<Product> productList;
    //    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper sqLiteDatabase;


    public ProductAdapter(@NonNull Context context, int resource, List<Product> productList) {
        super(context, resource, productList);
        this.productList = productList;
        //this.sqLiteDatabase = sqLiteDatabase;
        this.layoutRes = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = convertView;
        v = inflater.inflate(layoutRes, null);
        TextView nameTV = v.findViewById(R.id.tv_name);
        TextView salaryTV = v.findViewById(R.id.tv_price);
        TextView descTV = v.findViewById(R.id.tv_desc);

        final Product product = productList.get(position);
        nameTV.setText(product.getName());
        salaryTV.setText(String.valueOf(product.getPrice()));
        descTV.setText(product.getDescription());

        v.findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct(product);
            }

            public void updateProduct(final Product product){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View view = layoutInflater.inflate(R.layout.dialog_update_products, null);
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                final EditText etName = view.findViewById(R.id.et_name);
                final EditText etPrice = view.findViewById(R.id.et_price);
                final EditText etDesc = view.findViewById(R.id.et_description);

                etName.setText(product.getName());
                etDesc.setText(product.getDescription());
                etPrice.setText(String.valueOf(product.getPrice()));

                view.findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
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

                        if (price.isEmpty()) {
                            etPrice.setError("Price cannot be empty");
                            etPrice.requestFocus();
                            return;
                        }

                        if (sqLiteDatabase.updateProduct(product.getId(), name, desc, Double.parseDouble(price)))
                            loadProducts();
                        alertDialog.dismiss();
                    }
                });
            }

        });
        v.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(product);
            }

            private void deleteProduct(final Product product) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (sqLiteDatabase.deleteProduct(product.getId()))
                            loadProducts();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "The Product (" + product.getName() + ") is not deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        Log.d(TAG, "getView: " + getCount());
        return v;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    private void loadProducts() {
        String sql = "SELECT * FROM product";
        //Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        Cursor cursor = sqLiteDatabase.getAllProducts();
        productList.clear();
        if (cursor.moveToFirst()) {
            do {
                // create an product instance
                productList.add(new Product(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(4)
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
        notifyDataSetChanged();
    }
}
