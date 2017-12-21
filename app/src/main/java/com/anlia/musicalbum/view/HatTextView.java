package com.anlia.musicalbum.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.anlia.musicalbum.R;

/**
 * Created by anlia on 2017/12/20.
 */

public class HatTextView extends AppCompatTextView {
    public HatTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = changeBitmapSize(drawableToBitmap(getResources().getDrawable(R.drawable.hat64)),64,64);
        canvas.save();
        canvas.rotate(-45,32,32);
        canvas.drawBitmap(bitmap,-5,-5,null);
        canvas.restore();
    }

    /**
     * drawable图片资源转bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 改变bitmap的大小
     * @param bitmap 目标bitmap
     * @param newW 目标宽度
     * @param newH 目标高度
     * @return
     */
    public static Bitmap changeBitmapSize(Bitmap bitmap, int newW, int newH) {
        int oldW = bitmap.getWidth();
        int oldH = bitmap.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newW) / oldW;
        float scaleHeight = ((float) newH) / oldH;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, oldW, oldH, matrix, true);
        return bitmap;
    }
}
