package com.povodev.hemme.android.activity.memory_results;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.activity.NewGame_Activity;

/**
 * An activity representing a list of Result. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MemoryDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link MemoryResultsListFragment} and the item details
 * (if present) is a {@link MemoryDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link MemoryResultsListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class MemoryResultsListActivity extends FragmentActivity
        implements MemoryResultsListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);

        if (findViewById(R.id.test_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((MemoryResultsListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.test_list))
                    .setActivateOnItemClick(true);
        }

    }

    /**
     * Callback method from {@link MemoryResultsListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String position) {
        int realPosition = Integer.parseInt(position);
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putSerializable("result", MemoryResultsListFragment.getTest().get(realPosition));
            MemoryDetailFragment fragment = new MemoryDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.test_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, MemoryDetailActivity.class);
            detailIntent.putExtra("result", MemoryResultsListFragment.getTest().get(realPosition));
            startActivity(detailIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.memory_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_new_game:
                startNewGame();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startNewGame() {
        Intent intent = new Intent(this,NewGame_Activity.class);
        startActivity(intent);
        finish();
    }
}
