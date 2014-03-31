package com.kristyandkyle.nowplaying;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends ActionBarActivity implements MovieContentFragment.OnFragmentInteractionListener {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private PageControls mPageControls;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private ArrayList<MovieParcelable> mMovies;
    private ProgressBar mProgressBar;
    private long mLastLoad = 0;

    private static final long REFRESH_LIMIT = 120; // 2 hours

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //getSupportActionBar().setDisplayShowHomeEnabled(false);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setLogo(R.drawable.logo);

        mPager = (ViewPager) findViewById(R.id.container);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mPager.setPageTransformer(true, new DepthPageTransformer());
        }

        mPageControls = (PageControls) findViewById(R.id.page_controls);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        if (savedInstanceState == null) {
            getMovies();
        } else {
            mMovies = (ArrayList) savedInstanceState.getParcelableArrayList("Movies");
            mLastLoad = (long) savedInstanceState.getLong("LastLoad");

            loadMovies(mMovies);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
         * if we have movies and it's been 15 minutes since we last
         * loaded them, load them again
         */
        long currentTime = System.currentTimeMillis();
        long difference = (mLastLoad == 0) ? 0 : (currentTime - mLastLoad) / 1000 / 60;

        if (difference >= REFRESH_LIMIT) {
            getMovies();
        }

        mLastLoad = currentTime;
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("Movies", mMovies);
        outState.putLong("LastLoad", mLastLoad);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_refresh) {
            getMovies();
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * simple pager adapter
     */
    private class MoviePagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mFragments;

        public MoviePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

    }

    @Override
    public void onFragmentInteraction() {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("movie", mMovies.get(mPager.getCurrentItem()));
        startActivity(intent);
    }

    private void getMovies() {
        mPager.setVisibility(View.INVISIBLE);
        mPageControls.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Movie");
        query.orderByDescending("updatedAt");
        query.orderByAscending("index");
        query.setLimit(16);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    mFragments = new ArrayList<Fragment>();
                    mMovies = new ArrayList<MovieParcelable>();

                    Iterator<ParseObject> iterator = parseObjects.iterator();
                    while (iterator.hasNext()) {
                        MovieParcelable movie = new MovieParcelable(iterator.next());
                        mMovies.add(movie);
                    }

                    loadMovies(mMovies);
                } else {
                    Toast.makeText(MainActivity.this, "Could not load movies", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadMovies(ArrayList<MovieParcelable> movies) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mPager.setVisibility(View.VISIBLE);
        mPageControls.setVisibility(View.VISIBLE);

        // clear out any saved movies
        Iterator<MovieParcelable> iterator = movies.iterator();

        while (iterator.hasNext()) {
            mFragments.add(MovieContentFragment.newInstance(iterator.next()));
        }

        mPagerAdapter = new MoviePagerAdapter(getSupportFragmentManager(), mFragments);
        mPager.setAdapter(mPagerAdapter);

        mPageControls.setViewPager(mPager);
        mPageControls.setCurrentItem(0);
    }
}
