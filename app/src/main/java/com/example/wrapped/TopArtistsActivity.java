package com.example.wrapped;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TopArtistsActivity extends AppCompatActivity {

    private String mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_artists);

        // Animated gradient background that can change from one color to another
        LinearLayout cardTopArtist1 = findViewById(R.id.cardTopArtist1);
        AnimationDrawable animationDrawable1 = (AnimationDrawable) cardTopArtist1.getBackground();
        animationDrawable1.setEnterFadeDuration(1000);
        animationDrawable1.setExitFadeDuration(1000);
        animationDrawable1.start();

        // Animated gradient background that can change from one color to another
        LinearLayout cardTopArtist2 = findViewById(R.id.cardTopArtist2);
        AnimationDrawable animationDrawable2 = (AnimationDrawable) cardTopArtist2.getBackground();
        animationDrawable2.setEnterFadeDuration(1000);
        animationDrawable2.setExitFadeDuration(1000);
        animationDrawable2.start();

        // Animated gradient background that can change from one color to another
        LinearLayout cardTopArtist3 = findViewById(R.id.cardTopArtist3);
        AnimationDrawable animationDrawable3 = (AnimationDrawable) cardTopArtist3.getBackground();
        animationDrawable3.setEnterFadeDuration(1000);
        animationDrawable3.setExitFadeDuration(1000);
        animationDrawable3.start();

        // Animated gradient background that can change from one color to another
        LinearLayout cardTopArtist4 = findViewById(R.id.cardTopArtist4);
        AnimationDrawable animationDrawable4 = (AnimationDrawable) cardTopArtist4.getBackground();
        animationDrawable4.setEnterFadeDuration(1000);
        animationDrawable4.setExitFadeDuration(1000);
        animationDrawable4.start();

        // Animated gradient background that can change from one color to another
        LinearLayout cardTopArtist5 = findViewById(R.id.cardTopArtist5);
        AnimationDrawable animationDrawable5 = (AnimationDrawable) cardTopArtist5.getBackground();
        animationDrawable5.setEnterFadeDuration(1000);
        animationDrawable5.setExitFadeDuration(1000);
        animationDrawable5.start();

        String[] topArtists = getIntent().getStringArrayExtra("topArtists");
        String[] topArtistPics = getIntent().getStringArrayExtra("topArtistPics");
        String[] recArtists = getIntent().getStringArrayExtra("recArtists");
        String[] topGenres = getIntent().getStringArrayExtra("topGenres");
        String[] topTracks = getIntent().getStringArrayExtra("topTracks");
        String[] trackURIs = getIntent().getStringArrayExtra("trackURIs");

        mAccessToken = getIntent().getStringExtra("token");

        playTrack(trackURIs[4]);

        //TextView topArtistsTextView = findViewById(R.id.top_artists_text_view);
        Button toRecBtn = findViewById(R.id.to_rec_btn);

        toRecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopArtistsActivity.this, RecArtistsActivity.class);
                intent.putExtra("topArtists", topArtists);
                intent.putExtra("topArtistPics", topArtistPics);
                intent.putExtra("recArtists", recArtists);
                intent.putExtra("topGenres", topGenres);
                intent.putExtra("topTracks", topTracks);
                intent.putExtra("trackURIs",trackURIs);
                intent.putExtra("token",mAccessToken);
                startActivity(intent);
            }
        });

        TextView topArtist1 = findViewById(R.id.topArtist1);
        TextView topArtist2 = findViewById(R.id.topArtist2);
        TextView topArtist3 = findViewById(R.id.topArtist3);
        TextView topArtist4 = findViewById(R.id.topArtist4);
        TextView topArtist5 = findViewById(R.id.topArtist5);

        if (topArtists != null) {
            // Concatenate topArtists array elements into a single string
            String joinedTopArtists = TextUtils.join(", ", topArtists);

            // Display the concatenated string in the TextView
            //topArtistsTextView.setText(joinedTopArtists);

            String[] words = joinedTopArtists.split(", ");

            topArtist1.setText("#1  " + words[0]);
            topArtist2.setText("#2  " + words[1]);
            topArtist3.setText("#3  " + words[2]);
            topArtist4.setText("#4  " + words[3]);
            topArtist5.setText("#5  " + words[4]);

        } else {
            // Handle the case where topArtists array is null (optional)
            String ans="No top artists data available";
            //topArtistsTextView.setText(ans);

            topArtist1.setText(ans);
            topArtist2.setText(ans);
            topArtist3.setText(ans);
            topArtist4.setText(ans);
            topArtist5.setText(ans);

        }
    }

    public void playTrack(String trackURI) {

        if (trackURI == null) {
            Toast.makeText(this, "Track URI missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client = new OkHttpClient();
        String playUrl = "https://api.spotify.com/v1/me/player/play";

        JSONObject payload = new JSONObject();
        try {
            JSONArray uris = new JSONArray();
            uris.put(trackURI);
            payload.put("uris", uris);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(payload.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(playUrl)
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .put(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Spotify", "Error playing track", e);
                runOnUiThread(() -> Toast.makeText(TopArtistsActivity.this, "Failed to play track", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("Spotify", "Failed to command Spotify: " + response);
                    // Log response body for debugging
                    Log.e("Spotify", "Response body: " + response.body().string());
                    runOnUiThread(() -> Toast.makeText(TopArtistsActivity.this, "Failed to command Spotify", Toast.LENGTH_SHORT).show());
                } else {
                    Log.d("Spotify", "Successfully played track");
                    // Log response body for debugging
                    Log.d("Spotify", "Response body: " + response.body().string());
                }
            }
        });
    }
}