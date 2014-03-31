package com.kristyandkyle.nowplaying;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.app.NavUtils;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MovieDetailActivity extends ActionBarActivity {

    private static final String MOVIE = "movie";
    private static MovieParcelable mMovie;
    private static ImageView mPosterImageView;
    private static ImageView mPosterBackgroundImageView;
    private static Button mPlayBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mMovie = getIntent().getParcelableExtra(MOVIE);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        //getSupportActionBar().setTitle(mMovie.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Back");
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

            // typefaces
            Typeface robotoCondensed = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoCondensed-Bold.ttf");
            Typeface robotoThin = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
            Typeface robotoLight = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");

            // title
            TextView titleTextView = (TextView) rootView.findViewById(R.id.movie_title);
            titleTextView.setTypeface(robotoCondensed);
            titleTextView.setText(mMovie.getTitle());

            // rating
            TextView ratingTextView = (TextView) rootView.findViewById(R.id.movie_rating);
            ratingTextView.setTypeface(robotoThin);
            ratingTextView.setText(mMovie.getRating());

            TextView mpaaRatingLengthTextView = (TextView) rootView.findViewById(R.id.movie_mpaa_rating_length);
            mpaaRatingLengthTextView.setTypeface(robotoCondensed);
            mpaaRatingLengthTextView.setText(mMovie.getMpaaRating() + ", " + mMovie.getDuration());

            // synopsis
            TextView synopsisLabelTextView = (TextView) rootView.findViewById(R.id.movie_synopsis_label);
            synopsisLabelTextView.setTypeface(robotoLight);

            TextView synopsisTextView = (TextView) rootView.findViewById(R.id.movie_synopsis);
            synopsisTextView.setText(mMovie.getSynopsis());

            // critics consensus
            TextView criticsConsensusLabelTextView = (TextView) rootView.findViewById(R.id.movie_critics_consensus_label);
            criticsConsensusLabelTextView.setTypeface(robotoLight);

            TextView criticsConsensusTextView = (TextView) rootView.findViewById(R.id.movie_critics_consensus);
            criticsConsensusTextView.setText(mMovie.getCriticsConsensus());

            // work through the cast
            TextView castLabelTextView = (TextView) rootView.findViewById(R.id.movie_cast_label);
            castLabelTextView.setTypeface(robotoLight);

            ViewGroup castContainer = (ViewGroup) rootView.findViewById(R.id.movie_cast_container);

            ArrayList<HashMap<String, Object>> cast = (ArrayList) mMovie.getCast();
            for (HashMap castMember : cast) {
                View castView = inflater.inflate(R.layout.cast_member, castContainer, false);

                // set the cast member name
                String castMemberName = castMember.get("name").toString();

                TextView castMemberNameTextView = (TextView) castView.findViewById(R.id.cast_member_name);
                castMemberNameTextView.setText(castMemberName);

                TextView castMemberCharacterNameTextView = (TextView) castView.findViewById(R.id.cast_member_character);

                // if we have characters, set the first one
                if (castMember.get("characters") != null) {
                    String castMemberCharacterName = (String) ((ArrayList) castMember.get("characters")).get(0);
                    castMemberCharacterNameTextView.setText(castMemberCharacterName);
                } else {
                    castMemberCharacterNameTextView.setVisibility(View.GONE);
                }

                castContainer.addView(castView);
            }

            // set the poster images
            mPosterImageView = (ImageView) rootView.findViewById(R.id.movie_poster);
            mPosterBackgroundImageView = (ImageView) rootView.findViewById(R.id.movie_background);

            ImageLoader.getInstance().loadImage(mMovie.getPosterUrl(), new SimpleImageLoadingListener() {

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    mPosterImageView.setImageBitmap(loadedImage);

                    Bitmap blurredBackground = Blur.fastblur(getActivity(), loadedImage, 5);
                    mPosterBackgroundImageView.setImageBitmap(blurredBackground);
                }

            });

            // set the btn up
            mPlayBtn = (Button) rootView.findViewById(R.id.movie_play_btn);

            if (mMovie.getYoutubeId() == null) {
                mPlayBtn.setVisibility(View.INVISIBLE);
            } else {
                mPlayBtn.setOnClickListener(clickHandler());
                mPosterImageView.setOnClickListener(clickHandler());
            }

            return rootView;
        }

        private View.OnClickListener clickHandler() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(getActivity()).equals(YouTubeInitializationResult.SUCCESS)) {
                        Intent intent = YouTubeStandalonePlayer.createVideoIntent(getActivity(), "AIzaSyCIt90HLaxbIlCJh6fbC1GgSNwbdr5QiEk", mMovie.getYoutubeId(), 0, true, false);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "The YouTube application needs to be installed to play trailers", Toast.LENGTH_LONG).show();
                    }
                }
            };
        }
    }

}
