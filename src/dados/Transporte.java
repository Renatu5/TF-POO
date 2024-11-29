package dados;

import java.io.Serializable;
public abstract class Transporte implements Serializable {
    private static final long serialVersionUID = 1L;
    private int numero;
    private String nomeCliente;
    private String descricao;
    private double peso;
    private double latitudeOrigem;
    private double longitudeOrigem;
    private double latitudeDestino;
    private double longitudeDestino;
    private String tipoTransporte;
    private Estado situacao;
    private Drone drone;
    private int type;

    public Transporte(int type, String tipoTransporte, int numero, String nomeCliente, String descricao, double peso,
                      double latitudeOrigem, double longitudeOrigem,
                      double latitudeDestino, double longitudeDestino) {
        this.type = type;
        this.tipoTransporte = tipoTransporte;
        this.numero = numero;
        this.nomeCliente = nomeCliente;
        this.descricao = descricao;
        this.peso = peso;
        this.latitudeOrigem = latitudeOrigem;
        this.longitudeOrigem = longitudeOrigem;
        this.latitudeDestino = latitudeDestino;
        this.longitudeDestino = longitudeDestino;
        this.situacao = Estado.PENDENTE;
    }

    public abstract double calcularAcréscimos();

    public double calcularDistancia() {
        double raioTerra = 6371; // Raio da Terra em km
        double latDiff = Math.toRadians(latitudeDestino - latitudeOrigem);
        double lonDiff = Math.toRadians(longitudeDestino - longitudeOrigem);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(latitudeOrigem)) * Math.cos(Math.toRadians(latitudeDestino)) *
                        Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return raioTerra * c;
    }

    public double calcularCustoFinal(Drone drone) {
        double distancia = calcularDistancia();
        double custoKm = drone.calcularCustoKm();
        return (custoKm * distancia) + calcularAcréscimos();
    }

    public Estado getSituacao() {
        return situacao;
    }

    public int getNumero() {
        return numero;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPeso() {
        return peso;
    }

    public double getLongitudeOrigem() {
        return longitudeOrigem;
    }

    public double getLatitudeOrigem() {
        return latitudeOrigem;
    }

    public double getLatitudeDestino() {
        return latitudeDestino;
    }

    public double getLongitudeDestino() {
        return longitudeDestino;
    }

    public String getTipoTransporte(){
        return tipoTransporte;
    }

    public Drone getDrone() {
        return drone;
    }


    public void setSituacao(Estado situacao) {
        if (this.situacao == Estado.TERMINADO || this.situacao == Estado.CANCELADO) {
            throw new IllegalStateException("Não é possível alterar a situação de um transporte TERMINADO ou CANCELADO.");
        }
        this.situacao = situacao;
    }

    @Override
    public String toString() {
        return ("dados.Transporte N°" + numero
                + "\nTipo: " + tipoTransporte +
                "\nCliente: " + nomeCliente +
                "\nDescrição: " + descricao +
                "\nPeso: " + peso +
                "\nSituação: " + situacao +
                "\ndados.Drone atribuído: " + drone
                );
    }

    public void setDrone(Drone droneDisponivel) {

    }

    public int getType() {
        return type;
    }
}