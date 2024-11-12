import java.util.Random;

public class Proceso {
        int pId;
        int tiempoEjecucion;
        int estado; // 1: en ejecucion - 2: listo - 3: bloqueado

        public Proceso(int pId, int tiempoEjecucion, int estado){
            this.pId = pId;
            this.tiempoEjecucion = tiempoEjecucion;
            this.estado = estado;
        }

        Random random = new Random();

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

        public void ejecutar(int tiempo) {
            // Verificar si se encuentra bloqueado y generar random para determinar si pasa a listo
            if (estado == 3 && random.nextInt(2) == 0) {
                System.out.println("Entra proceso " + pId + ", No se ejecuta porque sigue bloqueado");
                return;
            }
            estado = (estado == 3) ? 2 : estado;

            if (tiempoEjecucion > tiempo) {
                tiempoEjecucion -= tiempo; //restar tiempo de ejecucion
                System.out.println("Entra proceso " + pId + ", se ejecuta " + tiempo + " unidades " + ((tiempoEjecucion <= 0) ? "y termina" : "") );
            } else {
                tiempoEjecucion = 0;
            }
        }

        public boolean estaCompleto() {
            return tiempoEjecucion <= 0;
        }

        @Override
        public String toString() {
            return "|" + pId + "\t|" + tiempoEjecucion + "\t|" + estado + "\t|";
        }

        public int getDuracion() {
            return 0;
        }

        public int getPrioridad() {
            return 0;
        }

        public void setDuracion(int tiempoRestante) {
        }

        public void setTiempoRestante(int i) {
        }

        public int getTiempoRestante() {
            return 0;
        }
    }
