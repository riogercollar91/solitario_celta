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

public class AlertDialogResetFragment extends DialogFragment {

    private static String LOG_TAG = "MIW_FEM";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity main = (MainActivity) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder
                .setTitle(R.string.txtDialogoFinalResetTitulo)
                .setMessage(R.string.txtDialogoFinalResetPregunta)
                .setPositiveButton(
                        getString(R.string.txtDialogoFinalResetAfirmativo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                main.juego.reiniciar();
                                main.mostrarTablero();
                                Log.d(LOG_TAG, "partida reiniciada.....");
                            }
                        }
                )
                .setNegativeButton(
                        getString(R.string.txtDialogoFinalResetNegativo),
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
