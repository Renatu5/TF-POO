import javax.swing.*;

public class Janela extends JFrame {
    private JPanel painelAtual;
    private FormDrones formDrones;
    private FormTransporte formTransporte;

    public Janela() {
        super();
        // Inicializa os dois formulários
        formTransporte = new FormTransporte(this); // Instância de FormTransporte
        formDrones = new FormDrones(this);    // Passa a instância de FormTransporte para Form

        // Exibe inicialmente o Form padrão
        painelAtual = formDrones.getPainel();
        this.add(painelAtual);

        // Configuração da janela
        this.setSize(1920, 1080);
        this.setTitle("ACMEDrone - O Futuro Começa Hoje");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }

    // Método para trocar o painel exibido
    public void trocarParaFormTransporte() {
        this.remove(painelAtual); // Remove o painel atual
        painelAtual = formTransporte.getPainel(); // Troca para o painel de FormTransporte
        this.add(painelAtual); // Adiciona o novo painel
        this.revalidate(); // Revalida a interface
        this.repaint(); // Re-renderiza a interface
        formTransporte.setDrones(formDrones.getDrones());
    }

    public void trocarParaForm() {
        this.remove(painelAtual); // Remove o painel atual
        painelAtual = formDrones.getPainel(); // Troca para o painel de Form
        this.add(painelAtual); // Adiciona o novo painel
        this.revalidate(); // Revalida a interface
        this.repaint();// Re-renderiza a interface
        formDrones.setTransportes(formTransporte);
    }
}

//    public Janela() {
//        super();
//        formTransporte = new FormTransporte();
//        this.add(formTransporte.getPainel());
//        this.setSize(1920, 1080);
//        this.setTitle("ACMEDrone - O Futuro Começa Hoje");
//        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
//        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        this.setVisible(true);
//    }
//}

/*public Janela() {
        super();
        form = new Form();
        this.add(form.getPainel());
        this.setSize(1920, 1080);
        this.setTitle("ACMEDrone - O Futuro Começa Hoje");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }*/