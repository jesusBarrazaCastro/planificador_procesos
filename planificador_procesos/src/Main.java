import java.util.Scanner;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int tipoAlgoritmo = 0;
        int algoritmoInteractivo = 0;
        int numProcesos = 0;



        System.out.println("Seleccione el tipo de algoritmo de planificación que usará por defecto:");
        System.out.println("1. Apropiativo (Preemptive)");
        System.out.println("2. No-apropiativo (Non-preemptive)");
        System.out.print("Ingrese su opción (1 o 2): ");

        tipoAlgoritmo = scanner.nextInt();

        if (tipoAlgoritmo == 1) {
            System.out.println("Has seleccionado el algoritmo Apropiativo (Preemptive) por defecto.");
        } else if (tipoAlgoritmo == 2) {
            System.out.println("Has seleccionado el algoritmo No-apropiativo (Non-preemptive) por defecto.");
        } else {
            System.out.println("Opción no válida. Por favor, reinicie el programa e intente de nuevo.");
            scanner.close();
            return;
        }


        System.out.println("\nSeleccione el algoritmo de planificación en sistemas interactivos que desea simular:");
        System.out.println("1. Round-robin");
        System.out.println("2. Prioridades");
        System.out.println("3. Múltiples colas de prioridad");
        System.out.println("4. Proceso más corto primero");
        System.out.println("5. Planificación garantizada");
        System.out.println("6. Boletos de Lotería");
        System.out.println("7. Participación equitativa");
        System.out.print("Ingrese su opción (1-7): ");

        algoritmoInteractivo = scanner.nextInt();

        switch (algoritmoInteractivo) {
            case 1:
                System.out.println("Has seleccionado Round-robin.");
                break;
            case 2:
                System.out.println("Has seleccionado Prioridades.");
                break;
            case 3:
                System.out.println("Has seleccionado Múltiples colas de prioridad.");
                break;
            case 4:
                System.out.println("Has seleccionado Proceso más corto primero.");
                break;
            case 5:
                System.out.println("Has seleccionado Planificación garantizada.");
                break;
            case 6:
                System.out.println("Has seleccionado Boletos de Lotería o Participación equitativa.");
                break;
            default:
                System.out.println("Opción no válida. Por favor, reinicie el programa e intente de nuevo.");
                break;
        }

        int tiempoMonitoreo = random.nextInt(10, 26); //GENERAR ALEATORIAMENTE EL TIEMPO DE MONITOREO DE CPU
        int quantum = random.nextInt(2, 5); //GENERAR ALEATORIAMENTE EL QUANTUM
        //INICIALIZAR EL SIMULADOR DE PROCESOS
        Simulador simulador = new Simulador(tiempoMonitoreo, quantum);

        // CALCULAR DE MANERA ALEATORIA EL NUMERO DE PROCESOS
        numProcesos = random.nextInt(1, 11);
        // CREAR CADA UNO DE LOS PROCESOS
        for(int i = 0; i < numProcesos; i++){
            int tiempoEjecucion = random.nextInt(3, 11); // CALCULAR ALEATORIAMENTE EL TIEMPO DE EJECUCION DE CADA PROCESO
            int estadoActual = random.nextInt(1, 4); // CALCULAR ALEATORIAMENTE EL ESTADO ACTUAL DEL PROCESO
            simulador.agregarProceso(new Proceso(i, tiempoEjecucion, estadoActual));
        }

        System.out.println("\nNumero de procesos: " + numProcesos);
        System.out.println("Tiempo a simular: " + tiempoMonitoreo);
        System.out.println("Quantum para cada proceso: " + quantum + "\n");
        System.out.println("Tabla de procesos creada: \n");
        System.out.println("Proceso | Tiempo restante ejecucion | Estado actual");
        simulador.getProcesos();

        simulador.simular(tipoAlgoritmo, algoritmoInteractivo);
        scanner.close();
    }
}