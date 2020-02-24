package mx.com.ghg.movies.ui;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ImageView;
import android.widget.TextView;

import mx.com.ghg.movies.R;

public class MovieDetailActivity extends AppCompatActivity {

    public static String ARG_MOVIE_UI = "arg.movie.ui.detail.activity";

    private ImageView _image;
    private TextView _title;
    private TextView _average;
    private TextView _release;
    private TextView _synopsis;

    private MovieUi movieUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intentDetail = getIntent();
        if (intentDetail != null) {
            if (intentDetail.hasExtra(ARG_MOVIE_UI)) {
                movieUi = (MovieUi) intentDetail.getSerializableExtra(ARG_MOVIE_UI);
            } else {
                throw new IllegalArgumentException("the movie data should'n be null");
            }
        }

        _image = (ImageView) findViewById(R.id.movie_detail_image);
        _title = (TextView) findViewById(R.id.movie_detail_title);
        _average = (TextView) findViewById(R.id.movie_detail_average_text);
        _release = (TextView) findViewById(R.id.movie_detail_date_text);
        _synopsis = (TextView) findViewById(R.id.movie_detail_overview_text);

        updateUiState();
    }

    private void updateUiState() {
        Glide
                .with(this)
                .load(movieUi.getImage())
                .centerCrop()
                .into(_image);

        _title.setText(movieUi.getTitle());
        _average.setText(movieUi.getRating().toString());
        _release.setText(movieUi.getReleaseDate());
        _synopsis.setText(movieUi.getSynopsis());
    }
}
