package es.upm.miw.SolitarioCelta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Rioger on 29/10/2016.
 */

public class AlertDialogReadFragment extends DialogFragment {
    private static String LOG_TAG = "MIW_FEM";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity main = (MainActivity) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder
                .setTitle(R.string.txtDialogoFinalReadTitulo)
                .setMessage(R.string.txtDialogoFinalReadPregunta)
                .setPositiveButton(
                        getString(R.string.txtDialogoFinalReadAfirmativo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String textoleido = main.leerFicheroPartidas();
                                JuegoCelta juego = main.getJuego();
                                juego.deserializaTablero(textoleido);
                                main.mostrarTablero();

                                Log.d(LOG_TAG, "partida recuperada....." + textoleido);
                            }
                        }
                )
                .setNegativeButton(
                        getString(R.string.txtDialogoFinalReadNegativo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }
                );

        return builder.create();
    }
}
