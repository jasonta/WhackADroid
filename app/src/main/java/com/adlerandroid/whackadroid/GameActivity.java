package com.adlerandroid.whackadroid;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends ActionBarActivity {

    private static final String TAG = "GameActivity";

    private static final int BASE_DELAY = 500;
    private static final int START_DELAY = 2000;
    private int mDelay = START_DELAY;
    private static final int START_DROID_LIFESPAN = 5000;
    private int mDroidLifespan = START_DROID_LIFESPAN;
    private UpdateDroidsThread mUpdateDroidsThread;
    private GridAdapter mAdapter;
    private TextView mScoreText;
    private int mScore;
    private Random mRandom = new Random(System.currentTimeMillis());
//    private AddDroidsRunnable mAddDroidsRunnable = new AddDroidsRunnable();
//    private RemoveDroidsRunnable mRemoveDroidsRunnable = new RemoveDroidsRunnable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mScore = 0;

        mScoreText = (TextView) findViewById(R.id.scoreText);
        updateScoreText();

        mAdapter = new GridAdapter(this);

        GridView gridview = (GridView) findViewById(R.id.grid);
        gridview.setAdapter(mAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Toast.makeText(GameActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                mAdapter.clearDroid(position);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mUpdateDroidsThread = new UpdateDroidsThread();
        mUpdateDroidsThread.start();

//        // start loop to have droids pop up at random times
//        mAddDroidsRunnable.resume();
//
//        // start loop to clear droids that have been visible for too long
//        mRemoveDroidsRunnable.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mUpdateDroidsThread.pause();

//        mAddDroidsRunnable.pause();
//        mRemoveDroidsRunnable.pause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateScoreText() {
        mScoreText.setText(String.format(getString(R.string.scoreFormat), mScore));
    }

    /**
     * Handle updating droids on screen by adding and removing them as necessary, in a background
     * thread.
     */
    private class UpdateDroidsThread extends Thread {
        public Handler mHandler;
        private boolean mIsPaused;

        @Override
        public void run() {
            mIsPaused = false;

            Looper.prepare();

            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // TODO handle messages
                }
            };

            mHandler.postDelayed(mAddDroidsRunnable, 3000);
            mHandler.postDelayed(mRemoveDroidsRunnable, 4000);

            Looper.loop();
        }

        public void pause() {
            mIsPaused = true;
        }

        private Runnable mAddDroidsRunnable = new Runnable() {
            @Override
            public void run() {
                if (mAdapter != null && !mIsPaused) {
                    // display a random new droid
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addNewDroid();
                        }
                    });

                    // repeat at a pseudo-random pace
                    final long delay = BASE_DELAY + mRandom.nextInt(mDelay);
                    Log.v(TAG, "==> next delay = " + delay);
                    mHandler.postDelayed(this, delay);
                }
            }
        };

        private Runnable mRemoveDroidsRunnable = new Runnable() {
            @Override
            public void run() {
                if (mAdapter != null && !mIsPaused) {
                    // remove droids that have been visible for a while
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.removeDroids();
                        }
                    });

                    // repeat at a reasonable pace to clean up elapsed droids on a timely basis
                    mHandler.postDelayed(this, 500);
                }
            }
        };
    }

    /**
     * Add droids to random locations on a random schedule.
     */
//    private class AddDroidsRunnable implements Runnable {
//        private boolean mIsPaused;
//
//        public AddDroidsRunnable() {
//            mIsPaused = false;
//        }
//
//        @Override
//        public void run() {
//            if (mAdapter != null && !mIsPaused) {
//                // display a random new droid
//                mAdapter.addNewDroid();
//
//                // repeat at a pseudo-random pace
//                final long delay = BASE_DELAY + mRandom.nextInt(mDelay);
//                Log.v(TAG, "==> next delay = " + delay);
//                mHandler.postDelayed(this, delay);
//            }
//        }
//
//        public synchronized void pause() {
//            mIsPaused = true;
//        }
//
//        public synchronized void resume() {
//            mIsPaused = false;
//
//            mHandler.postDelayed(this, BASE_DELAY);
//        }
//    }

    /**
     * Remove elapsed droids from grid in background thread.
     */
//    private class RemoveDroidsRunnable implements Runnable {
//        private boolean mIsPaused;
//
//        @Override
//        public void run() {
//            if (mAdapter != null && !mIsPaused) {
//                // remove droids that have been visible for a while
//                mAdapter.removeDroids();
//
//                // repeat at a reasonable pace to clean up elapsed droids on a timely basis
//                mHandler.postDelayed(this, 500);
//            }
//        }
//
//        public synchronized void pause() {
//            mIsPaused = true;
//        }
//
//        public void resume() {
//            mIsPaused = false;
//
//            mHandler.postDelayed(this, 500);
//        }
//    }

    /**
     * Container for a grid cell. Each cell is either empty or has a droid. If not empty, the
     * start time should have been set to aid in determining when it should be removed.
     */
    private class Droid {
        public boolean isEmpty;
        public long startTime;

        public Droid() {
            isEmpty = true;
            startTime = 0;
        }
    }

    /**
     * Adapter for GridView, which contains cells that are either empty or contain a droid.
     */
    private class GridAdapter extends BaseAdapter {
        private static final int COUNT = 16;
        private final Context mContext;
        private List<Droid> mCells;

        public GridAdapter(Context c) {
            mContext = c;
            mCells = new ArrayList<Droid>();
            for (int ii = 0; ii < COUNT; ++ii) {
                mCells.add(new Droid());
            }
        }

        public int getCount() {
            return COUNT;
        }

        public Object getItem(int position) {
            return mCells.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        // create a new ImageView for each item referenced by the Adapter
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(90, 90));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setBackgroundResource(R.drawable.box);

            final Droid droid = mCells.get(position);
            if (droid != null && !droid.isEmpty) {
                imageView.setImageResource(R.drawable.ic_launcher);
            } else {
                imageView.setImageDrawable(null);
            }

            return imageView;
        }

        public void removeDroids() {
            final long NOW = System.currentTimeMillis();
            int ii = 0;
            for (final Droid droid : mCells) {
                if (!droid.isEmpty && (NOW - droid.startTime >= mDroidLifespan)) {
                    Log.v(TAG, "removeDroid: " + ii);
                    droid.isEmpty = true;
                    droid.startTime = 0;

                    // lose a point by not whacking droid in time
                    --mScore;
                    updateScoreText();
                }
                ++ii;
            }

            notifyDataSetChanged();
        }

        public void addNewDroid() {
            boolean found = false;
            int pos;
            int count = 0;
            do {
                pos = mRandom.nextInt(COUNT);
                if (mCells.get(pos).isEmpty) {
                    found = true;
                }
                ++count;
                if (count > COUNT) {
                    break;
                }
            } while (!found);

            if (found) {
                Log.v(TAG, "addNewDroid: " + pos);
                final Droid droid = mCells.get(pos);
                droid.isEmpty = false;
                droid.startTime = System.currentTimeMillis();
            }

            notifyDataSetChanged();
        }

        public void clearDroid(int position) {
            if (position >= 0 && position < mCells.size()) {
                final Droid droid = mCells.get(position);
                if (!droid.isEmpty) {
                    Log.v(TAG, "clearDroid: " + position);
                    droid.isEmpty = true;
                    droid.startTime = 0;

                    // gain a point by whacking droid
                    ++mScore;
                    updateScoreText();
                }

                notifyDataSetChanged();
            }
        }
    }
}
