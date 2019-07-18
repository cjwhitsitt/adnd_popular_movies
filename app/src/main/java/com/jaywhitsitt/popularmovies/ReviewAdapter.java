package com.jaywhitsitt.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaywhitsitt.popularmovies.data.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Review[] mReviews;

    public void setData(Review[] reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mReviews == null ? 0 : mReviews.length;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.review_list_item, viewGroup, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = mReviews[position];
        holder.mAuthorTextView.setText(review.author);
        holder.mContentTextView.setText(review.content);
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        final TextView mAuthorTextView;
        final TextView mContentTextView;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            mAuthorTextView = itemView.findViewById(R.id.tv_review_author_name);
            mContentTextView = itemView.findViewById(R.id.tv_review_content);
        }
    }

}
