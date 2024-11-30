import dados.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;


public class FormDrones{
    private JLabel labelTitulo;
    private JPanel painel;
    private JLabel logo;
    private JLabel labelSubtitulo;
    private JLabel labelCodigo;
    private JTextField campoCodigo;
    private JComboBox selecaoTipo;
    private JLabel labelTipo;
    private JTextField campoCapacidadeQuantidade;
    private JLabel labelCapacidadeQuantidade;
    private JTextField campoAutonomia;
    private JLabel labelAutonomia;
    private JTextField campoCusto;
    private JLabel labelCusto;
    private JLabel labelInstrucao;
    private JCheckBox checkboxCargaInanimada;
    private JButton botaoCadastro;
    private JTextArea areaDeTexto;
    private JButton botaoSair;
    private JButton botaoDados;
    private JButton botaoLimpar;
    private JCheckBox checkboxCargaViva;
    private JLabel labelExtras;
    private JComboBox selecaoCarga;
    private JLabel labelTipoCarga;
    private JButton botaoTrocaJanela;
    private JButton botaoRelatorioGeral;
    private JButton botaoSalvarDados;
    private JButton botaoCarregarDados;
    private FormTransporte formTransporte;

    private ArrayList<Drone> drones = new ArrayList<>();

    public JPanel getPainel() {
        return painel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        logo = new JLabel(new ImageIcon("logotipo.png"));
    }
    public FormDrones(Janela janela) {
        this.formTransporte = null;
        selecaoTipo.addItem("Drone Pessoal");
        selecaoTipo.addItem("Drone de Carga");
        selecaoCarga.addItem("Inanimada");
        selecaoCarga.addItem("Viva");

        labelCapacidadeQuantidade.setVisible(true);
        campoCapacidadeQuantidade.setVisible(true);

        selecaoTipo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarCampos();
            }
        });

        campoCapacidadeQuantidade.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                return verificarEntrada();
            }
        });


        botaoCadastro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarDrone();
            }
        });

        botaoTrocaJanela.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                janela.trocarParaFormTransporte();
            }
        });

        botaoLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });

        botaoDados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDados();
            }
        });

        botaoSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        botaoRelatorioGeral.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarRelatorioGeral(formTransporte);
            }
        });

        botaoSalvarDados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarDados();
            }
        });

        botaoCarregarDados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarDados();
            }
        });

    }

    public ArrayList<Drone> getDrones() {
        return drones;
    }

    public void setTransportes(FormTransporte transportes) {
        this.formTransporte = transportes;
    }

    private void cadastrarDrone() {
        try {
            String codigo = campoCodigo.getText().trim();
            String tipo = (String) selecaoTipo.getSelectedItem();

            if (codigo.isEmpty() || campoCapacidadeQuantidade.getText().trim().isEmpty() ||
                    campoAutonomia.getText().trim().isEmpty() || campoCusto.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(painel, "ERRO: Todos os campos devem estar preenchidos.");
                return;
            }

            double pesoMaximo;
            double autonomia;
            double custo;
            boolean disponivel = true;

            try {
                pesoMaximo = Double.parseDouble(campoCapacidadeQuantidade.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(painel, "ERRO: Valor inválido para 'Capacidade'.");
                return;
            }

            try {
                autonomia = Double.parseDouble(campoAutonomia.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(painel, "ERRO: Valor inválido para 'Autonomia'.");
                return;
            }

            try {
                custo = Double.parseDouble(campoCusto.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(painel, "ERRO: Valor inválido para 'Custo' (utilize '.' para valores quebrados).");
                return;
            }

            if (codigo.length() != 6 || !codigo.chars().allMatch(Character::isDigit)) {
                JOptionPane.showMessageDialog(painel, "ERRO: Código inválido. Deve ter exatamente 6 dígitos.");
                return;
            }

            // duplicidade de código
            for (Drone drone : drones) {
                if (drone.getCodigo() == Integer.parseInt(codigo)) {
                    JOptionPane.showMessageDialog(painel, "ERRO: Código de Drone já cadastrado.");
                    return;
                }
            }

            int pesoMaximoInt = (int) pesoMaximo;

            // drone conforme o tipo selecionado
            Drone novoDrone = null;
            if ("Drone Pessoal".equals(tipo)) {
                novoDrone = new DronePessoal(Integer.parseInt(codigo), custo, autonomia, pesoMaximoInt, disponivel);
            } else if ("Drone de Carga".equals(tipo)) {
                String tipoCarga = (String) selecaoCarga.getSelectedItem();
                boolean protegido = checkboxCargaInanimada.isSelected();
                boolean climatizado = checkboxCargaViva.isSelected();

                if ("Inanimada".equals(tipoCarga)) {
                    if (climatizado) {
                        JOptionPane.showMessageDialog(painel, "ERRO: O tipo Carga Inanimada não pode ser climatizado.");
                        return;
                    }
                    novoDrone = new DroneCargaInanimada(Integer.parseInt(codigo), custo, autonomia, disponivel, pesoMaximo, protegido);
                } else if ("Viva".equals(tipoCarga)) {
                    if (protegido) {
                        JOptionPane.showMessageDialog(painel, "ERRO: O tipo Carga Viva não pode ser protegido.");
                        return;
                    }
                    novoDrone = new DroneCargaViva(Integer.parseInt(codigo), custo, autonomia, disponivel, pesoMaximo, climatizado);
                }
            }

            // adicionar o drone na lista
            if (novoDrone != null) {
                drones.add(novoDrone);
                drones.sort(Comparator.comparingInt(Drone::getCodigo));
                areaDeTexto.setText("Drone cadastrado com sucesso!\n" + novoDrone);
            } else {
                JOptionPane.showMessageDialog(painel, "ERRO: Não foi possível cadastrar o drone.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(painel, "ERRO NÃO CATALOGADO: " + ex.getMessage());
        }
    }


    //limpar textos
    private void limparCampos() {
        campoCodigo.setText("");
        campoCapacidadeQuantidade.setText("");
        campoAutonomia.setText("");
        campoCusto.setText("");
        areaDeTexto.setText("");
        checkboxCargaInanimada.setSelected(false);
        checkboxCargaViva.setSelected(false);

    }

    //mostrar cadastros
    private void mostrarDados() {
        if (drones.isEmpty()) {
            JOptionPane.showMessageDialog(painel, "Nenhum Drone cadastrado.");
            return;
        }

        StringBuilder builder = new StringBuilder("DRONES CADASTRADOS:\n");
        for (Drone drone : drones) {
            if (drone instanceof DronePessoal) {
                DronePessoal dp = (DronePessoal) drone;
                builder.append("Drone Pessoal - Código: ").append(dp.getCodigo())
                        .append(", Custo Fixo: ").append(dp.getCustoFixo())
                        .append(", Autonomia: ").append(dp.getAutonomia())
                        .append(", Capacidade de Pessoas: ").append(dp.getQtdMaxPessoas()).append("\n");
            } else if (drone instanceof DroneCargaInanimada) {
                DroneCargaInanimada dci = (DroneCargaInanimada) drone;
                builder.append("Drone de Carga (Inanimada) - Código: ").append(dci.getCodigo())
                        .append(", Custo Fixo: ").append(dci.getCustoFixo())
                        .append(", Autonomia: ").append(dci.getAutonomia())
                        .append(", Proteção: ").append(dci.protegido() ? "Sim" : "Não")
                        .append(", Capacidade: ").append(dci.getPesoMaximo()+"kg").append("\n");

            } else if (drone instanceof DroneCargaViva) {
                DroneCargaViva dcv = (DroneCargaViva) drone;
                builder.append("Drone de Carga (Viva) - Código: ").append(dcv.getCodigo())
                        .append(", Custo Fixo: ").append(dcv.getCustoFixo())
                        .append(", Autonomia: ").append(dcv.getAutonomia())
                        .append(", Climatizado: ").append(dcv.climatizado() ? "Sim" : "Não")
                        .append(", Capacidade: ").append(dcv.getPesoMaximo()+"kg").append("\n");

            }
        }
        areaDeTexto.setText(builder.toString());
    }

    private void atualizarCampos() {
        String tipoSelecionado = (String) selecaoTipo.getSelectedItem();
        if ("Drone Pessoal".equals(tipoSelecionado)) {
            labelCapacidadeQuantidade.setText("Capacidade (Nº de Pessoas):");
            labelExtras.setVisible(false);
            labelTipoCarga.setVisible(false);
            selecaoCarga.setVisible(false);
            checkboxCargaInanimada.setVisible(false);
            checkboxCargaViva.setVisible(false);
        } else if ("Drone de Carga".equals(tipoSelecionado)) {
            labelCapacidadeQuantidade.setText("Capacidade (Peso em Kg):");
            labelExtras.setVisible(true);
            labelTipoCarga.setVisible(true);
            selecaoCarga.setVisible(true);
            checkboxCargaInanimada.setVisible(true);
            checkboxCargaViva.setVisible(true);
        }
        campoCapacidadeQuantidade.setVisible(true);
        labelCapacidadeQuantidade.setVisible(true);
        campoCapacidadeQuantidade.setText("");  // Limpar o campo
    }

    private boolean verificarEntrada() {
        String tipoSelecionado = (String) selecaoTipo.getSelectedItem();
        String texto = campoCapacidadeQuantidade.getText();
            if ("Drone Pessoal".equals(tipoSelecionado)) {
                Integer.parseInt(texto); // Validar como int
            } else if ("Drone de Carga".equals(tipoSelecionado)) {
                Double.parseDouble(texto); // Validar como double
            }
            return true;
        }


    private void mostrarRelatorioGeral(FormTransporte formTransporte) {
        // Verifica se há dados cadastrados em ambas as listas
        if (drones.isEmpty() && formTransporte.getTransportes().isEmpty()) {
            JOptionPane.showMessageDialog(painel, "Nenhum Drone ou Transporte cadastrado para gerar o relatório.");
            return;
        }

        StringBuilder builder = new StringBuilder("RELATÓRIO GERAL:\n\n");

        double custoTotalDrones = 0;
        double custoTotalTransportes = 0;

        builder.append("DRONES:\n");
        if (drones.isEmpty()) {
            builder.append(" - Nenhum Drone cadastrado.\n\n");
        } else {
            for (Drone drone : drones) {
                if (drone instanceof DronePessoal) {
                    DronePessoal dp = (DronePessoal) drone;
                    builder.append("Drone Pessoal N°").append(dp.getCodigo())
                            .append(", Custo Fixo: R$ ").append(dp.getCustoFixo())
                            .append(", Autonomia: ").append(dp.getAutonomia()).append(" km")
                            .append(", Capacidade (Nº de Pessoas): ").append(dp.getQtdMaxPessoas()).append("\n");
                    custoTotalDrones += dp.getCustoFixo();
                } else if (drone instanceof DroneCargaInanimada) {
                    DroneCargaInanimada dci = (DroneCargaInanimada) drone;
                    builder.append("Drone de Carga Inanimada Nº:").append(dci.getCodigo())
                            .append(", Custo Fixo: R$ ").append(dci.getCustoFixo())
                            .append(", Autonomia: ").append(dci.getAutonomia()).append(" km")
                            .append(", Proteção: ").append(dci.protegido() ? "Sim" : "Não").append("\n");
                    custoTotalDrones += dci.getCustoFixo();
                } else if (drone instanceof DroneCargaViva) {
                    DroneCargaViva dcv = (DroneCargaViva) drone;
                    builder.append("Drone de Carga Viva Nº:").append(dcv.getCodigo())
                            .append(", Custo Fixo: R$ ").append(dcv.getCustoFixo())
                            .append(", Autonomia: ").append(dcv.getAutonomia()).append(" km")
                            .append(", Climatizado: ").append(dcv.climatizado() ? "Sim" : "Não").append("\n");
                    custoTotalDrones += dcv.getCustoFixo();
                }
            }
        }

        builder.append("\nTRANSPORTES:\n");
        if (formTransporte.getTransportes().isEmpty()) {
            builder.append(" - Nenhum Transporte cadastrado.\n");
        } else {
            for (Transporte transporte : formTransporte.getTransportes()) {
                builder.append("Transporte ").append(transporte.getTipoTransporte()).append(" N°").append(transporte.getNumero())
                        .append(", Peso: ").append(transporte.getPeso()).append(" Pessoas/kgs")
                        .append(", Distância Total: ").append(transporte.calcularDistancia()).append(" km\n");
            }
        }

        // Adiciona os custos totais ao relatório
        builder.append("CUSTOS TOTAIS:\n")
                .append(" - Drones: R$ ").append(String.format("%.2f", custoTotalDrones)).append("\n")
                .append(" - Transportes: R$ ").append(String.format("%.2f", custoTotalTransportes)).append("\n")
                .append(" - Geral: R$ ").append(String.format("%.2f", custoTotalDrones + custoTotalTransportes)).append("\n");

        // Exibe o relatório no campo de texto
        areaDeTexto.setText(builder.toString());
    }

    private void salvarDados() {
        try {
            String nomeArquivo = JOptionPane.showInputDialog(painel, "Digite o nome do arquivo (SEM extensão):");
            if (nomeArquivo == null || nomeArquivo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(painel, "ERRO: Nome do arquivo inválido.");
                return;
            }

            // Salva drones
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeArquivo + "_drones.dat"))) {
                oos.writeObject(drones); // A lista "drones" deve estar na classe Form
            }

            // Salva transportes
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeArquivo + "_transportes.dat"))) {
                oos.writeObject(formTransporte.getTransportes()); // Obtenha os transportes de FormTransporte
            }

            JOptionPane.showMessageDialog(painel, "Dados salvos com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(painel, "ERRO: Não foi possível salvar os dados.\n" + e.getMessage());
        }
    }

    private void carregarDados() {
        try {
            String nomeArquivo = JOptionPane.showInputDialog(painel, "Digite o nome do arquivo (SEM extensão ou '_drones'/'_transportes'):");
            if (nomeArquivo == null || nomeArquivo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(painel, "ERRO: Nome do arquivo inválido.");
                return;
            }

            // Carrega drones
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomeArquivo + "_drones.dat"))) {
                drones = (ArrayList<Drone>) ois.readObject(); // Sobrescreve a lista de drones
            }

            // Carrega transportes
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomeArquivo + "_transportes.dat"))) {
                formTransporte.setTransportes((ArrayList<Transporte>) ois.readObject()); // Atualiza a lista de transportes
            }

            JOptionPane.showMessageDialog(painel, "Dados carregados com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(painel, "ERRO: Não foi possível carregar os dados.\n" + e.getMessage());
        }
    }

}




