package com.kristyandkyle.nowplaying;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kylebuchanan on 1/25/14.
 */
public class MovieParcelable implements Parcelable {

    private String title;
    private String synopsis;
    private String mpaaRating;
    private String criticsConsensus;
    private String posterUrl;
    private String rating;
    private String duration;
    private String youtubeId;
    private ArrayList cast;

    public MovieParcelable(ParseObject movie) {
        title = movie.getString("title");
        synopsis = movie.getString("synopsis");
        mpaaRating = movie.getString("mpaa_rating");
        criticsConsensus = movie.getString("critics_consensus");
        duration = calculateDuration(movie.getInt("runtime"));
        youtubeId = movie.getString("youtubeId");
        cast = (ArrayList<HashMap<String, Object>>) movie.get("abridged_cast");

        JSONObject posters = movie.getJSONObject("posters");
        JSONObject ratings = movie.getJSONObject("ratings");

        try {
            posterUrl = posters.getString("detailed");
            rating = ratings.getString("critics_score") + "%";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private MovieParcelable(Parcel in) {
        this.setTitle(in.readString());
        this.setSynopsis(in.readString());
        this.setMpaaRating(in.readString());
        this.setCriticsConsensus(in.readString());
        this.setPosterUrl(in.readString());
        this.setRating(in.readString());
        this.setDuration(in.readString());
        this.setYoutubeId(in.readString());
        this.setCast(in.readArrayList(ArrayList.class.getClassLoader()));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getMpaaRating() {
        return mpaaRating;
    }

    public void setMpaaRating(String mpaaRating) {
        this.mpaaRating = mpaaRating;
    }

    public String getCriticsConsensus() {
        return criticsConsensus;
    }

    public void setCriticsConsensus(String criticsConsensus) {
        this.criticsConsensus = criticsConsensus;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public ArrayList getCast() {
        return cast;
    }

    public void setCast(ArrayList cast) {
        this.cast = cast;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getTitle());
        dest.writeString(getSynopsis());
        dest.writeString(getMpaaRating());
        dest.writeString(getCriticsConsensus());
        dest.writeString(getPosterUrl());
        dest.writeString(getRating());
        dest.writeString(getDuration());
        dest.writeString(getYoutubeId());
        dest.writeList(getCast());
    }

    public static final Creator<MovieParcelable> CREATOR = new Creator<MovieParcelable>() {

        @Override
        public MovieParcelable createFromParcel(Parcel source) {
            return new MovieParcelable(source);
        }

        @Override
        public MovieParcelable[] newArray(int size) {
            return new MovieParcelable[size];
        }
    };

    private String calculateDuration(int runTime) {
        String hours = String.valueOf(runTime / 60);
        String minutes = String.valueOf(runTime % 60);
        String duration = hours + "h " + minutes + "m";

        return duration;
    }
}
