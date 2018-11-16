/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpgrafos;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

/**
 *
 * @author Athena Fernandes
 * @author Vinicius Teixeira
 */
public class TPGrafos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        String nomeArquivo = null;
        Grafo grafo;
        Scanner entrada = new Scanner(System.in);
        Scanner e = new Scanner(System.in);
        int check = 1, vertice;
        
        System.out.println(" - Carregar grafo -");
        System.out.print("Entre com o nome do arquivo: ");
        nomeArquivo = entrada.nextLine();
        grafo = new Grafo(nomeArquivo);
        grafo.ImprimeMatrizValores();
        
        do{
            System.out.println("***************** Menu *******************");
            System.out.println("* 1. Ordem                               *");
            System.out.println("* 2. Tamanho                             *");
            System.out.println("* 3. Vizinhos de um vertice              *");
            System.out.println("* 4. Grau de um vertice                  *");
            System.out.println("* 5. Verificar se grafo eh bipartido     *");
            System.out.println("* 6. Verificar se vertice eh articulacao *");
            System.out.println("* 7. Verificar se aresta eh ponte        *");
            System.out.println("* 8. Busca em profundidade               *");
            System.out.println("* 9. Busca em largura                   *");
            System.out.println("* 10. Componentes Conexas                *");
            System.out.println("* 11. Distancia e Caminho minimo         *");
            System.out.println("* 12. Verificar se ha circuito negativo  *");
            System.out.println("* 13. Arvore geradora minima             *");
            System.out.println("* 14. Verificar se grafo eh euleriano    *");
            System.out.println("* 15. Conjunto independente              *");
            System.out.println("* 0. SAIR                                *");
            System.out.println("******************************************");
            
            System.out.print("Selecione uma opcao: ");
            int opc = entrada.nextInt();
            
            switch (opc) {
                case 0:
                    check = 0;
                    break;
                case 1:
                    System.out.println("A ordem do grafo eh "+grafo.getOrdem());
                    break;
                case 2:
                    System.out.println("O tamanho do grafo eh "+grafo.getTamanho());
                    break;
                case 3:
                    System.out.print("Digite um vertice: ");
                    vertice = e.nextInt();
                    
                    if(vertice >=1 && vertice <= grafo.getOrdem()){
                        ArrayList<Integer> vizinhos = new ArrayList();
                        vizinhos = grafo.RetornaVizinho(vertice);
                        System.out.println("Vizinhos do vertice "+vertice+": "+vizinhos);
                    }
                    break;
                case 4:
                    System.out.print("Digite um vertice: ");
                    vertice = e.nextInt();
                    
                    System.out.println("Grau do vertice "+vertice+": "+grafo.GrauVertice(vertice));
                    break;
                case 5:
                    if(grafo.Bipartido(0)){
                        System.out.println("Grafo eh bipartido!");
                    } else {
                        System.out.println("Grafo nao eh bipartido");
                    }
                    break;
                case 6:
                    System.out.print("Digite um vertice: ");
                    vertice = e.nextInt();
                    
                    if(grafo.VerificaArticulacao(vertice, grafo.getNumConexas()))
                        System.out.println("Vertice "+vertice+" eh articulacao!");
                    else
                        System.out.println("Vertice "+vertice+" nao eh articulacao!");
                    break;
                case 7:
                    System.out.print("Digite um vertice: ");
                    vertice = e.nextInt();
                    
                    System.out.println("Digite o outro vertice: ");
                    int vertice2 = e.nextInt();
                    
                    if(grafo.VerificaPonte(vertice, vertice2, grafo.getNumConexas()))
                        System.out.println("A aresta "+vertice+"-"+vertice2+" eh ponte!");
                    else
                        System.out.println("A aresta "+vertice+"-"+vertice2+" nao eh ponte!");
                    break;
                case 8:
                    grafo.BuscaProfundidade();
                    break;
                case 9:
                    grafo.BuscaLargura();
                    break;
                case 10:
                    grafo.ComponentesConexas();
                    break;
                case 11:
                    System.out.print("Digite um vertice: ");
                    vertice = e.nextInt();
                    
                    for(int i = 0; i < grafo.getOrdem(); i++){
                        if(vertice != i){
                           float dist = grafo.MenorCaminho(i, vertice);
                            System.out.println("Distancia do vertice"+vertice+"para o vertice"+i+1+": "+dist);
                        }
                    }
                    break;
                    
                case 12:
                    if(grafo.VerificaCircuitoNegativo())
                        System.out.println("O grafo possui circuito negativo!");
                    else
                        System.out.println("O grafo nao possui ciruito negativo!");
                    break;
                case 13:
                    grafo.ArvoreGeradoraMin();
                    break;
                case 14:
                    if(grafo.VerificaEuleriano())
                        System.out.println("Grafo eh euleriano!");
                    else
                        System.out.println("Grafo nao eh euleriano!");
                    break;
                case 15:
                    grafo.conjuntoIndependente();
                    break;
            }
        }while(check != 0);
        
    }
    
    
}
