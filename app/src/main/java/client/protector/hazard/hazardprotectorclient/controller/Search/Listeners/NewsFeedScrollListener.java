package client.protector.hazard.hazardprotectorclient.controller.Search.Listeners;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
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
import client.protector.hazard.hazardprotectorclient.model.Articles.Article;
import client.protector.hazard.hazardprotectorclient.model.Articles.TableArticle;

/**
 * Created by Tautvilas on 29/03/2017.
 */

public class NewsFeedScrollListener implements AbsListView.OnScrollListener
{
    private ProgressBar loading;
    public boolean isLoading = false;
    private Context context;
    private ListView articlesListView;
    public NewsFeedScrollListener(Context context, ListView articlesListView, ProgressBar loading)
    {
        this.context = context;
        this.articlesListView = articlesListView;
        this.loading = loading;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0 && !isLoading)
        {
            loadMoreArticles(firstVisibleItem);
        }
    }

    public void loadMoreArticles(int firstVisibleItem)
    {
        ExecutorService es = Executors.newSingleThreadExecutor();
        int limit = 10;
        int offset = App.articlesList.size();
        Future f = es.submit(new TableArticle(context,limit,offset));
        try
        {
            isLoading = true;
            loading.setVisibility(View.VISIBLE);
            ArrayList<Article> newArticles = (ArrayList<Article>) f.get();
            ArrayList<Article> refinedArticles = new ArticleSort().sortByPreferences(newArticles);
            App.articlesList.addAll(refinedArticles);

            LoadFeed loadFeed = new LoadFeed(context,App.articlesList,articlesListView);
            loadFeed.populateList();
            loadFeed.adapter.notifyDataSetChanged();
            articlesListView.setSelection(offset-3);
            loading.setVisibility(View.INVISIBLE);
            isLoading = false;
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
}
