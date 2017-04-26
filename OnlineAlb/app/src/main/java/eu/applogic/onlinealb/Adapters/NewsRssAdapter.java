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
import eu.applogic.onlinealb.Objects.RssFeedObject;
import eu.applogic.onlinealb.R;

/**
 * Created by makis on 4/5/2017.
 */

public class NewsRssAdapter extends RecyclerView.Adapter<NewsRssAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<NewsRssFeedObject> news;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView newsTitle;
        public TextView newsDate;
        public TextView newsBody;
        public ImageView newsImage;

        public MyViewHolder(View view) {
            super(view);
            newsTitle = (TextView) view.findViewById(R.id.news_title);
            newsDate = (TextView) view.findViewById(R.id.news_date);
            newsBody = (TextView) view.findViewById(R.id.news_body);
            newsImage = (ImageView) view.findViewById(R.id.news_icon);
        }
    }

    public NewsRssAdapter(Context mContext, ArrayList<NewsRssFeedObject> news) {
        this.mContext = mContext;
        this.news = news;
    }

    @Override
    public NewsRssAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_rss_row, parent, false);
        return new NewsRssAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NewsRssAdapter.MyViewHolder holder, final int position) {
        NewsRssFeedObject newsFeed = news.get(position);

        holder.newsTitle.setText(newsFeed.getTitle());
        holder.newsDate.setText(newsFeed.getPubDate());
        holder.newsBody.setText(newsFeed.getDescription());

        // display profile image
        applyProfilePicture(holder, newsFeed);
    }

    private void applyProfilePicture(NewsRssAdapter.MyViewHolder holder, NewsRssFeedObject newsFeed) {
        if (!TextUtils.isEmpty(newsFeed.getEnclosure())) {
            Glide.with(mContext).load(newsFeed.getEnclosure())
                    .thumbnail(0.5f)
                    .crossFade()
                    //.transform(new CircleTransform(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.newsImage);
            holder.newsImage.setColorFilter(null);
        }
    }

    public void setAdapterData(ArrayList<NewsRssFeedObject> mData){
        this.news = mData;
    }

    @Override
    public int getItemCount() {
        return news.size();
    }
}
