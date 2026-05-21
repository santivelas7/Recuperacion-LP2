import java.io.*;

public class GestionArchivos {
    private static final String NOMBRE_ARCHIVO = "ruta.json";

    public static void guardarRuta(ListaLigadaRuta ruta) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(NOMBRE_ARCHIVO))) {
            out.println("[");
            Nodo actual = ruta.getCabeza();
            while (actual != null) {
                out.print(actual.parada.toJson());
                if (actual.siguiente != null) {
                    out.println(",");
                }
                actual = actual.siguiente;
            }
            out.println("\n]");
        }
    }

    public static void cargarRuta(ListaLigadaRuta ruta) throws Exception {
        File archivo = new File(NOMBRE_ARCHIVO);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            ruta.vaciar();
            String linea;
            StringBuilder sb = new StringBuilder();
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
            }
            
            String contenido = sb.toString().replace("[", "").replace("]", "").trim();
            if (!contenido.isEmpty()) {
                String[] objetos = contenido.split("\\}\\s*,\\s*\\{");
                for (String obj : objetos) {
                    obj = obj.replace("{", "").replace("}", "");
                    String nombre = buscarAtributoJson(obj, "nombre");
                    double lat = Double.parseDouble(buscarAtributoJson(obj, "latitud"));
                    double lon = Double.parseDouble(buscarAtributoJson(obj, "longitud"));
                    String tiempo = buscarAtributoJson(obj, "tiempoEstimado");
                    String desc = buscarAtributoJson(obj, "descripcion");
                    
                    ruta.agregar(new Parada(nombre, lat, lon, tiempo, desc));
                }
            }
        }
    }

    private static String buscarAtributoJson(String bloque, String atributo) {
        String clave = "\"" + atributo + "\":";
        int inicioClave = bloque.indexOf(clave);
        if (inicioClave == -1) return "";
        int inicioValor = inicioClave + clave.length();
        
        int finValor = bloque.indexOf(",", inicioValor);
        if (finValor == -1) finValor = bloque.length();
        
        String valor = bloque.substring(inicioValor, finValor).trim();
        return valor.replace("\"", "");
    }
}