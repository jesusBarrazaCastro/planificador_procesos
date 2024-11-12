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

    
    public static void agregarProceso(Proceso proceso){
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

    public static void ejecutarProcesos(PriorityQueue<Proceso> colaProcesos) {
        System.out.println("\nEjecutando procesos en orden de prioridad:");
        while (!colaProcesos.isEmpty()) {
            Proceso proceso = colaProcesos.poll(); // Saca el proceso con la mayor prioridad
            System.out.println("Ejecutando " + proceso);
            // Simula la ejecución
            try {
                Thread.sleep(proceso.getDuracion() * 100); // Duración en milisegundos
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("\nTodos los procesos han sido ejecutados.");
    }

    public static void EjecutarMultiplesColasdePrioridad (String[] args) {
        // Crea la cola de procesos con una prioridad de mayor a menor
        PriorityQueue<Proceso> colaProcesos = new PriorityQueue<>(Comparator.comparingInt(p -> -p.getPrioridad()));

        // Agrega procesos a la cola
        colaProcesos.add(new Proceso(1, 3, 2)); // ID=1, Duración=3, Prioridad=2
        colaProcesos.add(new Proceso(2, 1, 5)); // ID=2, Duración=1, Prioridad=5
        colaProcesos.add(new Proceso(3, 2, 1)); // ID=3, Duración=2, Prioridad=1
        colaProcesos.add(new Proceso(4, 4, 3)); // ID=4, Duración=4, Prioridad=3

        // Ejecuta los procesos en la cola
        ejecutarProcesos(colaProcesos);
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

    public static void main(String[] args) {
        // Agregar procesos de ejemplo con diferentes prioridades
        agregarProceso(new Proceso(1, 3000, 2)); // ID=1, Duración=3000, Prioridad=2
        agregarProceso(new Proceso(2, 1500, 5)); // ID=2, Duración=1500, Prioridad=5
        agregarProceso(new Proceso(3, 2000, 3)); // ID=3, Duración=2000, Prioridad=3
        agregarProceso(new Proceso(4, 4000, 2)); // ID=4, Duración=4000, Prioridad=2

        // Ejecuta los procesos en orden de prioridad con múltiples colas
        ejecutarProcesos();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}