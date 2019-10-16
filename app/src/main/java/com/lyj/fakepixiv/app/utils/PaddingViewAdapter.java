package com.lyj.fakepixiv.app.utils;

import android.graphics.drawable.*;
import android.os.Build.*;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.request.transition.Transition;


public class PaddingViewAdapter implements Transition.ViewAdapter {
	private final Transition.ViewAdapter realAdapter;
	private final int targetWidth;
	private final int targetHeight;

	public PaddingViewAdapter(Transition.ViewAdapter adapter, int targetWidth, int targetHeight) {
		this.realAdapter = adapter;
		this.targetWidth = targetWidth;
		this.targetHeight = targetHeight;
	}

	@Override public View getView() {
		return realAdapter.getView();
	}

	@Override public Drawable getCurrentDrawable() {
		Drawable drawable = realAdapter.getCurrentDrawable();
		if (drawable != null) {
			int padX = (targetWidth - drawable.getIntrinsicWidth()) / 2;
			int padY = (targetHeight - drawable.getIntrinsicHeight()) / 2;
			drawable = new InsetDrawable(drawable, padX, padY, padX, padY);
			Log.e("xxx", "padX:"+padX+"padY:"+padY);
//			int padX = Math.max(0, targetWidth - drawable.getIntrinsicWidth()) / 2;
//			int padY = Math.max(0, targetHeight - drawable.getIntrinsicHeight()) / 2;
//			if (padX > 0 || padY > 0) {
//				drawable = new InsetDrawable(drawable, padX, padY, padX, padY);
//			}
		}
		return drawable;
	}

	@Override public void setDrawable(Drawable drawable) {
		if (VERSION.SDK_INT >= VERSION_CODES.M && drawable instanceof TransitionDrawable) {
			// For some reason padding is taken into account differently on M than before in LayerDrawable
			// PaddingMode was introduced in 21 and gravity in 23, I think NO_GRAVITY default may play
			// a role in this, but didn't have time to dig deeper than this.
			((TransitionDrawable)drawable).setPaddingMode(TransitionDrawable.PADDING_MODE_STACK);
		}
		realAdapter.setDrawable(drawable);
	}
}
