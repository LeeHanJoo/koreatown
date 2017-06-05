package kr.nt.koreatown.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import kr.nt.koreatown.R;
import kr.nt.koreatown.vo.ClusterVO;

/**
 * Created by user on 2017-06-05.
 */

public class MyClusterRenderer extends DefaultClusterRenderer<ClusterVO> {

    private View mClusterView;
    private Context context;
    private ClusterManager<ClusterVO> clusterManager;
    public MyClusterRenderer(Context context, GoogleMap maps, ClusterManager<ClusterVO> clusterManager){
        super(context, maps, clusterManager);
        this.context = context;
        this.clusterManager = clusterManager;
    }

    public void setClusterView(View mClusterView){
        this.mClusterView = mClusterView;
    }




    @Override
    protected void onBeforeClusterItemRendered(ClusterVO markerItem, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(markerItem, markerOptions);

       // BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context, mClusterView,1));
      //  markerOptions.icon(bitmap);
        //LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());
        //String spotname = markerItem.getSpotname();

       // tv_marker.setText(spotname);
       // tv_marker.setBackgroundResource(R.drawable.ic_marker_phone);
       // tv_marker.setTextColor(Color.BLACK);

      //  markerOptions.title(spotname);
       // markerOptions.position(markerItem.getLocation());
       // markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActivity(), marker_root_view)));

    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<ClusterVO> cluster) {
        return cluster.getSize() > 0;
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<ClusterVO> cluster, MarkerOptions markerOptions){

        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context, mClusterView,cluster.getSize()));
        markerOptions.icon(bitmap);

    }



    private Bitmap createDrawableFromView(Context context, View view, int cnt) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        TextView cntView = (TextView)view.findViewById(R.id.cnt);
        cntView.setText(""+cnt);

        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)); view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels); view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels); view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


}
