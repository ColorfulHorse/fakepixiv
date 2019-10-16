package com.lyj.fakepixiv.app.utils;

import android.graphics.drawable.Drawable;

import com.bumptech.glide.request.transition.Transition;

public class PaddingAnimation<T extends Drawable> implements Transition<T> {
	private final Transition<? super T> realAnimation;

	public PaddingAnimation(Transition<? super T> animation) {
		this.realAnimation = animation;
	}

	@Override
	public boolean transition(T current, ViewAdapter adapter) {
		int width = current.getIntrinsicWidth();
		int height = current.getIntrinsicHeight();
		return realAnimation.transition(current, new PaddingViewAdapter(adapter, width, height));
	}
}