package dados;

import java.io.Serializable;
public class DronePessoal extends Drone implements Serializable {
    private static final long serialVersionUID = 1L;
    private int qtdMaxPessoas;

    public DronePessoal(int codigo, double custoFixo, double autonomia, int qtdMaxPessoas, boolean disponivel) {
        super(codigo, custoFixo, autonomia, disponivel);
        this.qtdMaxPessoas = qtdMaxPessoas;
    }

    @Override
    public double calcularCustoKm() {
        return getCustoFixo() + (qtdMaxPessoas * 2.0);
    }

    public int getQtdMaxPessoas() {
        return qtdMaxPessoas;
    }

    @Override
    public String toString() {
        return "Drone Pessoal N°"+getCodigo()+
                "\nCusto Fixo: "+"R$"+getCustoFixo()+
                "\nAutonomia: "+getAutonomia()+ " minutos"+
                "\nCapacidade: "+getQtdMaxPessoas()+" pessoas";
    }


}