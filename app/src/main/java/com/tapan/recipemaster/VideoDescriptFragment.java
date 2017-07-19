package com.tapan.recipemaster;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Target;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideoDescriptFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideoDescriptFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoDescriptFragment extends Fragment implements ExoPlayer.EventListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;



    SimpleExoPlayerView mSimpleExoPlayer;

    String videoUrl, description, thumbNail;

    long playback;
    int currentWindow;
    boolean playWhenReady;
    private SimpleExoPlayer player;
    private MediaSessionCompat mediaSessionCompat;
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
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoDescriptFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoDescriptFragment newInstance(String param1, String param2) {
        VideoDescriptFragment fragment = new VideoDescriptFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_video_descript, container, false);

        //get the data from the parent activity about the url and the description
        Bundle bundle = getArguments();
        videoUrl = bundle.getString(getString(R.string.video_url));
        description = bundle.getString(getString(R.string.description_url));
        thumbNail = bundle.getString(getString(R.string.thumb_url));


        if (rootView.findViewById(R.id.tv_video_title) != null){
            mTextVideoTitle = (TextView) rootView.findViewById(R.id.tv_video_title);
            if (description.isEmpty() || description.equals("")){
                mTextVideoTitle.setVisibility(View.GONE);
            }else {
                mTextVideoTitle.setText(description);
            }

            if (thumbNail.isEmpty() || thumbNail.equals("")){

            }else {
                mImageThumbnail = (ImageView) rootView.findViewById(R.id.iv_thumbnail);
                mImageThumbnail.setVisibility(View.VISIBLE);
                Picasso.with(getActivity())
                        .load(thumbNail)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                mImageThumbnail.setImageBitmap(bitmap);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                mImageThumbnail.setVisibility(View.GONE);
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });

            }


        }



        mSimpleExoPlayer = (SimpleExoPlayerView) rootView.findViewById(R.id.view_video_step);


        if (savedInstanceState !=null){
            currentWindow = savedInstanceState.getInt("currentWindow");
            playWhenReady = savedInstanceState.getBoolean("playWhenReady");
            playback = savedInstanceState.getLong("playbackPosition");
        }

        if (videoUrl.equals("")){
            mSimpleExoPlayer.setVisibility(View.GONE);

            //For landscape mode with no video, we will show a toast as their won't be any description too.
            if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                Toast toast = Toast.makeText(getContext(),"No Video Available",Toast.LENGTH_LONG);
                toast.show();
            }
        }else{
            //Initialize the media player
            initializePlayer();
            player.addListener(this);
            initializeMediaSession();
        }



        return rootView;
    }


    private void initializePlayer(){
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        mSimpleExoPlayer.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playback);

        Uri uri = Uri.parse(videoUrl);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);
    }


    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }



    private void initializeMediaSession(){
        mediaSessionCompat = new MediaSessionCompat(getContext(),TAG);
        mediaSessionCompat.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        );

        mediaSessionCompat.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder().setActions(PlaybackStateCompat.ACTION_PLAY |
                PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);

        mediaSessionCompat.setPlaybackState(mStateBuilder.build());

        mediaSessionCompat.setCallback(new MyMediaSessionCallback());

        mediaSessionCompat.setActive(true);
    }

    private void releasePlayer() {
        if (player != null) {
            playback = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
        //doing null check
        if(mediaSessionCompat !=null){
            mediaSessionCompat.setActive(false);
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


        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady ){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,player.getCurrentPosition(),1f);
        }else if(playbackState == ExoPlayer.STATE_READY){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,player.getCurrentPosition(),1f);
        }

        mediaSessionCompat.setPlaybackState(mStateBuilder.build());

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentWindow",currentWindow);
        outState.putBoolean("playWhenReady",playWhenReady);
        outState.putLong("playbackPosition",playback);
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
            player.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            player.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            player.seekTo(0);
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
