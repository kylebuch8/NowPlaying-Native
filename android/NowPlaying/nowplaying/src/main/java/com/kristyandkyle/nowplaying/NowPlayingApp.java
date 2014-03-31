package com.kristyandkyle.nowplaying;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;

/**
 * Created by kylebuchanan on 1/3/14.
 */
public class NowPlayingApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "xixYhHiMEB6sYZTW8jBgKFVXxi9d8EqIhablBDCo", "NqYM5VUyFZzFnHEY0uT5E4zwAMXQafKcpikYUtsI");

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisc(true)
                    .build())
                .build();
        ImageLoader.getInstance().init(config);
    }

}
