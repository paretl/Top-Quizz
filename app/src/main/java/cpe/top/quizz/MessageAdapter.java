package cpe.top.quizz;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

import cpe.top.quizz.beans.User;

/**
 * Created by Romain on 05/01/2017.
 */

public class MessageAdapter extends BaseAdapter {

    Context messageContext;
    ArrayList<Message> messageList;
    String user;
    private Resources resources;

    public MessageAdapter(Context context, ArrayList<Message> messages, String connectedUser) {
        messageList = messages;
        messageContext = context;
        user = connectedUser;
    }

    private static class MessageViewHolder {
        public TextView thumbnailImageView;
        public TextView senderView;
        public TextView bodyView;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageViewHolder holder;

        // if there is not already a view created for an item in the Message list.
        if (convertView == null) {
            LayoutInflater messageInflater = (LayoutInflater) messageContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            // create a view out of our `message.xml` file
            convertView = messageInflater.inflate(R.layout.activity_message, null);

            // create a MessageViewHolder
            holder = new MessageViewHolder();

            // set the holder's properties to elements in `message.xml`
            holder.thumbnailImageView = (TextView) convertView.findViewById(R.id.img_thumbnail);
            holder.senderView = (TextView) convertView.findViewById(R.id.message_sender);
            holder.bodyView = (TextView) convertView.findViewById(R.id.message_body);

            // assign the holder to the view we will return
            convertView.setTag(holder);
        } else {

            // otherwise fetch an already-created view holder
            holder = (MessageViewHolder) convertView.getTag();
        }

        // get the message from its position in the ArrayList
        Message message = (Message) getItem(position);

        // set the elements' contents
        holder.bodyView.setText(message.text);
        holder.senderView.setText(message.name);
        holder.thumbnailImageView.setText(String.valueOf((message.name).charAt(0)));

        String name = message.name.split(" \\(")[0];
        if (name.equals(user)){
            holder.thumbnailImageView.setBackgroundColor(Color.argb(255, 45, 45, 125));
        }else {
            holder.thumbnailImageView.setBackgroundColor(Color.argb(255, 150, 45, 125));
        }

        return convertView;
    }

    public void add(Message message){
        messageList.add(message);
        notifyDataSetChanged();
    }


}