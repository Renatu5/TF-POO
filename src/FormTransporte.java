import Utils.ScannerCSV;
import dados.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

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
    private JButton exportarDadosButton;
    private JButton importarDadosButton;
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
                janela.trocarParaForm(); // Troca para a tela FormTransporte
            }
        });

        botaoAlterarSituacao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alterarSituacaoTransporte();
            }
        });

        importarDadosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // importarDados();
            }
        });
        exportarDadosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportarDados();
            }
        });
    }

    public void setDrones(ArrayList<Drone> drones) {
        this.drones = drones;
    }

    private void cadastrarTransporte() {
        try {
            // Validações básicas
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

            // Validação numérica e conversão
            int numero, pessoas;
            double peso, latOrigem, lonOrigem, latDestino, lonDestino, tempMin = 0.0, tempMax = 0.0;

            try {
                numero = Integer.parseInt(numeroStr);
                pessoas = Integer.parseInt(campoTempMinEPessoas.getText().trim());

                // Get common information regardless of transport type
                peso = Double.parseDouble(campoPeso.getText().trim());
                latOrigem = Double.parseDouble(campoLatOrigem.getText().trim());
                lonOrigem = Double.parseDouble(campoLonOrigem.getText().trim());
                latDestino = Double.parseDouble(campoLatDestino.getText().trim());
                lonDestino = Double.parseDouble(campoLonDestino.getText().trim());

                validarLatLon(latOrigem, lonOrigem, "Origem");
                validarLatLon(latDestino, lonDestino, "Destino");

                // Check the transport type and get extra data if necessary
                if ("Carga Viva".equals(tipoTransporte)) {
                    tempMin = Double.parseDouble(campoTempMinEPessoas.getText().trim());
                    tempMax = Double.parseDouble(campoTempMax.getText().trim());
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(painelTransporte, "ERRO: Valores inválidos nos campos numéricos." + e);
                return;
            }

            // Verificar duplicidade de número
            for (Transporte t : transportes) {
                if (t.getNumero() == numero) {
                    JOptionPane.showMessageDialog(painelTransporte, "ERRO: Número de transporte já cadastrado.");
                    return;
                }
            }

            for (Transporte t : transportesPendentes) {
                if (t.getNumero() == numero) {
                    JOptionPane.showMessageDialog(painelTransporte, "ERRO: Número de transporte já cadastrado.");
                    return;
                }
            }

            // Criar o transporte com base no tipo selecionado
            Transporte novoTransporte = null;
            if ("Pessoal".equals(tipoTransporte)) {
                novoTransporte = new TransportePessoal(tipoTransporte, numero, nomeCliente, descricao, peso, latOrigem,
                        lonOrigem, latDestino, lonDestino, pessoas);
            } else if ("Carga Viva".equals(tipoTransporte)) {
                novoTransporte = new TransporteCargaViva(tipoTransporte, numero, nomeCliente, descricao, peso,
                        latOrigem, lonOrigem, latDestino, lonDestino, tempMin, tempMax);
            } else if ("Carga Inanimada".equals(tipoTransporte)) {
                boolean perigosa = checkBoxPerigosa.isSelected();
                novoTransporte = new TransporteCargaInanimada(tipoTransporte, numero, nomeCliente, descricao, peso,
                        latOrigem, lonOrigem, latDestino, lonDestino, perigosa);
            }

            if (novoTransporte != null) {
                // Adiciona o transporte na fila de pendentes
                transportesPendentes.add(novoTransporte);
                transportes.add(novoTransporte);
                areaDeTexto.setText("dados.Transporte adicionado à fila de pendentes.\n" + novoTransporte);
            } else {
                JOptionPane.showMessageDialog(painelTransporte, "ERRO: Não foi possível cadastrar o transporte.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(painelTransporte, "ERRO: " + ex.getMessage());
        }
    }

    private void validarLatLon(double lat, double lon, String tipo) {
        if (lat < -90 || lat > 90)
            throw new IllegalArgumentException("Latitude de " + tipo + " inválida.");
        if (lon < -180 || lon > 180)
            throw new IllegalArgumentException("Longitude de " + tipo + " inválida.");
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
                builder.append("dados.Transporte Pessoal - Número: ").append(tp.getNumero())
                        .append(", Cliente: ").append(tp.getNomeCliente())
                        .append(", Descrição: ").append(tp.getDescricao())
                        .append(", Peso: ").append(tp.getPeso())
                        .append(", Capacidade: ").append(tp.getQtdPessoas() + " pessoas")
                        .append(", Coordenadas de origem: ").append(tp.getLatitudeOrigem() + ", ")
                        .append(tp.getLongitudeOrigem())
                        .append(", Coordenadas de destino: ").append(tp.getLatitudeDestino() + ", ")
                        .append(tp.getLongitudeDestino())
                        .append(", Situação: ").append(tp.getSituacao()).append("\n");
            } else if (transportes instanceof TransporteCargaInanimada) {
                TransporteCargaInanimada tci = (TransporteCargaInanimada) transportes;
                builder.append("dados.Transporte de Carga Inanimada - Número: ").append(tci.getNumero())
                        .append(", Cliente: ").append(tci.getNomeCliente())
                        .append(", Descrição: ").append(tci.getDescricao())
                        .append(", Peso: ").append(tci.getPeso())
                        .append(", Coordenadas de origem: ").append(tci.getLatitudeOrigem() + ", ")
                        .append(tci.getLongitudeOrigem())
                        .append(", Coordenadas de destino: ").append(tci.getLatitudeDestino() + ", ")
                        .append(tci.getLongitudeDestino())
                        .append(", Situação: ").append(tci.getSituacao()).append("\n");
            } else if (transportes instanceof TransporteCargaViva) {
                TransporteCargaViva tcv = (TransporteCargaViva) transportes;
                builder.append("dados.Transporte de Carga Viva - Número: ").append(tcv.getNumero())
                        .append(", Cliente: ").append(tcv.getNomeCliente())
                        .append(", Descrição: ").append(tcv.getDescricao())
                        .append(", Peso: ").append(tcv.getPeso())
                        .append(", Coordenadas de origem: ").append(tcv.getLatitudeOrigem() + ", ")
                        .append(tcv.getLongitudeOrigem())
                        .append(", Coordenadas de destino: ").append(tcv.getLatitudeDestino() + ", ")
                        .append(tcv.getLongitudeDestino())
                        .append(", Situação: ").append(tcv.getSituacao()).append("\n");
            }
        }
        areaDeTexto.setText(builder.toString());
    }

    private void processarTransportesPendentes() {
        StringBuilder resultado = new StringBuilder("PROCESSAMENTO DE TRANSPORTES PENDENTES:\n");
        // Lista temporária para armazenar transportes não alocados
        ArrayList<Transporte> naoAlocados = new ArrayList<>();
        // Percorre cada transporte pendente
        for (Transporte transporte : transportesPendentes) {
            boolean alocado = false;
            // Tenta alocar o transporte a um drone disponível
            for (Drone drone : drones) {
                if (drone instanceof DronePessoal && transporte instanceof TransportePessoal) {
                    DronePessoal dronePessoal = (DronePessoal) drone;
                    TransportePessoal transportePessoal = (TransportePessoal) transporte;
                    if (dronePessoal.getQtdMaxPessoas() >= transportePessoal.getQtdPessoas()) {
                        resultado.append("dados.Transporte Pessoal ")
                                .append(transporte.getNumero())
                                .append(" alocado ao dados.Drone Pessoal ")
                                .append(drone.getCodigo()).append("\n");
                        alocado = true;
                        transportePessoal.setSituacao(Estado.ALOCADO);
                        transportesPendentes.remove(transporte);
                        break; // Sai do loop de drones após alocação
                    }
                } else if (drone instanceof DroneCargaViva && transporte instanceof TransporteCargaViva) {
                    DroneCargaViva droneCargaViva = (DroneCargaViva) drone;
                    TransporteCargaViva transporteCargaViva = (TransporteCargaViva) transporte;

                    if (droneCargaViva.climatizado()) {
                        resultado.append("dados.Transporte de Carga Viva ")
                                .append(transporte.getNumero())
                                .append(" alocado ao dados.Drone de Carga Viva ")
                                .append(drone.getCodigo()).append("\n");
                        alocado = true;
                        transporteCargaViva.setSituacao(Estado.ALOCADO);
                        transportesPendentes.remove(transporte);
                        break;
                    }
                } else if (drone instanceof DroneCargaInanimada && transporte instanceof TransporteCargaInanimada) {
                    DroneCargaInanimada droneCargaInanimada = (DroneCargaInanimada) drone;
                    TransporteCargaInanimada transporteCargaInanimada = (TransporteCargaInanimada) transporte;

                    if (droneCargaInanimada.protegido() || !transporteCargaInanimada.isPerigosa()) {
                        resultado.append("dados.Transporte de Carga Inanimada ")
                                .append(transporte.getNumero())
                                .append(" alocado ao dados.Drone de Carga Inanimada ")
                                .append(drone.getCodigo()).append("\n");
                        alocado = true;
                        transporteCargaInanimada.setSituacao(Estado.ALOCADO);
                        transportesPendentes.remove(transporte);
                        break;
                    }
                }
            }

            // Caso não tenha sido alocado, adiciona à lista de não alocados
            if (!alocado) {
                naoAlocados.add(transporte);
            }
        }

        // Exibe os transportes que não foram alocados
        if (!naoAlocados.isEmpty()) {
            resultado.append("\nTRANSPORTES NÃO ALOCADOS:\n");
            for (Transporte transporte : naoAlocados) {
                resultado.append("dados.Transporte Nº").append(transporte.getNumero() + ", Situação: ")
                        .append(transporte.getSituacao() + "\n");
            }
        } else {
            resultado.append("\nTodos os transportes foram alocados com sucesso!\n");
        }

        // Exibe o resultado na área de texto
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
        String input = JOptionPane.showInputDialog(painelTransporte,
                "Insira o número do transporte que deseja alterar:");

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
        for (Transporte transporte : transportesPendentes) {
            if (transporte.getNumero() == numeroTransporte) {
                transporteEncontrado = transporte;
                break;
            }
        }

        if (transporteEncontrado == null) {
            JOptionPane.showMessageDialog(painelTransporte,
                    "ERRO: dados.Transporte com o número indicado não encontrado.");
            return;
        }

        // Exibir dados do transporte encontrado
        String dadosTransporte = String.format(
                "dados.Transporte " + transporteEncontrado.getTipoTransporte() + " Nº: %d" +
                        "\nCliente: " + transporteEncontrado.getNomeCliente() +
                        "\nDescrição: " + transporteEncontrado.getDescricao() +
                        "\nPeso: " + transporteEncontrado.getPeso() + "kg" +
                        "\ndados.Drone atribuído: " + transporteEncontrado.getDrone() +
                        "\nSituação Atual: %s",

                transporteEncontrado.getNumero(),
                transporteEncontrado.getSituacao());
        JOptionPane.showMessageDialog(painelTransporte, dadosTransporte);

        // Verificar se pode ser alterado
        if (transporteEncontrado.getSituacao() == Estado.TERMINADO ||
                transporteEncontrado.getSituacao() == Estado.CANCELADO) {
            JOptionPane.showMessageDialog(painelTransporte,
                    "ERRO: A situação do transporte não pode ser alterada (TERMINADO ou CANCELADO).");
            return;
        }

        // Solicitar nova situação
        String[] opcoes = { "PENDENTE", "ALOCADO", "CANCELADO", "TERMINADO" };
        String novaSituacaoString = (String) JOptionPane.showInputDialog(painelTransporte,
                "Escolha a nova situação do transporte:",
                "Alterar Situação",
                JOptionPane.PLAIN_MESSAGE,
                null, opcoes, transporteEncontrado.getSituacao().toString());

        if (novaSituacaoString != null && !novaSituacaoString.trim().isEmpty()) {
            Estado novaSituacao = Estado.valueOf(novaSituacaoString);
            transporteEncontrado.setSituacao(novaSituacao);
            if (novaSituacao == Estado.PENDENTE)
                transportesPendentes.add(transporteEncontrado);
            JOptionPane.showMessageDialog(painelTransporte, "Situação do transporte alterada com sucesso!");
        } else {
            JOptionPane.showMessageDialog(painelTransporte, "ERRO: Nenhuma nova situação foi selecionada.");
        }
    }

    // public void importarDados() {
    // try {
    // String nomeArquivo = JOptionPane.showInputDialog(getPainel(), "Digite o nome
    // do arquivo CSV (SEM extensão ou '_drones'/'_transportes'):") + ".CSV";
    // if (nomeArquivo == null || nomeArquivo.trim().isEmpty()) {
    // JOptionPane.showMessageDialog(getPainel(), "ERRO: Nome do arquivo
    // inválido.");
    // return;
    // }
    // ScannerCSV sc = new ScannerCSV();
    // transportesPendentes.add(sc.readTransportes(nomeArquivo, transportes));
    // transportes.add(sc.readTransportes(nomeArquivo, transportes));
    // JOptionPane.showMessageDialog(getPainel(), "Dados carregados com sucesso!");
    // } catch (Exception e) {
    // JOptionPane.showMessageDialog(getPainel(), "ERRO: Não foi possível carregar
    // os dados.\n" + e.getMessage());
    // }
    // }

    public void exportarDados() {
        try {
            String nomeArquivo = JOptionPane.showInputDialog(getPainel(),
                    "Digite o nome do arquivo CSV em que deseja salvar os dados(SEM extensão ou '_drones'/'_transportes'):")
                    + ".CSV";
            if (nomeArquivo == null || nomeArquivo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(getPainel(), "ERRO: Nome do arquivo inválido.");
                return;
            }
            ScannerCSV sc = new ScannerCSV();
            sc.writeTransportes(nomeArquivo, transportes);
            JOptionPane.showMessageDialog(getPainel(), "Dados salvos em " + nomeArquivo + " com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(getPainel(), "ERRO: Não foi possível exportar os dados.\n" + e.getMessage());
        }

    }

    // public String gerarRelatorioTransportes() {
    // if (transportes.isEmpty()) {
    // return "Nenhum transporte cadastrado.\n";
    // }
    //
    // StringBuilder builder = new StringBuilder("RELATÓRIO DE TRANSPORTES:\n");
    // for (dados.Transporte transporte : transportes) {
    // builder.append("dados.Transporte Nº: ").append(transporte.getNumero())
    // .append("\nCliente: ").append(transporte.getNomeCliente())
    // .append("\nDescrição: ").append(transporte.getDescricao())
    // .append("\nPeso: ").append(transporte.getPeso())
    // .append("\nCoordenadas de Origem: ").append(transporte.getLatitudeOrigem())
    // .append(", ").append(transporte.getLongitudeOrigem())
    // .append("\nCoordenadas de Destino: ").append(transporte.getLatitudeDestino())
    // .append(", ").append(transporte.getLongitudeDestino())
    // .append("\nSituação: ").append(transporte.getSituacao())
    // .append("\nCusto Estimado: ").append(calcularCustoTransporte(transporte)) //
    // Calculate the cost
    // .append("\n---\n");
    // }
    // return builder.toString();
    // }

    private double calcularCustoTransporte(Transporte transporte) {
        // Implement your cost calculation logic here
        return 0.0; // Placeholder value
    }

    public ArrayList<Transporte> getTransportes() {
        return transportes;
    }

    public void setTransportes(ArrayList<Transporte> transportes) {
        if (transportes != null) {
            this.transportes = transportes;
        } else {
            this.transportes = new ArrayList<>(); // Initializes if the provided list is null
        }
    }
}