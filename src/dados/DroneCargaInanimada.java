package dados;

import java.io.Serializable;
public class DroneCargaInanimada extends DroneCarga implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean protecao;

    public DroneCargaInanimada(int codigo, double custoFixo, double autonomia, boolean disponivel, double pesoMaximo, boolean protecao) {
        super(codigo, custoFixo, autonomia, disponivel, pesoMaximo);
        this.protecao = protecao;
    }

    public boolean protegido() {
        return protecao;
    }

    @Override
    public boolean verificaCompatibilidade(double peso) {
        return peso <= getPesoMaximo();
    }

    @Override
    public String toString() {
        return super.toString() + "dados.DroneCargaInanimada protecao=" + protecao + "]";
    }

    @Override
    public double calcularCustoKm() {
        return 0;
    }
}
