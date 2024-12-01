package Utils;

import dados.*;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;

public class ScannerCSV {
    private Path input;
    private Path output;

    public void writeTransportes(String i, ArrayList<Transporte> transportes) {
        System.out.println(i);
        this.input = Paths.get(i);
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(input, StandardCharsets.UTF_8))) {
            int count = 0;
            while (count < transportes.size()) {
                writer.write(transportes.get(count).toCsv());
                count++;
            }
        } catch (Exception e) {
            System.err.format("%s %n", e);
            System.out.println("AAAAAAAAAAAAAAAAAAAA");
        }
    }

    public Transporte readTransportes(String nomeArquivo, ArrayList<Transporte> transportes) {
        output = Paths.get(nomeArquivo);
        try (BufferedReader reader = Files.newBufferedReader(output)) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                Scanner sc = new Scanner(line).useDelimiter(";");
                try {
                    int numero = Integer.parseInt(sc.next());
                    System.out.println("NUMERO:" + numero);
                    int type = Integer.parseInt(sc.next());
                    String nomeCliente = sc.next();
                    String descricao = sc.next();
                    double peso = Double.parseDouble(sc.next());
                    double latitudeOrigem = Double.parseDouble(sc.next());
                    double latitudeDestino = Double.parseDouble(sc.next());
                    double longitudeOrigem = Double.parseDouble(sc.next());
                    double longitudeDestino = Double.parseDouble(sc.next());
                    if (type == 1) {
                        int qtdPessoas = Integer.parseInt(sc.next());
//            selecaoTipoTransporte.addItem("Pessoal");
//            selecaoTipoTransporte.addItem("Carga Viva");
//            selecaoTipoTransporte.addItem("Carga Inanimada");

                        TransportePessoal tp = new TransportePessoal("Pessoal", numero,
                                nomeCliente, descricao, peso,
                                latitudeOrigem, longitudeDestino, latitudeDestino,
                                longitudeDestino, qtdPessoas);
                        return tp;
                    }
                    if (type == 2) {
                        Boolean cargPerigosa = Boolean.parseBoolean(sc.next());
                        TransporteCargaInanimada tci = new TransporteCargaInanimada("Carga Viva", numero,
                                nomeCliente, descricao, peso,
                                latitudeOrigem, longitudeOrigem, latitudeDestino,
                                longitudeDestino, cargPerigosa);
                        return tci;
                    }
                    if (type == 3) {
                        int tempMin = Integer.parseInt(sc.next());
                        int tempMax = Integer.parseInt(sc.next());
                        TransporteCargaViva tcv = new TransporteCargaViva("Carga Inanimada", numero, nomeCliente,
                                descricao, peso, latitudeOrigem,
                                longitudeOrigem, latitudeDestino, longitudeDestino, tempMin, tempMax);
                        return tcv;
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.format("%s %n", e);
        }
        return null;
    }
}


