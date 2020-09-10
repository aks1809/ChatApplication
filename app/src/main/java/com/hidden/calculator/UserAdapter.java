package com.hidden.calculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<UserList> implements View.OnClickListener {
    Context mContext;

    private static class ViewHolder{
        TextView uName;
        ImageView imageView;
    }

    public UserAdapter(ArrayList<UserList> data, Context context){
        super(context, R.layout.userlist, data);
        this.mContext = context;
    }

    @Override
    public void onClick(View view) {
        int position = (Integer) view.getTag();
        Object object = getItem(position);
        UserList singleUser = (UserList) object;

        // singleUser's info should be passed in intent
        if (view.getId() == R.id.displayImage) {
            Toast.makeText(mContext, "Image Clicked", Toast.LENGTH_LONG).show();
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserList data = getItem(position);
        ViewHolder viewHolder;

        final View result;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.userlist,parent,false);
            viewHolder.uName = (TextView) convertView.findViewById(R.id.username);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.displayImage);

            result = convertView;
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.uName.setText(data.getUser());
        viewHolder.imageView.setImageResource(R.drawable.profilepicture);

        viewHolder.imageView.setOnClickListener(this);
        viewHolder.imageView.setTag(position);

        return convertView;
    }
}
