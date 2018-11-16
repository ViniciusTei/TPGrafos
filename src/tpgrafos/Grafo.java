/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpgrafos;

import java.io.*;
import java.util.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author Athena Fernandes
 * @author Vinicius Teixeira
 */
public class Grafo {

    private int ordem;//numero de vertices
    private int tamanho;//numero de arestas
    private float[][] matrizValores;
    private int numConexas;

    ArrayList<Integer> VerticeMarcado = new ArrayList<>();
    ArrayList<String> Arvore = new ArrayList<>();
    ArrayList<String> Aresta = new ArrayList<>();

    Grafo(String nomeArquivo) throws FileNotFoundException, IOException {
        String dadosGrafo;
        int linha, coluna;
        float peso;
        FileReader fr = new FileReader(nomeArquivo);
        BufferedReader br = new BufferedReader(fr);
        tamanho = 0;
        ordem = Integer.parseInt(br.readLine());
        matrizValores = new float[ordem][ordem];

        for (int i = 0; i < ordem; i++) {
            for (int j = 0; j < ordem; j++) {
                matrizValores[i][j] = 0;//inicializando a matriz
            }
        }

        while ((dadosGrafo = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(dadosGrafo, " ");
            linha = Integer.parseInt(st.nextToken());
            coluna = Integer.parseInt(st.nextToken());
            peso = Float.parseFloat(st.nextToken());
            if (linha != 0 && coluna != 0 && linha <= ordem && coluna <= ordem) {
                matrizValores[linha - 1][coluna - 1] = peso;
                matrizValores[coluna - 1][linha - 1] = peso;
                tamanho++;
            }

        }

    }

    public int getOrdem() {
        return ordem;
    }

    public int getTamanho() {
        return tamanho;
    }
    
    public int getNumConexas() {
        return numConexas;
    }

    public void ImprimeMatrizValores() {
        for (int i = 0; i < getOrdem(); i++) {
            System.out.println((i + 1) + " ");
        }
        System.out.println("");
        for (int i = 0; i < getOrdem(); i++) {
            for (int j = 0; j < getOrdem(); j++) {
                System.out.println(matrizValores[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public ArrayList<Integer> RetornaVizinho(int vertice) {
        ArrayList<Integer> vizinho = new ArrayList<>();
        if (vertice > 0 && vertice <= getOrdem()) {//esta entre a quantidade de vertices
            for (int i = 0; i < getOrdem(); i++) {
                if (matrizValores[vertice - 1][i] != 0) {
                    vizinho.add(i + 1);
                }
            }

        }
        return vizinho;
    }

    public int GrauVertice(int vertice) {
        int grau = 0;
        if (vertice > 0 && vertice <= getOrdem()) {//esta entre a quantidade de vertices
            for (int i = 0; i < getOrdem(); i++) {
                if (matrizValores[vertice - 1][i] != 0) {
                    grau++;
                }
            }
            return grau;
        } else {
            return -1;
        }
    }

    //ordenaum vetor vertices por graus usando o algoritmo QuickSort
    //fonte: https://www.devmedia.com.br/algoritmos-de-ordenacao-em-java/32693
    public void ordenaVertices (int v[], int inicio, int fim) {
      if (inicio < fim) {
        int pivo = separar(v, inicio, fim);
        ordenaVertices(v, inicio, pivo - 1);
        ordenaVertices(v, pivo + 1, fim);
      }
    }

    private int separar (int[] vetor, int inicio, int fim) {
      int pivo = vetor[inicio];
      int i = inicio + 1;
      int j = fim;

      while (i <= j) {
        if (GrauVertice(vetor[i]) >= GrauVertice(pivo))
          i++;
          else if (GrauVertice(vetor[j]) < GrauVertice(pivo))
            j--;
            else {
              int aux = vetor[i];
              vetor[i] = vetor[j];
              vetor[j] = aux;
              i++;
              j--;
            }
      }

      vetor[inicio] = vetor[j];
      vetor[j] = pivo;

      return j;
    }

    //caminho mais curto entre todos os pares de vertices, nao pode ter ciclo negativo
    private void Floyd_Warshall(float matriz_L[][], int matriz_R[][]) {// matrizl custo do menor caminho entre dois vertices matrizr quais vertices cada menor caminho passa
        for (int i = 0; i < getOrdem(); i++) {
            for (int j = 0; j < getOrdem(); j++) {
                if (matrizValores[i][j] == 0) {
                    matriz_L[i][j] = Float.POSITIVE_INFINITY;//recebe infinito positivo
                } else {
                    matriz_L[i][j] = matrizValores[i][j];//recebe o peso dos vertices
                }

                if (matriz_L[i][j] == Float.POSITIVE_INFINITY) {
                    matriz_R[i][j] = 0;//se é infinito matrizr recebe 0
                } else {
                    matriz_R[i][j] = i + 1;//recebe o numero da linha
                }

            }
        }
        for (int i = 0; i < getOrdem(); i++) {
            for (int j = 0; j < getOrdem(); j++) {
                for (int k = 0; k < getOrdem(); k++) {
                    if (matriz_L[j][k] > matriz_L[j][i] + matriz_L[i][k]) {
                        matriz_L[j][k] = matriz_L[j][i] + matriz_L[i][k];
                        matriz_R[j][k] = matriz_R[i][k];
                    }
                }
            }
        }
    }

    public float MenorCaminho(int vertice1, int vertice2) {
        if (vertice1 != vertice2) {//se for igual nao tem menorcaminho
            float matriz_L[][] = new float[ordem][ordem];
            int matriz_R[][] = new int[ordem][ordem];
            Floyd_Warshall(matriz_L, matriz_R);
            return matriz_L[vertice1 - 1][vertice2 - 1];
        } else {
            return 0;
        }
    }
    
    public boolean VerificaCircuitoNegativo() {
        float matriz_L[][] = new float[ordem][ordem];
        int matriz_R[][] = new int[ordem][ordem];
        Floyd_Warshall(matriz_L, matriz_R);
        
        for(int i = 0; i < getOrdem(); i++){
            if(matriz_L[i][i] < 0)
                return true;
        }
        
        return false;
    }

    private boolean Euleriano() {
        for (int i = 0; i < getOrdem(); i++) {
            if (GrauVertice(i + 1) % 2 != 0) {// se for impar nao e euleriano
                return false;
            }
        }
        return true;
    }

    //forma cadeia fechada para ser usada no Hierholzer
    private ArrayList<Integer> FormaCadeia(int vertice, float matriz[][]) {
        ArrayList<Integer> cadeia = new ArrayList<>();
        int v = vertice;
        do {
            cadeia.add(v + 1);
            for (int i = 0; i < getOrdem(); i++) {
                if (matriz[v][i] != 0) {
                    matriz[v][i] = 0;
                    v = 1;
                    break;
                }
            }
        } while (v != vertice);
        cadeia.add(vertice + 1);
        return cadeia;
    }

    private boolean verificaMatrizVazia(float[][] matriz) {
        for (int i = 0; i < getOrdem(); i++) {
            for (int j = 0; j < getOrdem(); j++) {
                if (matriz[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private int escolheGrauMaiorZero(float matriz[][], ArrayList<Integer> cadeia) {
        int v = 0;
        int grau = 0;
        for (Integer i : cadeia) {
            for (int j = 0; j < getOrdem(); j++) {
                if (matriz[i - 1][j] != 0) {
                    grau++;
                }
            }
            if (grau > 0) {
                v = i - 1;
                break;
            }
        }
        return v;
    }

    //construcao de um ciclo euleriano, forma cadeias fechadas e junta a cada interacao
    private ArrayList<Integer> Hierholzer() {
        int v, indice;
        float n_Matriz[][] = matrizValores;
        ArrayList<Integer> cadeia = new ArrayList<>();
        ArrayList<Integer> cadeiaIntermediaria = new ArrayList<>();

        cadeia = FormaCadeia(0, n_Matriz);//cadeia recebe uma cadeia fechada

        while (!verificaMatrizVazia(n_Matriz)) {
            v = escolheGrauMaiorZero(n_Matriz, cadeia);//recebe um vertice maior que zero
            cadeiaIntermediaria = FormaCadeia(v, n_Matriz);//recebe uma cadeia fechada
            //v = cadeiaIntermediaria.remove(0);//para nao ter repeticao de um numero
            indice = cadeia.lastIndexOf(v);
            cadeia.addAll(indice + 1, cadeiaIntermediaria);
            cadeiaIntermediaria.clear();//removendo a cadeiaintermediaria
        }
        return cadeia;

    }

    public boolean VerificaEuleriano() {
        ArrayList<Integer> cadeia = new ArrayList<>();
        if (Euleriano()) {
            cadeia = Hierholzer();
            for (Integer i : cadeia) {
                System.out.println(i + " ");//imprimi ciclo euleriano
            }
            return true;
        }
        System.out.println("Grafo nao é euleriano");
        return false;
    }

    public void BP(int vertice) {
         ArrayList<Integer> vizinhos  = new ArrayList<>();
         String aresta, arestaReversa;
         VerticeMarcado.add(vertice);//marca o vertice
         vizinhos = RetornaVizinho(vertice);
         for(Integer i: vizinhos){
             if(!VerticeMarcado.contains(i)){
                 aresta = Integer.toString(vertice) + "," + Integer.toString(i);
                 Arvore.add(aresta);
                 BP(i);
             }else{
                  aresta = Integer.toString(vertice) + "," + Integer.toString(i);
                  arestaReversa = Integer.toString(i) + "," + Integer.toString(vertice);
                  if(!Arvore.contains(aresta) && !Arvore.contains(arestaReversa)){//verifica se a arvore ja contem essa aresta
                    if(!Aresta.contains(aresta) && !Aresta.contains(arestaReversa)){//verifica se o ArrayList de Aresta ja contem essa aresta
                        Aresta.add(aresta);
                    }
                  }
             }
         }

    }

    public void BuscaProfundidade(){
        System.out.println("Busca em Profundidade");
        ArrayList<Integer> verticesMarcadosBusca  = new ArrayList<>();
        while(verticesMarcadosBusca.size()<getOrdem()){
            for(int i=1;i<=getOrdem();i++){
                if(!verticesMarcadosBusca.contains(i)){
                    Arvore.clear();
                    Aresta.clear();
                    VerticeMarcado.clear();
                    BP(i);
                    System.out.print("Vertice visitados: ");
                    for(Integer v: VerticeMarcado){
                        System.out.print(" "+v);
                        verticesMarcadosBusca.add(v);
                    }
                    System.out.print("\nArvore de profundidade");
                    for(String a: Arvore){
                        System.out.print(" ("+a+")   ");

                    }
                    System.out.print("\nAresta de retorno");
                    for(String aresta: Aresta){
                        System.out.print(" ("+aresta+")   ");

                    }
                    System.out.println("");

                }
            }
        }
    }

    public void BL(int vertice){
        ArrayList<Integer> Q  = new ArrayList<>();
        ArrayList<Integer> vizinhos  = new ArrayList<>();
        String aresta, arestaReversa;
        VerticeMarcado.add(vertice);
        Q.add(vertice);

        while(Q.size()>0){
            vertice= Q.get(0);
            Q.remove(0);
            vizinhos = RetornaVizinho(vertice);
            for(Integer i : vizinhos){
                if(!VerticeMarcado.contains(i)){
                   aresta = Integer.toString(vertice) + "," + Integer.toString(i);
                   Arvore.add(aresta);
                   Q.add(i);
                   VerticeMarcado.add(i);
                }else{
                  aresta = Integer.toString(vertice) + "," + Integer.toString(i);
                  arestaReversa = Integer.toString(i) + "," + Integer.toString(vertice);
                  if(!Arvore.contains(aresta) && !Arvore.contains(arestaReversa)){//verifica se a arvore ja contem essa aresta
                    if(!Aresta.contains(aresta) && !Aresta.contains(arestaReversa)){//verifica se o ArrayList de Aresta ja contem essa aresta
                        Aresta.add(aresta);
                    }
                }
            }

        }
    }
}
 public void BuscaLargura(){
    System.out.println("\nBusca em Largura");
        ArrayList<Integer> verticesMarcadosBusca  = new ArrayList<>();
        while(verticesMarcadosBusca.size()<getOrdem()){
            for(int i=1;i<=getOrdem();i++){
                if(!verticesMarcadosBusca.contains(i)){
                    Arvore.clear();
                    Aresta.clear();
                    VerticeMarcado.clear();
                    BL(i);
                    System.out.print("Vertice visitados: ");
                    for(Integer v: VerticeMarcado){
                        System.out.print(" "+v);
                        verticesMarcadosBusca.add(v);
                    }
                    System.out.print("\nArvore de profundidade");
                    for(String a: Arvore){
                        System.out.print(" ("+a+")   ");

                    }
                    System.out.print("\nAresta de retorno");
                    for(String aresta: Aresta){
                        System.out.print(" ("+aresta+")   ");

                    }
                    System.out.println("");

                }
            }
        }
 }

 public void ComponentesConexas(){
     numConexas=0;
     ArrayList<Integer> verticesMarcadosBusca  = new ArrayList<>();
     System.out.println("\nComponentes Conexas");
     while(verticesMarcadosBusca.size()<getOrdem()){
            for(int i=1;i<=getOrdem();i++){
                if(!verticesMarcadosBusca.contains(i)){
                    Arvore.clear();
                    Aresta.clear();
                    VerticeMarcado.clear();
                    BL(i);
                    numConexas++;
                    System.out.print("Vertice(s) da componente "+ numConexas);
                    for(Integer v: VerticeMarcado){
                        System.out.print(v+" ");
                        verticesMarcadosBusca.add(v);
                    }
                    System.out.println("");

                }
            }
        }
     System.out.println("Numero de componentes conexas: "+ numConexas);

 }

 public void NovoComponenteConexas(int vertice, float NovaMatrizValores[][]){
     ArrayList<Integer> vizinhos  = new ArrayList<>();
     VerticeMarcado.add(vertice);

     for(int i=0;i<getOrdem();i++){
         if(NovaMatrizValores[vertice-1][i] !=0){
             vizinhos.add(i+1);
         }
     }

     for(Integer i: vizinhos){
         if(!VerticeMarcado.contains(i)){
             NovoComponenteConexas(i,NovaMatrizValores);
         }
     }
 }
 //verifica se retirando o vertice nao cria componente desconexa
 public boolean VerificaArticulacao(int vertice, int numConexas ){
    int numNovoConexas=0;
    int ordem = getOrdem();
     ArrayList<Integer> verticesMarcadosBusca  = new ArrayList<>();

     float[][]NovaMatrizValores = new float[ordem][ordem];

     for(int i=0;i<ordem;i++){
         for(int j=0;j<ordem;j++){
            NovaMatrizValores[i][j] = matrizValores[i][j];
         }
     }

     for(int i=0;i<ordem;i++){
            NovaMatrizValores[vertice-1][i] =0;
             NovaMatrizValores[i][vertice-1] =0;
         }

     while(verticesMarcadosBusca.size()<ordem){
        for(int i=1;i<=ordem;i++){
            if(!verticesMarcadosBusca.contains(i)){
                VerticeMarcado.clear();
                NovoComponenteConexas(i,NovaMatrizValores);
                numNovoConexas++;
                for(Integer v: VerticeMarcado){
                    verticesMarcadosBusca.add(v);
                }

            }
        }
     }
     return numNovoConexas > numConexas+1;

 }

 //verifica se retirando a aresta nao cria componente desconexa
 public boolean VerificaPonte(int vertice, int aresta, int numConexas ){
    int numNovoConexas=0;
    int ordem = getOrdem();
     ArrayList<Integer> verticesMarcadosBusca  = new ArrayList<>();

     float[][]NovaMatrizValores = new float[ordem][ordem];

     for(int i=0;i<ordem;i++){
         for(int j=0;j<ordem;j++){
            NovaMatrizValores[i][j] = matrizValores[i][j];
         }
     }


            NovaMatrizValores[vertice-1][aresta-1] =0;
             NovaMatrizValores[aresta-1][vertice-1] =0;


     while(verticesMarcadosBusca.size()<ordem){
        for(int i=1;i<=ordem;i++){
            if(!verticesMarcadosBusca.contains(i)){
                VerticeMarcado.clear();
                NovoComponenteConexas(i,NovaMatrizValores);
                numNovoConexas++;
                for(Integer v: VerticeMarcado){
                    verticesMarcadosBusca.add(v);
                }

            }
        }
     }
     return numNovoConexas > numConexas;

 }

 public boolean Bipartido (int raiz) {
     //Cria um vetor de cores e o inicia com -1 q representa q ele ainda nao foi colorido
     int[] cor = new int[getOrdem()];

     for(int i = 0; i < getOrdem(); i++){
         cor[i] = -1;
     }

     //colore raiz
     cor[raiz] = 1;

     //cria uma lista de vertices
     LinkedList<Integer> fila = new LinkedList<Integer>();
     fila.add(raiz);

     //Percorre a matriz procurando por arestas e verificando
     //se esta colorido ou se as cores sao iguais
     //se sair do while significa q a fila esta vazia e o grafo eh bipartido
     while(fila.size() != 0) {
         int u = fila.poll();

         for(int v = 0; v < getOrdem(); v++) {
             if(matrizValores[u][v] != 0 && cor[v] == -1) {
                 cor[v] = 1 - cor[u];
                 fila.add(v);
             }
             else if (matrizValores[u][v] != 0 && cor[v] == cor[u]) {
                 return false;
             }
         }
     }

     return true;
 }

 //Ultiliza o algoritmo de Primm para gerar a arvore e a escrever no arquivo
 public void ArvoreGeradoraMin () throws IOException {
     int parent[] = new int[getOrdem()];
     float key[] = new float[getOrdem()];
     boolean tree[] = new boolean[getOrdem()];
     
     Arrays.fill(key, Float.MAX_VALUE);
     Arrays.fill(tree, false);
     
     key[0] = 0f;
     parent[0] = -1;
     
     for(int count = 0; count < getOrdem() - 1; count++) {
         int u = minimo(key, tree);
         tree[u] = true;
         
         for(int v = 0; v < getOrdem(); v++){
             if(matrizValores[u][v]!=0 && tree[v]==false && matrizValores[u][v] < key[v]){
                 parent[v] = u;
                 key[v] = matrizValores[u][v];
             }
         }
     }
     
     FileWriter fw = new FileWriter("agm.txt");
     PrintWriter pw = new PrintWriter(fw);
     
     float peso = 0;
     for(int i = 0; i < key.length; i++){
         peso = peso + key[i];
     }
     
     pw.println(peso);
     for(int i = 1; i < getOrdem(); i++){
         pw.println(parent[i]+" "+i+" "+key[i]);
     }
     pw.close();
     System.out.println("Arvore geradora minima criada!");
 }
 
 
 private int minimo (float key[], boolean tree[]){
     int min_index = -1;
     float min = Float.MAX_VALUE;
     
     for(int v = 0; v < getOrdem(); v++){
         if(tree[v]==false && key[v] < min) {
             min = key[v];
             min_index = v;
         }
         
     }
     
     return min_index;
 }
 
 public void conjuntoIndependente () {
     //Cria um arrays list de vertices e ordena por grau usando
     //o metodo sort da classe Collections
     ArrayList N = new ArrayList();
        for(int i = 0; i < ordem; i++){
            N.add(i);
        }
        Collections.sort (N, new Comparator() {
            public int compare(Object o1, Object o2) {
                int v1 = (int) o1;
                int v2 = (int) o2;
                return GrauVertice(v1) > GrauVertice(v2) ? -1 : (GrauVertice(v1) < GrauVertice(v2) ? +1 : 0);
            }
        });
        
        ArrayList S = new ArrayList();
        ArrayList V = new ArrayList();
        
        while(!N.isEmpty()){
            for(int k = 0; k < N.size(); k++){
                V = RetornaVizinho(k);
                S.add(N.get(k));
                N.remove(k);
                
                for(int j = 0; j < V.size(); j++){
                    if(N.contains(V.get(j))){
                        N.remove(V.get(j));
                    }
                }
                
            }
        }
        
        System.out.println("Conjunto idependente: "+S);
 }
 public int[] insertionSort(int[] vetor) {
        for (int i = 0; i < vetor.length; i++) {
            int grauatual = GrauVertice(vetor[i]);
            int atual = vetor[i];
            int j = i - 1;
            while (j >= 0 && GrauVertice(vetor[j]) <= grauatual) {
                vetor[j + 1] = vetor[j];
                j--;
            }
            vetor[j + 1] = atual;;
        }
        
        return vetor;
    }
}
