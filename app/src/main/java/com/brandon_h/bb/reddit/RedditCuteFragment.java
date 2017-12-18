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
import com.brandon_h.bb.R;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class RedditCuteFragment extends Fragment {
    private TextView mTextView;
    private RequestQueue requestQueue;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reddit_cute, container, false);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        RecyclerView recyclerView = rootView.findViewById(R.id.rv_images);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        ImageGalleryAdapter adapter = new ImageGalleryAdapter(getActivity(), RedditPhoto.getSpacePhotos());
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>  {

        @Override
        public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View photoView = inflater.inflate(R.layout.reddit_photo, parent, false);
            ImageGalleryAdapter.MyViewHolder viewHolder = new ImageGalleryAdapter.MyViewHolder(photoView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ImageGalleryAdapter.MyViewHolder holder, int position) {

            RedditPhoto spacePhoto = mSpacePhotos[position];
            ImageView imageView = holder.mPhotoImageView;

            Glide.with(mContext)
                    .load(spacePhoto.getUrl())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_cloud_off_red))
                    .into(imageView);
        }

        @Override
        public int getItemCount() {
            return (mSpacePhotos.length);
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
                    RedditPhoto spacePhoto = mSpacePhotos[position];
                    Intent intent = new Intent(mContext, PhotoDetailActivity.class);
                    intent.putExtra(PhotoDetailActivity.EXTRA_SPACE_PHOTO, spacePhoto);
                    startActivity(intent);
                }
            }
        }

        private RedditPhoto[] mSpacePhotos;
        private Context mContext;

        public ImageGalleryAdapter(Context context, RedditPhoto[] spacePhotos) {
            mContext = context;
            mSpacePhotos = spacePhotos;
        }
    }
}
