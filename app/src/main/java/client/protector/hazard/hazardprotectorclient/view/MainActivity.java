package client.protector.hazard.hazardprotectorclient.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.controller.Search.Image.LoadImage;
import client.protector.hazard.hazardprotectorclient.controller.Search.Search.Finder;
import client.protector.hazard.hazardprotectorclient.model.Articles.Article;
import client.protector.hazard.hazardprotectorclient.model.Articles.TableArticle;
import client.protector.hazard.hazardprotectorclient.model.User.GetUser;
import client.protector.hazard.hazardprotectorclient.model.User.User;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Article> articlesList = new ArrayList<Article>();
    GoogleCloudMessaging gcm;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        user = (User) intent.getExtras().getSerializable("user");
        System.out.println("Useris "+user.toString());
        getSupportActionBar().setTitle("Hey, "+user.getFirstname());
    }


    public void loadFeed() throws IOException
    {
        getArticles();
        String keywords[] = {"war","terror" ,"flood","hazard","earthquake","disaster"};

        ExecutorService es = Executors.newSingleThreadExecutor();
        Future f = es.submit(new Finder(keywords,articlesList));

        ArrayList<Article> articlesListFiltered = new ArrayList<Article>();

        try
        {
            articlesListFiltered = (ArrayList<Article>) f.get();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        final TableLayout tableLayout = (TableLayout) findViewById(R.id.feedTable);

        if(articlesListFiltered.size() != 0) {

            for (int i = 0; i < 5; i++) {
                // Creation row
                final TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                Random r = new Random();

                ImageView image = new ImageView(this);
                image.setMinimumHeight(350);
                image.setMinimumWidth(450);
                image.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                new LoadImage(articlesListFiltered.get(i).getThumbnail(),image).execute();

                final TextView title = new TextView(this);
                title.setText(articlesListFiltered.get(i).getTitle());
                title.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                final TextView text = new TextView(this);
                text.setText(articlesListFiltered.get(i).getDescription());
                text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                tableRow.addView(image);
                tableRow.addView(title);
//                tableRow.addView(text);

                tableLayout.addView(tableRow);
            }
        }



    }


    public void getArticles() throws IOException {
//        TextView content = (TextView) findViewById(R.id.lblContent);
//        content.setMovementMethod(new ScrollingMovementMethod());
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future f = es.submit(new TableArticle(this));
        try {
            articlesList = (ArrayList<Article>) f.get();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
