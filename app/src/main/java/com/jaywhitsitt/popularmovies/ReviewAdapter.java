package com.jaywhitsitt.popularmovies;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaywhitsitt.popularmovies.data.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> mReviews;

    public void setData(List<Review> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mReviews == null ? 0 : mReviews.size();
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
        Review review = mReviews.get(position);
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
