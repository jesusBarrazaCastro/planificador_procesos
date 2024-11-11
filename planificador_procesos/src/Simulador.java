import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Simulador {
    private LinkedList<Proceso> procesos;
    private ArrayList<Integer> procesosCompletados;
    private ArrayList<Integer> procesosSinEjecutar;
    private ArrayList<Integer> procesosEnEjecucion;
    private int tiempoNonitoreo;
    private int quantum;

    public Simulador(int tiempoNonitoreo, int quantum) {
        this.procesos = new LinkedList<>();
        this.procesosCompletados = new ArrayList<>();
        this.procesosSinEjecutar = new ArrayList<>();
        this.procesosEnEjecucion = new ArrayList<>();
        this.tiempoNonitoreo = tiempoNonitoreo;
        this.quantum = quantum;
    }

    public void agregarProceso(Proceso proceso){
        procesos.add(proceso);
    }

    public void getProcesos(){
        for (Proceso proceso : procesos) {
            System.out.println(proceso.toString());
        }
    }
}
