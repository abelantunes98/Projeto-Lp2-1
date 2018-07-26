package classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import interfaces.ListaOrdenavel;
import interfaces.OrdenaItemLista;

public class ListaDeCompras implements ListaOrdenavel {

	private String descritor, data, localDeCompra, hora;
	private HashMap<Integer, ProdutoLista> produtosLista;
	private ArrayList<String> saidaOrdenada;
	private double valorFinal;
	private boolean finalizada;

	public ListaDeCompras(String descritor, String data, String hora) {
		this.descritor = descritor;
		this.data = data;
		this.hora = hora;
		this.produtosLista = new HashMap<>();
		this.saidaOrdenada = new ArrayList();
		this.finalizada = false;
		
		// Atrasa o funcionamento em 1 Microsegundo, pois estava sendo capaz de gerar mais de uma lista
		// em um milisegundo, o que dificultava a ordenacao por hora gerada.
		try {TimeUnit.MICROSECONDS.sleep(1);} catch (InterruptedException e){}
	}

	public void adicionaProdutoNaLista(Item item, int quantidade) {
		
		ProdutoLista produto = new ProdutoLista(item, quantidade);
		
		int id = produto.getId();
		if (!this.produtosLista.containsKey(id)) {
			this.produtosLista.put(id, produto);
		}

		else {
			throw new IllegalArgumentException("Esse produto ja foi adicionado!");
		}

		ordenaSaida();
	}
	
	public void adicionaProdutoNaLista(ProdutoLista produto) {
		
		int id = produto.getId();
		
		if (!this.produtosLista.containsKey(id)) {
			this.produtosLista.put(id, produto);
		}

		else {
			throw new IllegalArgumentException("Esse produto ja foi adicionado!");
		}

		ordenaSaida();
	}

	public void deletaProdutoLista(int itemId) {

		if (!this.produtosLista.containsKey(itemId)) {
			throw new IllegalArgumentException("Erro na exclusao de compra: compra nao encontrada na lista.");
		}

		this.produtosLista.remove(itemId);
		ordenaSaida();
	}

	public void atualizaProduto(int itemId, String operacao, int quantidade) {

		if (!operacao.equals("adiciona") && !operacao.equals("diminui")) {
			throw new IllegalArgumentException("Erro na atualizacao de compra: operacao invalida para atualizacao.");
		}

		if (!this.produtosLista.containsKey(itemId)) {
			throw new IllegalArgumentException("Erro na atualizacao de compra: compra nao encontrada na lista.");
		}

		if (operacao.equals("adiciona")) {
			int novaQuantidade = (this.produtosLista.get(itemId).getQuantidade()) + quantidade;
			this.produtosLista.get(itemId).setQuantidade(novaQuantidade);
		}

		else if (operacao.equals("diminui")) {
			int novaQuantidade = (this.produtosLista.get(itemId).getQuantidade()) - quantidade;

			if (novaQuantidade <= 0) {
				this.produtosLista.remove(itemId);
			}

			else {
				this.produtosLista.get(itemId).setQuantidade(novaQuantidade);
			}
		}

		ordenaSaida();
	}

	public String pesquisaCompraEmLista(int itemId) {

		if (!this.produtosLista.containsKey(itemId)) {
			throw new IllegalArgumentException("Erro na pesquisa de compra: compra nao encontrada na lista.");
		}

		return this.produtosLista.get(itemId).toString();

	}

	public String retornaItemPosicao(int itemPosicao) {
		if (itemPosicao < 0 || itemPosicao >= this.saidaOrdenada.size()) {
			return "";
		}

		return this.saidaOrdenada.get(itemPosicao);
	}

	public void finalizarLista(String localDaCompra, double valorFinalDaCompra) {
		if (localDaCompra == null || localDaCompra.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Erro na finalizacao de lista de compras: local nao pode ser vazio ou nulo.");
		} else if (valorFinalDaCompra <= 0) {
			throw new IllegalArgumentException(
					"Erro na finalizacao de lista de compras: valor final da lista invalido.");
		}
		
		this.finalizada = true;
		this.localDeCompra = localDaCompra;
		this.valorFinal = valorFinalDaCompra;
	}

	public Set<Integer> getIdsProdutos() {

		return this.produtosLista.keySet();
	}

	public String getDescritor() {
		return this.descritor;
	}
		
	public String getData() {
		return this.data;
	}
	
	public String getHora() {
		return this.hora;
	}
	
	public String getDescritorComData() {
		return String.format("%s - %s", this.data, this.descritor);
	}

	public boolean getExistenciaDeItem(int itemId) {
		
		if (this.produtosLista.containsKey(itemId)) {
			return true;
		}

		return false;
	}
	
	public boolean getExistenciaDeItem(String descritor) {

		for (ProdutoLista produto : this.produtosLista.values()) {
			if (descritor.equals(produto.getNome())) {
				return true;
			}
		}
		return false;
	}
	
	public int getQuantidadeItem(int id) {
		if (!this.getExistenciaDeItem(id)) {
			throw new IllegalArgumentException("Erro: Esse item nao esta na lista.");
		}
		
		return this.produtosLista.get(id).getQuantidade();
	}
	
	public ListaDeCompras getClone(String descritor, String data, String hora) {
		
		ListaDeCompras lista = new ListaDeCompras(descritor, data, hora);
		
		for (ProdutoLista produto : this.produtosLista.values()) {
			lista.adicionaProdutoNaLista(produto);
		}
		
		return lista;
	}

	public String toString() {

		String saida = "";

		for (String saidaProduto : this.saidaOrdenada) {
			saida += saidaProduto + System.lineSeparator();
		}

		return saida.trim();
	}

	private void ordenaSaida() {

		this.saidaOrdenada.clear();
		List<ProdutoLista> produtos = new LinkedList<>();
		produtos.addAll(this.produtosLista.values());

		Collections.sort(produtos, new OrdenaItemLista());

		for (ProdutoLista produto : produtos) {
			this.saidaOrdenada.add(produto.toString());
		}		
	}
	
}
