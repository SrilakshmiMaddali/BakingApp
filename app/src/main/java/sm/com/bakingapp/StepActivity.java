package sm.com.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import sm.com.bakingapp.util.VideoPlayerFragment;

public class StepActivity extends AppCompatActivity {

    private static String TAG = StepActivity.class.getSimpleName();
    public static String STEPS_EXTRA = "steps";
    public static String POS_EXTRA = "pos";
    public static String RECIPE_NAME_EXTRA = "name";

    private Toolbar mToolBar;
    private Button mNextButton;
    private Button mPreviousButton;
    private FragmentManager mFragmentManager;
    private VideoPlayerFragment mVideoPlayerFragment;

    private String recipeName = "";
    private int currentPos;
    private int defaultPos = 0;
    private List<Step> steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.recipe_steps_activity);

        mToolBar = findViewById(R.id.toolBar);
        mNextButton = findViewById(R.id.forward_button);
        mPreviousButton = findViewById(R.id.back_button);
        mFragmentManager = getSupportFragmentManager();

        Intent intent = getIntent();
        if (intent == null){
            closeOnError();
        }
        steps = intent.getParcelableArrayListExtra(STEPS_EXTRA);
        recipeName = intent.getStringExtra(RECIPE_NAME_EXTRA);
        if (savedInstanceState!= null) {
            currentPos = savedInstanceState.getInt(POS_EXTRA);
        } else {
            currentPos = intent.getIntExtra(POS_EXTRA, defaultPos);
            populatePlayerView();
        }

        mToolBar.setTitle(recipeName);
        mToolBar.setNavigationIcon(getResources().getDrawable(R.drawable.backarrow));
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Back button is clicked",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        setEnablePreviousNextButton();
    }

    private void populatePlayerView(){
        String stepDescription = steps.get(currentPos).getDescription();
        String url0 = steps.get(currentPos).getVideoURL();
        String url1 = steps.get(currentPos).getThumbnailURL();
        String videoUrl = (url1.equals(""))?url0:url1;

        Bundle bundle = new Bundle();
        bundle.putString(VideoPlayerFragment.DESCRIPTION_EXTRA,stepDescription);
        bundle.putString(VideoPlayerFragment.VIDEO_URL_EXTRA,videoUrl);
        mVideoPlayerFragment = new VideoPlayerFragment();
        mVideoPlayerFragment.setArguments(bundle);

        mFragmentManager.beginTransaction()
                .replace(R.id.player_fragment, mVideoPlayerFragment)
                .commit();

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, getString(R.string.close_on_intent_error), Toast.LENGTH_SHORT).show();
    }

    public void nextOnClick(View view) {
        Toast.makeText(this,"Next",Toast.LENGTH_SHORT).show();
        currentPos++;
        setEnablePreviousNextButton();
        populatePlayerView();
    }

    public void previousOnClick(View view) {
        Toast.makeText(this,"Previous",Toast.LENGTH_SHORT).show();
        currentPos--;
        setEnablePreviousNextButton();
        populatePlayerView();
    }

    public void setEnablePreviousNextButton(){
        if (currentPos == steps.size() - 1) {
            mNextButton.setEnabled(false);
        } else if (currentPos == 0) {
            mPreviousButton.setEnabled(false);
        } else {
            mNextButton.setEnabled(true);
            mPreviousButton.setEnabled(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POS_EXTRA,currentPos);
    }
}
