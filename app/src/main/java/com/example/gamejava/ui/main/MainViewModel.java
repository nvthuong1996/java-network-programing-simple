package com.example.gamejava.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gamejava.model.entities.Account;
import com.example.gamejava.model.entities.Message;
import com.example.gamejava.model.entities.User;
import com.example.gamejava.repository.SocketClient;

import java.util.List;

public class MainViewModel extends ViewModel {

    private MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<List<User>> list = new MutableLiveData<>();
    private MutableLiveData<User> userInvite = new MutableLiveData<>();
    private MutableLiveData<RESULT_INVITE> inviteResult = new MutableLiveData<>();

    private MutableLiveData<Message> message = new MutableLiveData<>();

    public MainViewModel(){}

    public void fetchListUser(){
        try{
            SocketClient.getInstance().listUser(this);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void acceptInvite(){
        Message send = new Message(Message.TYPE.ACCEPT_INVITE);
        send.setUser(userInvite.getValue());
        try{
            SocketClient.getInstance().sendMessage(send);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void invitePlay(Message message){
        try{
            SocketClient.getInstance().invitePlay(this,message);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public MutableLiveData<List<User>> getList() {
        return list;
    }

    public void setList(MutableLiveData<List<User>> list) {
        this.list = list;
    }

    public MutableLiveData<User> getUser() {
        return user;
    }

    public void setUser(MutableLiveData<User> user) {
        this.user = user;
    }

    public MutableLiveData<User> getUserInvite() {
        return userInvite;
    }

    public void setUserInvite(MutableLiveData<User> userInvite) {
        this.userInvite = userInvite;
    }

    public MutableLiveData<RESULT_INVITE> getInviteResult() {
        return inviteResult;
    }

    public void setInviteResult(MutableLiveData<RESULT_INVITE> inviteResult) {
        this.inviteResult = inviteResult;
    }

    public MutableLiveData<Message> getMessage() {
        return message;
    }

    public void setMessage(MutableLiveData<Message> message) {
        this.message = message;
    }

    public static enum RESULT_INVITE {
        SUCCESS,
        FAIL
    }
}
