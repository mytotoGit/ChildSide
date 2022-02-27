package com.ishuinzu.childside.animation;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

public class PushDownAnimationList implements PushDown {
    private final List<PushDownAnimation> pushDownList = new ArrayList<>();

    PushDownAnimationList(View... views) {
        for (View view : views) {
            PushDownAnimation pushDown = PushDownAnimation.setPushDownAnimationTo(view);
            pushDown.setOnTouchEvent(null);
            this.pushDownList.add(pushDown);
        }
    }

    @Override
    public PushDownAnimationList setScale(float scale) {
        for (PushDownAnimation pushDown : pushDownList) {
            pushDown.setScale(scale);
        }
        return this;
    }

    @Override
    public PushDown setScale(int mode, float scale) {
        for (PushDownAnimation pushDown : pushDownList) {
            pushDown.setScale(mode, scale);
        }
        return this;
    }

    @Override
    public PushDownAnimationList setDurationPush(long duration) {
        for (PushDownAnimation pushDown : pushDownList) {
            pushDown.setDurationPush(duration);
        }
        return this;
    }

    @Override
    public PushDownAnimationList setDurationRelease(long duration) {
        for (PushDownAnimation pushDown : pushDownList) {
            pushDown.setDurationRelease(duration);
        }
        return this;
    }

    @Override
    public PushDownAnimationList setInterpolatorPush(AccelerateDecelerateInterpolator interpolatorPush) {
        for (PushDownAnimation pushDown : pushDownList) {
            pushDown.setInterpolatorPush(interpolatorPush);
        }
        return this;
    }

    @Override
    public PushDownAnimationList setInterpolatorRelease(AccelerateDecelerateInterpolator interpolatorRelease) {
        for (PushDownAnimation pushDown : pushDownList) {
            pushDown.setInterpolatorRelease(interpolatorRelease);
        }
        return this;
    }

    @Override
    public PushDownAnimationList setOnClickListener(View.OnClickListener clickListener) {
        for (PushDownAnimation pushDown : pushDownList) {
            if (clickListener != null) {
                pushDown.setOnClickListener(clickListener);
            }
        }
        return this;
    }

    @Override
    public PushDown setOnLongClickListener(View.OnLongClickListener clickListener) {
        for (PushDownAnimation pushDown : pushDownList) {
            if (clickListener != null) {
                pushDown.setOnLongClickListener(clickListener);
            }
        }
        return this;
    }

    public PushDownAnimationList setOnTouchEvent(final View.OnTouchListener eventListener) {
        for (PushDownAnimation pushDown : pushDownList) {
            if (eventListener != null) {
                pushDown.setOnTouchEvent(eventListener);
            }
        }
        return this;
    }
}