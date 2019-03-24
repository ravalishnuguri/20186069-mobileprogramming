package com.example.todonew;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class todolistActivity extends AppCompatActivity {
    public int counter;
    TextView text;
    Button button, timerbutton;
    TextView textView, timertextview;
    CountDownTimer countdowntimer;

    DatabaseHelper dbHelper;
    ArrayAdapter<String> myAdapter;
    ListView listTask;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        dbHelper = new DatabaseHelper(this);

        listTask = (ListView) findViewById(R.id.listTask);

        loadTaskList();
        text = findViewById(R.id.taskTitle);
        button = findViewById(R.id.btnPending);
        view = this.getWindow().getDecorView();
//        timerbutton = (Button)findViewById(R.id.button1);
//        timertextview = (TextView)findViewById(R.id.textView1);
    }
    private void loadTaskList() {
        String[] todo = {"basic todo"};
        ArrayList<String> taskList = dbHelper.getTaskList();
        if(myAdapter==null){
            myAdapter = new ArrayAdapter<String>(this,R.layout.rowlayout,R.id.taskTitle,taskList);
            listTask.setAdapter(myAdapter);
        }
        else{
            myAdapter.clear();
            myAdapter.addAll(taskList);
            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        //Change menu icon color
        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionAddTask:
                final EditText taskEditText = new EditText(this);
                final EditText taskEditText2 = new EditText(this);

                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add New Task")
                        .setMessage("What do you want to do next?")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String task = String.valueOf(taskEditText.getText());
                                dbHelper.insertNewTask(task);
                                loadTaskList();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create();
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteTask(View view){
        View parent = (View)view.getParent();
        TextView taskTextView = (TextView)parent.findViewById(R.id.taskTitle);
        Log.e("String", (String) taskTextView.getText());
        String task = String.valueOf(taskTextView.getText());
        dbHelper.deleteTask(task);
        loadTaskList();
    }

    public void goGray(View view) {
        View parent = (View)view.getParent();
        TextView taskTextView = (TextView)parent.findViewById(R.id.taskTitle);
//        view.setBackgroundResource(R.color.Gray);
        taskTextView.setTextColor(Color.parseColor("#ff0000"));
    }

    public void doneTask(View view) {
        View parent = (View)view.getParent();
        TextView taskTextView = (TextView)parent.findViewById(R.id.taskTitle);
        if(!taskTextView.getPaint().isStrikeThruText()) {
            taskTextView.setPaintFlags(taskTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            taskTextView.setPaintFlags(taskTextView.getPaintFlags() & (Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
//    public void startTimer() {
//        countdowntimer = new CountDownTimerClass(10000, 1000);
//        countdowntimer.start();
//    }
//    public class CountDownTimerClass extends CountDownTimer {
//        public CountDownTimerClass(long millisInFuture, long countDownInterval) {
//            super(millisInFuture, countDownInterval);
//        }
//        @Override
//        public void onTick(long millisUntilFinished) {
//            int progress = (int) (millisUntilFinished/1000);
//            timertextview.setText(Integer.toString(progress));
//        }
//        @Override
//        public void onFinish() {
//            timertextview.setText(" Count Down Finish ");
//        }
//    }
}
