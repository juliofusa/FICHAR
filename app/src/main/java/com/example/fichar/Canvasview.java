package com.example.fichar;

/**
 * Created by JULIO on 06/03/2017.
 */
/**
 * Created by JULIO on 15/10/2015.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.provider.Settings;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * clase personalizada de dibujo
 */
public class Canvasview extends View {

    public int width;
    public int height,color= Color.BLUE;
    private Bitmap mBitmap;
    public Bitmap temp;
    private Canvas mCanvas;
    private Path mPath;
    Context context;
    private Paint mPaint;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    Paint Marcagua = new Paint();
    Paint LOGO = new Paint();

    public Canvasview(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        // we set a new Path
        mPath = new Path();

        // and we set a new Paint with the desired attributes
        mPaint = new Paint();
        // Valores iniciales por defecto
        SetDefaultSetings();
    }
    private void SetDefaultSetings(){
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);




    }
    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw the mPath with the mPaint on the canvas when onDraw

        canvas.drawPath(mPath, mPaint);
        Marcagua.setTypeface(Typeface.SANS_SERIF);
        Marcagua.setTextSize(30);
        Marcagua.setColor(getResources().getColor(R.color.gris));
        //canvas.scale(1f, -1f, 10, 10);
        //Marcagua.setTextAlign(Paint.Align.CENTER);
        Marcagua.setAlpha(75);

        canvas.drawText(getResources().getString(R.string.SERVICIO)+ADAPTADORES.tiempocompleto(), 50 , getHeight()-10, Marcagua);

        Bitmap icon = BitmapFactory.decodeResource( getResources(), R.drawable.log_eulenbn);
        LOGO.setAlpha(20);
        canvas.drawBitmap( icon, (getWidth()/ 2) - (icon.getWidth() / 2.f), (getHeight() / 2.f) - (icon.getHeight()/2.f), LOGO);

    }

    // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;

    }

    // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    public void clearCanvas() {
        mPath.reset();
        invalidate();
    }
    public Bitmap getBitmapFromView()
    {
        temp= mBitmap;
        return temp;
    }
    public void Color_rojo(int grosor) {
        mPath.reset();
        invalidate();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(grosor);
    }
    public void Color_azul(int grosor) {
        mPath.reset();
        invalidate();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(grosor);
    }
    public void Color_NEGRO(int grosor) {
        mPath.reset();
        invalidate();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(grosor);
    }
    public void Color_VERDE(int grosor) {
        mPath.reset();
        invalidate();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.rgb(46,189,25));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(grosor);
    }
    public void Color_VIOLETA(int grosor){
        mPath.reset();
        invalidate();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.rgb(170,102,204));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(grosor);
    }
    // when ACTION_UP stop touch
    private void upTouch() {
        mPath.lineTo(mX, mY);
    }

    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
        }
        return true;
    }


}
