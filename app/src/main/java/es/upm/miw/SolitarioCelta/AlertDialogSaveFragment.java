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

public class AlertDialogSaveFragment extends DialogFragment {

    private static String LOG_TAG = "MIW_FEM";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity main = (MainActivity) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder
                .setTitle(R.string.txtDialogoFinalSaveTitulo)
                .setMessage(R.string.txtDialogoFinalSavePregunta)
                .setPositiveButton(
                        getString(R.string.txtDialogoFinalSaveAfirmativo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String tableroserializado = main.getJuegoSerializado();
                                main.escribirFicheroPartidas(tableroserializado);

                                Log.d(LOG_TAG, "partida guardada....." + tableroserializado);
                            }
                        }
                )
                .setNegativeButton(
                        getString(R.string.txtDialogoFinalSaveNegativo),
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
