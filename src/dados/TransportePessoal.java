package dados;

import java.io.Serializable;
public class TransportePessoal extends Transporte implements Serializable {
    private static final long serialVersionUID = 1L;
    private int qtdPessoas;

    public TransportePessoal(String tipoTransporte, int numero, String nomeCliente, String descricao, double peso,
                             double latitudeOrigem, double longitudeOrigem,
                             double latitudeDestino, double longitudeDestino, int qtdPessoas) {
        super(1, tipoTransporte, numero, nomeCliente, descricao, peso, latitudeOrigem, longitudeOrigem, latitudeDestino, longitudeDestino);
        this.qtdPessoas = qtdPessoas;
    }

    @Override
    public double calculaCusto() {
        double distancia = calcularDistancia();
        double custoKm = getDrone().calcularCustoKm();
        double acrescimo = qtdPessoas * 10.0;
        return (custoKm * distancia) + acrescimo;
    }

    public int getQtdPessoas() {
        return qtdPessoas;
    }

    public String toCsv() {
        {
            return "type;Numero Identificador;Nome do Cliente;Descricao;peso;LatitudeOrigem;LatitudeDestino;longitudeOrigem;LongitudeDestino;situacao;Quantidade de Pessoas\n"
                    +getType()+";"+getNumero()+";"+getNomeCliente()+";"+getDescricao()+";"+getPeso()+";"+getLatitudeOrigem()+";"+getLatitudeDestino()+";"+getLongitudeOrigem()+";"+getLongitudeDestino()+";"+getSituacao()+";"+getQtdPessoas()+";\n";
        }
    }



}

