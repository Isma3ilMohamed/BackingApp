package com.udacity.backingapp.ui.fragment;


import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
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


    private static final String SELECTED_POSITION = "position";
    private static final String PLAYER_READY = "ready";
    private long position;

    private Steps step;
    private String url;


    //EXO Variables
    private SimpleExoPlayer player;
    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;


    public StepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mStepBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_step, container, false);


        if (savedInstanceState != null) {
            position = savedInstanceState.getLong(SELECTED_POSITION, C.TIME_UNSET);

        }


        if (this.getArguments() != null) {
            step = this.getArguments().getParcelable("step");
            mStepBinding.tvMainDesc.setText(step.getShortDescription());
            mStepBinding.tvDetailDesc.setText(step.getDescription());
            url = step.getVideoURL();
        }
        if (url.equals("") || TextUtils.isEmpty(url)) {
            mStepBinding.playerView.setVisibility(View.GONE);
        }

        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();

        mediaDataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), getString(R.string.app_name)),
                (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();

        return mStepBinding.getRoot();
    }

    private void initializePlayer() {

        mStepBinding.playerView.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

        mStepBinding.playerView.setPlayer(player);

        player.setPlayWhenReady(shouldAutoPlay);


        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(
                Uri.parse(url),
                mediaDataSourceFactory, extractorsFactory, null, null);
       /* MediaSource mediaSource = new HlsMediaSource(Uri.parse(url),
                mediaDataSourceFactory, null, null);*/

        player.prepare(mediaSource);

    }


    private void releasePlayer() {
        if (player != null) {
            position = player.getCurrentPosition();
            shouldAutoPlay = player.getPlayWhenReady();
            player.setPlayWhenReady(false);
            player.release();
            player = null;
            trackSelector = null;
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        } else {
            player.seekTo(position);

            player.setPlayWhenReady(shouldAutoPlay);
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SELECTED_POSITION, position);
        outState.putBoolean(PLAYER_READY, shouldAutoPlay);
    }
}
