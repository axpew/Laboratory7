package domain.queue;

import domain.queue.LinkedQueue;
import domain.queue.QueueException;
import domain.stack.LinkedStack;
import domain.stack.StackException;
import org.junit.jupiter.api.Test;


class LinkedQueueTest {

    // ---- PUNTO 5 ----
    @Test
    void test2() {

        LinkedQueue linkedQueue = new LinkedQueue();

        try {

        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("A. IsBalanced Test");
        System.out.println("------------------------------------------------------------------------------------");

        String[] array = {"(())()", "(()", "())(", "", "((()))", "(()(()))"};
        for (String i : array) {
            boolean result = isBalanced(linkedQueue,i);
            System.out.println("Expression: " + i + " → " + (result ? "is balanced " : "is not balanced "));
        }//end for

        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("B. Remove Duplicates Test");
        System.out.println("------------------------------------------------------------------------------------");

        for (int i = 0; i < 20; i++)
            linkedQueue.enQueue(util.Utility.random(20));

        System.out.println("--> With duplicates: "+linkedQueue.toString2());
        LinkedQueue duplicates = removeDuplicates(linkedQueue);
        System.out.println("--> Without duplicates: "+linkedQueue.toString2());
        System.out.println("--> Duplicate Elements: " +duplicates.toString3());

        System.out.println("------------------------------------------------------------------------------------");


        } catch (QueueException e) {throw new RuntimeException(e);}

    }//end test2

    private LinkedQueue removeDuplicates(LinkedQueue linkedQueue) {
        LinkedQueue aux = new LinkedQueue();
        LinkedQueue duplicates = new LinkedQueue(); //Lista para mostrar los duplicados

        try {
            while (!linkedQueue.isEmpty()) {
                Object current = linkedQueue.deQueue();
                boolean isDuplicate = false;

                //Revisa si el elemento actual ya está en la cola auxiliar
                LinkedQueue tempQueue = new LinkedQueue(); //Cola temporal para revisar los elementos en aux
                while (!aux.isEmpty()) {
                    Object temp = aux.deQueue(); //Saco el elemento para compararlo

                    if (util.Utility.compare(temp, current) == 0){
                        isDuplicate = true;
                        duplicates.enQueue(current);
                        }


                    tempQueue.enQueue(temp); // Guarda el elemento en la cola temporal
                }//end while2

                //Si no es un duplicado, lo agrega a la cola auxiliar
                if (!isDuplicate)
                    aux.enQueue(current);


                //Reestablece la cola auxiliar en su estado original
                while (!tempQueue.isEmpty())
                    aux.enQueue(tempQueue.deQueue());
            }//end while

            //Reestablece la cola original sin los duplicados
            while (!aux.isEmpty()) {
                linkedQueue.enQueue(aux.deQueue());
            }
            //Voltea la cola a su orden original
            reverseQueue(linkedQueue);

           return duplicates;

        } catch (QueueException e) {throw new RuntimeException(e);}
    }//end removeDuplicates


    private boolean isBalanced(LinkedQueue queue, String expression) {
        int counter = 0;

        if (expression == null || expression.trim().isEmpty()) {
            return true; //una cadena vacía o de solo espacios se considera balanceada
        }

        try {
            //Agregar solo paréntesis a la cola
            for (char character : expression.toCharArray()) {
                if (character == '(' || character == ')') {
                    queue.enQueue(character);
                }
            }//end for

            //Comenzamos a desencolar cada elemento
            while (!queue.isEmpty()) {
                char c = (char) queue.deQueue();

                if (util.Utility.compare(c, '(') == 0) {
                    counter++;
                } else if (util.Utility.compare(c, ')') == 0) {
                    counter--;
                    if (counter < 0) return false;
                }
            }

            return counter == 0; // true si está balanceado

        } catch (QueueException e) {
            throw new RuntimeException(e);
        }

    }//end isBalanced

    private void reverseQueue(LinkedQueue queue) {

        LinkedStack stack = new LinkedStack();

        try {
            //Transferir todos los elementos de la cola a la pila
            while (!queue.isEmpty())
                stack.push(queue.deQueue());


            //Volver a transferir los elementos de la pila a la cola (el último en entrar es el primero en salir)
            while (!stack.isEmpty())
                queue.enQueue(stack.pop());


        } catch (QueueException e) {throw new RuntimeException(e);} catch (StackException e) {throw new RuntimeException(e);}
    }

}