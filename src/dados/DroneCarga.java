package dados;

public abstract class DroneCarga extends Drone {
    private double pesoMaximo;

    // Construtor
    public DroneCarga(int codigo, double custoFixo, double autonomia, boolean disponivel, double pesoMaximo) {
        super(codigo, custoFixo, autonomia, disponivel);
        this.pesoMaximo = pesoMaximo;
    }

    // Getter para peso máximo
    public double getPesoMaximo() {
        return pesoMaximo;
    }

    // Método abstrato a ser implementado pelas subclasses
    public abstract boolean verificaCompatibilidade(double peso);




}