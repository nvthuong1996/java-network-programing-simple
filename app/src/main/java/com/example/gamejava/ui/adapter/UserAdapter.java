package com.example.gamejava.ui.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.example.gamejava.R;

import androidx.annotation.NonNull;

import com.example.gamejava.model.entities.Message;
import com.example.gamejava.model.entities.User;
import com.example.gamejava.ui.main.MainActivity;
import com.example.gamejava.ui.main.MainViewModel;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    private Activity context;
    private List<User> objects;
    private  int tuan;
    private MainViewModel viewModel;

    public UserAdapter(Activity context, int resource, List<User> objects,MainViewModel viewModel) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.viewModel = viewModel;
    }
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = this.context.getLayoutInflater().inflate(R.layout.user_item, null);
        TextView textTenNguoiChoi = (TextView) row.findViewById(R.id.tenNguoiChoi);
        TextView txtDiemSo = (TextView) row.findViewById(R.id.textDiemSo);
        TextView textTrangThai = (TextView) row.findViewById(R.id.txtTrangThai);
        Button button = (Button)row.findViewById(R.id.button);
        final User user = (User) this.objects.get(position);

        textTenNguoiChoi.setText(user.getName());
        txtDiemSo.setText("( " +user.getPoint() + " điểm )");
        textTrangThai.setText("Online");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                     new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Xác nhận")
                        .setMessage("Bạn muốn gửi lời mời thách đấu đến " + user.getName() + " ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Message message = new Message(Message.TYPE.INVITE);
                            message.setUser(user);
                            viewModel.invitePlay(message);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();


            }
        });


        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Dialog dialog = new Dialog(getContext());
//                dialog.setContentView(R.layout.tkb_detail);
//
//                TextView giangvienTxt = (TextView)dialog.findViewById(R.id.textView26);
//                giangvienTxt.setText(lichHocModel.getCBGV());
//
//                TextView nhomTxt = (TextView)dialog.findViewById(R.id.textView27);
//                nhomTxt.setText(lichHocModel.getTH() + "");
//
//                TextView sotinchiTxt = (TextView)dialog.findViewById(R.id.textView33);
//                sotinchiTxt.setText(lichHocModel.getSTC() + "");
//
//                TextView msmhTxt = (TextView)dialog.findViewById(R.id.textView34);
//                msmhTxt.setText(lichHocModel.getMaMH());
//
//                TextView tenmhTxt = (TextView)dialog.findViewById(R.id.textView35);
//                tenmhTxt.setText(lichHocModel.getTenMH());
//
//                dialog.show();

            }
        });

        return row;
    }
}
