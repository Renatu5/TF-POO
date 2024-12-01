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
    public double calculaCusto() {
        double distancia = calcularDistancia();
        double custoKm = getDrone().calcularCustoKm();
        double acrescimo = (temperaturaMaxima - temperaturaMinima > 10) ? 1000.0 : 0.0;
        return (custoKm * distancia) + acrescimo;
    }

    public double getTempMin() {
        return temperaturaMinima;
    }

    public double getTempMax() {
        return temperaturaMaxima;
    }


    public String toCsv() {
        {
            return "type;Numero Identificador;Nome do Cliente;Descricao;peso;LatitudeOrigem;LatitudeDestino;longitudeOrigem;LongitudeDestino;temperaturaMinima;temperaturaMaxima;"
                    + getType() + ";" + getNumero() + ";" + getNomeCliente() + ";" + getDescricao() + ";" + getPeso() + ";" + getLatitudeOrigem() + ";" + getLatitudeDestino() + ";" + getLongitudeOrigem() + ";" + getLongitudeDestino() + ";" + temperaturaMinima + ";" + temperaturaMaxima + ";\n";
        }
    }
}
