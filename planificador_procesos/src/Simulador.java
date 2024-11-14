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
        boolean preemptive = tipoAlgoritmo == 1;
        switch (algoritmo){
            case 1:
                ejecutarRoundRobin(preemptive);
                break;
            case 2:
                ejecutarPrioridades(preemptive);
                break;
            case 3:
                ejecutarMultiplesColasPrioridad(preemptive);
                break;
            case 4:
                ejecutarProcesoMasCorto(preemptive);
                break;
            case 5:
                ejecutarPlanificacionGarantizada(preemptive);
                break;
            case 6:
                ejecturarBoletosLoteria(preemptive);
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

            boolean exec = procesoActual.ejecutar(preemptive ? quantum : procesoActual.getTiempoEjecucion());

            if (procesoActual.estaCompleto()) {
                procesosCompletados.add(procesoActual);
                procesosEnEjecucion.remove(procesoActual);
            } else {
                procesos.add(procesoActual);  // Si no terminó, regresa a la cola
            }

            if(exec) tiempoMonitoreo -= (preemptive ? quantum : procesoActual.getTiempoEjecucion());
        }

        for (Proceso proceso : procesos) {
            if (!procesosEnEjecucion.contains(proceso)) {
                procesosEnEjecucion.add(proceso);
            }
        }
    }

    public void ejecutarMultiplesColasPrioridad(boolean preemptive) {
        procesos.sort(Comparator.comparingInt(Proceso::getPrioridad).reversed());
        while (!procesos.isEmpty() && tiempoMonitoreo > 0) {
            Proceso procesoActual = procesos.poll();

            if (!procesosEnEjecucion.contains(procesoActual)) {
                procesosEnEjecucion.add(procesoActual);
            }

            boolean exec = procesoActual.ejecutar(preemptive ? quantum : procesoActual.getTiempoEjecucion());

            if (procesoActual.estaCompleto()) {
                procesosCompletados.add(procesoActual);
                procesosEnEjecucion.remove(procesoActual);
            } else {
                procesos.add(procesoActual);  // Si no terminó, regresa a la cola
            }

            if(exec) tiempoMonitoreo -= (preemptive ? quantum : procesoActual.getTiempoEjecucion());
        }

        for (Proceso proceso : procesos) {
            if (!procesosEnEjecucion.contains(proceso)) {
                procesosEnEjecucion.add(proceso);
            }
        }
    }

        public void ejecutarProcesoMasCorto(boolean preemptive) {
            while (!procesos.isEmpty() && tiempoMonitoreo > 0) {
                // Ordena los procesos por el tiempo de ejecución restante en orden ascendente
                procesos.sort(Comparator.comparingInt(Proceso::getTiempoEjecucion));
        
                // Obtiene el proceso con el menor tiempo de ejecución restante
                Proceso procesoActual = procesos.poll();
        
                if (!procesosEnEjecucion.contains(procesoActual)) {
                    procesosEnEjecucion.add(procesoActual);
                }
        
                // Ejecuta el proceso por el tiempo especificado o hasta que termine
                boolean exec = procesoActual.ejecutar(preemptive ? quantum : procesoActual.getTiempoEjecucion());

                // Verifica si el proceso ha completado su ejecución
                if (procesoActual.estaCompleto()) {
                    procesosCompletados.add(procesoActual);
                    procesosEnEjecucion.remove(procesoActual);
                } else {
                    // Si el proceso no ha terminado, lo agrega de nuevo a la cola
                    procesos.add(procesoActual);
                }
        
                // Actualiza el tiempo de monitoreo
                if(exec) tiempoMonitoreo -= (preemptive ? quantum : procesoActual.getTiempoEjecucion());
            }
        
            // Agrega los procesos restantes no ejecutados a la lista de procesos sin ejecutar
            for (Proceso proceso : procesos) {
                if (!procesosEnEjecucion.contains(proceso)) {
                    procesosSinEjecutar.add(proceso);
                }
            }
        }

        public void ejecutarPlanificacionGarantizada(boolean preemptive) {
            // Mientras haya procesos por ejecutar y tiempo de monitoreo disponible
            while (!procesos.isEmpty() && tiempoMonitoreo > 0) {
                int n = procesos.size(); // Número actual de procesos en cola
                int tiempoAsignado = tiempoMonitoreo / n; // Fracción del tiempo de CPU para cada proceso
                
                // Si el tiempo asignado es menor que 1, asignamos al menos 1 unidad de tiempo por proceso
                if (tiempoAsignado < 1) tiempoAsignado = 1;
                
                Proceso procesoActual = procesos.poll(); // Obtiene y elimina el primer proceso en la cola
        
                if (!procesosEnEjecucion.contains(procesoActual)) {
                    procesosEnEjecucion.add(procesoActual);
                }
        
                // Ejecuta el proceso por el tiempo asignado o hasta que termine
                boolean exec = procesoActual.ejecutar(preemptive ? tiempoAsignado : procesoActual.getTiempoEjecucion());
        
                // Actualizar el tiempo de monitoreo
                tiempoMonitoreo -= tiempoAsignado;
        
                // Si el proceso ha completado su ejecución
                if (procesoActual.estaCompleto()) {
                    procesosCompletados.add(procesoActual);
                    procesosEnEjecucion.remove(procesoActual);
                } else {
                    // Si no ha terminado, se coloca nuevamente al final de la cola
                    procesos.add(procesoActual);
                }
                if(exec) tiempoMonitoreo -= (preemptive ? tiempoAsignado : procesoActual.getTiempoEjecucion());
            }
        
            // Agrega los procesos restantes a la lista de procesos sin ejecutar
            for (Proceso proceso : procesos) {
                if (!procesosEnEjecucion.contains(proceso)) {
                    procesosSinEjecutar.add(proceso);
                }
            }
        }
        

        public void ejecturarBoletosLoteria(boolean preemptive) {
            // Asignar boletos a cada proceso según su prioridad
            Map<Proceso, Integer> boletos = new TreeMap<>();
            int totalBoletos = 0;
        
            for (Proceso proceso : procesos) {
                int cantidadBoletos = proceso.getPrioridad() + 1; // Definir boletos según la prioridad, sumando al menos uno
                boletos.put(proceso, cantidadBoletos);
                totalBoletos += cantidadBoletos;
            }
        
            Random rand = new Random();
        
            while (!procesos.isEmpty() && tiempoMonitoreo > 0) {
                // Elegir un número aleatorio como boleto ganador
                int boletoGanador = rand.nextInt(totalBoletos);
        
                // Buscar el proceso que contiene el boleto ganador
                Proceso procesoSeleccionado = null;
                int acumulado = 0;
                for (Map.Entry<Proceso, Integer> entry : boletos.entrySet()) {
                    acumulado += entry.getValue();
                    if (acumulado > boletoGanador) {
                        procesoSeleccionado = entry.getKey();
                        break;
                    }
                }
        
                if (procesoSeleccionado != null) {
                    procesos.remove(procesoSeleccionado);
                    if (!procesosEnEjecucion.contains(procesoSeleccionado)) {
                        procesosEnEjecucion.add(procesoSeleccionado);
                    }
        
                    // Ejecutar el proceso seleccionado
                    boolean exec = procesoSeleccionado.ejecutar(preemptive ? 1 : procesoSeleccionado.getTiempoEjecucion());
        
                    // Actualizar el tiempo de monitoreo
                    tiempoMonitoreo -= (preemptive ? 1 : procesoSeleccionado.getTiempoEjecucion());
        
                    // Si el proceso termina, moverlo a completados
                    if (procesoSeleccionado.estaCompleto()) {
                        procesosCompletados.add(procesoSeleccionado);
                        procesosEnEjecucion.remove(procesoSeleccionado);
                        totalBoletos -= boletos.get(procesoSeleccionado); // Reducir los boletos totales
                        boletos.remove(procesoSeleccionado);
                    } else {
                        // Reinsertar el proceso en la lista si no ha terminado
                        procesos.add(procesoSeleccionado);
                    }
                }
            }
        
            // Agregar los procesos restantes a la lista de procesos sin ejecutar
            for (Proceso proceso : procesos) {
                if (!procesosEnEjecucion.contains(proceso)) {
                    procesosSinEjecutar.add(proceso);
                }
            }
        }
        
        

        

    }   
    
    

        

        


        
    




    