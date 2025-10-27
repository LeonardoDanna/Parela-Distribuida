package com.ppd.sequencial;

import java.util.concurrent.ThreadLocalRandom;

public class ContagemSequencial {
    public static void main(String[] args) {
        int n = args.length > 0 ? Integer.parseInt(args[0]) : 5_000_000;
        byte[] v = new byte[n];
        ThreadLocalRandom r = ThreadLocalRandom.current();
        for (int i = 0; i < n; i++) v[i] = (byte) r.nextInt(-100, 101);
        byte alvo = v[r.nextInt(n)];

        long ini = System.nanoTime();
        int c = 0; for (byte b : v) if (b == alvo) c++;
        long fim = System.nanoTime();

        System.out.printf("Contagem: %d | Tempo: %.3f ms%n", c, (fim - ini)/1e6);
    }
}
