package pt.ulisboa.tecnico.blockkeyserver;

import java.io.IOException;
import java.util.Arrays;


public class Block {
	
	private byte[] id;
	private byte memoria[];
    private int conta;
    
    public Block() {
        this(64);
    }
    
    public Block(int tamanho) {
        if (tamanho < 0) {
            throw new IllegalArgumentException(
            		"O tamanho do bloco eh negativo: " + tamanho);
        }
        memoria = new byte[tamanho];
    }

    private void verificaCapacidade(int capacidadeMinima) {
        // Estouro a capacidade especificada ?
        if (capacidadeMinima - memoria.length > 0)
        	aumenta(capacidadeMinima);
    }
    
    private void aumenta(int capacidadeMinima) {
        int capacidadeAntiga = memoria.length;
        int novaCapacidade = capacidadeAntiga << 1; //doubra a capacidade
        
        if (novaCapacidade - capacidadeMinima < 0){ //se nova capacidade for menor que a minima
        	novaCapacidade = capacidadeMinima; // entao considero a minima
        }
        
        if (novaCapacidade < 0) {
            if (capacidadeMinima < 0) // transbordou
                throw new OutOfMemoryError();
            novaCapacidade = Integer.MAX_VALUE;
        }
        memoria = Arrays.copyOf(memoria, novaCapacidade);
    }
    
    public synchronized byte[] escreve(byte dado[], int posicao, int tamanho) 
    {
        if ((posicao < 0) || (tamanho < 0)) 
        {
            throw new IndexOutOfBoundsException();
        }
        verificaCapacidade(conta + tamanho);
        if(conta < posicao)
        {
        	for (int i = conta; i < posicao-1; i++)
        		memoria[i] = '0';//Depois substituirei por (byte)0
        }
        if(posicao < conta)//para nunca subsescrever o conteudo existente
        	posicao = conta + 1;
        copiarDado(dado, 0, memoria, posicao - 1, tamanho);
        conta = tamanho + posicao - 1;
        //retorna o id do bloco que o hash do conteudo no bloco
        this.id = this.getBytes();
        return this.id;
    }
    
    public synchronized void reinicia() {
        conta = 0;
    }
    
    public void copiarDado(byte orig[], int inicioOrig, byte dest[], int inicioDest, int nBytes){
    	for(int i = inicioOrig, j = inicioDest, k = 0; k < nBytes; i++,j++, k++)
    		dest[j] = orig[i];
    }
    
    public synchronized byte[] getBytes(){
        return Arrays.copyOf(memoria, conta);
    }

    public synchronized int tamanho() {
        return conta;
    }
    
    public synchronized String getString() {
        return new String(memoria, 0, conta);
    }

    public void close() throws IOException {
    }
}
