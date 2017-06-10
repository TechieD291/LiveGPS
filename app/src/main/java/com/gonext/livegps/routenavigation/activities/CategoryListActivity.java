package com.gonext.livegps.routenavigation.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gonext.livegps.routenavigation.R;
import com.gonext.livegps.routenavigation.adpter.CategoryListRecyclerViewAdpter;
import com.gonext.livegps.routenavigation.models.CategoryModelClass;
import com.gonext.livegps.routenavigation.sqllite.SqlLiteDbHelper;
import com.gonext.livegps.routenavigation.utils.PopUtils;
import com.gonext.livegps.routenavigation.utils.StaticUtils;
import com.gonext.livegps.routenavigation.utils.view.CustomEditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryListActivity extends AppCompatActivity implements CategoryListRecyclerViewAdpter.Clickable {
    ArrayList<CategoryModelClass> CategoryArrayList;
    String lat, lon;
    View view;
    @BindView(R.id.trainlive_et_trainno)
    CustomEditText trainliveEtTrainno;
    @BindView(R.id.trainlive_img_search)
    ImageView trainliveImgSearch;
    @BindView(R.id.trainlive_rel_layout_trainno)
    RelativeLayout trainliveRelLayoutTrainno;
    @BindView(R.id.rvCategory)
    RecyclerView rvCategory;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private RecyclerView.Adapter rvCategoryListAdapter;
    private RecyclerView.LayoutManager rvCategoryListLayoutManager;
    SqlLiteDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat", 0.0d) + "";
        lon = intent.getDoubleExtra("lon", 0.0d) + "";

        dbHelper = new SqlLiteDbHelper(CategoryListActivity.this);
        CategoryArrayList = dbHelper.getCategoryList();
        rvCategoryListLayoutManager = new LinearLayoutManager(CategoryListActivity.this);
        rvCategory.setLayoutManager(rvCategoryListLayoutManager);
        rvCategoryListAdapter = new CategoryListRecyclerViewAdpter(CategoryArrayList, this, this);
        rvCategory.setAdapter(rvCategoryListAdapter);
    }

    private void dialogOpen() {
        final Dialog dialog = new Dialog(CategoryListActivity.this);
        dialog.setContentView(R.layout.dialog_addcategory);
        dialog.show();
        final EditText edtSearch = (EditText) dialog.findViewById(R.id.edtSearch);
        Button btnSearch = (Button) dialog.findViewById(R.id.btnSearch);
        Button btnCancle = (Button) dialog.findViewById(R.id.btnCancle);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtSearch.getText().toString().equals("")) {
                    CategoryModelClass categoryModelClass = new CategoryModelClass();
                    categoryModelClass.setCategoryIcon("extras");
                    categoryModelClass.setCategoryName(edtSearch.getText().toString());
                    categoryModelClass.setCategorySelected(1);
                    CategoryArrayList.add(categoryModelClass);
                    rvCategoryListAdapter.notifyDataSetChanged();
                    dbHelper.inssertToCategory(edtSearch.getText().toString(), "extras", 1);
                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter Number", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    @Override
    public void onclick(int position) {

    }

    @OnClick({R.id.trainlive_img_search, R.id.fab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.trainlive_img_search:
                if (!trainliveEtTrainno.getText().toString().equals("")) {
                    /*fragment = new CategoryDetailListFragment();
                    Bundle args = new Bundle();
                    args.putString("lat", lat);
                    args.putString("lon", lon);
                    args.putString("icon", "extras");
                    args.putString("keyword", trainliveEtTrainno.getText().toString());
                    fragment.setArguments(args);
                    replaceFragment(fragment, R.id.framelayout, true, true, false);*/
                }
                break;
            case R.id.fab:
                dialogOpen();
                break;
        }
    }
}
