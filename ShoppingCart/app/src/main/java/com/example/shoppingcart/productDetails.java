package com.example.shoppingcart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.shoppingcart.Model.ProductCollection;
import com.example.shoppingcart.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class productDetails extends AppCompatActivity {
    //    private FloatingActionButton addToCart;
    private ImageView image;
    private ElegantNumberButton numberButton;
    TextView productName, productPrice, productDescription, productQuantity;
    private String productId = "";
    Button addToCart, viewCart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details2);

        productId = getIntent().getStringExtra("ProductId");


//        addToCart = (FloatingActionButton) findViewById(R.id.add_to_cart);
        addToCart = (Button) findViewById(R.id.add_to_cart);
        viewCart = (Button) findViewById ( R.id.view_cart );
        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        productName = (TextView) findViewById(R.id.product_display_name);
        productPrice = (TextView) findViewById(R.id.product_display_price);
        productDescription = (TextView) findViewById(R.id.product_display_description);
        productQuantity = (TextView) findViewById(R.id.product_display_quantity);
        image = (ImageView) findViewById(R.id.product_display_image);
        numberButton.setOnClickListener ( new ElegantNumberButton.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                String[]tokens = productQuantity.getText ().toString ().split ( " " );
                if (Integer.valueOf ( numberButton.getNumber ()) > Integer.valueOf ( tokens[1] )) {
                    Toast.makeText ( productDetails.this, "Cannot select more than the quantity" , Toast.LENGTH_SHORT).show ();
                    addToCart.setEnabled(false);
                    numberButton.setEnabled (false);
                }

            }
        } );
        getProductDetails(productId);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart.setEnabled(true);
                addtoCart();
            }
        });
        viewCart.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( productDetails.this, CartActivity.class );
                startActivity ( intent );
            }
        } );
    }

    private void addtoCart() {
        final DatabaseReference cartList = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String, Object> map = new HashMap<>();
        map.put("pid", productId);
        map.put("pname", productName.getText().toString());
        map.put("price", productPrice.getText().toString());
        map.put("quantity", numberButton.getNumber());

        cartList.child("User view").child(Prevalent.currentOnlineUsers.getPhone()).child("ProductCollection").child(productId).updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            cartList.child("Admin view").child(Prevalent.currentOnlineUsers.getPhone())
                                    .child("ProductCollection").child(productId).updateChildren(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                final DatabaseReference list = FirebaseDatabase.getInstance().getReference().child("ProductCollection");
                                                list.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String[] tokens = productQuantity.getText ().toString ().split ( " " );
                                                        dataSnapshot.getRef().child("Quantity").setValue(Integer.valueOf ( tokens[1]) - Integer.valueOf ( numberButton.getNumber ()));

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                                Toast.makeText(productDetails.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(productDetails.this, ProductInfo.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }


    private void getProductDetails(final String productId) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("ProductCollection");
        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ProductCollection product = dataSnapshot.getValue(ProductCollection.class);

                    productName.setText(product.getName());
                    productDescription.setText ( "Description: "+product.getDescription ());
                    productPrice.setText("Price: "+String.valueOf(product.getPrice()));
                    Log.i("Image", "http://msitmp.herokuapp.com" + product.getProductPicUrl());
                    Picasso.with(productDetails.this).load("http://msitmp.herokuapp.com"+product.getProductPicUrl()).into(image);

                    productQuantity.setText("Quantity: "+String.valueOf(product.getQuantity()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
