package com.simulation.neteasy.view;

import com.zifeng.wangyi.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;


/**
 * 
 * @author 紫枫
 *
 */
public class MyProgressBar extends ProgressBar {

	private String zifengContent;
	public MyProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.costom);
		zifengContent = typedArray.getString(typedArray
				.getIndex(R.styleable.costom_text));

	}
	
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		float width = getWidth() / 2;
		float measureWidth = paint.measureText(zifengContent);
		float start = width - measureWidth / 2;
		canvas.drawText(zifengContent, start, getHeight() / 4 * 3, paint);
	}

	
	public void setText(String content) {
		this.zifengContent = content;
		invalidate();
	}

}
