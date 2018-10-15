package com.example.nicolas.miauto.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolas.miauto.BaseDeDatos.baseDatos;
import com.example.nicolas.miauto.BaseDeDatos.bdHelper;
import com.example.nicolas.miauto.R;


public class GarageActivity extends Activity {

    ImageView btnAuto;
    ImageButton btnBorrarAuto;
    boolean hayAuto;
    TextView tvPatente;
    private static baseDatos carsHelper;
    private static SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);
        carsHelper = new baseDatos(getApplicationContext(), "DBTest1", null, 1);
        bd = carsHelper.getWritableDatabase();
        hayAuto = bdHelper.hayAuto(bd);
        btnAuto = (ImageView) findViewById(R.id.btnSelectAuto);
        btnBorrarAuto = (ImageButton) findViewById(R.id.btnBorrarAuto);
        tvPatente = (TextView)findViewById(R.id.tvPatente);
        if (hayAuto){
            btnBorrarAuto.setVisibility(View.VISIBLE);
            tvPatente.setText(bdHelper.damePatente(bd));
            btnAuto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(GarageActivity.this, MenuActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            tvPatente.setText("Crear Auto...");
            btnAuto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GarageActivity.this, FormAutoActivity.class);
                    startActivity(intent);
                }
            });
        }

        btnBorrarAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarAuto();
            }
        });
        //chequea permisos GPS
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }



    private void borrarAuto() {

        AlertDialog.Builder builder = new AlertDialog.Builder(GarageActivity.this);
        builder.setMessage("Está seguro que desea borrar este auto?")
                .setTitle("Patente: " + bdHelper.damePatente(bd));

        builder.setCancelable(true);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bdHelper.eliminarAuto(bd);
                btnBorrarAuto.setVisibility(View.INVISIBLE);
                tvPatente.setText("Crear Auto...");
                Toast.makeText(getApplicationContext(),"Auto eliminado correctamente!",Toast.LENGTH_LONG).show();
                btnAuto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GarageActivity.this, FormAutoActivity.class);
                        startActivity(intent);

                    }
                });
            }
        });
        builder.setIcon(R.drawable.remove_car);
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            //no hace nada
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
            }

    }

