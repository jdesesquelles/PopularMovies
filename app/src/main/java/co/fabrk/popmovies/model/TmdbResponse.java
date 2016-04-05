
package co.fabrk.popmovies.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.validation.Valid;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class TmdbResponse implements Parcelable
{

    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("results")
    @Expose
    @Valid
    private List<Result> results = new ArrayList<Result>();
    @SerializedName("total_results")
    @Expose
    private int totalResults;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    public final static Parcelable.Creator<TmdbResponse> CREATOR = new Creator<TmdbResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public TmdbResponse createFromParcel(Parcel in) {
            TmdbResponse instance = new TmdbResponse();
            instance.page = ((int) in.readValue((int.class.getClassLoader())));
            in.readList(instance.results, (co.fabrk.popmovies.model.Result.class.getClassLoader()));
            instance.totalResults = ((int) in.readValue((int.class.getClassLoader())));
            instance.totalPages = ((int) in.readValue((int.class.getClassLoader())));
            return instance;
        }

        public TmdbResponse[] newArray(int size) {
            return (new TmdbResponse[size]);
        }

    }
    ;

    /**
     * 
     * @return
     *     The page
     */
    public int getPage() {
        return page;
    }

    /**
     * 
     * @param page
     *     The page
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * 
     * @return
     *     The results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     * 
     * @param results
     *     The results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

    /**
     * 
     * @return
     *     The totalResults
     */
    public int getTotalResults() {
        return totalResults;
    }

    /**
     * 
     * @param totalResults
     *     The total_results
     */
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    /**
     * 
     * @return
     *     The totalPages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * 
     * @param totalPages
     *     The total_pages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(page);
        dest.writeList(results);
        dest.writeValue(totalResults);
        dest.writeValue(totalPages);
    }

    public int describeContents() {
        return  0;
    }

}
