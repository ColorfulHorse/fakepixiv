package com.lyj.fakepivix.app.utils;

import android.graphics.drawable.Drawable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.transition.TransitionFactory;

public class PaddingAnimationFactory<T extends Drawable> implements TransitionFactory<T> {
	private final DrawableCrossFadeFactory realFactory;

	public PaddingAnimationFactory(DrawableCrossFadeFactory factory) {
		this.realFactory = factory;
	}

	@Override
	public Transition<T> build(DataSource dataSource, boolean isFirstResource) {
		return new PaddingAnimation<>(realFactory.build(dataSource, isFirstResource));
	}
}
