import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.TreeMap;

public class Simulador {
    private static LinkedList<Proceso> procesos;
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
            System.out.printf("%-10s %-20d %-10s %-10d%n",
                    proceso.getpId(),
                    proceso.getTiempoEjecucion(),
                    proceso.getEstado(),
                    proceso.getPrioridad()
            );
        }
    }

    public void simular(int tipoAlgoritmo, int algoritmo){
        boolean preemprive = tipoAlgoritmo == 1;
        switch (algoritmo){
            case 1:
                ejecutarRoundRobin(preemprive);
                break;
            case 2:
                ejecutarPrioridades(preemprive);
                break;
        }
        System.out.println("\nInforme final de ejecucion: ");
        System.out.println("Procesos completados: " + Proceso.getProcesosIds(procesosCompletados));
        System.out.println("Procesos sin ejecutar: " + Proceso.getProcesosIds(procesosSinEjecutar));
        System.out.println("Procesos que siguen en ejecucion: " + Proceso.getProcesosIds(procesosEnEjecucion));
        //System.out.println();
    }

    public void ejecutarRoundRobin(boolean preemtive) {
        while (!procesos.isEmpty() && tiempoMonitoreo > 0) {
            Proceso procesoActual = procesos.poll(); // Obtiene y remueve el primer proceso en la cola
            if(!procesosEnEjecucion.contains(procesoActual)){
                procesosEnEjecucion.add(procesoActual);
            }

            // Ejecuta el proceso por el tiempo del quantum
            boolean exec = procesoActual.ejecutar(preemtive ? quantum : procesoActual.getTiempoEjecucion());

            // Verifica si el proceso ha completado su ejecución
            if (procesoActual.estaCompleto()) {
                procesosCompletados.add(procesoActual);
                procesosEnEjecucion.remove(procesoActual);
            } else {
                // Si no ha terminado, lo coloca de nuevo al final de la cola
                procesos.add(procesoActual);
            }
            // Actualiza el estado de los procesos
            if(exec) tiempoMonitoreo -= (preemtive ? quantum : procesoActual.getTiempoEjecucion());
        }
        for (Proceso proceso : procesos) {
            if (!procesosEnEjecucion.contains(proceso)) {
                procesosSinEjecutar.add(proceso);
            }
        }
    }

    public void ejecutarPrioridades(boolean preemptive) {
        procesos.sort(Comparator.comparingInt(Proceso::getPrioridad).reversed());
        while (!procesos.isEmpty() && tiempoMonitoreo > 0) {
            Proceso procesoActual = procesos.poll();

            if (!procesosEnEjecucion.contains(procesoActual)) {
                procesosEnEjecucion.add(procesoActual);
            }

            boolean exec = procesoActual.ejecutar(preemptive ? procesoActual.getTiempoEjecucion() : 1);

            if (procesoActual.estaCompleto()) {
                procesosCompletados.add(procesoActual);
                procesosEnEjecucion.remove(procesoActual);
            } else {
                procesos.add(procesoActual);  // Si no terminó, regresa a la cola
            }

            tiempoMonitoreo -= exec ? procesoActual.getTiempoEjecucion() : 0;
        }

        for (Proceso proceso : procesos) {
            if (!procesosEnEjecucion.contains(proceso)) {
                procesosEnEjecucion.add(proceso);
            }
        }
    }




}