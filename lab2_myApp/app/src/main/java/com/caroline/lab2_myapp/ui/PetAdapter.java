package com.caroline.lab2_myapp.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.caroline.lab2_myapp.R;
import com.caroline.lab2_myapp.database.petEntity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetAdapter extends RecyclerView.Adapter <PetAdapter.ViewHolder> {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private final List<petEntity> mPets;

    public PetAdapter(List<petEntity> mPets, Context mContext) {
        this.mPets = mPets;
        this.mContext = mContext;
    }

    private final Context mContext;

    @NonNull
    @Override
    public PetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item_home,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetAdapter.ViewHolder viewHolder, int i) {
        final petEntity pet = mPets.get(i);
        viewHolder.txtUserName.setText(pet.getName());
        //Drawable d = LoadImageFromWebOperations(pet.getImage());
        //viewHolder.imageView.setImageDrawable(d);
        DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(viewHolder.imageView);
        downloadTask.execute(pet.getImage());
    }

    @Override
    public int getItemCount() {
        return mPets.size();
    }
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            Log.e("Image error","error fetching image");
            return null;
        }
    }
    private class DownloadImageWithURLTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageWithURLTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String pathToFile = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(pathToFile).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtUserName)
        TextView txtUserName;
        @BindView(R.id.imageViewPost)
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
