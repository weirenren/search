package acr.browser.lightning.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import acr.browser.lightning.ILightningViewGetter;

/**
 * Created by weichao13 on 2017/8/6.
 */

public class TouchableContainer extends LinearLayout implements ILightningViewGetter {

    private OnTouchListener mTouchListener;

    private LightningView mLightningView;

    public void setLightningView(LightningView lightningView) {
        this.mLightningView = lightningView;
    }

    public void setTouchListener(OnTouchListener touchListener) {
        if (touchListener != null) {
            this.mTouchListener = touchListener;
        }
    }
    public TouchableContainer(Context context) {
        super(context);
    }

    public TouchableContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchableContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mTouchListener != null) {
            mTouchListener.onTouch(this, ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public LightningView getView() {
        return mLightningView;
    }

}
