package es.upm.miw.SolitarioCelta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    private static String LOG_TAG = "MIW_FEM";
    private static String NOMBRE_JUGADOR = "RIOGER";

    JuegoCelta juego;
    String tableroserializado;
    String tableroserializadoresultado;


    private final String GRID_KEY = "GRID_KEY";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        juego = new JuegoCelta();
        mostrarTablero();

        TextView textoEditado;
        textoEditado= (TextView) findViewById(R.id.TextView2);
        tableroserializadoresultado = juego.serializaTablero();
        int NumeroPiezasQuedan = this.NumeroDeFichas(tableroserializadoresultado, "1");
        String numero= NumeroPiezasQuedan+"";
        textoEditado.setText(numero);

    }

    /**
     * Se ejecuta al pulsar una ficha
     * Las coordenadas (i, j) se obtienen a partir del nombre, ya que el botón
     * tiene un identificador en formato pXY, donde X es la fila e Y la columna
     *
     * @param v Vista de la ficha pulsada
     */
    public void fichaPulsada(View v) {
        String resourceName = getResources().getResourceEntryName(v.getId());
        int i = resourceName.charAt(1) - '0';   // fila
        int j = resourceName.charAt(2) - '0';   // columna

        juego.jugar(i, j);

        TextView textoEditado;
        textoEditado= (TextView) findViewById(R.id.TextView2);
        tableroserializadoresultado = juego.serializaTablero();
        int NumeroPiezasQuedan = this.NumeroDeFichas(tableroserializadoresultado, "1");
        String numero= NumeroPiezasQuedan+"";
        textoEditado.setText(numero);

        mostrarTablero();
        if (juego.juegoTerminado()) {
            // TODO guardar puntuación

            tableroserializadoresultado = juego.serializaTablero();
            java.util.Date fecha = new Date();
            DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = fechaHora.format(fecha);

            int numerodepiezas = this.NumeroDeFichas(tableroserializadoresultado, "1");

            this.escribirFicheroResultados(NOMBRE_JUGADOR, date, numerodepiezas);

            new AlertDialogFragment().show(getFragmentManager(), "ALERT DIALOG");
        }

    }

    /**
     * Visualiza el tablero
     */
    public void mostrarTablero() {
        RadioButton button;
        String strRId;
        String prefijoIdentificador = getPackageName() + ":id/p"; // formato: package:type/entry
        int idBoton;

        TextView textoEditado;
        textoEditado= (TextView) findViewById(R.id.TextView2);
        tableroserializadoresultado = juego.serializaTablero();
        int NumeroPiezasQuedan = this.NumeroDeFichas(tableroserializadoresultado, "1");
        String numero= NumeroPiezasQuedan+"";
        textoEditado.setText(numero);

        for (int i = 0; i < JuegoCelta.TAMANIO; i++)
            for (int j = 0; j < JuegoCelta.TAMANIO; j++) {
                strRId = prefijoIdentificador + Integer.toString(i) + Integer.toString(j);
                idBoton = getResources().getIdentifier(strRId, null, null);
                if (idBoton != 0) { // existe el recurso identificador del botón
                    button = (RadioButton) findViewById(idBoton);
                    button.setChecked(juego.obtenerFicha(i, j) == JuegoCelta.FICHA);
                }
            }
    }

    /**
     * Guarda el estado del tablero (serializado)
     *
     * @param outState Bundle para almacenar el estado del juego
     */
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(GRID_KEY, juego.serializaTablero());
        super.onSaveInstanceState(outState);

    }

    /**
     * Recupera el estado del juego
     *
     * @param savedInstanceState Bundle con el estado del juego almacenado
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String grid = savedInstanceState.getString(GRID_KEY);
        juego.deserializaTablero(grid);
        mostrarTablero();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcAjustes:
                startActivity(new Intent(this, SCeltaPrefs.class));
                return true;

            case R.id.opcAcercaDe:
                startActivity(new Intent(this, AcercaDe.class));
                return true;

            case R.id.opcReiniciarPartida:
                new AlertDialogResetFragment().show(getFragmentManager(), "ALERT DIALOG");

                return true;

            case R.id.opcGuardarPartida:

                tableroserializado = juego.serializaTablero();
                new AlertDialogSaveFragment().show(getFragmentManager(), "ALERT DIALOG");

                return true;

            case R.id.opcRecuperarPartida:

                new AlertDialogReadFragment().show(getFragmentManager(), "ALERT DIALOG");

                return true;

            case R.id.opcMejoresResultados:

                new AlertDialogBestResultFragment().show(getFragmentManager(), "ALERT DIALOG");

                return true;

            default:
                Toast.makeText(
                        this,
                        getString(R.string.txtSinImplementar),
                        Toast.LENGTH_SHORT
                ).show();
        }
        return true;
    }


    public void escribirFicheroPartidas(String tableroserializado) {
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(openFileOutput("fichero_solitario_celta.txt", Context.MODE_PRIVATE));
            writer.write(tableroserializado + "\n");
        } catch (Exception ex) {
            Log.e("Error", "Error al escribir fichero");
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String leerFicheroPartidas() {
        try {
            BufferedReader fin =
                    new BufferedReader(new InputStreamReader(openFileInput("fichero_solitario_celta.txt")));

            String linea = fin.readLine();
            String todo = "";
            while (linea != null) {
                todo = todo + linea;
                linea = fin.readLine();
            }
            fin.close();

            return todo;

        } catch (Exception ex) {
            Log.e("Error", "Error al leer fichero");
        }
        return "";
    }

    public String getJuegoSerializado() {
        return this.tableroserializado;
    }

    public JuegoCelta getJuego() {
        return this.juego;
    }

    public void escribirFicheroResultados(String nombre, String fecha, int numero_piezas) {
        OutputStreamWriter writer = null;
        try {
            String resultado = nombre + "      " + fecha + "      " + numero_piezas;

            Log.d(LOG_TAG, "Resultado guardado --- " + resultado);

            writer = new OutputStreamWriter(openFileOutput("fichero_resultados.txt", Context.MODE_APPEND));
            writer.write(resultado + "\n");
        } catch (Exception ex) {
            Log.e("Error", "Error al escribir fichero");
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String leerFicheroResultados() {
        try {
            BufferedReader fin =
                    new BufferedReader(new InputStreamReader(openFileInput("fichero_resultados.txt")));

            String linea = fin.readLine();
            String todo = "";
            while (linea != null) {
                todo = todo + linea + "\n" + "\n";
                linea = fin.readLine();
            }
            fin.close();

            Log.d(LOG_TAG, "Resultados leidos.....   " + todo);
            return todo;

        } catch (Exception ex) {
            Log.e("Error", "Error al leer fichero");
        }
        return "";
    }


    public int NumeroDeFichas(String tableroserializadoresultado, String caracter) {
        int posicion, contador = 0;

        posicion = tableroserializadoresultado.indexOf(caracter);
        while (posicion != -1) {
            contador++;

            posicion = tableroserializadoresultado.indexOf(caracter, posicion + 1);
        }
        return contador;
    }

    public void BorrarContenidoFichero(){
//        String sFichero = "fichero_resultados.txt";
//        File f = new File(sFichero);
//        f.delete();
//        try {
//            f.createNewFile();
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(openFileOutput("fichero_resultados.txt", Context.MODE_PRIVATE));
            writer.write("");
        } catch (Exception ex) {
            Log.e("Error", "Error al borrar al contenido del fichero");
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
