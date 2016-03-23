package com.app.spott.adapters;

import android.content.Context;
import android.view.Gravity;
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
            holder.body = (TextView) convertView.findViewById(R.id.tvBody);
            convertView.setTag(holder);
        }
        final Message message = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        final boolean isMe = message.getUserId().equals(selfUserId);
        if (isMe) {
            holder.imageMe.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(selfUser.getProfileImageUrl()).into(holder.imageMe);
            holder.imageOther.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        } else {
            holder.imageOther.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(otherUser.getProfileImageUrl()).into(holder.imageOther);
            holder.imageMe.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        }
//        final ImageView profileView = isMe ? holder.imageMe : holder.imageOther;
//        Glide.with(getContext()).load(getProfileUrl(message.getUserId())).into(profileView);
        holder.body.setText(message.getBody());
        return convertView;
    }

    final class ViewHolder {
        public ImageView imageOther;
        public ImageView imageMe;
        public TextView body;
    }

}
