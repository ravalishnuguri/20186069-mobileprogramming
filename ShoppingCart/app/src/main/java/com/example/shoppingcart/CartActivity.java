package com.example.shoppingcart;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingcart.Model.Cart;
import com.example.shoppingcart.Prevalent.Prevalent;
import com.example.shoppingcart.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button btn;
    private TextView totalAmt;
    private int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        btn = (Button) findViewById(R.id.next_btn);
        totalAmt = (TextView) findViewById(R.id.cart_price);

        btn.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( CartActivity.this, Confirm.class);
                intent.putExtra ( "TotalPrice" , String.valueOf ( totalPrice ));
                startActivity ( intent );
                finish ();
            }
        } );

    }


    @Override
    protected void onStart() {
//        System.out.println("in method");
        super.onStart();

        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartRef.child("User view").child(Prevalent.currentOnlineUsers.getPhone()).child("ProductCollection"), Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
//                        System.out.println("in data");

                        holder.productName.setText(model.getPname());
//                        System.out.println("in cart ");
                        holder.productPrice.setText(model.getPrice());
                        holder.productQuantity.setText("Quantity : "+ model.getQuantity());
                        String[] tokens = model.getPrice ().split ( " " );

                        int product = ((Integer.valueOf ( tokens[1] ))) * Integer.valueOf ( model.getQuantity () );
                        totalPrice = totalPrice + product;
                        holder.itemView.setOnClickListener ( new View.OnClickListener ( ) {
                            @Override
                            public void onClick(View v) {
                                cartRef.child ( "User view" )
                                        .child ( Prevalent.currentOnlineUsers.getPhone () )
                                        .child ( "ProductCollection" )
                                        .child ( model.getPid () )
                                        .removeValue ()
                                        .addOnCompleteListener ( new OnCompleteListener<Void> ( ) {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful ()) {
                                                    Toast.makeText ( CartActivity.this, "Deleted", Toast.LENGTH_SHORT ).show ();
                                                }
                                            }
                                        } );
                            }
                        } );

                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout, parent, false);
                        CartViewHolder holder = new CartViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
