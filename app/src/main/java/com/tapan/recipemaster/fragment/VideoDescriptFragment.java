package com.tapan.recipemaster.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.squareup.picasso.Picasso;
import com.tapan.recipemaster.R;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideoDescriptFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideoDescriptFragment} factory method to
 * create an instance of this fragment.
 */
public class VideoDescriptFragment extends Fragment implements ExoPlayer.EventListener {
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SimpleExoPlayerView mSimpleExoPlayerView;

    private String videoUrl, description, thumbNail;

    long mPlayBack;
    int currentWindow;
    boolean playWhenReady;
    private SimpleExoPlayer simpleExoPlayer;
    private MediaSessionCompat mMediaSessionCompat;
    private PlaybackStateCompat.Builder mStateBuilder;


    private TextView mTextVideoTitle;
    private ImageView mImageThumbnail;

    public VideoDescriptFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment VideoDescriptFragment.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_video_descript, container, false);

        if (savedInstanceState != null) {
            currentWindow = savedInstanceState.getInt(getString(R.string.windowNow));
            playWhenReady = savedInstanceState.getBoolean(getString(R.string.play));
            mPlayBack = savedInstanceState.getLong(getString(R.string.postionPlay));
        }

        Bundle bundle = getArguments();
        videoUrl = bundle.getString(getString(R.string.video_url));
        description = bundle.getString(getString(R.string.description_url));
        thumbNail = bundle.getString(getString(R.string.thumb_url));
        setUp(rootView);

        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.windowNow), currentWindow);
        outState.putBoolean(getString(R.string.play), playWhenReady);
        outState.putLong(getString(R.string.postionPlay), mPlayBack);
    }

    private void setUp(View rootView) {

        if (rootView.findViewById(R.id.tv_video_title) != null) {
            mTextVideoTitle = (TextView) rootView.findViewById(R.id.tv_video_title);
            if (TextUtils.isEmpty(description)) {
                mTextVideoTitle.setVisibility(View.GONE);
            } else {
                mTextVideoTitle.setText(description);
            }

            if (TextUtils.isEmpty(thumbNail)) {
            } else {
                mImageThumbnail = (ImageView) rootView.findViewById(R.id.iv_thumbnail);
                mImageThumbnail.setVisibility(View.VISIBLE);
                Picasso.with(getActivity())
                        .load(thumbNail)
                        .centerCrop()
                        .into(mImageThumbnail);
            }
        }

        mSimpleExoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.view_video_step);

        if (TextUtils.isEmpty(videoUrl)) {
            mSimpleExoPlayerView.setVisibility(View.GONE);
        } else {
            startThePlayer(videoUrl);

            addMediaSession();
        }
    }


    private void startThePlayer(String videoUrl) {

        if (simpleExoPlayer == null) {
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());


            mSimpleExoPlayerView.setPlayer(simpleExoPlayer);
            simpleExoPlayer.addListener(this);
            simpleExoPlayer.setPlayWhenReady(playWhenReady);
            simpleExoPlayer.seekTo(currentWindow, mPlayBack);

            Uri uri = Uri.parse(videoUrl);
            MediaSource mediaSource = buildMediaSource(uri);
            simpleExoPlayer.prepare(mediaSource, true, false);
        }
    }


    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }


    private void addMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSessionCompat = new MediaSessionCompat(getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSessionCompat.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        );


        // Do not let MediaButtons restart the simpleExoPlayer when the app is not visible.
        mMediaSessionCompat.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the simpleExoPlayer.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);


        mMediaSessionCompat.setPlaybackState(mStateBuilder.build());
        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSessionCompat.setCallback(new MyMediaSessionCallback());
        // Start the Media Session since the activity is active.
        mMediaSessionCompat.setActive(true);
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            mPlayBack = simpleExoPlayer.getCurrentPosition();
            currentWindow = simpleExoPlayer.getCurrentWindowIndex();
            playWhenReady = simpleExoPlayer.getPlayWhenReady();
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (( simpleExoPlayer == null)) {
            startThePlayer(videoUrl);
        }

    }

    @Override
    public void onPause() {
        super.onPause();

            releasePlayer();
            if (mMediaSessionCompat != null) {
                mMediaSessionCompat.setActive(false);

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

        if (mStateBuilder == null){
            return;
        }

        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, simpleExoPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == ExoPlayer.STATE_READY) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, simpleExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSessionCompat.setPlaybackState(mStateBuilder.build());
    }


    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    private class MyMediaSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            simpleExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            simpleExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            simpleExoPlayer.seekTo(0);
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
