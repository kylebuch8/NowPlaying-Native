package com.kristyandkyle.nowplaying;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.parse.ParseObject;

import java.io.Serializable;
import java.util.HashMap;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieContentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class MovieContentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MOVIE = "movie";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private MovieParcelable mMovie;
    private Boolean mDataSet = false;

    private OnFragmentInteractionListener mListener;

    private ImageView mPosterImageView;
    private ImageView mPosterBackgroundImageView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param movie Parameter 1.
     * @return A new instance of fragment MovieContentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieContentFragment newInstance(MovieParcelable movie) {
        MovieContentFragment fragment = new MovieContentFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    public MovieContentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovie = (MovieParcelable) getArguments().getParcelable(MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_content, container, false);

       Typeface robotoCondensed = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoCondensed-Bold.ttf");
        Typeface robotoThin = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");

        TextView movieTitleTextView = (TextView) rootView.findViewById(R.id.movie_title);
        movieTitleTextView.setTypeface(robotoCondensed);
        movieTitleTextView.setText(mMovie.getTitle());

        TextView movieRatingTextView = (TextView) rootView.findViewById(R.id.movie_rating);
        movieRatingTextView.setTypeface(robotoThin);
        movieRatingTextView.setText(mMovie.getRating());

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

        // set some click listeners
        View movieContent = rootView.findViewById(R.id.movie_content);
        movieContent.setOnClickListener(goToMovieDetails());
        mPosterImageView.setOnClickListener(goToMovieDetails());

        return rootView;
    }

    private View.OnClickListener goToMovieDetails() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed();
            }
        };
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction();
    }

}
