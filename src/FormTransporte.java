import dados.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class FormTransporte {
    private JPanel painelTransporte;
    private JLabel logotipo;
    private JLabel labelSubtitulo;
    private JLabel labelInstrucao;
    private JLabel labelCliente;
    private JTextField campoCliente;
    private JLabel labelLatOrigem;
    private JTextField campoLatOrigem;
    private JTextArea areaDeTexto;
    private JButton botaoCadastro;
    private JButton botaoDados;
    private JButton botaoLimpar;
    private JButton botaoSair;
    private JComboBox<String> selecaoTipoTransporte;
    private JLabel labelTipoTransporte;
    private JLabel labelLatDestino;
    private JTextField campoLatDestino;
    private JLabel labelLonDestino;
    private JTextField campoLonDestino;
    private JLabel labelLonOrigem;
    private JTextField campoLonOrigem;
    private JTextField campoDescricao;
    private JLabel labelDescricao;
    private JTextField campoPeso;
    private JLabel labelPeso;
    private JTextField campoNumero;
    private JLabel labelNumero;
    private JCheckBox checkBoxPerigosa;
    private JLabel labelTempMax;
    private JLabel labelTempMinEPessoas;
    private JTextField campoTempMinEPessoas;
    private JLabel labelCargaPerigosa;
    private JTextField campoTempMax;
    private JButton botaoProcessar;
    private JButton botaoTrocaJanelaForm;
    private JButton botaoAlterarSituacao;
    private ArrayList<Transporte> transportes = new ArrayList<>();
    private ArrayList<Drone> drones;
    private Queue<Transporte> transportesPendentes = new LinkedList<>();

    private void createUIComponents() {
        logotipo = new JLabel(new ImageIcon("logo.png"));
    }

    public JPanel getPainel() {
        return painelTransporte;
    }

    public FormTransporte(Janela janela) {
        drones = new ArrayList<>();
        labelTempMax.setVisible(false);
        campoTempMax.setVisible(false);
        labelCargaPerigosa.setVisible(false);
        checkBoxPerigosa.setVisible(false);
        selecaoTipoTransporte.addItem("Pessoal");
        selecaoTipoTransporte.addItem("Carga Viva");
        selecaoTipoTransporte.addItem("Carga Inanimada");

        selecaoTipoTransporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarCampos();
            }
        });

        botaoCadastro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarTransporte();
            }
        });

        botaoDados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDados();
            }
        });

        botaoLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });

        botaoSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        botaoProcessar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processarTransportesPendentes();
            }
        });

        // Botão para trocar para Form
        botaoTrocaJanelaForm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                janela.trocarParaForm();
            }
        });

        botaoAlterarSituacao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alterarSituacaoTransporte();
            }
        });

    }

    public void setDrones(ArrayList<Drone> drones){
        this.drones = drones;
    }

    private void cadastrarTransporte() {
        try {
            String numeroStr = campoNumero.getText().trim();
            String nomeCliente = campoCliente.getText().trim();
            String descricao = campoDescricao.getText().trim();
            String tipoTransporte = (String) selecaoTipoTransporte.getSelectedItem();

            if (numeroStr.isEmpty() || nomeCliente.isEmpty() || descricao.isEmpty() ||
                    campoPeso.getText().trim().isEmpty() ||
                    campoLatOrigem.getText().trim().isEmpty() ||
                    campoLonOrigem.getText().trim().isEmpty() ||
                    campoLatDestino.getText().trim().isEmpty() ||
                    campoLonDestino.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(painelTransporte, "ERRO: Todos os campos devem estar preenchidos.");
                return;
            }

            int numero, pessoas;
            double peso, latOrigem, lonOrigem, latDestino, lonDestino, tempMin = 0.0, tempMax = 0.0;

            try {
                numero = Integer.parseInt(numeroStr);
                pessoas = Integer.parseInt(campoTempMinEPessoas.getText().trim());

                peso = Double.parseDouble(campoPeso.getText().trim());
                latOrigem = Double.parseDouble(campoLatOrigem.getText().trim());
                lonOrigem = Double.parseDouble(campoLonOrigem.getText().trim());
                latDestino = Double.parseDouble(campoLatDestino.getText().trim());
                lonDestino = Double.parseDouble(campoLonDestino.getText().trim());

                validarLatLon(latOrigem, lonOrigem, "Origem");
                validarLatLon(latDestino, lonDestino, "Destino");

                if ("Carga Viva".equals(tipoTransporte)) {
                    tempMin = Double.parseDouble(campoTempMinEPessoas.getText().trim());
                    tempMax = Double.parseDouble(campoTempMax.getText().trim());
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(painelTransporte, "ERRO: Valores inválidos nos campos numéricos." + e);
                return;
            }

            // duplicidade de número
            for (Transporte t : transportes) {
                if (t.getNumero() == numero) {
                    JOptionPane.showMessageDialog(painelTransporte, "ERRO: Número de transporte já cadastrado.");
                    return;
                }
            }

            // cria o transporte com base no tipo selecionado
            Transporte novoTransporte = null;
            if ("Pessoal".equals(tipoTransporte)) {
                novoTransporte = new TransportePessoal(tipoTransporte, numero, nomeCliente, descricao, peso, latOrigem, lonOrigem, latDestino, lonDestino, pessoas);
            } else if ("Carga Viva".equals(tipoTransporte)) {
                novoTransporte = new TransporteCargaViva(tipoTransporte, numero, nomeCliente, descricao, peso, latOrigem, lonOrigem, latDestino, lonDestino, tempMin, tempMax);
            } else if ("Carga Inanimada".equals(tipoTransporte)) {
                boolean perigosa = checkBoxPerigosa.isSelected();
                novoTransporte = new TransporteCargaInanimada(tipoTransporte, numero, nomeCliente, descricao, peso, latOrigem, lonOrigem, latDestino, lonDestino, perigosa);
            }

            if (novoTransporte != null) {
                transportes.add(novoTransporte);
                areaDeTexto.setText("Transporte adicionado à fila de pendentes.\n" + novoTransporte);
            } else {
                JOptionPane.showMessageDialog(painelTransporte, "ERRO: Não foi possível cadastrar o transporte.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(painelTransporte, "ERRO: " + ex.getMessage());
        }
    }

    private void validarLatLon(double lat, double lon, String tipo) {
        if (lat < -90 || lat > 90) throw new IllegalArgumentException("Latitude de " + tipo + " inválida.");
        if (lon < -180 || lon > 180) throw new IllegalArgumentException("Longitude de " + tipo + " inválida.");
    }

    private void mostrarDados() {
        if (transportes.isEmpty()) {
            JOptionPane.showMessageDialog(painelTransporte, "Nenhum transporte cadastrado.");
            return;
        }

        StringBuilder builder = new StringBuilder("TRANSPORTES CADASTRADOS:\n");
        for (Transporte transportes : transportes) {
            if (transportes instanceof TransportePessoal) {
                TransportePessoal tp = (TransportePessoal) transportes;
                builder.append("Transporte Pessoal - Número: ").append(tp.getNumero())
                        .append(", Cliente: ").append(tp.getNomeCliente())
                        .append(", Descrição: ").append(tp.getDescricao())
                        .append(", Peso: ").append(tp.getPeso())
                        .append(", Capacidade: ").append(tp.getQtdPessoas() + " pessoas")
                        .append(", Coordenadas de origem: ").append(tp.getLatitudeOrigem() + ", ").append(tp.getLongitudeOrigem())
                        .append(", Coordenadas de destino: ").append(tp.getLatitudeDestino() + ", ").append(tp.getLongitudeDestino())
                        .append(", Situação: ").append(tp.getSituacao()).append("\n");
            } else if (transportes instanceof TransporteCargaInanimada) {
                TransporteCargaInanimada tci = (TransporteCargaInanimada) transportes;
                builder.append("Transporte de Carga Inanimada - Número: ").append(tci.getNumero())
                        .append(", Cliente: ").append(tci.getNomeCliente())
                        .append(", Descrição: ").append(tci.getDescricao())
                        .append(", Peso: ").append(tci.getPeso())
                        .append(", Coordenadas de origem: ").append(tci.getLatitudeOrigem() + ", ").append(tci.getLongitudeOrigem())
                        .append(", Coordenadas de destino: ").append(tci.getLatitudeDestino() + ", ").append(tci.getLongitudeDestino())
                        .append(", Situação: ").append(tci.getSituacao()).append("\n");
            } else if (transportes instanceof TransporteCargaViva) {
                TransporteCargaViva tcv = (TransporteCargaViva) transportes;
                builder.append("Transporte de Carga Viva - Número: ").append(tcv.getNumero())
                        .append(", Cliente: ").append(tcv.getNomeCliente())
                        .append(", Descrição: ").append(tcv.getDescricao())
                        .append(", Peso: ").append(tcv.getPeso())
                        .append(", Coordenadas de origem: ").append(tcv.getLatitudeOrigem() + ", ").append(tcv.getLongitudeOrigem())
                        .append(", Coordenadas de destino: ").append(tcv.getLatitudeDestino() + ", ").append(tcv.getLongitudeDestino())
                        .append(", Situação: ").append(tcv.getSituacao()).append("\n");
            }
        }
        areaDeTexto.setText(builder.toString());
    }

    private void processarTransportesPendentes() {
        StringBuilder resultado = new StringBuilder("PROCESSAMENTO DE TRANSPORTES PENDENTES:\n");

        // uma lista temporária para armazenar transportes não alocados
        ArrayList<Transporte> naoAlocados = new ArrayList<>();
        // percorre cada transporte pendente
        for (Transporte transporte : transportes) {
            boolean alocado = false;
            // tenta alocar o transporte a um drone disponível
            for (Drone drone : drones) {
                if (drone instanceof DronePessoal && transporte instanceof TransportePessoal) {
                    DronePessoal dronePessoal = (DronePessoal) drone;
                    TransportePessoal transportePessoal = (TransportePessoal) transporte;
                    if (dronePessoal.getQtdMaxPessoas() >= transportePessoal.getQtdPessoas()) {
                        resultado.append("Transporte Pessoal ")
                                .append(transporte.getNumero())
                                .append(" alocado ao Drone Pessoal ")
                                .append(drone.getCodigo()).append("\n");
                        alocado = true;
                        transportePessoal.setSituacao(Estado.ALOCADO);
                        break; // Sai do loop de drones após alocação
                    }
                } else if (drone instanceof DroneCargaViva && transporte instanceof TransporteCargaViva) {
                    DroneCargaViva droneCargaViva = (DroneCargaViva) drone;
                    TransporteCargaViva transporteCargaViva = (TransporteCargaViva) transporte;

                    if (droneCargaViva.climatizado()) {
                        resultado.append("Transporte de Carga Viva ")
                                .append(transporte.getNumero())
                                .append(" alocado ao Drone de Carga Viva ")
                                .append(drone.getCodigo()).append("\n");
                        alocado = true;
                        transporteCargaViva.setSituacao(Estado.ALOCADO);
                        break;
                    }
                } else if (drone instanceof DroneCargaInanimada && transporte instanceof TransporteCargaInanimada) {
                    DroneCargaInanimada droneCargaInanimada = (DroneCargaInanimada) drone;
                    TransporteCargaInanimada transporteCargaInanimada = (TransporteCargaInanimada) transporte;

                    if (droneCargaInanimada.protegido() || !transporteCargaInanimada.isPerigosa()) {
                        resultado.append("Transporte de Carga Inanimada ")
                                .append(transporte.getNumero())
                                .append(" alocado ao Drone de Carga Inanimada ")
                                .append(drone.getCodigo()).append("\n");
                        alocado = true;
                        transporteCargaInanimada.setSituacao(Estado.ALOCADO);
                        break;
                    }
                }
            }

            // se não foi alocado, adiciona à lista de não alocados
            if (!alocado) {
                naoAlocados.add(transporte);
            }
        }

        // mostra os transportes que não foram alocados
        if (!naoAlocados.isEmpty()) {
            resultado.append("\nTRANSPORTES NÃO ALOCADOS:\n");
            for (Transporte transporte : naoAlocados) {
                resultado.append("Transporte Nº").append(transporte.getNumero()+", Situação: ").append(transporte.getSituacao()+"\n");
            }
        } else {
            resultado.append("\nTodos os transportes foram alocados com sucesso!\n");
        }

        areaDeTexto.setText(resultado.toString());
    }


    private void atualizarCampos() {
        String tipoSelecionado = (String) selecaoTipoTransporte.getSelectedItem();
        if ("Pessoal".equals(tipoSelecionado)) {
            labelTempMinEPessoas.setText("Capacidade de Pessoas");
            labelTempMinEPessoas.setVisible(true);
            campoTempMinEPessoas.setVisible(true);
            labelTempMax.setVisible(false);
            campoTempMax.setVisible(false);
            labelCargaPerigosa.setVisible(false);
            checkBoxPerigosa.setVisible(false);
        } else if ("Carga Viva".equals(tipoSelecionado)) {
            labelTempMinEPessoas.setText("Temperatura Mínima");
            labelTempMinEPessoas.setVisible(true);
            campoTempMinEPessoas.setVisible(true);
            labelTempMax.setVisible(true);
            campoTempMax.setVisible(true);
            labelCargaPerigosa.setVisible(false);
            checkBoxPerigosa.setVisible(false);
        } else if ("Carga Inanimada".equals(tipoSelecionado)) {
            labelTempMinEPessoas.setVisible(false);
            campoTempMinEPessoas.setVisible(false);
            labelTempMax.setVisible(false);
            campoTempMax.setVisible(false);
            labelCargaPerigosa.setVisible(true);
            checkBoxPerigosa.setVisible(true);
        }
    }

    private void limparCampos() {
        campoCliente.setText("");
        campoLatOrigem.setText("");
        campoLonOrigem.setText("");
        campoLatDestino.setText("");
        campoLonDestino.setText("");
        campoDescricao.setText("");
        campoPeso.setText("");
        campoNumero.setText("");
        areaDeTexto.setText("");
        campoTempMinEPessoas.setText("");
    }


    public void alterarSituacaoTransporte() {
        String input = JOptionPane.showInputDialog(painelTransporte, "Insira o número do transporte que deseja alterar:");

        if (input == null || input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(painelTransporte, "ERRO: Nenhum número de transporte foi inserido.");
            return;
        }

        int numeroTransporte;
        try {
            numeroTransporte = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(painelTransporte, "ERRO: Número do transporte inválido.");
            return;
        }

        Transporte transporteEncontrado = null;
        for (Transporte transporte : transportes) {
            if (transporte.getNumero() == numeroTransporte) {
                transporteEncontrado = transporte;
                break;
            }
        }

        if (transporteEncontrado == null) {
            JOptionPane.showMessageDialog(painelTransporte, "ERRO: Transporte com o número indicado não encontrado.");
            return;
        }

        // mostra dados do transporte encontrado
        String dadosTransporte = String.format("Transporte "+transporteEncontrado.getTipoTransporte()+" Nº: %d" +
                        "\nCliente: "+transporteEncontrado.getNomeCliente() +
                        "\nDescrição: "+transporteEncontrado.getDescricao() +
                        "\nPeso: "+transporteEncontrado.getPeso()+"kg" +
                        "\nDrone atribuído: "+transporteEncontrado.getDrone() +
                        "\nSituação Atual: %s",

                transporteEncontrado.getNumero(),
                transporteEncontrado.getSituacao());
        JOptionPane.showMessageDialog(painelTransporte, dadosTransporte);

        // vê se a situação pode ser alterada
        if (transporteEncontrado.getSituacao() == Estado.TERMINADO ||
                transporteEncontrado.getSituacao() == Estado.CANCELADO) {
            JOptionPane.showMessageDialog(painelTransporte, "ERRO: A situação do transporte não pode ser alterada (TERMINADO ou CANCELADO).");
            return;
        }

        // solicitar uma situação nova
        String[] opcoes = {"PENDENTE", "ALOCADO", "CANCELADO","TERMINADO"};
        String novaSituacaoString = (String) JOptionPane.showInputDialog(painelTransporte,
                "Escolha a nova situação do transporte:",
                "Alterar Situação",
                JOptionPane.PLAIN_MESSAGE,
                null, opcoes, transporteEncontrado.getSituacao().toString());

        if (novaSituacaoString != null && !novaSituacaoString.trim().isEmpty()) {
            Estado novaSituacao = Estado.valueOf(novaSituacaoString);
            transporteEncontrado.setSituacao(novaSituacao);
            JOptionPane.showMessageDialog(painelTransporte, "Situação do transporte alterada com sucesso!");
        } else {
            JOptionPane.showMessageDialog(painelTransporte, "ERRO: Nenhuma nova situação foi selecionada.");
        }
    }

    private double calcularCustoTransporte(Transporte transporte) {
        // Implement your cost calculation logic here
        return 0.0;
    }

    public ArrayList<Transporte> getTransportes() {
        return transportes;
    }

    public void setTransportes(ArrayList<Transporte> transportes) {
        if (transportes != null) {
            this.transportes = transportes;
        } else {
            this.transportes = new ArrayList<>();
        }
    }
    private void listarCsv(){
        for(Transporte t : transportes){
            System.out.println(t.getType() + t.getNumero() + t.getNomeCliente() + t.getDescricao() + t.getPeso() + t.getLatitudeOrigem() + t.getLatitudeDestino() + t.getLongitudeDestino() + t.getSituacao());
        }
    }
}
