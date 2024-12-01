package Utils;

public class CalcularDistancia {
    private double lat1Rad;
    private double lon1Rad;
    private double lat2Rad;
    private double lon2Rad;

public double Haversine(double lat1, double lon1, double lat2, double lon2) {
   this.lat1Rad = Math.toRadians(lat1);
    this.lon1Rad = Math.toRadians(lon1);
    this.lat2Rad = Math.toRadians(lat2);
    this.lon2Rad = Math.toRadians(lon2);
    // Converter graus para radianos
    final double EARTH_RADIUS = 6371.0;


    // Diferença de latitude e longitude
    double deltaLat = lat2Rad - lat1Rad;
    double deltaLon = lon2Rad - lon1Rad;

    // Aplicar a fórmula de Haversine
    double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
            + Math.cos(lat1Rad) * Math.cos(lat2Rad)
            + Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    // Distância final
    return EARTH_RADIUS * c;
    }
}

