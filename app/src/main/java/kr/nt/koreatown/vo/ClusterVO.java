package kr.nt.koreatown.vo;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by user on 2017-05-12.
 */

public class ClusterVO implements ClusterItem {

    private LatLng location;
    private FeedVO.Feed feed;

    public FeedVO.Feed getFeed() {
        return feed;
    }

    public void setFeed(FeedVO.Feed feed) {
        this.feed = feed;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    @Override
    public LatLng getPosition() {
        return location;
    }
}
