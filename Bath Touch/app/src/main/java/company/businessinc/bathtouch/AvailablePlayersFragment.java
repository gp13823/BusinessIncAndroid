package company.businessinc.bathtouch;


import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.List;

import company.businessinc.bathtouch.adapters.AvailablePlayersAdapter;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.dataModels.Player;

/**
 * Created by user on 30/01/15.
 */
public class AvailablePlayersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mRecyclerView;
    private View mLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private AvailablePlayersAdapter mAdapter;
    private boolean available_toggle;
    private int matchID;
    private AvailablePlayersListener mCallbacks;

    private List<Player> selectedPlayers;
    private List<Player> unselectedPlayers;

    public interface AvailablePlayersListener {
        public void createGhostPlayerEvent();
    }


    public static AvailablePlayersFragment newInstance(int matchID) {
        AvailablePlayersFragment fragment = new AvailablePlayersFragment();
        Bundle args = new Bundle();

        args.putInt("matchID", matchID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            matchID = bundle.getInt("matchID");
            getLoaderManager().initLoader(DBProviderContract.MYTEAMPLAYERSAVAILABILITY_URL_QUERY, null, this);
        }

        mCallbacks = (AvailablePlayersListener) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.fragment_team_roster, container, false);

        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.team_roster_recycle);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        //Adapter loads the data fror the leagues
        mAdapter = new AvailablePlayersAdapter(available_toggle, getActivity(), matchID);
        mRecyclerView.setAdapter(mAdapter);


        //add stikcy header decoration
//        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
//        mRecyclerView.addItemDecoration(headersDecor);
//        mRecyclerView.addItemDecoration(new DividerDecoration(getActivity().getBaseContext()));


        FloatingActionButton fab = (FloatingActionButton) mLayout.findViewById(R.id.team_roster_fab);
        fab.attachToRecyclerView(mRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.createGhostPlayerEvent(); //TODO Fix this
            }
        });

        return mLayout;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case DBProviderContract.MYTEAMPLAYERSAVAILABILITY_URL_QUERY:
                return new CursorLoader(getActivity(), DBProviderContract.MYTEAMPLAYERSAVAILABILITY_TABLE_CONTENTURI, null, DBProviderContract.SELECTION_MATCHID, new String[]{Integer.toString(matchID)}, null);
            default:
                // An invalid id was passed in
                return null;
        }
    }

    //query has finished
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case DBProviderContract.MYTEAMPLAYERSAVAILABILITY_URL_QUERY:
                if (data.moveToFirst()) {
                    while (!data.isAfterLast()) {
                        Player player = new Player(data);
//                        if(player.getIsPlaying() == available_toggle){
//                            ((AvailablePlayersAdapter)mAdapter).addToPlayerList(player);
//                        }
                        ((AvailablePlayersAdapter) mAdapter).addToPlayerList(player);

                        data.moveToNext();
                    }
                }
                break;
        }
    }

    //when data gets updated, first reset everything
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public class DividerDecoration extends RecyclerView.ItemDecoration {

        private final int[] ATTRS = new int[]{
                android.R.attr.listDivider
        };

        public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

        public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

        private Drawable mDivider;

        public DividerDecoration(Context context) {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();
        }

        private int getOrientation(RecyclerView parent) {
            LinearLayoutManager layoutManager;
            try {
                layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            } catch (ClassCastException e) {
                throw new IllegalStateException("DividerDecoration can only be used with a " +
                        "LinearLayoutManager.", e);
            }
            return layoutManager.getOrientation();
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
            if (getOrientation(parent) == VERTICAL_LIST) {
                drawVertical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }
        }

        public void drawVertical(Canvas c, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        public void drawHorizontal(Canvas c, RecyclerView parent) {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getHeight() - parent.getPaddingBottom();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int left = child.getRight() + params.rightMargin;
                final int right = left + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (getOrientation(parent) == VERTICAL_LIST) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        }
    }


}
