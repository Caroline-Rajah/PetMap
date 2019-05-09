package com.cmpe277.petMap;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cmpe277.petMap.database.petEntity;
import com.cmpe277.petMap.ui.PetAdapter;
import com.cmpe277.petMap.viewmodel.MainViewModel;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    //@OnClick(R.id.fab)
    void fabClickHandler(){
        Intent intent = new Intent(this,AddPetActivity.class);
        startActivity(intent);
    }

    private PetAdapter mAdapter;

    private List<petEntity> petsData = new ArrayList<petEntity>();

    private MainViewModel mViewModel;
    private BoomMenuButton bmb;

    private ImageButton imgBtnHome;
    private ImageButton imgBtnSearch;
    private ImageButton imgBtnFeed;
    private ImageButton imgBtnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        imgBtnHome = findViewById(R.id.imageButtonHome);
        imgBtnSearch = findViewById(R.id.imageButtonSearch);
        imgBtnFeed = findViewById(R.id.imageButtonFeed);
        imgBtnSettings = findViewById(R.id.imageButtonSettings);

        bmb = (BoomMenuButton) findViewById(R.id.bmb);
        bmb.setButtonEnum(ButtonEnum.Ham);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_3_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.Vertical);
        String[] animals = {"Cats","Dogs","Birds"};
        String[] subTexts = {"Looking for cats?", "Looking for dogs?", "Looking for birds?"};
        //ButtonPlaceEnum.Vertical.buttonNumber();
        //ButtonPlaceEnum.Vertical.buttonNumber();
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            HamButton.Builder builder = new HamButton.Builder()
                    .normalImageRes(R.drawable.ic_grade_black_24dp)
                    .normalText(animals[i])
                    .subNormalText(subTexts[i]).listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            Toast.makeText(MainActivity.this, "Clicked " + index, Toast.LENGTH_SHORT).show();
                            switch (index){
                                case 0:mViewModel.addPetsByType("cat");
                                break;
                                case 1:mViewModel.addPetsByType("dog");
                                break;
                                case 2:mViewModel.addPetsByType("bird");
                            }
                        }
                    });
            bmb.addBuilder(builder);
        }
        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();


        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        String[] listSort = getResources().getStringArray(R.array.sortCriteria);
        String[] listSize = getResources().getStringArray(R.array.Size);
        String[] listAge = getResources().getStringArray(R.array.Age);
        String[] listGender = getResources().getStringArray(R.array.Gender);
        Comparator<petEntity> compareBySize = (petEntity o1, petEntity o2) ->
                o1.getSize().compareTo( o2.getSize() );

        Comparator<petEntity> compareByAge = (petEntity o1, petEntity o2) ->
                o1.getAge().compareTo( o2.getAge() );

        Comparator<petEntity> compareByName = (petEntity o1, petEntity o2) ->
                o1.getName().compareTo( o2.getName() );

        Comparator<petEntity> compareByGender = (petEntity o1, petEntity o2) ->
                o1.getGender().compareTo( o2.getGender() );
        imgBtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });

        imgBtnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Sort according to");
                mBuilder.setSingleChoiceItems(listSort, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Collections.sort(petsData, compareByAge);
                            mAdapter = new PetAdapter(petsData, MainActivity.this);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                        else if (i == 1) {
                            Collections.sort(petsData, compareBySize);
                            mAdapter = new PetAdapter(petsData, MainActivity.this);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                        else if (i == 2) {
                            Collections.sort(petsData, compareByGender);
                            mAdapter = new PetAdapter(petsData, MainActivity.this);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

    }

    private void initViewModel() {
        final Observer<List<petEntity>> petsObserver = new Observer<List<petEntity>>() {
            @Override
            public void onChanged(@Nullable List<petEntity> petEntities) {
                petsData.clear();
                petsData.addAll(petEntities);
                if(mAdapter == null) {
                    mAdapter = new PetAdapter(petsData, MainActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                }else{
                    mAdapter.notifyDataSetChanged();
                }
            }
        };

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.mPets.observe(this,petsObserver);
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_sample) {
            addSamplePets();
            return true;
        }else if(id == R.id.action_delete_all){
            new AlertDialog.Builder(this)
                    .setTitle("Deletion Alert")
                    .setMessage("Are you sure you want to delete all pets?")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteAllPets();
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }}).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openCamera(MenuItem menuItem) {
        startActivity(new Intent(MainActivity.this, AddActivity.class));
    }

    private void deleteAllPets() {
        mViewModel.deleteAllData();
    }

    private void addSamplePets() {
        mViewModel.addSamplePets();
    }
}
