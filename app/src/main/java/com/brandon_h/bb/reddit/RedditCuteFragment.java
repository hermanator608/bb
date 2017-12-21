package com.brandon_h.bb.reddit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.brandon_h.bb.R;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class RedditCuteFragment extends Fragment {
    private TextView mTextView;
    public static RequestQueue requestQueue;

    public RedditCuteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RedditCuteFragment.
     */
    public static RedditCuteFragment newInstance() {
        return new RedditCuteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reddit_cute, container, false);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        final RecyclerView recyclerView = rootView.findViewById(R.id.rv_images);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        RedditPhoto.getSpacePhotos(new RedditPhoto.VolleyCallback() {
            @Override
            public void onSuccess(ArrayList<RedditPhoto> result) {
                ImageGalleryAdapter adapter = new ImageGalleryAdapter(getActivity(), result);
                recyclerView.setAdapter(adapter);
            }
        });


        return rootView;
    }

    private class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>  {

        @Override
        public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View photoView = inflater.inflate(R.layout.reddit_photo, parent, false);
            return new MyViewHolder(photoView);
        }

        @Override
        public void onBindViewHolder(ImageGalleryAdapter.MyViewHolder holder, int position) {

            RedditPhoto redditPhoto = mRedditPhotos.get(position);
            ImageView imageView = holder.mPhotoImageView;

            Glide.with(mContext)
                    .load(redditPhoto.getUrl())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_cloud_off_red))
                    .into(imageView);
        }

        @Override
        public int getItemCount() {
            return (mRedditPhotos.size());
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView mPhotoImageView;

            public MyViewHolder(View itemView) {

                super(itemView);
                mPhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    RedditPhoto redditPhoto = mRedditPhotos.get(position);
                    Intent intent = new Intent(mContext, PhotoDetailActivity.class);
                    intent.putExtra(PhotoDetailActivity.EXTRA_REDDIT_PHOTO, redditPhoto);
                    startActivity(intent);
                }
            }
        }

        private ArrayList<RedditPhoto> mRedditPhotos;
        private Context mContext;

        public ImageGalleryAdapter(Context context, ArrayList<RedditPhoto> redditPhotos) {
            mContext = context;
            mRedditPhotos = redditPhotos;
        }
    }
}
