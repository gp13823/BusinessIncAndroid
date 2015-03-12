package company.businessinc.bathtouch.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;

import company.businessinc.bathtouch.DateFormatter;
import company.businessinc.bathtouch.R;
import company.businessinc.bathtouch.data.DBObserver;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.Team;

/**
 * Created by user on 21/11/14.
 */
public class TeamResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DBObserver {

    private int expandedPosition = -1;
    private OnResultSelectedCallbacks mCallbacks;
    private Context mContext;
    private int leagueID;
    private String leagueName;
    private List<Match> leagueScores, leagueFixtures;
    private List<Team> allTeams = new ArrayList<Team>();
    private String teamName;

    public interface OnResultSelectedCallbacks {
        public void showMatchOverview(int position, int matchID);
    }


    public class ViewHolderResults extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTeam1Name, mTeam2Name, mTeam1Score, mTeam2Score, mLocation, mDate, mLeague;
        public ImageView mImageView, mCloseButton, mOppTeamImg;
        public RelativeLayout mCard, mMatchCardButton, mExpandable;

        public ViewHolderResults(View v) {
            super(v);
            mTeam1Name = (TextView) v.findViewById(R.id.match_result_item_match_team1_name);
            mTeam2Name = (TextView) v.findViewById(R.id.match_result_item_match_team2_name);
            mTeam1Score = (TextView) v.findViewById(R.id.match_result_item_match_team1_score);
            mTeam2Score = (TextView) v.findViewById(R.id.match_result_item_match_team2_score);
            mLeague = (TextView) v.findViewById(R.id.match_result_league);
            mDate = (TextView) v.findViewById(R.id.match_result_date);
            mLocation = (TextView) v.findViewById(R.id.match_result_location);
            mImageView = (ImageView) v.findViewById(R.id.match_result_item_result_image);
            mCloseButton = (ImageView) v.findViewById(R.id.match_result_close_button);
            mCard = (RelativeLayout) v.findViewById(R.id.match_result_item_container);
            mMatchCardButton = (RelativeLayout) v.findViewById(R.id.match_result_match_overview_button);
            mExpandable = (RelativeLayout) v.findViewById(R.id.match_result_expandable);
            mOppTeamImg = (ImageView) v.findViewById(R.id.match_result_opp_team_image);


            mCard.setOnClickListener(this);
            mCloseButton.setOnClickListener(this);

            //If clicked, callback to main fragment to start the match over view fragment
            mMatchCardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int matchID;
                    if(getPosition() > leagueScores.size() - 1) {
                        matchID = leagueFixtures.get(getPosition() - leagueScores.size()).getMatchID();
                    } else {
                        matchID = leagueScores.get(getPosition()).getMatchID();
                    }
                    Log.d("TeamResultsAdapter", getPosition() + " " + (getPosition() - leagueScores.size()));
                    mCallbacks.showMatchOverview(getPosition(), matchID);
                }
            });
        }

        @Override
        public void onClick(View v) {
            //Toggle whether the hidden element of the viewholder is displayed on a click
            changeVis(getPosition());
        }
    }

    public TeamResultsAdapter(Fragment context, int leagueID) {
        leagueScores = new ArrayList<>();
        leagueFixtures = new ArrayList<>();
        this.leagueID = leagueID;
        mCallbacks = (OnResultSelectedCallbacks) context;
        mContext = context.getActivity();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
        if(DataStore.getInstance(mContext).isUserLoggedIn()){
            DataStore.getInstance(mContext).registerTeamsScoresDBObserver(this);
            DataStore.getInstance(mContext).registerTeamsFixturesDBObserver(this);
        } else {
            DataStore.getInstance(mContext).registerLeagueScoreDBObserver(this);
            DataStore.getInstance(mContext).registerLeaguesFixturesDBObserver(this);
        }
        DataStore.getInstance(mContext).registerAllTeamsDBObservers(this);
        DataStore.getInstance(mContext).registerAllLeagueDBObserver(this);
        setData();
        setLeagueName();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_result_item, parent, false);

        switch(viewType){
            default:
                return new ViewHolderResults(v);
        }
    }

    @Override
    public void onDetachedFromRecyclerView (RecyclerView recyclerView){
        if(DataStore.getInstance(mContext).isUserLoggedIn()){
            DataStore.getInstance(mContext).unregisterTeamsScoresDBObserver(this);
            DataStore.getInstance(mContext).unregisterTeamsFixturesDBObserver(this);
        } else {
            DataStore.getInstance(mContext).unregisterLeagueScoreDBObserver(this);
            DataStore.getInstance(mContext).unregisterLeaguesFixturesDBObserver(this);
        }
        DataStore.getInstance(mContext).unregisterAllLeagueDBObserver(this);
        DataStore.getInstance(mContext).unregisterAllTeamsDBObservers(this);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void notify(String tableName, Object data) {
        switch (tableName){
            case DBProviderContract.ALLLEAGUES_TABLE_NAME:
                setLeagueName();
                notifyDataSetChanged();
                break;
            case DBProviderContract.TEAMSSCORES_TABLE_NAME:
            case DBProviderContract.LEAGUESSCORE_TABLE_NAME:
            case DBProviderContract.ALLTEAMS_TABLE_NAME:
            case DBProviderContract.TEAMSFIXTURES_TABLE_NAME:
            case DBProviderContract.LEAGUESFIXTURES_TABLE_NAME:
                setData();
                notifyDataSetChanged();
                break;
        }
    }

    private void setData() {
        if(DataStore.getInstance(mContext).isUserLoggedIn()){
            int teamID = DataStore.getInstance(mContext).getUserTeamID();
            this.leagueFixtures = DataStore.getInstance(mContext).getTeamFixtures(leagueID, teamID);
            this.leagueScores = DataStore.getInstance(mContext).getTeamScores(leagueID,
                                                                teamID, Match.SortType.ASCENDING);
            teamName = DataStore.getInstance(mContext).getUserTeam();
        } else {
            this.leagueFixtures = DataStore.getInstance(mContext).getLeagueFixtures(leagueID);
            this.leagueScores = DataStore.getInstance(mContext).getLeagueScores(leagueID);
            teamName = "";
        }
        this.allTeams = DataStore.getInstance(mContext).getAllTeams();
    }

    private void setLeagueName(){
        leagueName = DataStore.getInstance(mContext).getLeagueName(leagueID);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        bindResultItem((ViewHolderResults)holder, position );

//        if(position == 0){
//            bindHeaderItem((ViewHolderHeader) holder, position);
//        }
//        else{
//            bindResultItem((ViewHolderResults)holder, position );
//        }


    }

    public void bindResultItem(ViewHolderResults v, int position){

        Match match;
        Boolean played;
        if (position >= leagueScores.size()) {
            match = leagueFixtures.get(position - leagueScores.size());
            played = false;
        } else {
            match = leagueScores.get(position);
            played = true;
        }

        Team oppTeam = null;
        String oppTeamName;

        if(match.getTeamOne().equals(teamName)){
            oppTeamName = match.getTeamTwo();
        }else{
            oppTeamName = match.getTeamOne();
        }

        for(Team e : allTeams){
            if(e.getTeamName().equals(oppTeamName)){
                oppTeam = e;
            }
        }


        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        v.mTeam1Name.setText(match.getTeamOne());
        v.mTeam2Name.setText(match.getTeamTwo());
        v.mTeam1Score.setText(match.getTeamOnePoints().toString());
        v.mTeam2Score.setText(match.getTeamTwoPoints().toString());
        v.mLocation.setText(match.getPlace().toString());

        if(leagueName.equals("")){
            Log.d("TEAMADAPTER", "league name was null");
        }
        v.mLeague.setText(leagueName);

        DateFormatter df = new DateFormatter();
        v.mDate.setText(df.format(match.getDateTime()));

        //get the context of the adapater, to use resources later on
        Context context = v.mImageView.getContext();
        TextDrawable win = TextDrawable.builder()
                .buildRound("W", context.getResources().getColor(R.color.darkgreen));
        TextDrawable draw = TextDrawable.builder()
                .buildRound("D", context.getResources().getColor(R.color.darkorange));
        TextDrawable lose = TextDrawable.builder()
                .buildRound("L", context.getResources().getColor(R.color.darkred));
        TextDrawable notPlayed = TextDrawable.builder()
                .buildRound("N", context.getResources().getColor(R.color.dark_divider));

        if (match.getTeamOne().equals(teamName)) {
            v.mTeam1Name.setTypeface(null, Typeface.BOLD);
            v.mTeam1Score.setTypeface(null, Typeface.BOLD);
            v.mTeam2Name.setTypeface(null, Typeface.NORMAL);
            v.mTeam2Score.setTypeface(null, Typeface.NORMAL);
            if(played) {
                if (match.getTeamOnePoints() > match.getTeamTwoPoints()) {
                    v.mImageView.setImageDrawable(win);
                } else if(match.getTeamOnePoints() == match.getTeamTwoPoints()) {
                    v.mImageView.setImageDrawable(draw);
                } else {
                    v.mImageView.setImageDrawable(lose);
                }
            } else {
                v.mImageView.setImageDrawable(notPlayed);
            }
        } else if(match.getTeamTwo().equals(teamName)) {
            v.mTeam1Name.setTypeface(null, Typeface.NORMAL);
            v.mTeam1Score.setTypeface(null, Typeface.NORMAL);
            v.mTeam2Name.setTypeface(null, Typeface.BOLD);
            v.mTeam2Score.setTypeface(null, Typeface.BOLD);
            if(played) {
                if (match.getTeamOnePoints() < match.getTeamTwoPoints()) {
                    v.mImageView.setImageDrawable(win);
                } else if (match.getTeamOnePoints() == match.getTeamTwoPoints()) {
                    v.mImageView.setImageDrawable(draw);
                } else {
                    v.mImageView.setImageDrawable(lose);
                }
            } else {
                v.mImageView.setImageDrawable(notPlayed);
            }
        } else {
            v.mTeam1Name.setTypeface(null, Typeface.NORMAL);
            v.mTeam1Score.setTypeface(null, Typeface.NORMAL);
            v.mTeam2Name.setTypeface(null, Typeface.NORMAL);
            v.mTeam2Score.setTypeface(null, Typeface.NORMAL);
            if (! played) {
                v.mImageView.setImageDrawable(notPlayed);
            }
        }

        if (position == expandedPosition) {
            int teamColor;
            try{
                //set specific team colors if it has been loaded by db
                teamColor = Color.parseColor(oppTeam.getTeamColorPrimary());

                Drawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .textColor(teamColor)
                        .toUpperCase()
                        .endConfig()
                        .buildRound("D", Color.WHITE);
                v.mOppTeamImg.setImageDrawable(drawable);

            }
            catch (Exception e){
                teamColor = Color.GRAY;
            }
 //        check whether to open close or leave a card alone
            v.mExpandable.setVisibility(View.VISIBLE);
            v.mExpandable.setBackgroundColor(teamColor);
            v.mExpandable.getBackground().setAlpha(200);
        } else {
            v.mExpandable.setVisibility(View.GONE);
        }


    }

    public void changeVis(int loc) {

        if (expandedPosition == loc) { //if clicking an open view, close it
            expandedPosition = -1;
            notifyItemChanged(loc);
        } else {
            if (expandedPosition > -1) {
                int prev = expandedPosition;
                expandedPosition = -1;
                notifyItemChanged(prev);
            }
            expandedPosition = loc;
            notifyItemChanged(expandedPosition);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        //plus one because of header fragment
        return leagueScores.size() + leagueFixtures.size();
    }

    public int getFixturesCount() {
        return leagueFixtures.size();
    }

    public int getScoresCount() {
        return leagueScores.size();
    }
}

