package com.caroline.lab2_myapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caroline.lab2_myapp.R;
import com.caroline.lab2_myapp.database.petEntity;

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
    }

    @Override
    public int getItemCount() {
        return mPets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtUserName)
        TextView txtUserName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
