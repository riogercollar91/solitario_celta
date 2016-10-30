package es.upm.miw.SolitarioCelta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
;

/**
 * Created by Rioger on 30/10/2016.
 */

public class AlertDialogBestResultFragment extends DialogFragment {
    private static String LOG_TAG = "MIW_FEM";
    private String read_resultado;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity main = (MainActivity) getActivity();

        read_resultado = main.leerFicheroResultados();

        final AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder
                .setTitle("Nombre               Fecha               # Piezas")
                .setMessage(read_resultado)
                .setPositiveButton(
                        getString(R.string.txtDialogoBestResultAfirmativo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }
                )
                .setNegativeButton(
                        getString(R.string.txtDialogoBestResultNegativo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // limpiar listado
                                main.BorrarContenidoFichero();
                                read_resultado = main.leerFicheroResultados();
                                builder.setMessage(read_resultado);

                                Log.d(LOG_TAG, "Historial de resultados borrados....." + read_resultado);

                                builder.show();
                            }
                        }
                );


        return builder.create();
    }
}
