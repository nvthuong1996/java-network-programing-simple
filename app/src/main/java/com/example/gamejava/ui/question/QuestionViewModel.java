package com.example.gamejava.ui.question;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gamejava.model.entities.Message;
import com.example.gamejava.model.entities.User;
import com.example.gamejava.repository.SocketClient;

import java.util.List;

public class QuestionViewModel extends ViewModel {
    private MutableLiveData<Message> message = new MutableLiveData<>();
    private MutableLiveData<RESULT_PLAY> result = new MutableLiveData<>();

    private int me;
    private int friend;
    public QuestionViewModel(){
        this.me = -1;
        this.friend = -1;
    }

    public void nopbai(){
        try{
            SocketClient.getInstance().nopBai(this,message.getValue());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void setViewModel(){
        try{
            SocketClient.getInstance().setQuestionViewModel(this);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public MutableLiveData<RESULT_PLAY> getResult() {
        return result;
    }

    public void handeNopBaiRes(Message message){
        if(message.getUser().getUsername() == this.message.getValue().getUser().getUsername()){
            // me
            this.me = message.getCountPass();
            if(this.friend != -1){
                // ket thuc
                if(this.me == 3 && this.friend < 3){
                    this.result.postValue(RESULT_PLAY.WIN);
                }else if(this.me < 3 && this.friend == 3){
                    this.result.postValue(RESULT_PLAY.LOST);
                }else{
                    this.result.postValue(RESULT_PLAY.HOA);
                }
            }
        }else{
            //friend
            this.friend = message.getCountPass();
            if(this.me != -1){
                // ket thuc
                if(this.me == 3 && this.friend < 3){
                    this.result.postValue(RESULT_PLAY.WIN);
                }else if(this.me < 3 && this.friend == 3){
                    this.result.postValue(RESULT_PLAY.LOST);
                }else{
                    this.result.postValue(RESULT_PLAY.HOA);
                }
            }
        }
    }

    public MutableLiveData<Message> getMessage() {
        return message;
    }
    public void setMessage(MutableLiveData<Message> message) {
        this.message = message;
    }

    public static enum RESULT_PLAY {
        WIN,
        LOST,
        HOA
    }

}
