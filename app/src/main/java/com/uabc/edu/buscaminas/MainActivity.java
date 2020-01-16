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

import java.util.Random;

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

        this.disponerBombas();
        this.contarBombasPerimetro();
        activo = true;

        fondo.invalidate();
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
                                //TODO Recorrer
                                recorrer(i,j);

                            }
                            //TODO Invalidar el fondo
                            fondo.invalidate();
                        }
                    }
                }
            }

        if (gano() && activo) {
            Toast.makeText(this,
                    "Ganaste",
                    Toast.LENGTH_LONG).show();
            activo = false;
        }
        return true;
    }

    private boolean gano(){
        int cant=0;
        for (int i = 0; i < 8 ; i++) {
            for (int j = 0; j <8 ; j++) {
                if(casillas[i][j].isDestapado()){
                    cant++;
                }
            }
        }

        if(cant==56){
            return true;
        }else{
            return false;
        }
    }

    private void disponerBombas(){
        int cantidad=8;
        do{
            Random random=new Random();
            random.nextInt(8);
            int fila= (int)random.nextInt(8);
            int columna=(int)random.nextInt(8);
            if(casillas[fila][columna].getContenido()==VALOR){
                casillas[fila][columna].setContenido(BOMBA);
                cantidad--;
            }
        }while (cantidad!=0);
    }
    private int contarCoordenadas(int fila,int columna){
        int total=0;
        if(fila-1 >=0 && columna-1 >=0){
            if(casillas[fila-1][columna-1].getContenido()==BOMBA){
                total++;
            }
        }
        if(fila-1>=0){
            if(casillas[fila-1][columna].getContenido()==BOMBA){
                total++;
            }
        }
        if(fila-1 >=0 && columna+1<8){
            if(casillas[fila-1][columna+1].getContenido()==BOMBA){
                total++;
            }
        }
        if (columna + 1 < 8) {
            if (casillas[fila][columna + 1].getContenido() == BOMBA)
                total++;
        }
        if (fila + 1 < 8 && columna + 1 < 8) {
            if (casillas[fila + 1][columna + 1].getContenido() == BOMBA)
                total++;
        }

        if (fila + 1 < 8) {
            if (casillas[fila + 1][columna].getContenido() == BOMBA)
                total++;
        }
        if (fila + 1 < 8 && columna - 1 >= 0) {
            if (casillas[fila + 1][columna - 1].getContenido() == BOMBA)
                total++;
        }
        if (columna - 1 >= 0) {
            if (casillas[fila][columna - 1].getContenido() == BOMBA)
                total++;
        }
        return total;
    }

private void recorrer(int fila, int columna){
        if(fila>=0 && fila <8 && columna >=0 && columna<8){
            if(casillas[fila][columna].getContenido()==VALOR){
                casillas[fila][columna].setDestapado(true);
                casillas[fila][columna].setContenido(50);
                recorrer(fila,columna+1);
                recorrer(fila,columna-1);
                recorrer(fila-1,columna);
                recorrer(fila+1,columna);
                recorrer(fila-1,columna-1);
                recorrer(fila+1,columna+1);
                recorrer(fila-1,columna+1);
                recorrer(fila+1,columna-1);
            }else if(casillas[fila][columna].getContenido() >=1
            &&casillas[fila][columna].getContenido()<=8){
                casillas[fila][columna].setDestapado(true);
            }
        }
}
private void contarBombasPerimetro() {
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                if (casillas[f][c].getContenido() == 0) {
                    int cant = contarCoordenadas(f, c);
                    casillas[f][c].setContenido(cant);
                }
            }
        }
    }


    class Tablero extends View{

        public Tablero(Context context) {
            super(context);
        }

        protected void onDraw(Canvas canvas) {
            canvas.drawRGB(0, 0, 0);
            int ancho = 0;
            if (canvas.getWidth() < canvas.getHeight())
                ancho = fondo.getWidth();
            else
                ancho = fondo.getHeight();
            int anchocua = ancho / 8;
            Paint paint = new Paint();
            paint.setTextSize(50);
            Paint paint2 = new Paint();
            paint2.setTextSize(50);
            paint2.setTypeface(Typeface.DEFAULT_BOLD);
            paint2.setARGB(255, 0, 0, 255);
            Paint paintlinea1 = new Paint();
            paintlinea1.setARGB(255, 255, 255, 255);
            int filaact = 0;
            for (int f = 0; f < 8; f++) {
                for (int c = 0; c < 8; c++) {
                    casillas[f][c].fijarXY(c * anchocua, filaact, anchocua);
                    if (casillas[f][c].isDestapado() == false)
                        paint.setARGB(153, 204, 204, 204);
                    else
                        paint.setARGB(255, 153, 153, 153);
                    canvas.drawRect(c * anchocua, filaact, c * anchocua
                            + anchocua - 2, filaact + anchocua - 2, paint);
                    // linea blanca
                    canvas.drawLine(c * anchocua, filaact, c * anchocua
                            + anchocua, filaact, paintlinea1);
                    canvas.drawLine(c * anchocua + anchocua - 1, filaact, c
                                    * anchocua + anchocua - 1, filaact + anchocua,
                            paintlinea1);

                    if (casillas[f][c].getContenido() >= 1
                            && casillas[f][c].getContenido() <= 8
                            && casillas[f][c].isDestapado())
                        canvas.drawText(
                                String.valueOf(casillas[f][c].getContenido()), c
                                        * anchocua + (anchocua / 2) - 8,
                                filaact + anchocua / 2, paint2);

                    if (casillas[f][c].getContenido() == 80
                            && casillas[f][c].isDestapado()) {
                        Paint bomba = new Paint();
                        bomba.setARGB(255, 255, 0, 0);
                        canvas.drawCircle(c * anchocua + (anchocua / 2),
                                filaact + (anchocua / 2), 8, bomba);
                    }

                }
                filaact = filaact + anchocua;
            }
        }
    }
}
