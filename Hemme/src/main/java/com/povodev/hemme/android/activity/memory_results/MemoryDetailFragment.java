package com.povodev.hemme.android.activity.memory_results;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.bean.Result;
import com.povodev.hemme.android.utils.Formatters;

/**
 * A fragment representing a single Test detail screen.
 * This fragment is either contained in a {@link MemoryResultsListActivity}
 * in two-pane mode (on tablets) or a {@link MemoryDetailActivity}
 * on handsets.
 */
public class MemoryDetailFragment extends Fragment {

    /**
     * The content this fragment is presenting.
     */
    private Result result;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MemoryDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        result = (Result) getArguments().getSerializable("result");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test_detail, container, false);

        // Show the content as text in TextViews
        if (result != null) {
            ((TextView) rootView.findViewById(R.id.test_grade)).setText(result.getGrade());
            String time = Formatters.timeFormat(result.getTime());
            ((TextView) rootView.findViewById(R.id.test_time)).setText(time);
            String date = Formatters.timestampFormat(result.getDate());
            ((TextView) rootView.findViewById(R.id.test_date)).setText(date);
        }

        return rootView;
    }

}
