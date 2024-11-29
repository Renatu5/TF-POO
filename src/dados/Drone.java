package dados;

import java.io.Serializable;
public abstract class Drone implements Serializable {
    private static final long serialVersionUID = 1L;
    private int codigo;
    private double custoFixo;
    private double autonomia;
    private boolean disponivel;
    public Drone(int codigo, double custoFixo, double autonomia, boolean disponivel) {
        this.codigo = codigo;
        this.custoFixo = custoFixo;
        this.autonomia = autonomia;
        this.disponivel = disponivel;
    }

    public abstract double calcularCustoKm();

    public int getCodigo() {
        return codigo;
    }

    public double getCustoFixo() {
        return custoFixo;
    }

    public double getAutonomia() {
        return autonomia;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean b) {
    }

}