package com.lyj.fakepixiv.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.lyj.fakepixiv.GlideApp
import com.lyj.fakepixiv.R
import kotlin.reflect.*
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

/**
 * @author green sun
 *
 * @date 2019/12/20
 *
 * @desc
 */
class UrlImageSpan(val context: Context, val url: String, val placeholder: Int = R.drawable.placeholder_emoji)
    : ImageSpan(context, placeholder) {

    var img: Bitmap? = null

    override fun getDrawable(): Drawable {
        if (img == null) {
            GlideApp.with(context)
                    .asBitmap()
                    .load(url)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onLoadCleared(placeholder: Drawable?) {

                        }

                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            img = resource
                            val drawable = BitmapDrawable(context.resources, resource)
                            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                            try {

                                val drawableRef = ImageSpan::class.declaredMemberProperties
                                        .find { it.name == "mDrawable" } as KMutableProperty1<ImageSpan, Drawable>
                                drawableRef.set(this@UrlImageSpan, drawable)
                            }catch (e: Exception) {

                            }
//                            Resources resources = tv.getContext().getResources();
//                            int targetWidth = (int) (resources.getDisplayMetrics().widthPixels * 0.8);
//                            Bitmap zoom = zoom(resource, targetWidth);
//                            BitmapDrawable b = new BitmapDrawable(resources, zoom);
//
//                            b.setBounds(0, 0, b.getIntrinsicWidth(), b.getIntrinsicHeight());
//                            Field mDrawable;
//                            Field mDrawableRef;
//                            try {
//                                mDrawable = ImageSpan.class.getDeclaredField("mDrawable");
//                                mDrawable.setAccessible(true);
//                                mDrawable.set(UrlImageSpan.this, b);
//
//                                mDrawableRef = DynamicDrawableSpan.class.getDeclaredField("mDrawableRef");
//                                mDrawableRef.setAccessible(true);
//                                mDrawableRef.set(UrlImageSpan.this, null);
//
//                                picShowed = true;
//                                tv.setText(tv.getText());
//                            } catch (IllegalAccessException e) {
//                                e.printStackTrace();
//                            } catch (NoSuchFieldException e) {
//                                e.printStackTrace();
//                            }
                        }

                    })
        }
        return super.getDrawable()
    }

    fun zoom(bmp: Bitmap, size: Int): Bitmap {
        val scale = size.toFloat() / bmp.width
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        return Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
    }

}