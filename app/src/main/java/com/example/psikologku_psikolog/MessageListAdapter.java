package com.example.psikologku_psikolog;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psikologku_psikolog.Chat.UserMessage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageListViewHolder> {
    private Context mContext;
    private List<UserMessage>  mList;
    private String imageUrl;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    MediaPlayer mediaPlayer = new MediaPlayer();
    private SharedPreferences sp;

    public MessageListAdapter(Context context , List<UserMessage> message , String imgUrl){
        this.mContext = context;
        this.mList = message;
        this.imageUrl = imgUrl;
    }

    @Override
    public int getItemViewType(int position) {
        sp = mContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String id = sp.getString("id","");
        if(mList.get(position).getSended().equals(id)){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }

    public class MessageListViewHolder extends RecyclerView.ViewHolder{
        public TextView message;
        public ImageView profile_image;

        public MessageListViewHolder(View itemView){
            super(itemView);
            message = itemView.findViewById(R.id.message);
            profile_image = itemView.findViewById(R.id.psikolok_profile);
        }
    }
    @NonNull
    @Override
    public MessageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT)
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_message_received,parent,false);
            return new MessageListAdapter.MessageListViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_message_sent,parent,false);
            return new MessageListAdapter.MessageListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageListViewHolder holder, int position) {
        final UserMessage message = mList.get(position);
        if(message.getMessage_type().equals("audio"))
        {
            holder.message.setText("Audio File");
        }
        else{
            holder.message.setText(message.getMessage());
        }
        holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(message.getMessage_type().equals("audio"))
               {

                    try{
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(message.getMessage());
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();
                            }
                        });

                        mediaPlayer.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
               }
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        stopMediaPlayer();
                    }
                });
            }
        });
        /*if(imageUrl.equals("default")){

        }*/
    }

    private void stopMediaPlayer()
    {
        if(mediaPlayer!=null)
        {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }
    /*private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;
        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }

        void bind(UserMessage message) {
            messageText.setText(message.getMessage());
            // Format the stored timestamp into a readable String using method.
            timeText.setText(Utils.formatDateTime(message.getCreatedAt()));
        }
    }*/



}
