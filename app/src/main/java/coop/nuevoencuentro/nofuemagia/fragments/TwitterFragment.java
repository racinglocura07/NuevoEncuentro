package coop.nuevoencuentro.nofuemagia.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CollectionTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TwitterListTimeline;
import com.twitter.sdk.android.tweetui.UserTimeline;

import coop.nuevoencuentro.nofuemagia.R;

/**
 * Created by Tanoo on 13/8/2016.
 */
public class TwitterFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setListShown(false);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_twitter, container, false);

        TwitterListTimeline timeline = new TwitterListTimeline.Builder().slugWithOwnerScreenName("nuevo-encuentro", "necomuna10").build();


        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                .setTimeline(timeline)
                .setOnActionCallback(new Callback<Tweet>() {
                    @Override
                    public void success(Result<Tweet> result) {
                        setListShown(true);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        setEmptyText("Error buscando los tweets");
                        exception.printStackTrace();
                    }
                })
                .setViewStyle(com.twitter.sdk.android.tweetui.R.style.tw__TweetLightWithActionsStyle)
                .build();
        setListAdapter(adapter);

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_tweet);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                    @Override
                    public void success(Result<TimelineResult<Tweet>> result) {
                        swipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Toast or some other action
                        exception.printStackTrace();
                    }
                });
            }
        });

        return v;
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        View v = inflater.inflate(R.layout.fragment_twitter, container, false);
//        //setListShown(false);
//
//        UserTimeline userTimeline = new UserTimeline.Builder()
//                .screenName("Sabbatella")
//                .screenName("NuestrasVocesOK")
//                .screenName("mfmacha")
//                .screenName("JoseCampagnoli")
//                .build();
//
//        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getContext())
//                .setTimeline(userTimeline)
//                .build();
//
////        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getContext())
////                .setTimeline(userTimeline)
////                .build();
//
//        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_tweet);
//        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeLayout.setRefreshing(true);
//                adapter.refresh(new Callback<TimelineResult<Tweet>>() {
//                    @Override
//                    public void success(Result<TimelineResult<Tweet>> result) {
//                        swipeLayout.setRefreshing(false);
//                    }
//
//                    @Override
//                    public void failure(TwitterException exception) {
//                        // Toast or some other action
//                        exception.printStackTrace();
//                    }
//                });
//            }
//        });
//
//        //ListView lv = (ListView) v.findViewById(R.id.list_tweets);
//        setListAdapter(adapter);
//
//        return v;
//    }

    public ListView mList;
    boolean mListShown;
    View mProgressContainer;
    View mListContainer;

    public void setListShown(boolean shown, boolean animate) {
        if (mListShown == shown) {
            return;
        }
        mListShown = shown;
        if (shown) {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
            }
            mProgressContainer.setVisibility(View.GONE);
            mListContainer.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
            }
            mProgressContainer.setVisibility(View.VISIBLE);
            mListContainer.setVisibility(View.INVISIBLE);
        }
    }

    public void setListShown(boolean shown) {
        setListShown(shown, true);
    }

    public void setListShownNoAnimation(boolean shown) {
        setListShown(shown, false);
    }
}
