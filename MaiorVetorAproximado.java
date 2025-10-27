public class MaiorVetorAproximado {
    public static void main(String[] args) {
        System.out.println("Estimando o maior tamanho possível de vetor em Java...");

        long inicio = System.currentTimeMillis();
        int tamanho = 1_000_000; // começa com 1 milhão
        int ultimoBemSucedido = 0;

        while (true) {
            try {
                byte[] vetor = new byte[tamanho];
                ultimoBemSucedido = tamanho;
                vetor = null; // libera
                System.gc();

                // aumenta o tamanho em 50% para a próxima tentativa
                if (tamanho > Integer.MAX_VALUE / 3 * 2) break;
                tamanho /= 2;
                tamanho *= 3;

                System.out.printf("Alocado com sucesso: %,d elementos%n", ultimoBemSucedido);
            } catch (OutOfMemoryError e) {
                System.out.printf("Falhou em %,d elementos%n", tamanho);
                break;
            }
        }

        long fim = System.currentTimeMillis();

        System.out.println("\nMaior vetor que coube (aproximadamente): " +
                String.format("%,d", ultimoBemSucedido));

        System.out.printf("Memória estimada: %.2f MB%n",
                ultimoBemSucedido * 1.0 / (1024 * 1024));

        System.out.printf("Tempo total: %.2f segundos%n", (fim - inicio) / 1000.0);
    }
}
// rode o programa com o comando:
// java -Xmx8G MaiorVetorAproximado

// java -Xmx13G MaiorVetorAproximado

// para disponibilizar 8gb de memória para uso do Java, para criar o vetor grande
// não ponha 8gb, se 8gb for toda memória que seu computador tem
// subtraia ao menos 3gb da memória que seu computador tem