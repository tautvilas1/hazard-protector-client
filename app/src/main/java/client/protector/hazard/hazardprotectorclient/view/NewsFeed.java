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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NewsFeed()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFeed.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFeed newInstance(String param1, String param2)
    {
        NewsFeed fragment = new NewsFeed();
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
//        getArticles();
//        loadFeed(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        getArticles();
        loadFeed(view);
        return view;
    }

    public void getArticles()
    {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future f = es.submit(new TableArticle(getActivity(),30,0));
        try
        {
            App.articlesList = (ArrayList<Article>) f.get();
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

        ListView articlesListView = (ListView) view.findViewById(R.id.articlesListView);
        ProgressBar loading = (ProgressBar) view.findViewById(R.id.newsFeedLoading);
        if(App.articlesList.size() > 0)
        {
            ArticleSort articleSort = new ArticleSort();
            App.articlesList = articleSort.sortByDate(App.articlesList);
            Collections.reverse(App.articlesList);
            ArrayList<Article> refinedList = articleSort.sortByPreferences(App.articlesList);
            LoadFeed loadFeed = new LoadFeed(getActivity(),refinedList,articlesListView);
            loadFeed.populateList();
            articlesListView.setOnScrollListener(new NewsFeedScrollListener(getActivity(),articlesListView,loading));
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
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
