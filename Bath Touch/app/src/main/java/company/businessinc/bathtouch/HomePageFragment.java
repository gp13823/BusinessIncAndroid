package company.businessinc.bathtouch;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heinrichreimersoftware.materialdrawer.DrawerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import company.businessinc.bathtouch.adapters.HomePageAdapter;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.dataModels.Match;


public class HomePageFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private HomePageCallbacks mCallbacks;
    private View mLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private League league;
    private List<LeagueTeam> leagueStandings;
    private List<Match> leagueScores;
    private List<Match> leagueFixtures;


    public static HomePageFragment newInstance() {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if(DataStore.getInstance(getActivity()).isUserLoggedIn()) {
            if(DataStore.getInstance(getActivity()).isReferee()) {
                getLoaderManager().initLoader(DBProviderContract.MYUPCOMINGREFEREEGAMES_URL_QUERY, null, this);
            }
            getLoaderManager().initLoader(DBProviderContract.MYUPCOMINGGAMES_URL_QUERY, null, this);
            getLoaderManager().initLoader(DBProviderContract.MYLEAGUES_URL_QUERY, null, this);
            getLoaderManager().initLoader(DBProviderContract.TEAMSSCORES_URL_QUERY, null, this);
            getLoaderManager().initLoader(DBProviderContract.TEAMSFIXTURES_URL_QUERY, null, this);
        } else {
            getLoaderManager().initLoader(DBProviderContract.ALLLEAGUES_URL_QUERY, null, this);
            getLoaderManager().initLoader(DBProviderContract.LEAGUESSCORE_URL_QUERY, null, this);
        }
        getLoaderManager().initLoader(DBProviderContract.LEAGUESSTANDINGS_URL_QUERY, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.alternative_home_page, container, false);

        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Home");
        actionBar.setElevation(getResources().getDimension(R.dimen.headerbar_elevation));

        DrawerFrameLayout navigationDrawerLayout = (DrawerFrameLayout) getActivity().findViewById(R.id.drawer_layout);
        navigationDrawerLayout.selectItem(0);

        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.alt_home_page_cards_recycle);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

//        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getBaseContext(),
//                new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Log.d("FRAGMENT", Integer.toString(view.getId()));
//                Log.d("FRAGMENT", Integer.toString(R.id.home_page_card_team_container));
//
//                switch (view.getId()){
//                    //the card clicked decides which fragment to start
//                    case(R.id.home_page_card_team_container):
//                        if(position == 3) {
//                            Log.d("fragment", "changing on id for team container");
//                            selectCard(2);
//                        }
//                        break;
//                    case(R.id.home_page_card_table):
//                        Log.d("fragment", "changing on id for table container");
//                        selectCard(1);
//                        break;
//                    case(R.id.home_page_card_next_match_container):
//                        selectCard(0);
//                        break;
//                    case(R.id.card_team_overview_next_match_box):
////                        Log.d("ANIMATION", "doing animaltion");
////                        Animation slide = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_down);
////                        CardView cardView = (CardView) mLayout.findViewById(R.id.card_team_overview_next_match_box);
////                        cardView.startAnimation(slide);
//                    default:
//                        Log.d("FRAGMENT", Integer.toString(view.getId()));
//                        selectCard(position);
//                        break;
//                }
//            }
//        }));

        mAdapter = new HomePageAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        //set the onclick events for the home cards


        return mLayout;
    }

    private void selectCard(int position) {
        if (mCallbacks != null) {
            mCallbacks.onHomePageCardSelected(position );
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (HomePageCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    //Invoked when the cursor loader is created
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case DBProviderContract.MYUPCOMINGREFEREEGAMES_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.MYUPCOMINGREFEREEGAMES_TABLE_CONTENTURI, null, null, null, null);
            case DBProviderContract.MYUPCOMINGGAMES_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.MYUPCOMINGGAMES_TABLE_CONTENTURI, null, null, null, null);
            case DBProviderContract.MYLEAGUES_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.MYLEAGUES_TABLE_CONTENTURI, null, null, null, null);
            case DBProviderContract.ALLLEAGUES_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.ALLLEAGUES_TABLE_CONTENTURI, null, null, null, null);
            case DBProviderContract.LEAGUESSTANDINGS_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.LEAGUESSTANDINGS_TABLE_CONTENTURI, null, null, null, null);
            case DBProviderContract.LEAGUESSCORE_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.LEAGUESSCORE_TABLE_CONTENTURI, null, null, null, null);
            case DBProviderContract.TEAMSSCORES_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.TEAMSSCORES_TABLE_CONTENTURI, null, null, null, null);
            case DBProviderContract.TEAMSFIXTURES_URL_QUERY:
                // Returns a new CursorLoader
                return new CursorLoader(getActivity(), DBProviderContract.TEAMSFIXTURES_TABLE_CONTENTURI, null, null, null, null);
            default:
                // An invalid id was passed in
                return null;
        }
    }

    //query has finished
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()){
            return;
        }
        switch(loader.getId()) {
            case DBProviderContract.MYUPCOMINGREFEREEGAMES_URL_QUERY:
                List<Match> nextRefMatch = new ArrayList<>();
                while(!data.isAfterLast()){
                    nextRefMatch.add(new Match(data));
                    data.moveToNext();
                }
                if(nextRefMatch.size() > 0 && mAdapter != null){
                    nextRefMatch = Match.sortList(nextRefMatch, Match.SortType.ASCENDING);
                    ((HomePageAdapter)mAdapter).setNextMatch(nextRefMatch.get(0), true);
                }
                break;
            case DBProviderContract.MYUPCOMINGGAMES_URL_QUERY:
                List<Match> nextMatch = new ArrayList<>();
                while(!data.isAfterLast()){
                    nextMatch.add(new Match(data));
                    data.moveToNext();
                }
                if(nextMatch.size() > 0 && mAdapter != null){
                    nextMatch = Match.sortList(nextMatch, Match.SortType.ASCENDING);
                    ((HomePageAdapter)mAdapter).setNextMatch(nextMatch.get(0), false);
                }
                break;
            case DBProviderContract.MYLEAGUES_URL_QUERY:
            case DBProviderContract.ALLLEAGUES_URL_QUERY:
                if(league == null){
                    while(!data.isAfterLast()){
                        league = new League(data);
                        data.moveToNext();
                    }
                    if(league != null){
                        Cursor rCursor = getActivity().getContentResolver().query(DBProviderContract.LEAGUESSTANDINGS_TABLE_CONTENTURI,null,DBProviderContract.SELECTION_LEAGUEID,new String[]{Integer.toString(league.getLeagueID())},null);
                        if(rCursor.getCount() > 0 && mAdapter != null){
                            leagueStandings = loadLeagueTeams(rCursor);
                            ((HomePageAdapter)mAdapter).setLeague(leagueStandings, league);
                        }
                        rCursor.close();
                        if(DataStore.getInstance(getActivity()).isUserLoggedIn()) {
                            rCursor = getActivity().getContentResolver().query(DBProviderContract.TEAMSSCORES_TABLE_CONTENTURI,
                                    null,
                                    DBProviderContract.SELECTION_LEAGUEIDANDTEAMID,
                                    new String[]{Integer.toString(league.getLeagueID()), Integer.toString(DataStore.getInstance(getActivity()).getUserTeamID())},
                                    null);
                        } else {
                            rCursor = getActivity().getContentResolver().query(DBProviderContract.LEAGUESSCORE_TABLE_CONTENTURI,
                                    null,
                                    DBProviderContract.SELECTION_LEAGUEID,
                                    new String[]{Integer.toString(league.getLeagueID())},
                                    null);
                        }
                        if(rCursor.getCount() > 0 && mAdapter != null){
                            leagueScores = loadLeagueMatches(rCursor);
                            ((HomePageAdapter)mAdapter).setLeagueStandings(leagueScores, league);
                        }
                        rCursor.close();
                    }
                }
                break;
            case DBProviderContract.LEAGUESSTANDINGS_URL_QUERY:
                if(league != null){
                    leagueStandings = loadLeagueTeams(data);
                    if(leagueStandings.size() > 0 && mAdapter != null){
                        ((HomePageAdapter)mAdapter).setLeague(leagueStandings, league);
                    }
                }
                break;
            case DBProviderContract.LEAGUESSCORE_URL_QUERY:
            case DBProviderContract.TEAMSSCORES_URL_QUERY:
                if(league != null){
                    leagueScores = loadLeagueMatches(data);
                    if(leagueScores.size() > 0 && mAdapter != null){
                        ((HomePageAdapter)mAdapter).setLeagueStandings(leagueScores, league);
                    }
                }
                break;
            case DBProviderContract.TEAMSFIXTURES_URL_QUERY:
                break;
        }
    }

    public List<LeagueTeam> loadLeagueTeams(Cursor data){
        List<LeagueTeam> leagueTeams = new ArrayList<>();
        data.moveToFirst();
        while(!data.isAfterLast()){
            if(data.getInt(0) == league.getLeagueID()) {
                leagueTeams.add(new LeagueTeam(data));
            }
            data.moveToNext();
        }
        return leagueTeams;
    }

    public List<Match> loadLeagueMatches(Cursor data){
        List<Match> matchList = new ArrayList<>();
        data.moveToFirst();
        while(!data.isAfterLast()){
            if(data.getInt(0) == league.getLeagueID()) {
                matchList.add(new Match(data));
            }
            data.moveToNext();
        }
        return matchList;
    }

    //when data gets updated, first reset everything
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    public static interface HomePageCallbacks {

        void onHomePageCardSelected(int position);
    }

}
