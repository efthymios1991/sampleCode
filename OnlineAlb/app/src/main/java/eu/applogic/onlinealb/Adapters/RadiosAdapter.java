package eu.applogic.onlinealb.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import eu.applogic.onlinealb.HelperClasses.CircleTransform;
import eu.applogic.onlinealb.Objects.NewsRssFeedObject;
import eu.applogic.onlinealb.Objects.RadioStationObject;
import eu.applogic.onlinealb.R;

/**
 * Created by makis on 4/6/2017.
 */

public class RadiosAdapter extends RecyclerView.Adapter<RadiosAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<RadioStationObject> radios;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView radioTitle;
        public ImageView radioIcon;

        public MyViewHolder(View view) {
            super(view);
            radioTitle = (TextView) view.findViewById(R.id.from);
            radioIcon = (ImageView) view.findViewById(R.id.icon_profile);
        }
    }

    public RadiosAdapter(Context mContext, ArrayList<RadioStationObject> radios) {
        this.mContext = mContext;
        this.radios = radios;
    }

    @Override
    public RadiosAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_list_row, parent, false);
        return new RadiosAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RadiosAdapter.MyViewHolder holder, final int position) {
        RadioStationObject radio = radios.get(position);
        holder.radioTitle.setText(radio.getName());
        applyProfilePicture(holder, radio);
    }

    public static String getStationImage(String imageName) {
        return "http://mobapplications.net/logos_2016/" + imageName + ".png";
    }

    private void applyProfilePicture(RadiosAdapter.MyViewHolder holder, RadioStationObject radio) {
        if (!TextUtils.isEmpty(radio.getImage())) {
            Glide.with(mContext).load(getStationImage(radio.getImage()))
                    .thumbnail(0.5f)
                    .crossFade()
                    .transform(new CircleTransform(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.radioIcon);
            holder.radioIcon.setColorFilter(null);
        }
    }

    public void setAdapterData(ArrayList<RadioStationObject> mData){
        this.radios = mData;
    }

    @Override
    public int getItemCount() {
        return radios.size();
    }
}
