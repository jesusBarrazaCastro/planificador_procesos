import java.util.List;
import java.util.Random;

public class Proceso {
        int pId;
        int tiempoEjecucion;
        int estado; // 1: en ejecucion - 2: listo - 3: bloqueado
        int prioridad;
        int user;

        public Proceso(int pId, int tiempoEjecucion, int estado){
            this.pId = pId;
            this.tiempoEjecucion = tiempoEjecucion;
            this.estado = estado;
            prioridad = new Random().nextInt(1, 5);
            user = new Random().nextInt(1, 4);
        }

        Random random = new Random();

        public int getpId() {
            return pId;
        }

        public int getTiempoEjecucion() {
            return tiempoEjecucion;
        }

        public int getEstado() {
            return estado;
        }

        public int getPrioridad() {
            return prioridad;
        }

        public int getUser() {
            return user;
        }

        public void setEstado(int estado) {
            this.estado = estado;
        }

        public boolean ejecutar(int tiempo) {
            // Verificar si se encuentra bloqueado y generar random para determinar si pasa a listo
            if (estado == 3 && random.nextInt(2) == 0) {
                System.out.println("Entra proceso " + pId + ", No se ejecuta porque sigue bloqueado");
                return false;
            }
            estado = (estado == 3) ? 2 : estado;

            if (tiempoEjecucion > 0) {
                tiempoEjecucion -= tiempo; //restar tiempo de ejecucion
                System.out.println("Entra proceso " + pId + ", se ejecuta " + tiempo + " unidades " + ((tiempoEjecucion <= 0) ? "y termina" : "") );
            } else {
                tiempoEjecucion = 0;
            }
            return true;
        }

        public boolean estaCompleto() {
            return tiempoEjecucion <= 0;
        }

        @Override
        public String toString() {
            return "|" + pId + "\t|" + tiempoEjecucion + "\t|" + estado + "\t|";
        }

        public static String getProcesosIds(List<Proceso> procesos) {
            StringBuilder result = new StringBuilder();
            procesos.forEach(proceso -> {
                result.append(proceso.getpId()).append(", ");
            });
            return result.toString();
        }

    }
