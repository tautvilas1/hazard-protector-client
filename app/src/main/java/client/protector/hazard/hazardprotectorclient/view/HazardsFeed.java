package client.protector.hazard.hazardprotectorclient.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.controller.Search.Article.ArticleSort;
import client.protector.hazard.hazardprotectorclient.controller.Search.Core.App;
import client.protector.hazard.hazardprotectorclient.controller.Search.Feed.LoadFeed;
import client.protector.hazard.hazardprotectorclient.controller.Search.Search.FindArticlesById;

import client.protector.hazard.hazardprotectorclient.model.Articles.Article;
import client.protector.hazard.hazardprotectorclient.model.Articles.TableArticle;

public class HazardsFeed extends Fragment implements Observer
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ArrayList<Article> articlesList, hazardousArticlesList;
    private ListView articlesListView;
    private View view;
    private TextView txtNewsFeed;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.d("log","new hazard articles received");
        }
    };

    public HazardsFeed()
    {
    }

    public static HazardsFeed newInstance(String param1, String param2)
    {
        HazardsFeed fragment = new HazardsFeed();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("notificationListener"));
        App.user.addObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_hazards_feed, container, false);
        articlesListView = (ListView) view.findViewById(R.id.articlesListView2);
        txtNewsFeed = (TextView) view.findViewById(R.id.lblHazards);
        getHazardousArticles();
        loadFeed(view);
        return view;
    }

    private boolean getHazardousArticles()
    {
        boolean result = false;
        if(App.articlesList.size() > 0)
        {
            ExecutorService es = Executors.newSingleThreadExecutor();
            Future f = es.submit(new FindArticlesById(App.articlesList, App.user.getHazardArticlesList()));
            try
            {
                hazardousArticlesList = (ArrayList<Article>) f.get();
                if(hazardousArticlesList.size() == 0)
                {
                    displayError("Unable to find hazardous articles, you are hazard free!");
                    result = false;
                }
                else
                {
                    result = true;
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            result = false;
            displayError("No articles in global article list");
        }
        return result;
    }

    public void loadFeed(View view)
    {
        if(getHazardousArticles())
        {
            ArticleSort articleSort = new ArticleSort();
            articleSort.sortByDate(hazardousArticlesList);
            Collections.reverse(hazardousArticlesList);
            Collections.shuffle(hazardousArticlesList);
            articlesListView.setVisibility(View.VISIBLE);
            txtNewsFeed.setVisibility(View.GONE);
            LoadFeed loadFeed = new LoadFeed(getActivity(),hazardousArticlesList,articlesListView);
            loadFeed.populateList();
        }
    }

    private void displayError(String msg)
    {
        articlesListView.setVisibility(View.INVISIBLE);
        txtNewsFeed.setVisibility(View.VISIBLE);
        txtNewsFeed.setTextColor(Color.RED);
        txtNewsFeed.setText(msg);
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

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("notificationListener"));
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void update(Observable o, Object arg)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                App.getArticles(getActivity(), 300, 0);
                getHazardousArticles();
                loadFeed(view);
            }
        });

    }

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }
}
