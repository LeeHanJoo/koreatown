package kr.nt.koreatown.view;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Created by user on 2017-04-27.
 */

public class ExpandView extends Animation {

    private View animationView = null;
    private LinearLayout.LayoutParams mViewLayoutParams;
    private int delta;
    private int baseHeight;
    public ExpandView(View view, int duration,int startHeight, int endHeight){

        animationView = view;
        setDuration(duration);
        mViewLayoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();

        baseHeight = startHeight;
        delta = endHeight - startHeight;

        animationView.getLayoutParams().height = baseHeight;
        animationView.requestLayout();
        //mHeight = mViewLayoutParams.height;
       // mMarginEnd = (mMarginStart == 0 ? (0 - view.getHeight()) : 0);


    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        if (interpolatedTime < 1.0f) {
            int val = baseHeight + (int) (delta * interpolatedTime);
            animationView.getLayoutParams().height = val;
            animationView.requestLayout();
        } else {
            int val = baseHeight + delta;
            animationView.getLayoutParams().height = val;
            animationView.requestLayout();
        }

      /*  if (interpolatedTime < 1.0f) {

            // Calculating the new bottom margin, and setting it
            mViewLayoutParams.bottomMargin = mMarginStart
                    + (int) ((mMarginEnd - mMarginStart) * interpolatedTime);

            // Invalidating the layout, making us seeing the changes we made
            mAnimatedView.requestLayout();
            mAnimatedView.setVisibility(View.VISIBLE);
            // Making sure we didn't run the ending before (it happens!)
        } else if (!mWasEndedAlready) {
            mViewLayoutParams.bottomMargin = mMarginEnd;
            mAnimatedView.requestLayout();

            if (mIsVisibleAfter) {
                mAnimatedView.setVisibility(View.GONE);
            }
            mWasEndedAlready = true;
        }*/
    }

}
