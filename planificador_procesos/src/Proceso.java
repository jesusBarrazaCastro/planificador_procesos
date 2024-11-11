
public class Proceso {
    int pId;
    int tiempoEjecucion;
    int estado;

    public Proceso(int pId, int tiempoEjecucion, int estado){
        this.pId = pId;
        this.tiempoEjecucion = tiempoEjecucion;
        this.estado = estado;
    }

    public int getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    public void setTiempoEjecucion(int tiempoEjecucion) {
        this.tiempoEjecucion = tiempoEjecucion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "|" + pId + "\t|" + tiempoEjecucion + "\t|" + estado + "\t|";
    }
}
