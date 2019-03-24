package com.example.shoppingcart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class Confirm extends AppCompatActivity {
    private TextView total, display;
    Button submit;
    private String price = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_confirm );
        total = (TextView) findViewById ( R.id.total_price );
        display = (TextView) findViewById ( R.id.display );
        submit = findViewById ( R.id. submit);
        price = getIntent ().getStringExtra ( "TotalPrice" );
        display.setText ( "Rs: " + price );
        submit.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Toast.makeText ( Confirm.this, "Placed order succesfuuly", Toast.LENGTH_SHORT ).show ();
                Intent intent = new Intent ( Confirm.this, ProductInfo.class );
                startActivity ( intent );
                DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
                cartRef.removeValue ();
            }
        } );
    }
}
