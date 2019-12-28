package com.example.gamejava.ui.main;

import android.content.Intent;
import android.os.Bundle;

import com.example.gamejava.model.entities.Message;
import com.example.gamejava.model.entities.User;
import com.example.gamejava.ui.adapter.UserAdapter;
import com.example.gamejava.ui.question.QuestionActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gamejava.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listview = findViewById(R.id.list_user);


        viewModel = ViewModelProviders.of(this)
                .get(MainViewModel.class);

        viewModel.fetchListUser();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        viewModel.getList().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                UserAdapter adapter = new UserAdapter(MainActivity.this,
                        R.layout.user_item, users,viewModel);
                listview.setAdapter(adapter);
            }
        });

        viewModel.getUserInvite().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null){
                    View parentLayout = findViewById(android.R.id.content);
                    showSnackbar(parentLayout,user);
                }
            }
        });

        viewModel.getInviteResult().observe(this, new Observer<MainViewModel.RESULT_INVITE>() {
            @Override
            public void onChanged(MainViewModel.RESULT_INVITE result_invite) {
                if(result_invite == MainViewModel.RESULT_INVITE.FAIL){
                    showToast("Người chơi không online hoặc đang bận");
                }else if(result_invite == MainViewModel.RESULT_INVITE.SUCCESS){
                    showToast("Gửi lời mời thành công");
                }
            }
        });

        viewModel.getMessage().observe(this, new Observer<Message>() {
            @Override
            public void onChanged(Message message) {
                if(message != null && message.getType() == Message.TYPE.ACCEPT_INVITE){
                    Intent messageIntent = new Intent(getApplication(), QuestionActivity.class);
                    startActivity(messageIntent);
                    finish();
                }
            }
        });
    }

    public void showSnackbar(View view, User user)
    {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, user.getName() +" muốn thách đấu bạn", Snackbar.LENGTH_LONG);
        // Set an action on it, and a handler
        snackbar.setAction("Chấp nhận", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.acceptInvite();
            }
        });

        snackbar.show();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
