package com.app.spott.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.spott.R;
import com.app.spott.models.Message;
import com.app.spott.models.User;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by aparnajain on 3/14/16.
 */
public class ChatListAdapter extends ArrayAdapter<Message> {
    private String selfUserId;
    private ChatAssetsProvider provider;
    private Context mContext;
    private User selfUser;
    private User otherUser;


    public interface ChatAssetsProvider {
        User selfUser();

        User otherUser();
    }

    public ChatListAdapter(Context context, List<Message> messages) {
        super(context, 0, messages);
        this.mContext = context;
        if (context instanceof ChatAssetsProvider) {
            this.provider = (ChatAssetsProvider) context;
        } else {
            throw new ClassCastException(context.toString() + " should be of type ChatAssetsProvider");
        }
        this.selfUser = this.provider.selfUser();
        this.selfUserId = this.selfUser.getObjectId();
        this.otherUser = this.provider.otherUser();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.item_chat, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.imageOther = (ImageView) convertView.findViewById(R.id.ivProfileOther);
            holder.imageMe = (ImageView) convertView.findViewById(R.id.ivProfileMe);
            holder.cvOtherMessage = (CardView) convertView.findViewById(R.id.cvOtherMessage);
            holder.cvMyMessage = (CardView) convertView.findViewById(R.id.cvMyMessage);
            holder.tvOtherMessage = (TextView) convertView.findViewById(R.id.tvOtherMessage);
            holder.tvMyMessage = (TextView) convertView.findViewById(R.id.tvMyMessage);
            convertView.setTag(holder);
        }
        final Message message = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        final boolean isMe = message.getUserId().equals(selfUserId);
        if (isMe) {
            holder.imageMe.setVisibility(View.VISIBLE);
            holder.cvMyMessage.setVisibility(View.VISIBLE);
            holder.tvMyMessage.setVisibility(View.VISIBLE);
            holder.tvMyMessage.setText(message.getBody());
            Glide.with(mContext).load(selfUser.getProfileImageUrl()).into(holder.imageMe);

            holder.imageOther.setVisibility(View.GONE);
            holder.cvOtherMessage.setVisibility(View.GONE);
            holder.tvOtherMessage.setVisibility(View.GONE);
        } else {
            holder.imageOther.setVisibility(View.VISIBLE);
            holder.cvOtherMessage.setVisibility(View.VISIBLE);
            holder.tvOtherMessage.setVisibility(View.VISIBLE);
            holder.tvOtherMessage.setText(message.getBody());
            Glide.with(mContext).load(otherUser.getProfileImageUrl()).into(holder.imageOther);

            holder.imageMe.setVisibility(View.GONE);
            holder.cvMyMessage.setVisibility(View.GONE);
            holder.tvMyMessage.setVisibility(View.GONE);
        }
        return convertView;
    }

    final class ViewHolder {
        public ImageView imageOther;
        public ImageView imageMe;
        public CardView cvOtherMessage;
        public CardView cvMyMessage;
        public TextView tvMyMessage;
        public TextView tvOtherMessage;
    }

}
