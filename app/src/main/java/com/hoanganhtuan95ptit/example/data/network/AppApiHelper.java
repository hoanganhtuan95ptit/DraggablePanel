package com.hoanganhtuan95ptit.example.data.network;

import android.content.res.TypedArray;

import com.hoanganhtuan95ptit.example.App;
import com.hoanganhtuan95ptit.example.R;
import com.hoanganhtuan95ptit.example.data.model.Channel;
import com.hoanganhtuan95ptit.example.data.model.Video;

import java.io.IOException;
import java.util.ArrayList;

public class AppApiHelper implements ApiHelper {

    public static float ratios[] = {16f / 9, 1f, 9f / 16, 6f / 8, 8f / 6};

    @Override
    public ArrayList<Video> getHomeVideo() throws IOException {
        ArrayList<Video> videos = new ArrayList<>();
        TypedArray videoTitleArray = App.self().getResources().obtainTypedArray(R.array.video_title);
        TypedArray videoThumbArray = App.self().getResources().obtainTypedArray(R.array.video_thumb);
        TypedArray videoTitle2Array = App.self().getResources().obtainTypedArray(R.array.video_title_2);
        TypedArray videoThumb2Array = App.self().getResources().obtainTypedArray(R.array.video_thumb);
        TypedArray channelTitleArray = App.self().getResources().obtainTypedArray(R.array.channel_title);
        TypedArray channelThumbArray = App.self().getResources().obtainTypedArray(R.array.video_thumb);

        for (int i = 0; i < 10; i++) {
            Channel channel = new Channel(String.valueOf(i), channelThumbArray.getResourceId(i, -1), channelTitleArray.getResourceId(i, -1));

            Video video = new Video(String.valueOf(i), videoThumbArray.getResourceId(i, -1), videoTitleArray.getResourceId(i, -1));
            video.setRatio(ratios[i % ratios.length]);
            video.setChannel(channel);

            videos.add(video);
        }

        videoTitleArray.recycle();
        videoThumbArray.recycle();
        videoTitle2Array.recycle();
        videoThumb2Array.recycle();
        channelTitleArray.recycle();
        channelThumbArray.recycle();
        return videos;
    }

    @Override
    public ArrayList<Video> getTrendingVideo() throws IOException {
        ArrayList<Video> videos = new ArrayList<>();
        TypedArray videoTitleArray = App.self().getResources().obtainTypedArray(R.array.video_title);
        TypedArray videoThumbArray = App.self().getResources().obtainTypedArray(R.array.video_thumb);
        TypedArray videoTitle2Array = App.self().getResources().obtainTypedArray(R.array.video_title_2);
        TypedArray videoThumb2Array = App.self().getResources().obtainTypedArray(R.array.video_thumb);
        TypedArray channelTitleArray = App.self().getResources().obtainTypedArray(R.array.channel_title);
        TypedArray channelThumbArray = App.self().getResources().obtainTypedArray(R.array.video_thumb);

        for (int i = 9; i >= 0; i--) {
            Channel channel = new Channel(String.valueOf(i), channelThumbArray.getResourceId(i, -1), channelTitleArray.getResourceId(i, -1));

            Video video = new Video(String.valueOf(i), videoThumbArray.getResourceId(i, -1), videoTitleArray.getResourceId(i, -1));
            video.setRatio(ratios[i % ratios.length]);
            video.setChannel(channel);

            videos.add(video);
        }

        videoTitleArray.recycle();
        videoThumbArray.recycle();
        videoTitle2Array.recycle();
        videoThumb2Array.recycle();
        channelTitleArray.recycle();
        channelThumbArray.recycle();
        return videos;
    }

    @Override
    public ArrayList<Video> getRelationVideo() throws IOException {
        ArrayList<Video> videos = new ArrayList<>();
        TypedArray videoTitleArray = App.self().getResources().obtainTypedArray(R.array.video_title);
        TypedArray videoThumbArray = App.self().getResources().obtainTypedArray(R.array.video_thumb);
        TypedArray videoTitle2Array = App.self().getResources().obtainTypedArray(R.array.video_title_2);
        TypedArray videoThumb2Array = App.self().getResources().obtainTypedArray(R.array.video_thumb);
        TypedArray channelTitleArray = App.self().getResources().obtainTypedArray(R.array.channel_title);
        TypedArray channelThumbArray = App.self().getResources().obtainTypedArray(R.array.video_thumb);

        for (int i = 0; i < 10; i++) {
            Channel channel = new Channel(String.valueOf(i), channelThumbArray.getResourceId(i, -1), channelTitleArray.getResourceId(i, -1));

            Video video = new Video(String.valueOf(i), videoThumbArray.getResourceId(i, -1), videoTitleArray.getResourceId(i, -1));
            video.setRatio(ratios[i % ratios.length]);
            video.setChannel(channel);

            videos.add(video);
        }

        videoTitleArray.recycle();
        videoThumbArray.recycle();
        videoTitle2Array.recycle();
        videoThumb2Array.recycle();
        channelTitleArray.recycle();
        channelThumbArray.recycle();
        return videos;
    }
}
