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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class TopGenresActivity extends AppCompatActivity {

    private String mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_genres);


        // Animated gradient background that can change from one color to another
        LinearLayout cardTopGenre1 = findViewById(R.id.cardTopGenre1);
        AnimationDrawable animationDrawable1 = (AnimationDrawable) cardTopGenre1.getBackground();
        animationDrawable1.setEnterFadeDuration(1000);
        animationDrawable1.setExitFadeDuration(1000);
        animationDrawable1.start();

        // Animated gradient background that can change from one color to another
        LinearLayout cardTopGenre2 = findViewById(R.id.cardTopGenre2);
        AnimationDrawable animationDrawable2 = (AnimationDrawable) cardTopGenre2.getBackground();
        animationDrawable2.setEnterFadeDuration(1000);
        animationDrawable2.setExitFadeDuration(1000);
        animationDrawable2.start();

        // Animated gradient background that can change from one color to another
        LinearLayout cardTopGenre3 = findViewById(R.id.cardTopGenre3);
        AnimationDrawable animationDrawable3 = (AnimationDrawable) cardTopGenre3.getBackground();
        animationDrawable3.setEnterFadeDuration(1000);
        animationDrawable3.setExitFadeDuration(1000);
        animationDrawable3.start();

        // Animated gradient background that can change from one color to another
        LinearLayout cardTopGenre4 = findViewById(R.id.cardTopGenre4);
        AnimationDrawable animationDrawable4 = (AnimationDrawable) cardTopGenre4.getBackground();
        animationDrawable4.setEnterFadeDuration(1000);
        animationDrawable4.setExitFadeDuration(1000);
        animationDrawable4.start();

        // Animated gradient background that can change from one color to another
        LinearLayout cardTopGenre5 = findViewById(R.id.cardTopGenre5);
        AnimationDrawable animationDrawable5 = (AnimationDrawable) cardTopGenre5.getBackground();
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

        playTrack(trackURIs[2]);

        //TextView topGenresTextView = findViewById(R.id.top_genres_text_view);
        Button toTrackBtn = findViewById(R.id.to_track_btn);

        toTrackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopGenresActivity.this, TopTracksActivity.class);
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

        TextView topGenre1 = findViewById(R.id.topGenre1);
        TextView topGenre2 = findViewById(R.id.topGenre2);
        TextView topGenre3 = findViewById(R.id.topGenre3);
        TextView topGenre4 = findViewById(R.id.topGenre4);
        TextView topGenre5 = findViewById(R.id.topGenre5);

        if (topGenres != null) {
            String joinedTopGenres = TextUtils.join(", ", topGenres);
            //topGenresTextView.setText(joinedTopGenres);

            String[] words = joinedTopGenres.split(", ");

            topGenre1.setText("#1  " + words[0]);
            topGenre2.setText("#2  " + words[1]);
            topGenre3.setText("#3  " + words[2]);
            topGenre4.setText("#4  " + words[3]);
            topGenre5.setText("#5  " + words[4]);

        } else {
            String ans="No top genre data available";
            //topGenresTextView.setText(ans);

            topGenre1.setText(ans);
            topGenre2.setText(ans);
            topGenre3.setText(ans);
            topGenre4.setText(ans);
            topGenre5.setText(ans);

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
                runOnUiThread(() -> Toast.makeText(TopGenresActivity.this, "Failed to play track", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("Spotify", "Failed to command Spotify: " + response);
                    // Log response body for debugging
                    Log.e("Spotify", "Response body: " + response.body().string());
                    runOnUiThread(() -> Toast.makeText(TopGenresActivity.this, "Failed to command Spotify", Toast.LENGTH_SHORT).show());
                } else {
                    Log.d("Spotify", "Successfully played track");
                    // Log response body for debugging
                    Log.d("Spotify", "Response body: " + response.body().string());
                }
            }
        });
    }
}