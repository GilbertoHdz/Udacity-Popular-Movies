package mx.com.ghg.movies.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import mx.com.ghg.movies.R;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<MovieUi> _items;

    final private MovieItemClickListener mOnMovieItemClickListener;

    public MovieAdapter(MovieItemClickListener listener) {
        this.mOnMovieItemClickListener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.movie_item_layout;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieUi movieUi = _items.get(position);
        holder.bind(movieUi);
    }

    @Override
    public int getItemCount() {
        if (null == _items) return 0;
        return _items.size();
    }

    public void setMovieItems(ArrayList<MovieUi> moviesUi) {
        _items = moviesUi;
        notifyDataSetChanged();
    }

    public interface MovieItemClickListener {
        void onMovieItemClick(MovieUi movieUi);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView mMoviewItemImage;
        private final View _itemView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            _itemView = itemView;
            mMoviewItemImage = (ImageView) itemView.findViewById(R.id.movie_image_item);
            itemView.setOnClickListener(this);
        }

        public void bind(MovieUi movieUi) {

            Glide
                    .with(_itemView.getContext())
                    .load(movieUi.getImage())
                    .centerCrop()
                    // .placeholder(R.drawable.loading_spinner)
                    .into(mMoviewItemImage);
        }

        @Override
        public void onClick(View view) {
            if (mOnMovieItemClickListener == null) return;
            int position = getAdapterPosition();
            MovieUi movieUi = _items.get(position);
            mOnMovieItemClickListener.onMovieItemClick(movieUi);
        }
    }
}
