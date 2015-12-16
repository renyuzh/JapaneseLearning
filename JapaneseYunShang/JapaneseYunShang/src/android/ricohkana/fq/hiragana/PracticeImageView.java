package android.ricohkana.fq.hiragana;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.ricohkana.fq.utils.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class PracticeImageView extends ImageView {
	protected int CANVAS_HEIGHT = 336;
	protected int CANVAS_WIDTH = 336;
	private Bitmap cacheBitmap = null;
	private Canvas cacheCanvas = null;
	protected Paint paint = null;
	protected Path path = null;
	private float preX = 0.0F;
	private float preY = 0.0F;

	public PracticeImageView(Context paramContext,
			AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}

	public void clear() {
		this.cacheBitmap = Bitmap.createBitmap(this.CANVAS_WIDTH,
				this.CANVAS_HEIGHT, Bitmap.Config.ARGB_4444);
		this.cacheCanvas = new Canvas();
		this.cacheCanvas.setBitmap(this.cacheBitmap);
		this.path = new Path();
		this.paint = new Paint();
		invalidate();
	}

	protected void onDraw(Canvas paramCanvas) {
		Paint localPaint = new Paint();
		paramCanvas.drawBitmap(this.cacheBitmap, 0.0F, 0.0F, localPaint);
		super.onDraw(paramCanvas);
	}

	public boolean onTouchEvent(MotionEvent paramMotionEvent) {
		this.paint.setAntiAlias(true);
		this.paint.setStrokeWidth(Constant.paintStrokeWidth);
		this.paint.setDither(true);
		this.paint.setStyle(Paint.Style.STROKE);
		this.paint.setStrokeJoin(Paint.Join.ROUND);
		this.paint.setStrokeCap(Paint.Cap.ROUND);
		float f1 = paramMotionEvent.getX();
		float f2 = paramMotionEvent.getY();

		switch (paramMotionEvent.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//起始点
			this.path.moveTo(f1, f2);
			this.preX = f1;
			this.preY = f2;
			return true;
		case MotionEvent.ACTION_MOVE:
			//preX, preY为操作点，f1, f2为终点，这里只是设置path的属性，并不负责绘制
			this.path.quadTo(this.preX, this.preY, f1, f2);
			// 绘制曲线（Path）
			this.cacheCanvas.drawPath(this.path, this.paint);
			//更新绘制
			invalidate();
			//第二次执行时，第一次结束调用的坐标值将作为第二次调用的初始坐标值
			this.preX = f1;
			this.preY = f2;
			this.path.moveTo(f1, f2);
			return true;
		case MotionEvent.ACTION_UP:
			return true;
		default:
			break;
		}

		return super.onTouchEvent(paramMotionEvent);
	}
}
