package org.fossasia.openevent.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.fossasia.openevent.Adapters.SpeakersListAdapter;
import org.fossasia.openevent.R;
import org.fossasia.openevent.data.Session;
import org.fossasia.openevent.data.Speaker;
import org.fossasia.openevent.dbutils.DbContract;
import org.fossasia.openevent.dbutils.DbSingleton;

import java.text.ParseException;
import java.util.List;

/**
 * Created by MananWason on 08-07-2015.
 */
public class SessionDetailActivity extends AppCompatActivity {
    RecyclerView speakersRecyclerView;
    SpeakersListAdapter adapter;
    Session session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sessions_detail);
        DbSingleton dbSingleton = DbSingleton.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_details);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = getIntent().getStringExtra("SESSION");
        TextView tv_title = (TextView) findViewById(R.id.title_session);
        TextView tv_subtitle = (TextView) findViewById(R.id.subtitle_session);
        TextView tv_time = (TextView) findViewById(R.id.tv_time);
        TextView tv_room1 = (TextView) findViewById(R.id.tv_location);
        TextView summary = (TextView) findViewById(R.id.tv_abstract_text);
        TextView descrip = (TextView) findViewById(R.id.tv_description);

        speakersRecyclerView = (RecyclerView) findViewById(R.id.list_speakerss);
        List<Speaker> speakers = null;

        try {
            speakers = dbSingleton.getSpeakersbySessionName(title);
            session = dbSingleton.getSessionbySessionname(title);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tv_room1.setText(String.valueOf(session.getMicrolocations()));
        tv_title.setText(title);
        tv_subtitle.setText(session.getSubtitle());
        tv_time.setText(session.getStartTime());
        summary.setText(session.getSummary());
        descrip.setText(session.getDescription());
        adapter = new SpeakersListAdapter(speakers);
        speakersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        speakersRecyclerView.setAdapter(adapter);
        speakersRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.add_bookmark:

                Log.d("BOOKMARKS", session.getId() + "");
                addToDb(session.getId());

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_session_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void addToDb(int id) {
        String query_normal = "INSERT INTO %s VALUES ('%d');";
        String query = String.format(
                query_normal,
                DbContract.Bookmarks.TABLE_NAME,
                id
        );
        Log.d("BOOKMARKS", query);
        DbSingleton dbSingleton = DbSingleton.getInstance();
        dbSingleton.insertQuery(query);
    }
}