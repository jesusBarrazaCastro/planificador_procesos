import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Simulador {
    private LinkedList<Proceso> procesos;
    private ArrayList<Proceso> procesosCompletados;
    private ArrayList<Proceso> procesosSinEjecutar;
    private ArrayList<Proceso> procesosEnEjecucion;
    private int tiempoMonitoreo;
    private int quantum;

    public Simulador(int tiempoMonitoreo, int quantum) {
        this.procesos = new LinkedList<>();
        this.procesosCompletados = new ArrayList<>();
        this.procesosSinEjecutar = new ArrayList<>();
        this.procesosEnEjecucion = new ArrayList<>();
        this.tiempoMonitoreo = tiempoMonitoreo;
        this.quantum = quantum;
    }

    int cantidadCambios = 0;

    public void agregarProceso(Proceso proceso){
        procesos.add(proceso);
    }

    public void getProcesos(){
        for (Proceso proceso : procesos) {
            System.out.println(proceso.toString());
        }
    }

    public void ejecutarRoundRobin() {
        while (!procesos.isEmpty()) {
            Proceso procesoActual = procesos.poll(); // Obtiene y remueve el primer proceso en la cola
            procesosEnEjecucion.add(procesoActual);

            // Ejecuta el proceso por el tiempo del quantum
            procesoActual.ejecutar(quantum);

            // Verifica si el proceso ha completado su ejecución
            if (procesoActual.estaCompleto()) {
                procesosCompletados.add(procesoActual);
            } else {
                // Si no ha terminado, lo coloca de nuevo al final de la cola
                procesos.add(procesoActual);
            }

            // Actualiza el estado de los procesos
            procesosEnEjecucion.remove(procesoActual);
            procesosSinEjecutar.remove(procesoActual);
            
        }
        System.out.println("Todos los procesos han sido completados.");
    }
}
