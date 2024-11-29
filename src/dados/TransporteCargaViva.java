package dados;

import java.io.Serializable;
public class TransporteCargaViva extends Transporte implements Serializable {
    private static final long serialVersionUID = 1L;
    private double temperaturaMinima;
    private double temperaturaMaxima;

    public TransporteCargaViva(String tipoTransporte, int numero, String nomeCliente, String descricao, double peso,
                               double latitudeOrigem, double longitudeOrigem,
                               double latitudeDestino, double longitudeDestino,
                               double temperaturaMinima, double temperaturaMaxima) {
        super(3, tipoTransporte, numero, nomeCliente, descricao, peso, latitudeOrigem, longitudeOrigem, latitudeDestino, longitudeDestino);
        this.temperaturaMinima = temperaturaMinima;
        this.temperaturaMaxima = temperaturaMaxima;
    }

    @Override
    public double calcularAcréscimos() {
        double intervalo = temperaturaMaxima - temperaturaMinima;
        return intervalo > 10.0 ? 1000.0 : 0.0;
    }

    public double getTempMin() {
        return temperaturaMinima;
    }

    public double getTempMax() {
        return temperaturaMaxima;
    }
}
