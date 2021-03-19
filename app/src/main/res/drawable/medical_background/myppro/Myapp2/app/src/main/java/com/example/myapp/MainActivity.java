package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView question1;
    private Button show_true;
    private Button show_false;
    private Button show_c;
    private Button show_d;
    private ImageButton nextbutton;
    private ImageButton prevbutton;
    private int currentindex=0;

    private questions [] questionbank = new questions[]{
            new questions(R.string.question1,"A"),
            new questions(R.string.question2,"B"),
            new questions(R.string.question3,"C"),
            new questions(R.string.question4,"D")
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        question1=findViewById(R.id.text);
        show_false=findViewById(R.id.showfalse);
        show_true=findViewById(R.id.showtrue);
        nextbutton=findViewById(R.id.shownext);
        prevbutton=findViewById(R.id.showprev);
        show_c=findViewById(R.id.showc);
        show_d=findViewById(R.id.showd);

        show_false.setOnClickListener(this);
        show_true.setOnClickListener(this);
        show_c.setOnClickListener(this);
        show_d.setOnClickListener(this);
        nextbutton.setOnClickListener(this);
        prevbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showfalse:
                checkanswer("B");
                break;
            case R.id.showtrue:
                checkanswer("A");
                break;
            case R.id.showc:
                checkanswer("C");
                break;
            case R.id.showd:
                checkanswer("D");
                break;

            case R.id.shownext:
                currentindex=(currentindex+1)%questionbank.length;
                question1.setText(questionbank[currentindex].getAnswerResid());
                Log.d("position", "onClick: "+currentindex);
                break;
            case R.id.showprev:
                if(currentindex==0){
                    currentindex=(questionbank.length);
                }
                currentindex=currentindex-1;
                question1.setText(questionbank[currentindex].getAnswerResid());

        }

    }private void checkanswer(String userChoosecorrect){
        String rightanswer=questionbank[currentindex].getAnswertrue();
        if(userChoosecorrect==rightanswer){
            Toast.makeText(MainActivity.this,R.string.for_correct,Toast.LENGTH_SHORT).show();
    }else{
            Toast.makeText(MainActivity.this,R.string.for_wrong,Toast.LENGTH_SHORT).show();
        }
}}
