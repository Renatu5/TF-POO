package dados;

import java.io.Serializable;
public class TransporteCargaInanimada extends Transporte implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean cargaPerigosa;

    public TransporteCargaInanimada(String tipoTransporte, int numero, String nomeCliente, String descricao, double peso,
                                    double latitudeOrigem, double longitudeOrigem,
                                    double latitudeDestino, double longitudeDestino, boolean cargaPerigosa) {
        super(2, tipoTransporte, numero, nomeCliente, descricao, peso, latitudeOrigem, longitudeOrigem, latitudeDestino, longitudeDestino);
        this.cargaPerigosa = cargaPerigosa;
    }

    @Override
    public double calculaCusto() {
        double distancia = calcularDistancia();
        double custoKm = getDrone().calcularCustoKm();
        double acrescimo = cargaPerigosa ? 500.0 : 0.0;
        return (custoKm * distancia) + acrescimo;
    }

    public boolean isPerigosa() {
        return cargaPerigosa;
    }
}
