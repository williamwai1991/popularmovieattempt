package williamwai.sc_recyclerviewproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetails extends AppCompatActivity {

    ImageView imageView;
    TextView tv_Name;
    TextView tv_Overview;
    String poster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        imageView = (ImageView) findViewById(R.id.movie_image);
        tv_Name = (TextView) findViewById(R.id.movie_name);
        tv_Overview = (TextView) findViewById(R.id.movie_overview);


        //imageView.setImageResource(getIntent().getIntExtra("img_id", 00));
        poster = getIntent().getStringExtra("img_id");

        Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w185/" + poster).into(imageView);


        tv_Name.setText("Name : " + getIntent().getStringExtra("name"));
        tv_Overview.setText("Overview :" + getIntent().getStringExtra("overview"));
    }
}
