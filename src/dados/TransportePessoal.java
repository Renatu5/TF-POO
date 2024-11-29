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
    public double calcularAcréscimos() {
        return qtdPessoas * 10.0;
    }

    public int getQtdPessoas() {
        return qtdPessoas;
    }


}

