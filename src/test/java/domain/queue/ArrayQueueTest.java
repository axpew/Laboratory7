package domain.queue;

import domain.Person;
import domain.queue.ArrayQueue;
import domain.queue.QueueException;
import org.junit.jupiter.api.Test;

class ArrayQueueTest {

    // ---- PUNTO 4 ----
    @Test
    void test() {

        ArrayQueue queue = new ArrayQueue(20);

        try {
            for (int i = 0; i < 20; i++)
                queue.enQueue(new Person(util.Utility.getPersonName(), util.Utility.getMood(), util.Utility.getAttention()));

            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("A. Encolar 20 Objetos tipo Person");
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println(queue);
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("C. Prueba de Método Size");
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("Array Queue Size: " + queue.size());
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("C. Prueba de Método Contains e IndexOf");
            System.out.println("------------------------------------------------------------------------------------");

            // Buscar 20 personas aleatorias en la cola
            for (int i = 0; i < 20; i++) {
                Person randomPerson = new Person(util.Utility.getPersonName(), util.Utility.getMood(), util.Utility.getAttention());
                boolean contains = queue.contains(randomPerson);
                int index = queue.indexOf(randomPerson);

                System.out.println("Buscando persona: " + randomPerson.getName());
                if (contains) {
                    System.out.println("La persona se encuentra en la cola en el índice: " + index);
                } else {
                    System.out.println("La persona no se encuentra en la cola");
                }
            }

            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("D. Desencolar todas las personas con mood 'Cheerful'");
            System.out.println("------------------------------------------------------------------------------------");

            // Crear una cola auxiliar para guardar temporalmente los elementos
            ArrayQueue auxQueue = new ArrayQueue(20);
            int originalSize = queue.size();

            // Desencolar y volver a encolar, saltándose las personas con mood "Cheerful"
            for (int i = 0; i < originalSize; i++) {
                Person p = (Person) queue.deQueue();
                if (!p.getMood().equals("Cheerful")) {
                    auxQueue.enQueue(p);
                } else {
                    System.out.println("Desencolando persona con mood Cheerful: " + p);
                }
            }

            // Restaurar la cola original sin las personas Cheerful
            while (!auxQueue.isEmpty()) {
                queue.enQueue(auxQueue.deQueue());
            }

            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("E. Contenido final de la cola");
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println(queue);

        } catch (QueueException e) {throw new RuntimeException(e);}

    }//end test

}//END TEST CLASS