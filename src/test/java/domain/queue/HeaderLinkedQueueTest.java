package domain.queue;

import domain.Climate;
import domain.Place;
import domain.Weather;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeaderLinkedQueueTest {
    //@Test
    void test() {
        HeaderLinkedQueue headerLinkedQueue = new HeaderLinkedQueue();

        try {
            for (int i = 0; i < 15; i++)
                headerLinkedQueue.enQueue(util.Utility.random(30));

            System.out.println(headerLinkedQueue);

        } catch (QueueException e) {throw new RuntimeException(e);}
    }//end test

    @Test
    void testClimateQueuePuntoC() {
        // punto 6.a
        HeaderLinkedQueue q1 = new HeaderLinkedQueue();
        HeaderLinkedQueue q2 = new HeaderLinkedQueue();
        HeaderLinkedQueue q3 = new HeaderLinkedQueue();

        try {
            // punto 6.b
            for (int i = 0; i < 20; i++) {
                q1.enQueue(new Climate(new Place(util.Utility.getPlace()),
                        new Weather(util.Utility.getWeather())));
            }

            // punto 6.c
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println("Punto 6.C: Contenido de q1 (20 objetos Climate):");
            System.out.println(q1);
            System.out.println("-----------------------------------------------------------------------------");
        } catch (QueueException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testClimateQueuePuntosDE() {
        HeaderLinkedQueue q1 = new HeaderLinkedQueue();
        HeaderLinkedQueue q2 = new HeaderLinkedQueue();
        HeaderLinkedQueue q3 = new HeaderLinkedQueue();

        try {
            for (int i = 0; i < 20; i++) {
                q1.enQueue(new Climate(new Place(util.Utility.getPlace()),
                        new Weather(util.Utility.getWeather())));
            }

            // punto 6.d
            System.out.println("Punto 6.D: Desencolar objetos con estados 'sunny' y 'foggy' de q1 y encolarlos en q2");

            HeaderLinkedQueue auxQueue = new HeaderLinkedQueue();
            int size = q1.size();

            // Filtrar los objetos
            for (int i = 0; i < size; i++) {
                Climate climate = (Climate) q1.deQueue();
                String weather = climate.getWeather().getDescription();

                if (weather.equals("sunny") || weather.equals("foggy")) {
                    System.out.println("Moviendo a q2: " + climate);
                    q2.enQueue(climate);
                } else {
                    auxQueue.enQueue(climate);
                }
            }

            // Restaurar q1 sin los elementos filtrados
            while (!auxQueue.isEmpty()) {
                q1.enQueue(auxQueue.deQueue());
            }

            // punto 6.e
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println("Punto 6.E: Contenido de q1 (sin sunny/foggy):");
            System.out.println(q1);
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println("Punto 6.E: Contenido de q2 (solo sunny/foggy):");
            System.out.println(q2);
            System.out.println("-----------------------------------------------------------------------------");
        } catch (QueueException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testClimateQueuePuntosFG() {
        HeaderLinkedQueue q1 = new HeaderLinkedQueue();
        HeaderLinkedQueue q2 = new HeaderLinkedQueue();
        HeaderLinkedQueue q3 = new HeaderLinkedQueue();

        try {
            for (int i = 0; i < 20; i++) {
                q1.enQueue(new Climate(new Place(util.Utility.getPlace()),
                        new Weather(util.Utility.getWeather())));
            }

            // punto 6.f
            System.out.println("Punto 6.F: Desencolar objetos de lugares 'Paraíso' y 'Liberia' de q1 y encolarlos en q3");

            HeaderLinkedQueue auxQueue = new HeaderLinkedQueue();
            int size = q1.size();

            for (int i = 0; i < size; i++) {
                Climate climate = (Climate) q1.deQueue();
                String place = climate.getPlace().getName();

                if (place.equals("Paraíso") || place.equals("Liberia")) {
                    System.out.println("Moviendo a q3: " + climate);
                    q3.enQueue(climate);
                } else {
                    auxQueue.enQueue(climate);
                }
            }

            while (!auxQueue.isEmpty()) {
                q1.enQueue(auxQueue.deQueue());
            }

            // punto 6.g
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println("Punto 6.G: Contenido de q1 (sin Paraíso/Liberia):");
            System.out.println(q1);
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println("Punto 6.G: Contenido de q3 (solo Paraíso/Liberia):");
            System.out.println(q3);
            System.out.println("-----------------------------------------------------------------------------");
        } catch (QueueException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testClimateQueuePuntosHI() {

        HeaderLinkedQueue q1 = new HeaderLinkedQueue();
        HeaderLinkedQueue q2 = new HeaderLinkedQueue();
        HeaderLinkedQueue q3 = new HeaderLinkedQueue();

        try {
            for (int i = 0; i < 20; i++) {
                q1.enQueue(new Climate(new Place(util.Utility.getPlace()),
                        new Weather(util.Utility.getWeather())));
            }

            // punto 6.h
            System.out.println("Punto 6.H: Desencolar objetos con estados 'thunderstorm' y 'cloudy' de q1 y encolarlos alternadamente en q2 y q3");

            HeaderLinkedQueue auxQueue = new HeaderLinkedQueue();
            int size = q1.size();
            boolean enqueueToQ2 = true; // Alternar entre q2 y q3

            for (int i = 0; i < size; i++) {
                Climate climate = (Climate) q1.deQueue();
                String weather = climate.getWeather().getDescription();

                if (weather.equals("thunderstorm") || weather.equals("cloudy")) {
                    if (enqueueToQ2) {
                        System.out.println("Moviendo a q2: " + climate);
                        q2.enQueue(climate);
                        enqueueToQ2 = false;
                    } else {
                        System.out.println("Moviendo a q3: " + climate);
                        q3.enQueue(climate);
                        enqueueToQ2 = true;
                    }
                } else {
                    auxQueue.enQueue(climate);
                }
            }

            while (!auxQueue.isEmpty()) {
                q1.enQueue(auxQueue.deQueue());
            }

            // punto 6.i
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println("Punto 6.I: Contenido final de q1:");
            System.out.println(q1);
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println("Punto 6.I: Contenido final de q2:");
            System.out.println(q2);
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println("Punto 6.I: Contenido final de q3:");
            System.out.println(q3);
            System.out.println("-----------------------------------------------------------------------------");
        } catch (QueueException e) {
            throw new RuntimeException(e);
        }
    }
}