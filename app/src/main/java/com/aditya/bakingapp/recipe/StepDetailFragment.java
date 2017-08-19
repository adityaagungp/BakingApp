package com.aditya.bakingapp.recipe;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aditya.bakingapp.R;
import com.aditya.bakingapp.object.Step;
import com.aditya.bakingapp.util.Constants;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener {

    @BindView(R.id.playerView)
    SimpleExoPlayerView playerView;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.shortDescription)
    TextView shortDescription;
    @BindView(R.id.description)
    TextView description;

    private Step mStep;
    private boolean mTwoPane;
    private SimpleExoPlayer mExoPlayer;
    private DataSource.Factory mDataSourceFactory;
    private DefaultTrackSelector mTrackSelector;
    private BandwidthMeter mBandwidthMeter;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, view);
        Bundle data = getArguments();
        mTwoPane = false;

        if (data.containsKey(Constants.Param.STEP)) {
            mStep = data.getParcelable(Constants.Param.STEP);
            if (data.containsKey(Constants.Param.TWO_PANE)) {
                mTwoPane = data.getBoolean(Constants.Param.TWO_PANE);
            }
            loadStep();
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.Param.STEP, mStep);
        outState.putBoolean(Constants.Param.TWO_PANE, mTwoPane);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle inState) {
        super.onActivityCreated(inState);
        if (inState != null) {
            mStep = inState.getParcelable(Constants.Param.STEP);
            mTwoPane = inState.getBoolean(Constants.Param.TWO_PANE);
        }
        loadStep();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    private void loadStep() {
        if (!mTwoPane) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mStep.getShortDescription());
        }
        if (mStep.getVideoURL() == null || mStep.getVideoURL().length() == 0) {
            playerView.setVisibility(View.GONE);
        } else {
            playerView.setVisibility(View.VISIBLE);
            setupPlayerView();
            initMediaSession();
        }
        if (mStep.getThumbnailURL() == null || mStep.getThumbnailURL().length() == 0) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(mStep.getThumbnailURL()).placeholder(R.mipmap.ic_launcher).into(imageView);
        }
        shortDescription.setText(mStep.getShortDescription());
        description.setText(mStep.getDescription());
    }

    private void setupPlayerView() {
        playerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mBandwidthMeter = new DefaultBandwidthMeter();
        mDataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "BakingApp"),
                (TransferListener<? super DataSource>) mBandwidthMeter);
        LoadControl loadControl = new DefaultLoadControl();
        TrackSelection.Factory videoTrackSelection = new AdaptiveVideoTrackSelection.Factory(mBandwidthMeter);
        mTrackSelector = new DefaultTrackSelector(videoTrackSelection);
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), mTrackSelector, loadControl);
        playerView.setPlayer(mExoPlayer);
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mStep.getVideoURL()), mDataSourceFactory,
                extractorsFactory, null, null);
        mExoPlayer.addListener(this);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }

    private void initMediaSession() {
        mStateBuilder = new PlaybackStateCompat.Builder().setActions(PlaybackStateCompat.ACTION_PLAY |
                PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mMediaSession = new MediaSessionCompat(getContext(), "Step");
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);
        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());
        mMediaSession.setActive(true);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (mStateBuilder != null && mExoPlayer != null && mMediaSession != null){
            if (playbackState == ExoPlayer.STATE_READY && playWhenReady){
                mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getCurrentPosition(), 1f);
            } else if (playbackState == ExoPlayer.STATE_READY){
                mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mExoPlayer.getCurrentPosition(), 1f);
            }
            mMediaSession.setPlaybackState(mStateBuilder.build());
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay(){
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause(){
            mExoPlayer.setPlayWhenReady(false);
        }
    }
}
