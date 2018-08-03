package classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import interfaces.ListaOrdenavel;
import interfaces.OrdenaItemLista;
import interfaces.ProdutoListaOrdenavel;

public class ListaDeCompras implements ListaOrdenavel, Serializable {

	private String descritor, data, localDeCompra, hora;
	private HashMap<Integer, ProdutoLista> produtosLista;
	private ArrayList<String> saidaOrdenada;
	private double valorFinal;
	private boolean finalizada;

	/**
	 * Construtor do metodo
	 * 
	 * @param descritor
	 *            da lista
	 * @param data
	 *            de cricao da lista
	 * @param hora
	 *            de cricao da lista
	 */
	public ListaDeCompras(String descritor, String data, String hora) {
		this.descritor = descritor;
		this.data = data;
		this.hora = hora;
		this.produtosLista = new HashMap<>();
		this.saidaOrdenada = new ArrayList();
		this.finalizada = false;
	}

	/**
	 * Adiciona um protudo na lista
	 * 
	 * @param item
	 *            a ser adicionado
	 * @param quantidade
	 *            do item
	 */
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

	/**
	 * Adiciona um produto na lista
	 * 
	 * @param produto
	 *            a ser adiconado
	 */
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

	/**
	 * Remove um item da lista
	 * 
	 * @param itemId
	 *            id do item
	 */
	public void deletaProdutoLista(int itemId) {

		if (!this.produtosLista.containsKey(itemId)) {
			throw new IllegalArgumentException("Erro na exclusao de compra: compra nao encontrada na lista.");
		}

		this.produtosLista.remove(itemId);
		ordenaSaida();
	}

	/**
	 * Altera a quantidade do produto na lista
	 * 
	 * @param itemId
	 *            do item
	 * @param operacao
	 *            a ser realizada(aumenta e diminui)
	 * @param quantidade
	 *            valor da mudanca
	 */
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

	/**
	 * Busca um item na lista pelo seu id
	 * 
	 * @param itemId
	 *            do item
	 * @return representacao textual de um produto da lista
	 */
	public String pesquisaCompraEmLista(int itemId) {

		if (!this.produtosLista.containsKey(itemId)) {
			throw new IllegalArgumentException("Erro na pesquisa de compra: compra nao encontrada na lista.");
		}

		return this.produtosLista.get(itemId).toString();

	}

	/**
	 * Representacao de um item da lista
	 * 
	 * @param itemPosicao
	 *            do item
	 * @return representacao textual do produto
	 */
	public String retornaItemPosicao(int itemPosicao) {
		if (itemPosicao < 0 || itemPosicao >= this.saidaOrdenada.size()) {
			return "";
		}

		return this.saidaOrdenada.get(itemPosicao);
	}

	/**
	 * Fecha a lista
	 * 
	 * @param localDaCompra
	 *            onde foi comprado os produtos da lista
	 * @param valorFinalDaCompra
	 *            quanto foi pago
	 */
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

	/**
	 * Set contendo tododos os ids dos items na lista que sera usado
	 * 
	 * @return set dos ids dos items na lista
	 */
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

		return this.produtosLista.containsKey(itemId);
	}

	public HashMap<Integer, Integer> retornaItensEQuantidades() {
		HashMap<Integer, Integer> itensEQuantidades = new HashMap<>();

		for (ProdutoLista produto : this.produtosLista.values()) {
			itensEQuantidades.put(produto.getId(), produto.getQuantidade());
		}

		return itensEQuantidades;
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

	public ArrayList<String> ordenaProdutosParaSupermercado(ArrayList<Integer> ids) {
		ArrayList<String> saidaProdutos = new ArrayList<>();
		ArrayList<ProdutoListaOrdenavel> produtosParaOrdenar = new ArrayList<>();

		for (int id : ids) {
			produtosParaOrdenar.add(this.produtosLista.get(id));
		}

		Collections.sort(produtosParaOrdenar, new OrdenaItemLista());

		for (ProdutoListaOrdenavel produto : produtosParaOrdenar) {
			saidaProdutos.add("- " + produto.toString());
		}

		return saidaProdutos;
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
		ArrayList<ProdutoLista> produtos = new ArrayList<ProdutoLista>();
		produtos.addAll(this.produtosLista.values());

		Collections.sort(produtos, new OrdenaItemLista());

		for (ProdutoLista produto : produtos) {
			this.saidaOrdenada.add(produto.toString());
		}
	}

}
