package mx.com.ghg.movies.ui.moviedetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import mx.com.ghg.movies.R;

public class MovieDetailAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_VIDEO = 0;
    private static final int VIEW_TYPE_REVIEW = 1;
    private static final int VIEW_TYPE_SEPARATOR_TITLE = 2;

    private ArrayList<MovieDetailUi> _items;

    final private VideoItemClickListener mOnVideoItemClickListener;

    public MovieDetailAdapter(VideoItemClickListener listener) {
        this.mOnVideoItemClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == VIEW_TYPE_VIDEO) {

            int layoutId = R.layout.movie_detail_video_item_layout;
            View view = inflater.inflate(layoutId, parent, false);

            return new VideoViewHolder(view);
        } else if (viewType == VIEW_TYPE_REVIEW) {

            int layoutId = R.layout.movie_detail_review_item_layout;
            View view = inflater.inflate(layoutId, parent, false);

            return new ReviewViewHolder(view);
        } else if (viewType == VIEW_TYPE_SEPARATOR_TITLE) {

            int layoutId = R.layout.movie_detail_empty_item_layout;
            View view = inflater.inflate(layoutId, parent, false);

            return new EmptyViewHolder(view);
        } else {
            throw new IllegalStateException("");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MovieDetailUi videoItem = _items.get(position);
        if (_items.get(position) instanceof VideoUi) {
            ((VideoViewHolder) holder).bind((VideoUi) videoItem);
        } else if (_items.get(position) instanceof ReviewUi) {
            ((ReviewViewHolder) holder).bind((ReviewUi) videoItem);
        } else if (_items.get(position) instanceof EmptyDivider) {
            ((EmptyViewHolder) holder).bind((EmptyDivider) videoItem);
        }
    }

    @Override
    public int getItemCount() {
        if (null == _items) return 0;
        return _items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (_items.get(position) instanceof VideoUi) {
            return VIEW_TYPE_VIDEO;
        } else if (_items.get(position) instanceof ReviewUi) {
            return VIEW_TYPE_REVIEW;
        } else if (_items.get(position) instanceof EmptyDivider) {
            return VIEW_TYPE_SEPARATOR_TITLE;
        }
        throw new IllegalStateException("should return some value in position" + position);
    }

    public void setVideoItems(ArrayList<MovieDetailUi> videos) {
        _items = videos;
        _items.add(0, new EmptyDivider("Trailers:"));
        notifyDataSetChanged();
    }

    public void addReviewItems(ArrayList<MovieDetailUi> reviews) {
        _items.add(new EmptyDivider("Reviews:"));
        for (int i = 0; i < reviews.size(); i++) {
            _items.add(reviews.get(i));
            notifyItemInserted(_items.size() - 1);
        }
    }

    interface VideoItemClickListener {
        void onVideoItemClick(MovieDetailUi movieUI);
    }

    /**
     * Video View Holder
     */
    private class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mMovieDetailVideoItemAction;
        private final View _itemView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            this._itemView = itemView;

            mMovieDetailVideoItemAction =
                    (TextView) itemView.findViewById(R.id.movieDetailVideoItemAction);

            itemView.setOnClickListener(this);
        }

        public void bind(VideoUi videoItem) {
            mMovieDetailVideoItemAction.setText(videoItem.getName());
        }

        @Override
        public void onClick(View v) {
            if (null == mOnVideoItemClickListener) return;
            int position = getAdapterPosition();
            MovieDetailUi videoItem = _items.get(position);
            mOnVideoItemClickListener.onVideoItemClick(videoItem);
        }
    }

    /**
     * Review View Holder
     */
    private class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mAuthorText;
        private final TextView mContentText;

        private final View _itemView;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            this._itemView = itemView;

            mAuthorText =
                    (TextView) itemView.findViewById(R.id.movieDetailRewardItemAuthor);
            mContentText =
                    (TextView) itemView.findViewById(R.id.movieDetailRewardItemContent);

            itemView.setOnClickListener(this);
        }

        public void bind(ReviewUi videoItem) {
            mAuthorText.setText(videoItem.getAuthor());
            mContentText.setText(videoItem.getContent());
        }

        @Override
        public void onClick(View v) {
            if (null == mOnVideoItemClickListener) return;
            int position = getAdapterPosition();
            MovieDetailUi videoItem = _items.get(position);
            mOnVideoItemClickListener.onVideoItemClick(videoItem);
        }
    }

    /**
     * Empty separator View Holder
     */
    private class EmptyViewHolder extends RecyclerView.ViewHolder {

        private TextView _emptyText;

        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            _emptyText = (TextView) itemView.findViewById(R.id.movieDetailEmptyItemText);
        }

        public void bind(EmptyDivider divider) {
            _emptyText.setText(divider.getId());
        }
    }
}
