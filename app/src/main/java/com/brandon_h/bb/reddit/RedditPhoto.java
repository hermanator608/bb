package com.brandon_h.bb.reddit;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.brandon_h.bb.Constants.CHAT_SERVER_URL;

public class RedditPhoto implements Parcelable {

    private String mUrl;
    private String mTitle;

    public RedditPhoto(String url, String title) {
        mUrl = url;
        mTitle = title;
    }

    protected RedditPhoto(Parcel in) {
        mUrl = in.readString();
        mTitle = in.readString();
    }

    public static final Creator<RedditPhoto> CREATOR = new Creator<RedditPhoto>() {
        @Override
        public RedditPhoto createFromParcel(Parcel in) {
            return new RedditPhoto(in);
        }

        @Override
        public RedditPhoto[] newArray(int size) {
            return new RedditPhoto[size];
        }
    };

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    private void loadImages(JSONObject imagesData) {
        try {
            JSONArray images = imagesData.getJSONArray("posts");

            for(int i = 0; i < images.length(); i++){
                JSONObject image = images.getJSONObject(i);
                String imageUrl = image.getString("imageUrl");
                String title = image.getString("title");


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getSpacePhotos(final VolleyCallback callback) {

//        return new RedditPhoto[]{
//                new RedditPhoto("http://i.imgur.com/zuG2bGQ.jpg", "Galaxy"),
//                new RedditPhoto("http://i.imgur.com/ovr0NAF.jpg", "Space Shuttle"),
//                new RedditPhoto("http://i.imgur.com/n6RfJX2.jpg", "Galaxy Orion"),
//                new RedditPhoto("http://i.imgur.com/qpr5LR2.jpg", "Earth"),
//                new RedditPhoto("http://i.imgur.com/pSHXfu5.jpg", "Astronaut"),
//                new RedditPhoto("http://i.imgur.com/3wQcZeY.jpg", "Satellite"),
//        };

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, CHAT_SERVER_URL + "/reddit/aww", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(convertRedditPhotoArray(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        RedditCuteFragment.requestQueue.add(stringRequest);
    }

    private static ArrayList<RedditPhoto> convertRedditPhotoArray(JSONObject jsonObject) {
        ArrayList<RedditPhoto> photoArray = new ArrayList<>();
        JSONArray imageArray;

        try {
            imageArray = jsonObject.getJSONArray("posts");
        } catch (JSONException e) {
            e.printStackTrace();
            return photoArray;
        }

        for (int i = 0; i < imageArray.length(); i++) {
            try {

                JSONObject current = imageArray.getJSONObject(i);
                RedditPhoto tempPhoto = new RedditPhoto(current.getString("imageUrl"), current.getString("title"));
                photoArray.add(tempPhoto);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return photoArray;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mUrl);
        parcel.writeString(mTitle);
    }

    public interface VolleyCallback{
        void onSuccess(ArrayList<RedditPhoto> result);
    }
}