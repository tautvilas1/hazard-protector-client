package client.protector.hazard.hazardprotectorclient.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import client.protector.hazard.hazardprotectorclient.R;
import client.protector.hazard.hazardprotectorclient.controller.Search.Core.App;
import client.protector.hazard.hazardprotectorclient.controller.Search.Image.LoadImage;
import client.protector.hazard.hazardprotectorclient.model.Articles.Article;

public class ArticleActivity extends AppCompatActivity {

    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        setupActionBar();
        getArticle();
        loadData();
    }

    private void setupActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Back");

        LayoutInflater inflator = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View v = inflator.from(this).inflate(R.layout.settings, null);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.TOP | Gravity.RIGHT);
        getSupportActionBar().setCustomView(v,layoutParams);
    }

    public void openSettings(View view)
    {
        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        System.out.println("clicked"+item.getItemId());
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getArticle()
    {
        article = (Article) getIntent().getSerializableExtra("article");
    }

    private void loadData()
    {
        TextView lblTitle = (TextView) findViewById(R.id.lblArticleTitle);
        TextView lblDescription = (TextView) findViewById(R.id.lblArticleDescription);
        TextView lblPublishDate = (TextView) findViewById(R.id.lblPublishDate);
        ImageView imgMain = (ImageView) findViewById(R.id.imgMain);

        lblTitle.setText(article.getTitle());

        String description = article.getFullDescription();
        if(description == "" || description == null)
        {
            description = article.getDescription();
        }
        else
        {
            description = description.replaceAll("(\r\n|\n)", "\n \n");
        }
        lblDescription.setText(description);

        lblPublishDate.setText(article.getDateAdded());

        if(article.getThumbnail() != "")
        {
            LoadImage loadImage = new LoadImage(article.getThumbnail(),imgMain);
            loadImage.execute();
        }
        else
        {
            imgMain.setImageResource(R.drawable.no_image);
        }
    }

    public void goToSource(View view)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getLink()));
        startActivity(intent);
    }


}
