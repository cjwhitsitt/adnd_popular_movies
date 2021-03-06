package com.jaywhitsitt.popularmovies;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaywhitsitt.popularmovies.data.Video;

import java.util.List;

interface VideoOnClickHandler {
    void onClick(Video video);
}

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterViewHolder> {

    private List<Video> mVideos;
    final private VideoOnClickHandler mOnClickHandler;

    public VideoAdapter(VideoOnClickHandler onClickHandler) {
        mOnClickHandler = onClickHandler;
    }

    public void setData(List<Video> videos) {
        mVideos = videos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mVideos == null ? 0 : mVideos.size();
    }

    @NonNull
    @Override
    public VideoAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.video_list_item, viewGroup, false);
        return new VideoAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapterViewHolder viewHolder, int i) {
        String title;
        Video video = mVideos.get(i);
        if (video != null && video.title != null) {
            title = video.title;
        } else {
            title = "Unknown title";
        }
        viewHolder.mTitleView.setText(title);
    }

    public class VideoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView mTitleView;

        public VideoAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleView = itemView.findViewById(R.id.tv_video_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Video video = mVideos.get(position);
            mOnClickHandler.onClick(video);
        }
    }
}