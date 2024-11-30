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
        this.input = Paths.get(i);
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(output, StandardCharsets.UTF_8))) {
            int count = 0;
            while (count <= transportes.size()) {
                writer.write(transportes.get(count).toCsv());
            }
        } catch (Exception e) {
            System.err.format("%s %n", e);
        }
    }

    public Transporte readTransportes(Scanner sc, Queue<Transporte> transportesPendentes, ArrayList<Transporte> transportes) {
        try {
            int numero = Integer.parseInt(sc.next());
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
        return null;
    }
}

