package com.jaywhitsitt.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaywhitsitt.popularmovies.data.Video;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterViewHolder> {

    private Video[] mVideos;

    public void setData(Video[] videos) {
        mVideos = videos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mVideos == null ? 0 : mVideos.length;
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
        Video video = mVideos[i];
        if (video != null && video.title != null) {
            title = video.title;
        } else {
            title = "Unknown title";
        }
        viewHolder.mTitleView.setText(title);
    }

    public static class VideoAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView mTitleView;

        public VideoAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleView = itemView.findViewById(R.id.tv_video_title);
        }

    }
}