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
        RecyclerView.Adapter<MovieDetailAdapter.MovieDetailViewHolder> {

    private ArrayList<MovieDetailUi> _items;

    final private VideoItemClickListener mOnVideoItemClickListener;

    public MovieDetailAdapter(VideoItemClickListener listener) {
        this.mOnVideoItemClickListener = listener;
    }

    @NonNull
    @Override
    public MovieDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.movie_detail_video_item_layout;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);
        return new MovieDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieDetailViewHolder holder, int position) {
        MovieDetailUi videoItem = _items.get(position);
        holder.bind(videoItem);
    }

    @Override
    public int getItemCount() {
        if (null == _items) return 0;
        return _items.size();
    }

    public void setVideoItems(ArrayList<MovieDetailUi> videos) {
        _items = videos;
        notifyDataSetChanged();
    }

    interface VideoItemClickListener {
        void onVideoItemClick(MovieDetailUi movieUI);
    }

    class MovieDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mMovieDetailVideoItemAction;
        private final View _itemView;

        public MovieDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            this._itemView = itemView;

            mMovieDetailVideoItemAction =
                    (TextView) itemView.findViewById(R.id.movieDetailVideoItemAction);

            itemView.setOnClickListener(this);
        }

        public void bind(MovieDetailUi videoItem) {
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

}
