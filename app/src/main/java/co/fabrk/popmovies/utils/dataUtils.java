package co.fabrk.popmovies.utils;

import android.net.Uri;

import java.util.HashSet;
import java.util.StringTokenizer;

/**
 * Created by ebal on 03/01/16.
 */
public class dataUtils {

    public static class ContentProvider {
        public static final String CONTENT_AUTHORITY = null;
        private static final Uri BASE_CONTENT_URI = null;
        HashSet<String> publishedResources;
    }
        // Data contract
    public static class DataContract {
        ContentProvider contentprovider;
        HashSet<Column> tableHashSet;
    }

    public static class Table {
        String name;
        HashSet<Column> columnHashSet;
    }

    public static class Column {
        int Index;
        String name;
    }
}
