package company.businessinc.bathtouch.data;

import android.net.Uri;
import android.provider.BaseColumns;

import company.businessinc.dataModels.League;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.Team;

/**
 * Created by Grzegorz on 27/01/2015.
 */
public class DBProviderContract {

    //Keywords
    private static final String CREATE_TABLE = "CREATE TABLE";

    // The URI scheme used for content URIs
    public static final String SCHEME = "content";

    // The provider's authority
    public static final String AUTHORITY = "company.businessinc.bathtouch";

    /**
     * The DataProvider content URI
     */
    public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);

    /**
     * Integers which indicate which query to run
     */
    public static final int ALLTEAMS_URL_QUERY = 0;
    public static final int MYLEAGUES_URL_QUERY = 1;
    public static final int ALLLEAGUES_URL_QUERY = 2;
    public static final int LEAGUESSCORE_URL_QUERY = 3;
    public static final int LEAGUESTEAMS_URL_QUERY = 4;
    public static final int LEAGUESFIXTURES_URL_QUERY = 5;
    public static final int LEAGUESSTANDINGS_URL_QUERY = 6;
    public static final int TEAMSFIXTURES_URL_QUERY = 7;
    public static final int TEAMSSCORES_URL_QUERY = 8;
    public static final int MYUPCOMINGGAMES_URL_QUERY = 9;
    public static final int MYUPCOMINGREFEREEGAMES_URL_QUERY = 10;

    /**
     * Table names
     */
    public static final String ALLTEAMS_TABLE_NAME = "AllTeams";
    public static final String MYLEAGUES_TABLE_NAME = "MyLeagues";
    public static final String ALLLEAGUES_TABLE_NAME = "AllLeagues";
    public static final String LEAGUESSCORE_TABLE_NAME = "LeaguesScore";
    public static final String LEAGUESTEAMS_TABLE_NAME = "LeaguesTeams";
    public static final String LEAGUESFIXTURES_TABLE_NAME = "LeaguesFixtures";
    public static final String LEAGUESSTANDINGS_TABLE_NAME = "LeaguesStandings";
    public static final String TEAMSFIXTURES_TABLE_NAME = "TeamsFixtures";
    public static final String TEAMSSCORES_TABLE_NAME = "TeamsScores";
    public static final String MYUPCOMINGGAMES_TABLE_NAME = "MyUpcomingGames";
    public static final String MYUPCOMINGREFEREEGAMES_TABLE_NAME = "MyUpcomingRefereeGames";

    public static final String[] TABLES = {ALLTEAMS_TABLE_NAME, MYLEAGUES_TABLE_NAME, ALLLEAGUES_TABLE_NAME, LEAGUESSCORE_TABLE_NAME, LEAGUESTEAMS_TABLE_NAME,
            LEAGUESFIXTURES_TABLE_NAME, LEAGUESSTANDINGS_TABLE_NAME, TEAMSFIXTURES_TABLE_NAME, TEAMSSCORES_TABLE_NAME, MYUPCOMINGGAMES_TABLE_NAME, MYUPCOMINGREFEREEGAMES_TABLE_NAME};

    /**
     * Content URI for modification tables
     */
    public static final Uri ALLTEAMS_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, ALLTEAMS_TABLE_NAME);
    public static final Uri MYLEAGUES_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, MYLEAGUES_TABLE_NAME);
    public static final Uri ALLLEAGUES_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, ALLLEAGUES_TABLE_NAME);
    public static final Uri LEAGUESSCORE_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, LEAGUESSCORE_TABLE_NAME);
    public static final Uri LEAGUESTEAMS_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, LEAGUESTEAMS_TABLE_NAME);
    public static final Uri LEAGUESFIXTURES_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, LEAGUESFIXTURES_TABLE_NAME);
    public static final Uri LEAGUESSTANDINGS_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, LEAGUESSTANDINGS_TABLE_NAME);
    public static final Uri TEAMSFIXTURES_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, TEAMSFIXTURES_TABLE_NAME);
    public static final Uri TEAMSSCORES_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, TEAMSSCORES_TABLE_NAME);
    public static final Uri MYUPCOMINGGAMES_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, MYUPCOMINGGAMES_TABLE_NAME);
    public static final Uri MYUPCOMINGREFEREEGAMES_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, MYUPCOMINGREFEREEGAMES_TABLE_NAME);

    /**
     * Create tables strings
     */
    public static final String CREATE_ALLTEAMS_TABLE = CREATE_TABLE + " " + ALLTEAMS_TABLE_NAME + "( " + Team.CREATE_TABLE + " )";
    public static final String CREATE_MYLEAGUES_TABLE = CREATE_TABLE + " " + MYLEAGUES_TABLE_NAME + "( " + League.CREATE_TABLE + " )";
    public static final String CREATE_ALLLEAGUES_TABLE = CREATE_TABLE + " " + ALLLEAGUES_TABLE_NAME + "( " + League.CREATE_TABLE + " )";

    public static final String CREATE_MYUPCOMINGGAMES_TABLE = CREATE_TABLE + " " + MYUPCOMINGGAMES_TABLE_NAME + "( " + Match.CREATE_TABLE + " )";
    public static final String CREATE_MYUPCOMINGREFEREEGAMES_TABLE = CREATE_TABLE + " " + MYUPCOMINGREFEREEGAMES_TABLE_NAME + "( " + Match.CREATE_TABLE + " )";


    public static final String[] CREATE_TABLES = {CREATE_ALLTEAMS_TABLE, CREATE_MYLEAGUES_TABLE, CREATE_ALLLEAGUES_TABLE, CREATE_MYUPCOMINGGAMES_TABLE, CREATE_MYUPCOMINGREFEREEGAMES_TABLE};

    // The content provider database name
    public static final String DATABASE_NAME = "BathTouchDB";

    // The starting version of the database
    public static final int DATABASE_VERSION = 1;
}
