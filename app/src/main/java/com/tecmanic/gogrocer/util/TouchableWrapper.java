package com.tecmanic.gogrocer.util;

import android.content.Context;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;

public class TouchableWrapper extends FrameLayout {
    LatLng geographicalPosition;
    private GoogleMap mGoogleMap = null;
    ScaleGestureDetector mScaleDetector = new ScaleGestureDetector(getContext(),
            new ScaleGestureDetector.SimpleOnScaleGestureListener() {

                @Override
                public boolean onScale(ScaleGestureDetector detector) {
                    float scaleFactor = detector.getScaleFactor();
                    float currentZoom = mGoogleMap.getCameraPosition().zoom;
                    int mapViewHeight = getHeight();
                    int mapViewWidth = getWidth();
                    if (scaleFactor > 1) {
                        if (currentZoom < 18) {
                            geographicalPosition = mGoogleMap.getProjection().fromScreenLocation(new Point(mapViewWidth / 2, mapViewHeight / 2));
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(geographicalPosition, currentZoom + 0.05f));
                        }
                    } else if (scaleFactor < 1) {
                        geographicalPosition = mGoogleMap.getProjection().fromScreenLocation(new Point(mapViewWidth / 2, mapViewHeight / 2));
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(geographicalPosition, currentZoom - 0.05f));
                    }
                    return true;
                }
            });
    private long mLastTouchTime;

    public TouchableWrapper(Context context) {
        super(context);
    }

    public void setGoogleMap(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (mGoogleMap != null) {
                    mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
                    long thisTime = System.currentTimeMillis();
                    lastTouchPoint(thisTime);
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                zoomControl();
                break;

            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                if (mGoogleMap != null) {
                    mGoogleMap.getUiSettings().setScrollGesturesEnabled(false);
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    private void zoomControl() {
        if (mGoogleMap != null) {
            mGoogleMap.getUiSettings().setZoomGesturesEnabled(false);
            mGoogleMap.getUiSettings().setScrollGesturesEnabled(false);
        }
    }

    private void lastTouchPoint(long thisTime) {
        if (thisTime - mLastTouchTime < ViewConfiguration.getDoubleTapTimeout()) {
            float currentZoom = mGoogleMap.getCameraPosition().zoom;
            int mapViewHeight = getHeight();
            int mapViewWidth = getWidth();
            Projection projection = mGoogleMap.getProjection();
            geographicalPosition = projection.fromScreenLocation(new Point(mapViewWidth / 2, mapViewHeight / 2));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(geographicalPosition.latitude, geographicalPosition.longitude), currentZoom + 1));
            mLastTouchTime = -1;
        } else {
            mLastTouchTime = thisTime;
            mGoogleMap.getUiSettings().setZoomGesturesEnabled(false);
        }
    }

}