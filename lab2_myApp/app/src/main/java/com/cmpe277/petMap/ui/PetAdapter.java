package com.cmpe277.petMap.ui;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.cmpe277.petMap.MapActivity;
import com.cmpe277.petMap.PetDetailsActivity;
import com.cmpe277.petMap.R;
import com.cmpe277.petMap.database.petEntity;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class PetAdapter extends RecyclerView.Adapter <PetAdapter.ViewHolder> {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private final List<petEntity> mPets;
    private final OnItemClickListener listener;

    public PetAdapter(List<petEntity> mPets, Context mContext) {
        this.mPets = mPets;
        this.mContext = mContext;
        this.listener = new OnItemClickListener();
    }

    private final Context mContext;

    class OnItemClickListener {
        void onItemClick(petEntity pet){
            Toast.makeText(mContext,pet.getName()+" was clicked",Toast.LENGTH_SHORT);
        }
    }

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
        viewHolder.textCaption.setText(pet.getDescription());
        viewHolder.txtComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MapActivity.class);
                intent.putExtra("address",pet.getAddress());
                mContext.startActivity(intent);
            }
        });
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
            if(pathToFile==null){
                return null;
            }
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
            if(result!=null) {
                bmImage.setImageBitmap(result);
            }
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtUserName)
        TextView txtUserName;
        @BindView(R.id.imageViewPost)
        ImageView imageView;
        @BindView(R.id.txtComments)
        TextView txtComments;
        @BindView(R.id.txtCaption)
        TextView textCaption;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        @OnClick(R.id.imageViewPost) void makeToast(){

            Toast.makeText(mContext,mPets.get(getAdapterPosition()).getName()+" was clicked!",Toast.LENGTH_SHORT).show();
            petEntity selectedPet = mPets.get(getAdapterPosition());
            Intent intent = new Intent(mContext, PetDetailsActivity.class);
            intent.putExtra("name", selectedPet.getName());

            intent.putExtra("image", selectedPet.getImage());
            intent.putExtra("gender",selectedPet.getGender());
            intent.putExtra("age",selectedPet.getAge());
            intent.putExtra("size",selectedPet.getSize());
            intent.putExtra("email",selectedPet.getEmail());
            intent.putExtra("phone",selectedPet.getPhone());
            mContext.startActivity(intent);
        }
    }
}
