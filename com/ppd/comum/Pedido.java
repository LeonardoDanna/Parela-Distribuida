package com.ppd.comum;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Pedido extends Comunicado {
    private static final long serialVersionUID = 1L;

    private final byte[] numeros;
    private final byte procurado;

    public Pedido(byte[] numeros, byte procurado) {
        this.numeros  = numeros;
        this.procurado = procurado;
    }

    public byte[] getNumeros()  { return this.numeros; }
    public byte getProcurado()  { return this.procurado; }

    // Exigido pelo enunciado. Implementado com paralelismo interno.
    public int contar() {
        int n = numeros.length;
        int nThreads = Math.max(1, Runtime.getRuntime().availableProcessors());

        // Para vetores pequenos, sequencial é mais rápido.
        if (n < 50_000 || nThreads == 1) {
            int c = 0; for (byte b : numeros) if (b == procurado) c++; return c;
        }

        ExecutorService pool = Executors.newFixedThreadPool(nThreads);
        List<Future<Integer>> fs = new ArrayList<>();
        int bloco = (n + nThreads - 1) / nThreads;

        for (int ini = 0; ini < n; ini += bloco) {
            final int i = ini, f = Math.min(n, ini + bloco);
            fs.add(pool.submit(() -> {
                int c = 0;
                for (int k = i; k < f; k++) if (numeros[k] == procurado) c++;
                return c;
            }));
        }

        int total = 0;
        try {
            for (Future<Integer> fut : fs) total += fut.get();
        } catch (Exception e) {
            // fallback seguro
            int c = 0; for (byte b : numeros) if (b == procurado) c++; total = c;
        } finally {
            pool.shutdown();
        }
        return total;
    }
}
