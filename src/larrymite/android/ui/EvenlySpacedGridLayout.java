package larrymite.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class EvenlySpacedGridLayout extends ViewGroup {

	int rowPadding;

	public EvenlySpacedGridLayout(Context context) {
		super(context);
	}

	public EvenlySpacedGridLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setValuesFromAttributes(context, attrs);
	}

	public EvenlySpacedGridLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setValuesFromAttributes(context, attrs);
	}

	private void setValuesFromAttributes(Context context, AttributeSet attrs) {
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EvenlySpacedGrid, 0, 0);
		try {
			rowPadding = (int) ta.getDimension(R.styleable.EvenlySpacedGrid_rowPadding, 0);
		} finally {
			ta.recycle();
		}
	}

	public void setRowPadding(int rowPadding) {
		this.rowPadding = rowPadding;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int desiredWidth = 240;
		int desiredHeight = 240;

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width, innerWidth, height;

		if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
			width = widthSize;
		} else {
			width = desiredWidth;
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else if (widthMode == MeasureSpec.AT_MOST) {
			height = Math.min(heightSize, desiredHeight);
		} else {
			int numElems = getVisibleElemCount();
			innerWidth = width - getPaddingLeft() - getPaddingRight();
			if (numElems > 0) {
				height = 0;
				List<View> elems = getVisibleElems();
				for (int i = 0; i < numElems; i++) {
					measureChild(elems.get(i), widthMeasureSpec, heightMeasureSpec);
				}

				int elemWidth = elems.get(0).getMeasuredWidth();
				if (elemWidth == 0) {
					elemWidth = 10;
				}
				int elemHeight = elems.get(0).getMeasuredHeight();
				int numCols = innerWidth / elemWidth;
				int numRows = (numElems / numCols);
				if (numElems % numCols != 0) {
					numRows++;
				}
				height = (numRows * elemHeight) + ((numRows - 1) * rowPadding);
				height += getTopPaddingOffset();
				height += getBottomPaddingOffset();
			} else {
				height = desiredHeight;
			}
		}

		setMeasuredDimension(width, height);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

		boolean fixedVerticalSpacing = true;

		int numElems = getVisibleElemCount();

		if (numElems > 0) {
			List<View> elems = getVisibleElems();
			int width = getMeasuredWidth();
			int innerWidth = width - getPaddingLeft() - getPaddingRight();
			int elemWidth = elems.get(0).getMeasuredWidth();
			if (elemWidth == 0) {
				elemWidth = 10;
			}
			int elemHeight = elems.get(0).getMeasuredHeight();
			if (elemHeight == 0) {
				elemHeight = 10;
			}
			int numCols = innerWidth / elemWidth;
			int extraSpace = innerWidth % elemWidth;

			if (numCols > numElems) {
				extraSpace += (numCols - numElems) * elemWidth;
			}

			int spacing = extraSpace / Math.min(numCols, numElems);

			for (int i = 0; i < numElems; i++) {
				int col = i % numCols;
				int row = i / numCols;

				final View child = elems.get(i);

				int childLeft = left + getPaddingLeft() + ((col * elemWidth) + (col * spacing) + (spacing / 2));
				int childTop = getTopPaddingOffset();
				if (fixedVerticalSpacing) {
					childTop += (row * elemHeight) + (row * rowPadding);

				} else {
					childTop += ((row * elemHeight) + (row * spacing) + (spacing / 2));
				}

				child.layout(childLeft, childTop, childLeft + child.getMeasuredWidth(), childTop
						+ child.getMeasuredHeight());
			}
		}
	}

	private int getVisibleElemCount() {
		int count = 0;
		for (int i = 0; i < getChildCount(); i++) {
			if (getChildAt(i).getVisibility() != View.GONE) {
				count++;
			}
		}
		return count;
	}

	private List<View> getVisibleElems() {
		List<View> visibleElems = new ArrayList<View>();

		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			if (view.getVisibility() != View.GONE) {
				visibleElems.add(view);
			}
		}

		return visibleElems;
	}

}
