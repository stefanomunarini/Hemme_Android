package com.povodev.hemme.android.activity.clinicalFolder;

import android.content.Intent;
import android.os.Bundle;

import com.povodev.hemme.android.R;

import roboguice.activity.RoboFragmentActivity;

/**
 * An activity representing a list of ClinicalEvents. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ClinicalFolderDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ClinicalFolderListFragment} and the item details
 * (if present) is a {@link ClinicalFolderDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ClinicalFolderListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ClinicalFolderListActivity extends RoboFragmentActivity
        implements ClinicalFolderListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinicalfolder_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.clinicalfolder_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ClinicalFolderListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.clinicalfolder_list))
                    .setActivateOnItemClick(true);
        }
    }

    /**
     * Callback method from {@link ClinicalFolderListFragment.Callbacks}
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
            arguments.putSerializable("clinical_event",ClinicalFolderListFragment.getClinicalFolder().get(realPosition));
            ClinicalFolderDetailFragment fragment = new ClinicalFolderDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.clinicalfolder_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ClinicalFolderDetailActivity.class);
            detailIntent.putExtra("clinical_event",ClinicalFolderListFragment.getClinicalFolder().get(realPosition));
            startActivity(detailIntent);
        }
    }
}
