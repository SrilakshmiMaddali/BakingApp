package sm.com.bakingapp.util;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import sm.com.bakingapp.R;

public class VideoPlayerFragment extends Fragment {

    public static String VIDEO_URL_EXTRA = "video_url";
    public static String DESCRIPTION_EXTRA = "description";
    public static String PLAYBACK_POS_EXTRA = "pbpe";
    public static String CURRENT_WINDOW_EXTRA = "window";
    public static String PLAY_WHEN_READY_EXTRA = "play";

    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private TextView mStepDescription;

    private long currentPosition;
    private int currentWindow;
    private boolean playWhenReady = true;
    private String videoUrl;
    private String stepDescription;

    public VideoPlayerFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.player_fragment,container, false);

        mPlayerView = view.findViewById(R.id.video_view);
        mStepDescription = view.findViewById(R.id.step_instruction_tv);

        videoUrl = getArguments().getString(VIDEO_URL_EXTRA);
        stepDescription = getArguments().getString(DESCRIPTION_EXTRA);
        initializePlayer(savedInstanceState);
        mStepDescription.setText(stepDescription);

        if (videoUrl.equals("")) {
            mPlayerView.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        currentPosition = mExoPlayer.getCurrentPosition();
        currentWindow = mExoPlayer.getCurrentWindowIndex();
        playWhenReady = mExoPlayer.getPlayWhenReady();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void initializePlayer(Bundle savedInstanceState) {
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());
        mPlayerView.setPlayer(mExoPlayer);
        MediaSource mediaSource = new ExtractorMediaSource.Factory(new
                DefaultHttpDataSourceFactory("bakingApp"))
                .createMediaSource(Uri.parse(videoUrl));
        mExoPlayer.prepare(mediaSource, false, false);

        if (savedInstanceState == null) {
            mExoPlayer.setPlayWhenReady(true);
        } else if (savedInstanceState.containsKey(PLAYBACK_POS_EXTRA)) {
            mExoPlayer.setPlayWhenReady(savedInstanceState.getBoolean(PLAY_WHEN_READY_EXTRA));
            mExoPlayer.seekTo(savedInstanceState.getInt(CURRENT_WINDOW_EXTRA),
                    savedInstanceState.getLong(PLAYBACK_POS_EXTRA));
        }

    }

    private void releasePlayer() {
        if (mExoPlayer != null) {

            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(PLAYBACK_POS_EXTRA, currentPosition);
        outState.putInt(CURRENT_WINDOW_EXTRA, currentWindow);
        outState.putBoolean(PLAY_WHEN_READY_EXTRA, playWhenReady);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
