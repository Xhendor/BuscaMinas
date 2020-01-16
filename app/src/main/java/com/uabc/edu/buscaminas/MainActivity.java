package com.uabc.edu.buscaminas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements View.OnTouchListener {

    private Tablero fondo;
    int x,y;
    private Casilla[][] casillas;
    private boolean activo=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        LinearLayout layout= findViewById(R.id.linearLayout1);
        fondo=new Tablero(this);
        fondo.setOnTouchListener(this);
        layout.addView(fondo);
        reiniciar();
        getSupportActionBar().hide();
        Button reiniciar= findViewById(R.id.boton);
        reiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reiniciar();
            }
        });
    }

    private void reiniciar() {
        casillas=new Casilla[8][8];
        for(int f=0;f<8;f++){ for(int c=0;c<8;c++){
            casillas[f][c]=new Casilla();
        }}
    }
                  private final int BOMBA=80;
                  private final int VALOR=0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

            if(activo){

                for (int i = 0; i < 8 ; i++) {
                    for (int j = 0; j < 8 ; j++) {

                        if(casillas[i][j].
                                dentro((int)event.getX(),
                                        (int)event.getY())){
                            casillas[i][j].setDestapado(true);
                            if(casillas[i][j].getContenido()==BOMBA){
                                Toast.makeText(this,
                                        "BOOOM!!!",Toast.LENGTH_SHORT)
                                        .show();
                                activo=false;
                            }else if(casillas[i][j].getContenido()==VALOR){
                                //Recorrer
                                //Invalidar el fondo
                            }

                        }
                    }
                }
            }

        return false;
    }


    class Tablero extends View{

        public Tablero(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {

            canvas.drawRGB(0,0,0);
            canvas.getWidth();
            canvas.getHeight();
            Paint paint=new Paint();
            paint.setTextSize(50);
            Paint paint2=new Paint();
            paint2.setTextSize(50);
            paint2.setTypeface(Typeface.DEFAULT_BOLD);
            paint2.setARGB(255,0,0,255);
            Paint paintLineal=new Paint();
            paintLineal.setARGB(255,255,255,255);



        }
    }
}
