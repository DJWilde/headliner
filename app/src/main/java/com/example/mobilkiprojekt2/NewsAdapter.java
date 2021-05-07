package com.example.mobilkiprojekt2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilkiprojekt2.models.NewsItem;
import com.example.mobilkiprojekt2.models.NewsItems;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsItemHolder> {
//    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private Context context;
    private List<NewsItem> newsItemList = NewsItems.getInstance().getNewsItems();

    public NewsAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NewsItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.list_record, parent, false);
        return new NewsItemHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsItemHolder holder, int position) {
        NewsItem newsItem = newsItemList.get(position);
        holder.textTitle.setText(newsItem.getTitle());
        holder.textSource.setText(newsItem.getSource());
        holder.textPubDate.setText(newsItem.getPubDate());
    }

    @Override
    public int getItemCount() {
        return newsItemList.size();
    }

    public void setData(List<NewsItem> newsItems) {
        this.newsItemList = newsItems;
        notifyDataSetChanged();
    }

    class NewsItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        ImageView imageView;
        TextView textTitle;
        TextView textSource;
        TextView textPubDate;
        final NewsAdapter adapter;

        public NewsItemHolder(View view, NewsAdapter adapter) {
            super(view);
            this.adapter = adapter;
            this.textTitle = view.findViewById(R.id.textTitle);
            this.textSource = view.findViewById(R.id.textSource);
            this.textPubDate = view.findViewById(R.id.textPubDate);
            context = view.getContext();
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getLayoutPosition();
            Intent intent = new Intent(context, NewsItemActivity.class);
            intent.putExtra("NEWS_ITEM_ID", pos);
            context.startActivity(intent);
        }
    }
}
