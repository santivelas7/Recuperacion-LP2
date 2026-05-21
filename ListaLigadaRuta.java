class Nodo {
    Parada parada;
    Nodo siguiente;

    public Nodo(Parada parada) {
        this.parada = parada;
        this.siguiente = null;
    }
}

public class ListaLigadaRuta {
    private Nodo cabeza;
    private int tamano;

    public ListaLigadaRuta() {
        this.cabeza = null;
        this.tamano = 0;
    }

    public int getTamano() { return tamano; }
    public Nodo getCabeza() { return cabeza; }

    public void agregar(Parada parada) {
        Nodo nuevoNodo = new Nodo(parada);
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoNodo;
        }
        tamano++;
    }

    public Parada obtener(int indice) {
        if (indice < 0 || indice >= tamano) return null;
        Nodo actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.siguiente;
        }
        return actual.parada;
    }

    public void modificar(int indice, Parada nuevaParada) {
        if (indice >= 0 && indice < tamano) {
            Nodo actual = cabeza;
            for (int i = 0; i < indice; i++) {
                actual = actual.siguiente;
            }
            actual.parada = nuevaParada;
        }
    }

    public void eliminar(int indice) {
        if (indice < 0 || indice >= tamano || cabeza == null) return;

        if (indice == 0) {
            cabeza = cabeza.siguiente;
        } else {
            Nodo actual = cabeza;
            for (int i = 0; i < indice - 1; i++) {
                actual = actual.siguiente;
            }
            if (actual.siguiente != null) {
                actual.siguiente = actual.siguiente.siguiente;
            }
        }
        tamano--;
    }

    public void vaciar() {
        cabeza = null;
        tamano = 0;
    }

    public double calcularDistanciaTotal() {
        if (tamano < 2) return 0.0;
        
        double distanciaTotal = 0.0;
        Nodo actual = cabeza;

        while (actual != null && actual.siguiente != null) {
            distanciaTotal += haversine(
                actual.parada.getLatitud(), actual.parada.getLongitud(),
                actual.siguiente.parada.getLatitud(), actual.siguiente.parada.getLongitud()
            );
            actual = actual.siguiente;
        }
        return distanciaTotal;
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; 
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}