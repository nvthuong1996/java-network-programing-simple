package com.example.gamejava.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;


import com.example.gamejava.R;
import com.example.gamejava.model.entities.Account;
import com.example.gamejava.model.entities.Message;
import com.example.gamejava.model.entities.User;
import com.example.gamejava.repository.SocketClient;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<LOGIN_STATUS> loginStatus = new MutableLiveData<>();

    public static  enum LOGIN_STATUS {
        NONE,
        SUCCESS,
        FAIL,
        PENDING
    }

    LoginViewModel(){
        loginStatus.postValue(LOGIN_STATUS.NONE);
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Account acc = new Account(username,password);
        Message mess = new Message(Message.TYPE.LOGIN);
        mess.setAccount(acc);

        loginStatus.postValue(LOGIN_STATUS.PENDING);

        try{
            SocketClient.getInstance().login(mess,this);
        }catch (Exception ex){
            ex.printStackTrace();
            loginStatus.postValue(LOGIN_STATUS.FAIL);
        }
    }

    public MutableLiveData<User> getUser() {
        return user;
    }

    public MutableLiveData<LOGIN_STATUS> getLoginStatus() {
        return loginStatus;
    }

    public void setUser(MutableLiveData<User> user) {
        this.user = user;
    }

    public void setLoginStatus(MutableLiveData<LOGIN_STATUS> loginStatus) {
        this.loginStatus = loginStatus;
    }
}
