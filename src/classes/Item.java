package classes;

import java.util.HashMap;

import interfaces.ItemCompravel;

public abstract class Item implements ItemCompravel {
	/**
	 * Mapa para o preco do item no supermercado
	 */
	protected HashMap<String, Double> precos;

	protected int id;
	protected String nome;
	protected String categoria;
	protected double menorPreco;

	public Item(int id, String nome, String categoria, String localDeCompra, double preco) {
		validaItem(nome, categoria, localDeCompra, preco);
		this.id = id;
		this.nome = nome;
		this.categoria = categoria;
		this.precos = new HashMap<>();
		this.precos.put(localDeCompra, preco);
		this.menorPreco = preco;
	}

	public void adicionaPrecoItem(String localDeCompra, double preco) {
		validaAdicionaPreco(localDeCompra, preco);
		this.precos.put(localDeCompra, preco);
		if (preco < this.menorPreco) {
			this.menorPreco = preco;
		}
	}

	public abstract void atualizaItem(String atributo, String novoValor);

	public double getMenorPreco() {
		return this.menorPreco;
	}

	public String getNome() {
		return this.nome;
	}

	public int getId() {
		return this.id;
	}

	public String getCategoria() {
		return this.categoria;
	}

	@Override
	public String toString() {
		String result = "";
		for (String supermercados : precos.keySet()) {
			result += String.format("%s, R$ %.2f;", supermercados, precos.get(supermercados));
		}
		return "<" + result + ">";
	}

	protected void validaAtualizaItem(String atributo, String novoValor) {
		if (atributo == null || atributo.trim().isEmpty()) {
			throw new IllegalArgumentException("Erro na atualizacao de item: atributo nao pode ser vazio ou nulo.");
		} else if (novoValor == null || novoValor.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Erro na atualizacao de item: novo valor de atributo nao pode ser vazio ou nulo.");
		}
	}

	private void validaAdicionaPreco(String localDeCompra, double preco) {
		if (localDeCompra == null || localDeCompra.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Erro no cadastro de preco: local de compra nao pode ser vazio ou nulo.");
		} else if (preco <= 0) {
			throw new IllegalArgumentException("Erro no cadastro de preco: preco de item invalido.");
		}
	}

	private void validaItem(String nome, String categoria, String localDeCompra, double preco) {
		if (nome == null || nome.trim().isEmpty()) {
			throw new IllegalArgumentException("Erro no cadastro de item: nome nao pode ser vazio ou nulo.");
		} else if (categoria == null || categoria.trim().isEmpty()) {
			throw new IllegalArgumentException("Erro no cadastro de item: categoria nao pode ser vazia ou nula.");
		} else if (!verificaCategoria(categoria)) {
			throw new IllegalArgumentException("Erro no cadastro de item: categoria nao existe.");
		} else if (localDeCompra == null || localDeCompra.trim().isEmpty()) {
			throw new IllegalArgumentException("Erro no cadastro de item: local de compra nao pode ser vazio ou nulo.");
		} else if (preco <= 0) {
			throw new IllegalArgumentException("Erro no cadastro de item: preco de item invalido.");
		}
	}

	protected boolean verificaCategoria(String categoria) {
		if (categoria.equals("limpeza") || categoria.equals("alimento industrializado")
				|| categoria.equals("higiene pessoal") || categoria.equals("alimento nao industrializado")) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (categoria == null) {
			if (other.categoria != null)
				return false;
		} else if (!categoria.equals(other.categoria))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

}
