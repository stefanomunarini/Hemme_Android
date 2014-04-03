package com.povodev.hemme.android.activity.gameTest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.povodev.hemme.android.R;
import com.povodev.hemme.android.bean.Result;

/**
 * A fragment representing a single Test detail screen.
 * This fragment is either contained in a {@link TestListActivity}
 * in two-pane mode (on tablets) or a {@link TestDetailActivity}
 * on handsets.
 */
public class TestDetailFragment extends Fragment {

    /**
     * The content this fragment is presenting.
     */
    private Result result;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TestDetailFragment() {
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
            ((TextView) rootView.findViewById(R.id.test_time)).setText(result.getTime()+"");
            ((TextView) rootView.findViewById(R.id.test_date)).setText(result.getDate()+"");
        }

        return rootView;
    }
}
