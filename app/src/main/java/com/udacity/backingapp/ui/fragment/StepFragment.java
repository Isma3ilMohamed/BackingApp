package com.udacity.backingapp.ui.fragment;


import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;

import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import com.udacity.backingapp.R;
import com.udacity.backingapp.databinding.FragmentStepBinding;
import com.udacity.backingapp.model.Steps;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment {


    //DataBinding Declaration
    FragmentStepBinding mStepBinding;

    private Steps step;
    private String url;


    private SimpleExoPlayer player;

    private long playbackPosition;
    public static final String PLAYBACK_POSITION = "playbackPosition";
    private boolean playWhenReady;
    public static final String PLAY_WHEN_READY = "playWhenReady";

    public static final String URI = "uri";


    public StepFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mStepBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_step, container, false);


        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
            url = savedInstanceState.getString(URI);

        }



        if (this.getArguments() != null) {
            step = this.getArguments().getParcelable("step");
            mStepBinding.tvMainDesc.setText(step.getShortDescription());
            mStepBinding.tvDetailDesc.setText(step.getDescription());
            url = step.getVideoURL();
        }
        if (url.equals("") || TextUtils.isEmpty(url)) {
            mStepBinding.videoView.setVisibility(View.GONE);
        }


        return mStepBinding.getRoot();
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        mStepBinding.videoView.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(playbackPosition);
        Uri uri = Uri.parse(url);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);

    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(getString(R.string.app_name))).
                createMediaSource(uri);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();

        } else {
            player.seekTo(playbackPosition);

            player.setPlayWhenReady(playWhenReady);
        }

    }

    private void releasePlayer() {
        if (player != null) {

            player.release();
            player = null;
        }
    }

    @Override
    public void onPause() {
        playbackPosition = player.getCurrentPosition();
        playWhenReady = player.getPlayWhenReady();
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        playbackPosition = player.getCurrentPosition();
        playWhenReady = player.getPlayWhenReady();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(PLAYBACK_POSITION, playbackPosition);
        outState.putBoolean(PLAY_WHEN_READY, playWhenReady);
        outState.putString(URI, url);
    }


}
