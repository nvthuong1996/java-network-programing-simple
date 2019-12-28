package com.example.gamejava.ui.question;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.gamejava.R;
import com.example.gamejava.model.entities.Message;
import com.example.gamejava.repository.SocketClient;
import com.example.gamejava.ui.adapter.QuestionAdapter;
import com.example.gamejava.ui.main.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class QuestionActivity extends AppCompatActivity {
    private QuestionViewModel viewModel;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        listview = findViewById(R.id.list_question);

        viewModel = ViewModelProviders.of(this)
                .get(QuestionViewModel.class);

        viewModel.setViewModel();

        try{
            viewModel.setMessage(SocketClient.getInstance().getMainViewModel().getMessage());
        }catch (Exception ex){
            ex.printStackTrace();
        }

        viewModel.getMessage().observe(this, new Observer<Message>() {
            @Override
            public void onChanged(Message message) {
                if(message.getQuestions() != null){
                    viewModel.getMessage().getValue().setMeAnwser(message.getQuestions());
                    QuestionAdapter adapter = new QuestionAdapter(QuestionActivity.this,
                            R.layout.question_item, message.getQuestions(),viewModel);
                    listview.setAdapter(adapter);
                }
            }
        });

        viewModel.getResult().observe(this, new Observer<QuestionViewModel.RESULT_PLAY>() {
            @Override
            public void onChanged(QuestionViewModel.RESULT_PLAY result_play) {
                if(result_play ==  QuestionViewModel.RESULT_PLAY.WIN){
                    new AlertDialog.Builder(QuestionActivity.this)
                            .setIcon(R.drawable.ic_check_circle_black_24dp)
                            .setTitle("You Win")
                            .setMessage("Bạn có muốn chơi tiếp không ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }else if(result_play ==  QuestionViewModel.RESULT_PLAY.LOST){
                    new AlertDialog.Builder(QuestionActivity.this)
                            .setIcon(R.drawable.ic_check_circle_black_24dp)
                            .setTitle("You LOST")
                            .setMessage("Bạn có muốn chơi tiếp không ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }else if(result_play ==  QuestionViewModel.RESULT_PLAY.HOA){
                    new AlertDialog.Builder(QuestionActivity.this)
                            .setIcon(R.drawable.ic_check_circle_black_24dp)
                            .setTitle("Hoà")
                            .setMessage("Bạn có muốn chơi tiếp không ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(QuestionActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có chắc muốn nộp bài ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.nopbai();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });


    }
}
