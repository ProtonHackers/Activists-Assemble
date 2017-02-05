package com.vikranth.activistsassemble;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.identity.intents.Address;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.messages_list)
    RecyclerView mRecycler;
    private RecyclerView.Adapter mainAdapter;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading events. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        PreferenceUtils.putBoolean(this, "Load", false);
        mRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setAutoMeasureEnabled(false);
        mRecycler.setLayoutManager(llm);
        initializeFirebaseQuery("");

        mRecycler.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        closeDialog();
                    }
                });

    }

    private void initializeFirebaseQuery(String query) {
        Query queryFirebase;
        if (query.equalsIgnoreCase("")) {
            queryFirebase = FirebaseDatabase.getInstance().getReference().child("village");
        } else {
            query = query.substring(0, 1).toUpperCase() + query.substring(1, query.length());
            queryFirebase = FirebaseDatabase.getInstance().getReference().orderByChild("name")
                    .startAt(query)
                    .endAt(query + "\uf8ff");
        }
        final Query postsQuery = FirebaseDatabase.getInstance().getReference();
        final String finalQuery = query;
        FirebaseRecyclerAdapter mAdapter =
                new FirebaseRecyclerAdapter<PointItem, EventViewHolder>
                        (PointItem.class,
                                R.layout.session_row,
                                EventViewHolder.class,
                                postsQuery.getRef().child("tweets").child("locations")) {

                    @Override
                    protected void populateViewHolder(final EventViewHolder viewHolder, final PointItem model, final int position) {
                        //                        final PointItem item = model.location.get(position);
                        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<android.location.Address> addresses = null;
                        try {
                            addresses = gcd.getFromLocation(model.x, model.y, 1);
                        } catch (IOException e) {
                            System.out.println("" + e.toString());
                        }
                        if (addresses.size() > 0) {
                            viewHolder.room.setText(addresses.get(0).getLocality() + "");
                            System.out.println(addresses.get(0).getLocality());
                        } else {
                            // do your staff
                        }

//                        if(finalQuery.equalsIgnoreCase("")|| finalQuery.equalsIgnoreCase(model.hashTag)) {
                        viewHolder.view.setVisibility(View.VISIBLE);
                        String sentiment = model.sentiment.toLowerCase();
                        switch (sentiment){
                            case "neutral":
                                viewHolder.sentiment.setText("Sentiment: " + "safe");
                                break;
                            case "positive":
                                viewHolder.sentiment.setText("Sentiment: " + "safe");
                                break;
                            case "negative":
                                viewHolder.sentiment.setText("Sentiment: " + "dangerous");
                                break;
                            default:
                                viewHolder.sentiment.setText("Sentiment: " + "dangerous");
                                break;
                        }
                        viewHolder.title.setText("#" + model.hashTag);
                        View.OnClickListener onClickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                        .edit().putString("MapKey", model.x + "," + model.y).apply();
                                startMapActivity();

                            }
                        };
                        viewHolder.google_button.setOnClickListener(onClickListener);

                        View.OnClickListener viewOnClick = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean isExpanded = PreferenceUtils.getBoolean(getApplicationContext(), "expanded" + model.x + "," + model.y);
                                PreferenceUtils.putBoolean(getApplicationContext(), "expanded" + model.x + "," + model.y, !isExpanded);
                                notifyItemChanged(position);
                            }
                        };

                        viewHolder.view.setOnClickListener(viewOnClick);
                        if (PreferenceUtils.getBoolean(getApplicationContext(), "expanded" + model.x + "," + model.y)) {
                            ListView listView = (ListView) viewHolder.view.findViewById(R.id.twitter_list);
                            SearchTimeline searchTimeline = new SearchTimeline.Builder()
                                    .query("#" + model.hashTag)
                                    .build();

                            final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getApplicationContext())
                                    .setTimeline(searchTimeline)
                                    .build();

                            listView.setAdapter(adapter);
                            viewHolder.expanded_layout.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.expanded_layout.setVisibility(View.GONE);
                        }
                        boolean isStar = PreferenceUtils.getBoolean(getApplicationContext(), "plan" + model.hashTag + model.x);
                        if (isStar) {
                            PreferenceUtils.putBoolean(getApplicationContext(), "plan" + model.hashTag + model.x, false);
                            viewHolder.star.setImageResource(R.drawable.ic_toggle_star);
                        } else {
                            PreferenceUtils.putBoolean(getApplicationContext(), "plan" + model.hashTag + model.x, true);
                            viewHolder.star.setImageResource(R.drawable.ic_star);
                        }

                        viewHolder.star.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addToPlan(viewHolder, model);
//                                notifyDataSetChanged();
                            }
                        });
//                        else{viewHolder.view.setVisibility(View.GONE);}
                    }

                };
        mRecycler.setAdapter(mAdapter);
    }

    private void addToPlan(EventViewHolder viewHolder, PointItem model) {
        boolean isStar = PreferenceUtils.getBoolean(getApplicationContext(), "plan" + model.hashTag + model.x);
        if (isStar) {
            PreferenceUtils.putBoolean(getApplicationContext(), "plan" + model.hashTag + model.x, false);
            viewHolder.star.setImageResource(R.drawable.ic_toggle_star);
        } else {
            PreferenceUtils.putBoolean(getApplicationContext(), "plan" + model.hashTag + model.x, true);
            viewHolder.star.setImageResource(R.drawable.ic_star);
        }
        addNotification();
    }

    private void addNotification() {
//        Calendar cal = Calendar.getInstance();
//
//        String[] item = time.split(":|-|\\s+");
//        int year = Integer.parseInt("20" + dayArray[2]);
//        int month = Integer.parseInt(dayArray[0]);
//        int day = Integer.parseInt(dayArray[1]);
//        int hour = Integer.valueOf(item[0]);
//        int minute = Integer.valueOf(item[1]);
//        cal.set(Calendar.YEAR, (year));
//        cal.set(Calendar.MONTH, month);
//        cal.set(Calendar.DAY_OF_MONTH, day);
//
//        cal.set(Calendar.HOUR_OF_DAY, hour);
//        cal.set(Calendar.MINUTE, minute);
//        cal.set(Calendar.SECOND, 0);

        int id = (int) System.currentTimeMillis();
        AlarmManager alarmMgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), Receiver.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("notification", new NotificationItem(getKey(), time, model, id));
//        intent.putExtras(bundle);
        long timeBefore = TimeUnit.MINUTES.toMillis(PreferenceUtils.getNotificationTime(this, "time"));
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this,
                id,
                intent, 0);
        Calendar calendar = Calendar.getInstance();
//        alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - timeBefore, alarmIntent);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 5 * 60 * 1000, alarmIntent);
    }

    private void closeDialog() {
        if (getApplicationContext() != null)
            pDialog.cancel();
    }

    private void startMapActivity() {
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
    }

    SearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
//        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
//        closeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initializeFirebaseQuery("");
//            }
//        });
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                initializeFirebaseQuery(query);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        public TextView title;
        @BindView(R.id.room)
        public TextView room;
        @BindView(R.id.expanded_layout)
        public RelativeLayout expanded_layout;

        @BindView(R.id.sentiment)
        public TextView sentiment;

        @BindView(R.id.star)
        ImageView star;
        @BindView(R.id.google_maps_image_button)
        public ImageView google_button;
        View view;

        public EventViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }

}
