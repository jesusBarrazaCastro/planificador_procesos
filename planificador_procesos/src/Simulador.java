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
            System.out.println(proceso.toString());
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
        System.out.println("Procesos que siguen en ejecucion: " + Proceso.getProcesosIds(procesosSinEjecutar));
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
        Collections.sort(procesos, Comparator.comparingInt(Proceso::getPrioridad).reversed());
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



    // Usamos un Map para almacenar colas separadas por cada nivel de prioridad
    private static Map<Integer, Queue<Proceso>> colasPrioridad = new TreeMap<>(Collections.reverseOrder());
    private static final int TIEMPO_QUANTUM = 1000; // Duración de cada turno en milisegundos

    // Método para agregar un proceso a su cola correspondiente según su prioridad
    public static void EjecutarMultiplesColasdePrioridad (Proceso proceso) {
        colasPrioridad.computeIfAbsent(proceso.getPrioridad(), k -> new LinkedList<>()).add(proceso);
        System.out.println("Proceso agregado: " + proceso);
    }

    // Método para ejecutar los procesos en el sistema
    public static void ejecutarProcesos() {
        System.out.println("\nEjecutando procesos con múltiples colas de prioridad:");
        while (!colasPrioridad.isEmpty()) {
            // Selecciona la cola con la prioridad más alta disponible
            int prioridadActual = colasPrioridad.keySet().iterator().next();
            Queue<Proceso> colaActual = colasPrioridad.get(prioridadActual);

            if (colaActual != null && !colaActual.isEmpty()) {
                // Round Robin en la cola actual
                Proceso proceso = colaActual.poll();
                System.out.println("Ejecutando " + proceso + " con prioridad " + prioridadActual);

                // Simulación de ejecución con quantum
                int tiempoRestante = proceso.getDuracion();
                try {
                    if (tiempoRestante > TIEMPO_QUANTUM) {
                        Thread.sleep(TIEMPO_QUANTUM);
                        tiempoRestante -= TIEMPO_QUANTUM;
                        proceso.setDuracion(tiempoRestante);
                        colaActual.add(proceso);  // Reenviar a la cola si queda tiempo restante
                    } else {
                        Thread.sleep(tiempoRestante);  // Ejecutar el tiempo restante
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // Eliminar la cola si está vacía
                if (colaActual.isEmpty()) {
                    colasPrioridad.remove(prioridadActual);
                }
            }
        }
        System.out.println("\nTodos los procesos han sido ejecutados.");
    }



}