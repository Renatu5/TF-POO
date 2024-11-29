package dados;

import java.io.Serializable;
public class DroneCargaViva extends DroneCarga implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean climatizado;

    public DroneCargaViva(int codigo, double custoFixo, double autonomia, boolean disponivel, double pesoMaximo, boolean climatizado) {
        super(codigo, custoFixo, autonomia, disponivel, pesoMaximo);
        this.climatizado = climatizado;
    }

    public boolean climatizado() {
        return climatizado;
    }

    @Override
    public boolean verificaCompatibilidade(double peso) {
        return peso <= getPesoMaximo() && climatizado;
    }

    @Override
    public String toString() {
        return super.toString() + " | dados.DroneCargaViva [climatizado=" + climatizado + "]";
    }

    @Override
    public double calcularCustoKm() {
        return 0;
    }

}