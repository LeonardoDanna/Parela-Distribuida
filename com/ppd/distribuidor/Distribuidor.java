package com.ppd.distribuidor;

import com.ppd.comum.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Distribuidor {
    private static final int PORTA = 12345;
    private static final String[] IPS = {
        // Coloque aqui os IPs dos servidores R
        "172.16.232.187", //lesio
        "172.16.232.117", //danna
        "127.0.0.1" // para teste local; adicione outros
    };

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.print("Tamanho do vetor grande? ");
        int n = Integer.parseInt(sc.nextLine().trim());
        byte[] grande = gerarVetor(n);

        System.out.print("Mostrar vetor gerado? (s/N) ");
        if (sc.nextLine().trim().equalsIgnoreCase("s"))
            System.out.println(Arrays.toString(grande));

        System.out.print("Testar número inexistente (111)? (s/N) ");
        boolean inexistente = sc.nextLine().trim().equalsIgnoreCase("s");

        byte procurado = inexistente
                ? (byte)111
                : grande[ThreadLocalRandom.current().nextInt(grande.length)];

        System.out.printf("[D] Número escolhido: %d%n", procurado);

        long ini = System.nanoTime();
        int total = contarDistribuido(grande, procurado);
        long fim = System.nanoTime();

        System.out.printf("[D] TOTAL de ocorrências: %d%n", total);
        System.out.printf("[D] Tempo distribuído: %.3f ms%n", (fim - ini)/1e6);

        enviarEncerramentoParaTodos();
    }

    private static byte[] gerarVetor(int n) {
        byte[] v = new byte[n];
        ThreadLocalRandom r = ThreadLocalRandom.current();
        for (int i = 0; i < n; i++) v[i] = (byte) r.nextInt(-100, 101);
        return v;
    }

    private static List<byte[]> fatiar(byte[] v, int partes) {
        List<byte[]> fatias = new ArrayList<>(partes);
        int n = v.length, base = n / partes, resto = n % partes, ini = 0;
        for (int p = 0; p < partes; p++) {
            int tam = base + (p < resto ? 1 : 0);
            fatias.add(Arrays.copyOfRange(v, ini, ini + tam));
            ini += tam;
        }
        return fatias;
    }

    private static int contarDistribuido(byte[] grande, byte procurado) throws InterruptedException {
        int servidores = IPS.length;
        List<byte[]> partes = fatiar(grande, servidores);
        int[] somas = new int[servidores];
        List<Thread> ts = new ArrayList<>();

        for (int i = 0; i < servidores; i++) {
            final int idx = i;
            Thread t = new Thread(() -> {
                try (Socket s = new Socket(IPS[idx], PORTA);
                     ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                     ObjectInputStream  in  = new ObjectInputStream(s.getInputStream())) {

                    System.out.printf("[D] Conectado a %s%n", IPS[idx]);

                    // Você pode enviar vários Pedidos sucessivos; aqui enviamos 1 por servidor
                    out.writeObject(new Pedido(partes.get(idx), procurado));
                    out.flush();

                    Resposta r = (Resposta) in.readObject();
                    somas[idx] = r.getContagem();
                } catch (Exception e) {
                    System.err.printf("[D] Falha com %s: %s%n", IPS[idx], e.getMessage());
                }
            }, "cli-" + idx);
            t.start();
            ts.add(t);
        }
        for (Thread t : ts) t.join();
        return Arrays.stream(somas).sum();
    }

    private static void enviarEncerramentoParaTodos() {
        for (String ip : IPS) {
            try (Socket s = new Socket(ip, PORTA);
                 ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream())) {
                out.writeObject(new ComunicadoEncerramento());
                out.flush();
                System.out.printf("[D] Encerramento enviado para %s%n", ip);
            } catch (IOException e) {
                System.err.printf("[D] Erro ao encerrar %s: %s%n", ip, e.getMessage());
            }
        }
    }
}
