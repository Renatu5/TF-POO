package Utils;

import dados.*;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class ScannerCSV {
    private Path input;
    private Path output;

    public void writeTransportes(Path i, ListaDeTransportes transportes) {
        this.input = i;
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(output, StandardCharsets.UTF_8))) {
            int count = 0;
            while (count <= transportes.getSize()) {
                writer.write(transportes.listarTransportes());
            }
        } catch (Exception e) {
            System.err.format("%s %n", e);
        }
    }
    public void readTransportes(Path o, ListaDeTransportes transportes){
        this.output = o;
        try(BufferedReader reader = Files.newBufferedReader(input)){
            String line = "";
            while((line = reader.readLine()) != null){
                Scanner sc = new Scanner(line).useDelimiter(";");
                try {
                    int id = Integer.parseInt(sc.next());
                    int type = Integer.parseInt(sc.next());
                    String nomeCliente = sc.next();
                    String descricao = sc.next();
                    double peso = Double.parseDouble(sc.next());
                    double latitudeOrigem = Double.parseDouble(sc.next());
                    double latitudeDestino = Double.parseDouble(sc.next());
                    double longitudeOrigem = Double.parseDouble(sc.next());
                    double longitudeDestino = Double.parseDouble(sc.next());
                    Estado estado = Estado.valueOf(sc.next());
                    if(type == 1){
                        int qtdPessoas = Integer.parseInt(sc.next());
                        TransportePessoal tp = new TransportePessoal(type, id,
                                nomeCliente, descricao, peso,
                                latitudeOrigem, latitudeDestino, longitudeOrigem,
                                longitudeDestino, estado, qtdPessoas);
                        transportes.cadastrarTransporte(tp);
                    }
                    if (type == 2){
                        Boolean cargPerigosa = Boolean.parseBoolean(sc.next());
                        TransporteCargaInanimada tci = new TransporteCargaInanimada(type, id,
                                nomeCliente, descricao, peso,
                                latitudeOrigem, latitudeDestino, longitudeOrigem,
                                longitudeDestino, estado, cargPerigosa);
                        transportes.cadastrarTransporte(tci);
                    }
                    if(type == 3){
                        int tempMin = Integer.parseInt(sc.next());
                        int tempMax = Integer.parseInt(sc.next());
                        TransporteCargaViva tcv = new TransporteCargaViva(type, id, nomeCliente,
                                descricao, peso, latitudeOrigem,
                                latitudeDestino, longitudeOrigem, longitudeDestino,
                                estado, tempMin, tempMax);
                        transportes.cadastrarTransporte(tcv);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }catch(Exception e){
            System.err.format("%s %n", e);
        }
    }
}

