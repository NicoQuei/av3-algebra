package ettjwrthtja;

import java.util.Arrays;
import java.util.Comparator;

public class questaoSeis {
    
    private static final int[][] A = {
            {0, 1, 1, 0},
            {0, 0, 1, 0},
            {1, 0, 0, 1},
            {1, 0, 0, 0}
    };

    private static int[][] calculoTransposta(int[][] matriz) { 
        int[][] transposta = new int[matriz[0].length][matriz.length]; 
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                transposta[j][i] = matriz[i][j]; 
            }
        }
        return transposta;
    }

    private static double calculoNorma(double[] vetor) {
        double somaQuadrados = 0.0;
        for (int i = 0; i < vetor.length; i++) {
            somaQuadrados += vetor[i] * vetor[i];
        }
        return Math.sqrt(somaQuadrados);
    }

    private static double[] normalizarVetor(double[] vetor) {
        double normaVetor = calculoNorma(vetor); 
        double[] vetorNormalizado = new double[vetor.length];
        for (int i = 0; i < vetor.length; i++) {
            vetorNormalizado[i] = vetor[i] / normaVetor;
        }
        return vetorNormalizado;
    }

    private static double[] multiplicarMatrizVetor(int[][] matriz, double[] vetor) {
        double[] resultado = new double[matriz.length]; 
        for (int i = 0; i < matriz.length; i++) {
            double soma = 0;
            for (int j = 0; j < matriz[i].length; j++) {
                soma += matriz[i][j] * vetor[j];
            }
            resultado[i] = soma;
        }
        return resultado;
    }

    private static double[] calcularAutoridade(int[][] A, int iteracoes, double tolerancia) {
        int[][] At = calculoTransposta(A); 
        int[][] AtA = multiplicarMatrizes(At, A); 

        double[] aK = new double[AtA.length];
        for (int i = 0; i < aK.length; i++) {
            aK[i] = 1.0; 
        }
        aK = normalizarVetor(aK); 

        double[] aK1; 
        for (int i = 0; i < iteracoes; i++) {
            aK1 = multiplicarMatrizVetor(AtA, aK); 
            aK1 = normalizarVetor(aK1); 

            boolean convergiu = true;
            for (int j = 0; j < aK.length; j++) {
                if (Math.abs(aK[j] - aK1[j]) >= tolerancia) {
                    convergiu = false;
                    break; 
                }
            }

            if (convergiu) break; 
            aK = aK1; 
        }

        return aK; 
    }

    private static int[][] multiplicarMatrizes(int[][] m1, int[][] m2) {
        int[][] resultado = new int[m1.length][m2[0].length]; 
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m2[0].length; j++) {
                for (int k = 0; k < m2.length; k++) {
                    resultado[i][j] += m1[i][k] * m2[k][j]; 
                }
            }
        }
        return resultado;
    }

    private static void mostrarResultados(double[] vetorAutoridade) {
        SiteAutoridade[] sitesRanqueados = new SiteAutoridade[vetorAutoridade.length];
        for (int i = 0; i < vetorAutoridade.length; i++) {
            sitesRanqueados[i] = new SiteAutoridade(i + 1, vetorAutoridade[i]); 
        }

        Arrays.sort(sitesRanqueados, Comparator.comparingDouble(SiteAutoridade::getAutoridade).reversed());

        System.out.println("Ranking de Sites por Autoridade:");
        for (SiteAutoridade site : sitesRanqueados) {
            System.out.printf("Site %d: %.4f%n", site.getSite(), site.getAutoridade()); 
        }
    }

    private static class SiteAutoridade {
        private final int site;
        private final double autoridade;

        public SiteAutoridade(int site, double autoridade) {
            this.site = site;
            this.autoridade = autoridade;
        }

        public int getSite() {
            return site;
        }

        public double getAutoridade() {
            return autoridade;
        }
    }

    public static void main(String[] args) {
        double[] vetorAutoridade = calcularAutoridade(A, 10, 1e-6);
        mostrarResultados(vetorAutoridade);
    }
}
