package com.udacity.sandwichclub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_also_known_as)
    TextView tvAlsoKnownAs;
    @BindView(R.id.tv_ingredients)
    TextView tvIngredients;
    @BindView(R.id.tv_place_of_origin)
    TextView tvPlaceOfOrigin;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.scroll)
    NestedScrollView scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_movie_details);
        ButterKnife.bind(this);



        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        setupInitialUI(sandwich);


        setTitle(sandwich.getMainName());
    }

    private void setupInitialUI(Sandwich sandwich) {

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        String itemTitle = sandwich.getMainName();
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(itemTitle);
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        Glide.with(image.getContext())
                .load(sandwich.getImage())
                .asBitmap().listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }


            @Override
            public boolean onResourceReady(Bitmap resource, String model,
                                           Target<Bitmap> target,
                                           boolean isFromMemoryCache,
                                           boolean isFirstResource) {

                image.setImageBitmap(resource);
                if (image.getDrawable() != null) {
                    Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        public void onGenerated(Palette palette) {
                            applyPalette(palette);
                        }
                    });
                }
                return true;
            }
        }).into(image);


        if(sandwich.getAlsoKnownAs().size()>0){
            tvAlsoKnownAs.setVisibility(View.VISIBLE);
            tvAlsoKnownAs.setText(R.string.also_known_as);

            for (String s : sandwich.getAlsoKnownAs())
                tvAlsoKnownAs.setText(String.format("%s %s,", tvAlsoKnownAs.getText().toString(), s));
            tvAlsoKnownAs.setText(String.format("%s.", tvAlsoKnownAs.getText().toString().substring(0, tvAlsoKnownAs
                    .getText().length() - 1)));

        }
        if(sandwich.getAlsoKnownAs().size()>0) {
            tvIngredients.setVisibility(View.VISIBLE);
            tvIngredients.setText(R.string.ingredients);

            for (String s : sandwich.getIngredients())
                tvIngredients.setText(String.format("%s %s,", tvIngredients.getText().toString(), s));

            tvIngredients.setText(String.format("%s.", tvIngredients.getText().toString().substring(0, tvIngredients.getText().length() - 1)));
        }


        if(!sandwich.getPlaceOfOrigin().isEmpty()) {
            tvPlaceOfOrigin.setVisibility(View.VISIBLE);
            tvPlaceOfOrigin.setText(String.format("Place of Origin : %s", sandwich.getPlaceOfOrigin()));
        }
        description.setText(sandwich.getDescription());
        title.setText(sandwich.getMainName());

    }

    private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
        int primary = getResources().getColor(R.color.colorPrimary);
        collapsingToolbar.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbar.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        //      updateBackground((FloatingActionButton) findViewById(R.id.share_fab), palette);
        supportStartPostponedEnterTransition();
        setTransparentStatusBar(palette);
    }

    private void setTransparentStatusBar(Palette palette) {
        int color = palette.getMutedColor(getResources().getColor(R.color.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (getWindow() != null)
                getWindow().setStatusBarColor(color);
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
