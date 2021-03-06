package eu.applogic.onlinealb.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import eu.applogic.onlinealb.HelperClasses.CircleTransform;
import eu.applogic.onlinealb.Objects.RssFeedObject;
import eu.applogic.onlinealb.R;

/**
 * Created by makis on 4/4/2017.
 */

public class ChannelsAdapter extends RecyclerView.Adapter<ChannelsAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<RssFeedObject> channels;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView from;
        public ImageView imgProfile;
        public RelativeLayout iconFront;

        public MyViewHolder(View view) {
            super(view);
            from = (TextView) view.findViewById(R.id.from);
            iconFront = (RelativeLayout) view.findViewById(R.id.icon_front);
            imgProfile = (ImageView) view.findViewById(R.id.icon_profile);
        }
    }


    public ChannelsAdapter(Context mContext, ArrayList<RssFeedObject> channels) {
        this.mContext = mContext;
        this.channels = channels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        RssFeedObject channel = channels.get(position);

        // displaying text view data
        holder.from.setText(channel.getTitle());

        // display profile image
        applyProfilePicture(holder, channel);
    }

    private void applyProfilePicture(MyViewHolder holder, RssFeedObject channel) {
        if (!TextUtils.isEmpty(channel.getImageService())) {
            Glide.with(mContext).load(channel.getImageService())
                    .thumbnail(0.5f)
                    .crossFade()
                    .transform(new CircleTransform(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgProfile);
            holder.imgProfile.setColorFilter(null);
        }

        /*
        else {
            holder.imgProfile.setImageResource(R.drawable.bg_circle);
            holder.imgProfile.setColorFilter(channel.getColor());
            holder.iconText.setVisibility(View.VISIBLE);
        }
        */
    }

    public void setAdapterData(ArrayList<RssFeedObject> mData){
        this.channels = mData;
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }
}
