package com.example.gamejava.repository;

import androidx.lifecycle.ViewModelProviders;

import com.example.gamejava.AppExecutors;
import com.example.gamejava.model.entities.Message;
import com.example.gamejava.model.entities.User;
import com.example.gamejava.ui.login.LoginActivity;
import com.example.gamejava.ui.login.LoginViewModel;
import com.example.gamejava.ui.login.LoginViewModelFactory;
import com.example.gamejava.ui.main.MainViewModel;
import com.example.gamejava.ui.question.QuestionViewModel;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class SocketClient extends Thread {
    public static SocketClient instance;
    Socket socket;
    private ObjectOutputStream objectOutputStream;
    private LoginViewModel loginViewModel;
    private MainViewModel mainViewModel;
    private QuestionViewModel questionViewModel;
    public static User user;


    public static synchronized SocketClient getInstance() throws IOException {
        if (instance == null) {
            instance = new SocketClient();
        }
        return instance;
    }

    public SocketClient() throws IOException {
        try {
            this.socket = new Socket("172.20.10.13", 9000);
            // this.socket = new Socket("10.0.2.2", 9000);
            this.start();
        } catch (IOException e) {
            this.socket = null;
            throw e;
        }
    }

    public MainViewModel getMainViewModel() {
        return mainViewModel;
    }

    public void sendMessage(Message message) {
        message.setMe(SocketClient.user);
        AppExecutors.getInstance().networkIO().execute(() -> {
            try {
                if(objectOutputStream == null){
                    objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
                }
                objectOutputStream.writeObject(message);
            } catch (Exception ex) {
                ex.printStackTrace();
                closeAll();
            }
        });

    }

    public void login(Message message,LoginViewModel viewModel) {
        this.loginViewModel = viewModel;
        AppExecutors.getInstance().networkIO().execute(() -> {
            this.sendMessage(message);
        });

    }

    public void listUser(MainViewModel viewModel) {
        this.mainViewModel = viewModel;
        Message message = new Message(Message.TYPE.LIST_USER);
        AppExecutors.getInstance().networkIO().execute(() -> {
            this.sendMessage(message);
        });
    }

    public void invitePlay(MainViewModel viewModel,Message message) {
        this.mainViewModel = viewModel;
        AppExecutors.getInstance().networkIO().execute(() -> {
            this.sendMessage(message);
        });
    }

    public void nopBai(QuestionViewModel viewModel,Message message){
        message.setType(Message.TYPE.NOPBAI);
        this.questionViewModel = viewModel;
        this.sendMessage(message);
    }

    public void setQuestionViewModel(QuestionViewModel questionViewModel) {
        this.questionViewModel = questionViewModel;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(this.socket.getInputStream());
            while (!this.socket.isClosed()) {
                Message message = (Message) objectInputStream.readObject();
                handleMessage(message);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Retry connect");
            try{
                instance = null;
                SocketClient.getInstance();
                System.out.println("Retry connect success");
            }catch (Exception e){
                System.out.println("Retry connect fail");
            }


        } finally {
            closeAll();
        }
    }

    private void handleMessage(Message message){
        switch (message.getType()){
            case LOGIN:
                handleLoginRes(message);
                return;
            case LIST_USER:
                handleListUser(message);
                return;
            case INVITE:
                handleInvitePlay(message);
                return;
            case INVITE_ERROR:
                handleInvitePlayError(message);
                return;
            case INVITE_SUCCESS:
                handleInvitePlaySuccess(message);
                return;
            case ACCEPT_INVITE:
                handleAcceptInvite(message);
                return;
            case NOPBAI:
                handleNopBai(message);
                return;
        }
    }

    void handleNopBai(Message message){
        this.questionViewModel.handeNopBaiRes(message);
    }

    void handleAcceptInvite(Message message){
        this.mainViewModel.getMessage().postValue(message);
    }

    void handleLoginRes(Message message){
        if(message.getUser() == null){
            this.loginViewModel.getLoginStatus().postValue(LoginViewModel.LOGIN_STATUS.FAIL);
        }else{
            this.loginViewModel.getLoginStatus().postValue(LoginViewModel.LOGIN_STATUS.SUCCESS);
            SocketClient.user = message.getUser();
            this.loginViewModel.getUser().postValue(message.getUser());
        }
    }

    void handleListUser(Message message){
        mainViewModel.getList().postValue(message.getList());
        mainViewModel.getUser().postValue(message.getUser());
    }

    void handleInvitePlay(Message message){
        this.mainViewModel.getUserInvite().postValue(message.getUser());
    }

    void handleInvitePlayError(Message message){
        this.mainViewModel.getInviteResult().postValue(MainViewModel.RESULT_INVITE.FAIL);
    }

    void handleInvitePlaySuccess(Message message){
        this.mainViewModel.getInviteResult().postValue(MainViewModel.RESULT_INVITE.SUCCESS);
    }

    private void closeAll() {
        safeClose(this.socket);

    }

    private void safeClose(Closeable object) {
        try {
            object.close();
        } catch (Exception ex) {

        }
    }

}
