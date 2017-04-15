package client.protector.hazard.hazardprotectorclient;

import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import client.protector.hazard.hazardprotectorclient.controller.Search.Search.FindArticlesById;
import client.protector.hazard.hazardprotectorclient.model.Articles.Article;
import client.protector.hazard.hazardprotectorclient.model.Articles.TableArticle;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Tautvilas on 11/04/2017.
 */

public class ArticleTests
{
    @Mock
    ExecutorService es = Executors.newSingleThreadExecutor();

    @Test
    public void Get_Articles_From_Database_According_To_Limit()
    {
        int limit = 10;
        int offset = 0;
        Future future = es.submit(new TableArticle(limit,offset));

        try
        {
            ArrayList<Article> articlesList = (ArrayList<Article>) future.get();
            assertTrue(articlesList.size() == limit);
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

    @Test
    public void Find_Articles_By_Id()
    {
        Article article = new Article();
        article.setId(0);
        Article article2 = new Article();
        article2.setId(2);
        Article article3 = new Article();
        article3.setId(8);
        ArrayList<Article> articlesList = new ArrayList<>();
        articlesList.add(article);
        articlesList.add(article2);
        articlesList.add(article3);

        ArrayList<String> articlesToFind = new ArrayList<>();
        int idToFind = 2;
        articlesToFind.add(String.valueOf(idToFind));

        Future future = es.submit(new FindArticlesById(articlesList,articlesToFind));
        try
        {
            ArrayList<Article> returnedArticles = (ArrayList<Article>) future.get();
            assertTrue(returnedArticles.get(0).getId() == idToFind);
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
