package com.ppd.receptor;

import com.ppd.comum.*;
import java.io.*;
import java.net.*;

public class Receptor {
    public static final int PORTA_PADRAO = 12345;

    public static void main(String[] args) {
        int porta = args.length > 0 ? Integer.parseInt(args[0]) : PORTA_PADRAO;
        System.out.printf("[R] Iniciando na porta %d...%n", porta);

        try (ServerSocket servidor = new ServerSocket(porta)) {
            while (true) {
                Socket conexao = servidor.accept();
                System.out.printf("[R] Conectado: %s%n",
                        conexao.getInetAddress().getHostAddress());
                new Thread(new Atendente(conexao)).start();
            }
        } catch (IOException e) {
            System.err.println("[R] Erro no servidor: " + e.getMessage());
        }
    }

    static class Atendente implements Runnable {
        private final Socket socket;
        Atendente(Socket s) { this.socket = s; }

        @Override public void run() {
            try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream  in = new ObjectInputStream(socket.getInputStream())) {

                while (true) {
                    Object obj = in.readObject();

                    if (obj instanceof Pedido p) {
                        int cont = p.contar(); // paralelismo interno
                        out.writeObject(new Resposta(cont));
                        out.flush();
                    }
                    else if (obj instanceof ComunicadoEncerramento) {
                        System.out.println("[R] Encerramento solicitado. Fechando conexão.");
                        break;
                    }
                    else {
                        System.out.println("[R] Objeto desconhecido; ignorando.");
                    }
                }
            } catch (EOFException e) {
                System.out.println("[R] Cliente encerrou a conexão.");
            } catch (Exception e) {
                System.err.println("[R] Erro na conexão: " + e.getMessage());
            } finally {
                try { socket.close(); } catch (IOException ignore) {}
            }
        }
    }
}
