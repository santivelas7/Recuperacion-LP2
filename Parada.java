public class Parada {
    private String nombre;
    private double latitud;
    private double longitud;
    private String tiempoEstimado;
    private String descripcion;

    public Parada(String nombre, double latitud, double longitud, String tiempoEstimado, String descripcion) {
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.tiempoEstimado = tiempoEstimado;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }
    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }
    public String getTiempoEstimado() { return tiempoEstimado; }
    public void setTiempoEstimado(String tiempoEstimado) { this.tiempoEstimado = tiempoEstimado; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return nombre + " (" + latitud + ", " + longitud + ")";
    }

    public String toJson() {
        return "{\n" +
                "  \"nombre\": \"" + nombre + "\",\n" +
                "  \"latitud\": " + latitud + ",\n" +
                "  \"longitud\": " + longitud + ",\n" +
                "  \"tiempoEstimado\": \"" + tiempoEstimado + "\",\n" +
                "  \"descripcion\": \"" + descripcion + "\"\n" +
                "}";
    }
}