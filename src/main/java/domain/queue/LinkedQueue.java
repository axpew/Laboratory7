package domain.queue;

import domain.Node;

public class LinkedQueue implements Queue {

    private Node front; //apunta al anterior
    private Node rear; //apunta al posterior
    private int counter; //contador de elementos encolados

    public LinkedQueue(){
        front=rear=null;
        counter=0;
    }

    @Override
    public int size() {
        return counter;
    }

    @Override
    public void clear() {
        front = rear = null;
        counter = 0;

    }

    @Override
    public boolean isEmpty() {
        return front == null;
    }

    @Override
    public int indexOf(Object element) throws QueueException {
        if(isEmpty())
            throw new QueueException("Linked Queue is Empty");

        LinkedQueue aux = new LinkedQueue(); //Cola auxiliar
        int pos1 = 1; //Contador de posición (comienza en 1)
        int pos2 = -1; //Resultado final, -1 indica que no existe

        while(!isEmpty()){
            if(util.Utility.compare(front(), element)==0){
                pos2 = pos1; //Si lo encuentra, guarda su posición
            }
            aux.enQueue(deQueue()); //Mueve el elemento a la cola auxiliar
            pos1++;
        }

        //Restaura la cola original
        while(!aux.isEmpty()) enQueue(aux.deQueue());

        return pos2;
    }

    @Override
    public void enQueue(Object element) throws QueueException {
        Node newNode = new Node(element);
        if(isEmpty()){ //la cola no existe
            rear = newNode;
            //garantizo q anterior quede apuntando al primer nodo
            front=rear; //anterior=posterior
        }else{ //significa q al menos hay un elemento en la cola
            rear.next = newNode; //posterior.sgte = nuevoNodo
            rear = newNode; //posterior = nuevoNodo
        }
        //al final actualizo el contador
        counter++;
    }

    @Override
    public Object deQueue() throws QueueException {
       if(isEmpty()) throw new QueueException("Linked Queue is Empty");
       Object result = front.data;
        if(front == rear){clear();}else{
            front = front.next;
        }
       counter--;
       return result;
    }

    @Override
    public boolean contains(Object element) throws QueueException {
        if(isEmpty()) throw new QueueException("Linked Queue is Empty");
        Boolean found = false;
        LinkedQueue auxQueue = new LinkedQueue();
        while(!isEmpty()){
            Object current = deQueue();
            if (util.Utility.compare(current, element) == 0) found = true;
            auxQueue.enQueue(current);
        }
        //Devolver la cola a su estado original
        while(!auxQueue.isEmpty()) enQueue(auxQueue.deQueue());

        return found;
    }

    @Override
    public Object peek() throws QueueException {
        if (isEmpty()) throw new QueueException("Linked Queue is Empty");
        return front.data;
    }

    @Override
    public Object front() throws QueueException {
        if (isEmpty()) throw new QueueException("Linked Queue is Empty");
        return front.data;
    }

    @Override
    public String toString() {
        if(isEmpty()) return "Linked Queue is Empty";
        String result = "Linked Queue content\n";
       LinkedQueue aux = new LinkedQueue();

        try {
            while(!isEmpty()) {
                result += front() + "\n";
                aux.enQueue(deQueue());
            }//end while

            //dejamos la cola con los valores default
            while(!aux.isEmpty()) enQueue(aux.deQueue());

        } catch (QueueException e) {throw new RuntimeException(e);}

        return result;
    }

    public String toString2() {
        if (isEmpty()) return "Linked Queue is Empty";

        StringBuilder result = new StringBuilder("Linked Queue content\n");

        try {
            //Recorrer la cola sin eliminar elementos
            LinkedQueue tempQueue = new LinkedQueue(); //Crear una cola temporal para iterar sin modificar la original
            while (!isEmpty()) {
                tempQueue.enQueue(deQueue()); //Guarda una copia de los elementos
            }

            //Agrega los elementos a la cadena de salida
            while (!tempQueue.isEmpty()) {
                result.append(tempQueue.front()).append(", ");
                enQueue(tempQueue.deQueue()); // Restauramos la cola original
            }

        } catch (QueueException e) {
            throw new RuntimeException(e);
        }

        return result.toString();
    }

    public String toString3() {
        if (isEmpty()) return "Linked Queue is Empty";

        StringBuilder result = new StringBuilder("");

        try {
            //Recorrer la cola sin eliminar elementos
            LinkedQueue tempQueue = new LinkedQueue(); //Crear una cola temporal para iterar sin modificar la original
            while (!isEmpty()) {
                tempQueue.enQueue(deQueue()); //Guarda una copia de los elementos
            }

            //Agrega los elementos a la cadena de salida
            while (!tempQueue.isEmpty()) {
                result.append(tempQueue.front()).append(", ");
                enQueue(tempQueue.deQueue()); // Restauramos la cola original
            }

        } catch (QueueException e) {
            throw new RuntimeException(e);
        }

        return result.toString();
    }

}
