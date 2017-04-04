package client.protector.hazard.hazardprotectorclient.view;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.controller.Search.Article.ArticleSort;
import client.protector.hazard.hazardprotectorclient.controller.Search.Core.App;
import client.protector.hazard.hazardprotectorclient.controller.Search.Feed.LoadFeed;
import client.protector.hazard.hazardprotectorclient.controller.Search.Listeners.NewsFeedScrollListener;
import client.protector.hazard.hazardprotectorclient.model.Articles.Article;
import client.protector.hazard.hazardprotectorclient.model.Articles.TableArticle;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFeed.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFeed#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFeed extends Fragment
{

    private View view;

    private OnFragmentInteractionListener mListener;

    public NewsFeed()
    {
    }


    public static NewsFeed newInstance(String param1, String param2)
    {
        NewsFeed fragment = new NewsFeed();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume()
    {
        super.onResume();
//        loadFeed(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        loadFeed(view);
        return view;
    }


    public void loadFeed(View view)
    {

        ListView articlesListView = (ListView) view.findViewById(R.id.articlesListView);
        if(App.articlesList.size() > 0)
        {
            ArticleSort articleSort = new ArticleSort();
            App.articlesList = articleSort.sortByDate(App.articlesList);
            Collections.reverse(App.articlesList);
            Collections.shuffle(App.articlesList);
            ArrayList<Article> refinedList = articleSort.sortByPreferences(App.articlesList);
            if(articlesListView != null)
            {
                LoadFeed loadFeed = new LoadFeed(getActivity(),refinedList,articlesListView);
                loadFeed.populateList();
            }
            else
            {
                Log.d("log","Articles list view is null");
            }

//            articlesListView.setOnScrollListener(new NewsFeedScrollListener(getActivity(),articlesListView));
        }
        else
        {
            TextView txtNewsFeed = (TextView) view.findViewById(R.id.txtNewsFeed);
            articlesListView.setVisibility(View.INVISIBLE);
            txtNewsFeed.setVisibility(View.VISIBLE);
            txtNewsFeed.setTextColor(Color.RED);
            txtNewsFeed.setText("No articles could be loaded");
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }
}
