package com.andresparedes.controlremoto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    Button btn_arriba;
    Button btn_abajo;
    Button btn_izquierda;
    Button btn_derecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_arriba = (Button) findViewById(R.id.btn_arriba);
        btn_abajo = (Button) findViewById(R.id.btn_abajo);
        btn_izquierda = (Button) findViewById(R.id.btn_izquierda);
        btn_derecha = (Button) findViewById(R.id.btn_derecha);

        btn_arriba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarPaquete("arriba");
            }
        });

        btn_abajo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarPaquete("abajo");
            }
        });

        btn_derecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarPaquete("derecha");
            }
        });

        btn_izquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarPaquete("izquierda");
            }
        });
    }

    public void enviarPaquete(String s){
        Sender sender = new Sender(s);
        sender.start();
    }

    public class Sender extends Thread{

        String msj;

        public Sender(String msj){
            this.msj= msj;
        }

        @Override
        public void run() {
            try {
                DatagramSocket socket = new DatagramSocket();
                int puertoServidor = 5000;
                InetAddress server = InetAddress.getByName("172.30.155.226");

                //ENVIAR MENSAJE
                DatagramPacket dp = new DatagramPacket(msj.getBytes(), msj.length(), server, puertoServidor);
                socket.send(dp);

                //RECIBIR
                byte[] buffer = new byte[1024];
                DatagramPacket paquete_recibido = new DatagramPacket(buffer,buffer.length);
                socket.receive(paquete_recibido);

                String mensaje_recibido = new String(paquete_recibido.getData());
                final String mensaje_procesado = mensaje_recibido.trim();

                System.out.println(mensaje_recibido);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "" + mensaje_procesado, Toast.LENGTH_SHORT).show();
                    }
                });


            }catch (Exception e){

            }
        }
    }
}
