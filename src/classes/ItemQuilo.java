package classes;

/**
 * Classe extendida de Item, implementa itens que seus precos são calculados a
 * partir do peso do kg passado como parametro
 *
 */
public class ItemQuilo extends Item {

	private double kg;

	/**
	 * Construtor que inicializa os atributos da classe Item, os atributos da classe
	 * ItemQuilo e verifica a excecao feita para validar itens que calculam preco
	 * por kg
	 * 
	 * @param id
	 * @param nome
	 * @param categoria
	 * @param kg
	 * @param localDeCompra
	 * @param preco
	 */
	public ItemQuilo(int id, String nome, String categoria, double kg, String localDeCompra, double preco) {
		super(id, nome, categoria, localDeCompra, preco);
		validaItemQuilo(kg);
		this.kg = kg;
	}

	/**
	 * Atualiza o item passando novos atributo e verifica se os novos
	 * atributos sao validos
	 */
	@Override
	public void atualizaItem(String atributo, String novoValor) {
		super.validaAtualizaItem(atributo, novoValor);
		switch (atributo) {
		case "nome":
			this.nome = novoValor;
			break;
		case "categoria":
			if (super.verificaCategoria(novoValor)) {
				this.categoria = novoValor;
				break;
			}
			throw new IllegalArgumentException("Erro na atualizacao de item: categoria nao existe.");
		case "kg":
			this.kg = verificaQuilo(novoValor);
			break;
		default:
			throw new IllegalArgumentException("Erro na atualizacao de item: atributo nao existe.");
		}
	}

	@Override
	public String toString() {
		return super.id + ". " + super.nome + ", " + super.categoria + ", Preco por quilo: " + super.toString();
	}

	/**
	 * Lanca excecao para verificar se os paramentros passados sao validos
	 * 
	 * @param valor
	 * @return
	 */
	private double verificaQuilo(String valor) {
		double quantidade;
		try {
			quantidade = Double.parseDouble(valor);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Erro na atualizacao de item: quilo nao numerica.");
		}

		if (quantidade < 0) {
			throw new IllegalArgumentException(
					"Erro na atualizacao de item: valor de quilos nao pode ser menor que zero.");
		}
		return quantidade;

	}

	/**
	 * Lanca exececao para verificar se a quantidade de kg passado como parametro
	 * pode ser usada
	 * 
	 * @param kg
	 */
	private void validaItemQuilo(double kg) {
		if (kg <= 0) {
			throw new IllegalArgumentException(
					"Erro no cadastro de item: valor de quilos nao pode ser menor que zero.");
		}
	}
}
