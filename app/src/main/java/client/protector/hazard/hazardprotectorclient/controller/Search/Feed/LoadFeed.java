package client.protector.hazard.hazardprotectorclient.controller.Search.Feed;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.model.Articles.Article;

/**
 * Created by Tautvilas on 11/03/2017.
 */

public class LoadFeed
{
    private Context context;
    private ArrayList<Article> articlesList;
    private ListView articlesListView;

    public LoadFeed(Context context, ArrayList<Article> articlesList, ListView articlesListView)
    {
        this.context = context;
        this.articlesList = articlesList;
        this.articlesListView = articlesListView;
    }

    public void populateList()
    {

        articlesListView.setAdapter(new ListItemAdapter(context,articlesList));
    }
}
