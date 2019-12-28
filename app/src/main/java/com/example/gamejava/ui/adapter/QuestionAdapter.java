package com.example.gamejava.ui.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.gamejava.R;
import com.example.gamejava.model.entities.Message;
import com.example.gamejava.model.entities.Question;
import com.example.gamejava.model.entities.User;
import com.example.gamejava.ui.question.QuestionViewModel;

import java.util.List;

public class QuestionAdapter extends ArrayAdapter<Question> {
    private Activity context;
    private List<Question> objects;
    private  int tuan;
    private QuestionViewModel viewModel;

    public QuestionAdapter(Activity context, int resource, List<Question> objects, QuestionViewModel viewModel) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.viewModel = viewModel;
    }
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = this.context.getLayoutInflater().inflate(R.layout.question_item, null);
        TextView cauhoi = (TextView) row.findViewById(R.id.tenNguoiChoi);
        TextView stt = (TextView) row.findViewById(R.id.sttcauhoi);
        RadioButton a = (RadioButton) row.findViewById(R.id.radioButton1);
        RadioButton b = (RadioButton) row.findViewById(R.id.radioButton2);
        RadioButton c = (RadioButton) row.findViewById(R.id.radioButton3);
        RadioGroup radioSexGroup = (RadioGroup) row.findViewById(R.id.radiobuttongroup);
        Question awser = viewModel.getMessage().getValue().getMeAnwser().get(position);
        radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb=(RadioButton)row.findViewById(checkedId);
                if(rb == a){
                    awser.setAnwser("a");
                }else if(rb == b){
                    awser.setAnwser("b");
                }else if(rb == c){
                    awser.setAnwser("c");
                }

            }
        });

        final Question question = (Question) this.objects.get(position);
        cauhoi.setText(question.getTitle());
        stt.setText("Câu "+ (position+1));
        a.setText(question.getAreply());
        b.setText(question.getBreply());
        c.setText(question.getCreply());

        return row;
    }
}
