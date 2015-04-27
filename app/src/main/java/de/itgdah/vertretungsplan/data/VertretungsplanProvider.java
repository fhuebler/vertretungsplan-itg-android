package de.itgdah.vertretungsplan.data;

import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import de.itgdah.vertretungsplan.data.VertretungsplanContract.AbsentClasses;
import de.itgdah.vertretungsplan.data.VertretungsplanContract.Days;
import de.itgdah.vertretungsplan.data.VertretungsplanContract.Vertretungen;
import de.itgdah.vertretungsplan.data.VertretungsplanContract.GeneralInfo;

/**
 * Created by moritz on 23.03.15.
 */
public class VertretungsplanProvider {
    private static final UriMatcher sURIMatcher = buildUriMatcher();
    private VertretungsplanDbHelper mDbHelper;

    private static final int VERTRETUNGEN = 100;
    private static final int VERTRETUNGEN_WITH_DATE = 102;
    private static final int VERTRETUNGEN_WITH_DATE_AND_ID = 103;
    private static final int DAYS = 200;
    private static final int GENERAL_INFO = 300;
    private static final int ABSENT_CLASSES = 400;

    private static final SQLiteQueryBuilder sVertretungenByDateQueryBuilder;

    static {
        sVertretungenByDateQueryBuilder = new SQLiteQueryBuilder();
        sVertretungenByDateQueryBuilder.setTables(Vertretungen.TABLE_NAME + " JOIN " +
                Days.TABLE_NAME + " ON" + Days._ID + " = " + Vertretungen._ID);
    }

    private static final String sVertretungenDateSelection = Days.TABLE_NAME + "." + Days.COLUMN_DATE + "= ? ";

    private Cursor getVertretungenByDate(Uri uri, String[] projection, String sortOrder) {
        String date = Vertretungen.getDateFromUri(uri);

        String[] selectionArgs = {date};
        String selection = sVertretungenDateSelection;
        return sVertretungenByDateQueryBuilder.query(mDbHelper.getReadableDatabase(), projection,
                selection, selectionArgs, null, null, sortOrder);
    }


    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = VertretungsplanContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, VertretungsplanContract.PATH_ABSENT_CLASSES, ABSENT_CLASSES);
        matcher.addURI(authority, VertretungsplanContract.PATH_DAYS, DAYS);
        matcher.addURI(authority, VertretungsplanContract.PATH_VERTRETUNGEN + "/#", VERTRETUNGEN_WITH_DATE);
        matcher.addURI(authority, VertretungsplanContract.PATH_VERTRETUNGEN, VERTRETUNGEN);
        matcher.addURI(authority, VertretungsplanContract.PATH_VERTRETUNGEN + "/#/#", VERTRETUNGEN_WITH_DATE_AND_ID);
        return matcher;
    }
}
