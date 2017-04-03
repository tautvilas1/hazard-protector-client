package client.protector.hazard.hazardprotectorclient.view;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import client.protector.hazard.hazardprotectorclient.controller.Search.Search.Finder;
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
    private View view;

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
        App.user.addObserver(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_hazards_feed, container, false);
        getArticles();
        getHazardousArticles();
        loadFeed(view);
        return view;
    }


    public void getArticles()
    {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future f = es.submit(new TableArticle(getActivity(),300,0));
        try
        {
            articlesList = (ArrayList<Article>) f.get();
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

    private void getHazardousArticles()
    {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future f = es.submit(new FindArticlesById(articlesList, App.user.getHazardArticlesList()));
        try
        {
            hazardousArticlesList = (ArrayList<Article>) f.get();
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

    public void loadFeed(View view)
    {
        getArticles();
        getHazardousArticles();
        ArticleSort articleSort = new ArticleSort();
        articleSort.sortByDate(hazardousArticlesList);
        Collections.reverse(hazardousArticlesList);
        ListView articlesListView = (ListView) view.findViewById(R.id.articlesListView2);
        if(hazardousArticlesList.size() > 0)
        {
            LoadFeed loadFeed = new LoadFeed(getActivity(),hazardousArticlesList,articlesListView);
            loadFeed.populateList();
        }
        else
        {
            TextView txtNewsFeed = (TextView) view.findViewById(R.id.lblHazards);
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

    @Override
    public void update(Observable o, Object arg)
    {
        getArticles();
        getHazardousArticles();
        loadFeed(view);
    }


    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }
}
