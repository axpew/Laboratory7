package domain.queue;

import domain.Node;

public class HeaderLinkedQueue implements Queue {

    private Node front; //apunta al anterior
    private Node rear; //apunta al posterior
    private int counter; //contador de elementos encolados

    public HeaderLinkedQueue(){
        front=rear=new Node();
        counter=0;
    }

    @Override
    public int size() {
        return counter;
    }

    @Override
    public void clear() {
        front = rear = new Node();
        counter = 0;

    }

    @Override
    public boolean isEmpty() {
        return front == rear;
    } //cuando ambos apuntan al nodo vacío

    @Override
    public int indexOf(Object element) throws QueueException {
        if(isEmpty())
            throw new QueueException("Linked Queue is Empty");

        HeaderLinkedQueue aux = new HeaderLinkedQueue(); //Cola auxiliar
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
        rear.next = newNode;
        rear = newNode; //movemos rear al nodo encolado
        counter++; //actualizar el contador
    }

    @Override
    public Object deQueue() throws QueueException {
       if(isEmpty()) throw new QueueException("Linked Queue is Empty");
       Object result = front.next.data;//Se salta el primer nodo que está vacío
       if(front.next == rear){
           clear();
       }else{
           front.next = front.next.next; //Salta desde el cabecera hasta el que sigue del que sigue
       }//end if
       counter--;
       return result;
    }

    @Override
    public boolean contains(Object element) throws QueueException {
        if(isEmpty()) throw new QueueException("Linked Queue is Empty");
        Boolean found = false;
        HeaderLinkedQueue auxQueue = new HeaderLinkedQueue();
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
        if (isEmpty()) throw new QueueException("Header Linked Queue is Empty");
        return front.next.data;
    }

    @Override
    public Object front() throws QueueException {
        if (isEmpty()) throw new QueueException("Header Linked Queue is Empty");
        return front.next.data;
    }

    @Override
    public String toString() {
        if(isEmpty()) return " Header Linked Queue is Empty";
        String result = " Header Linked Queue content\n";
       HeaderLinkedQueue aux = new HeaderLinkedQueue();

        try {
            while(!isEmpty()) {
                result += front() + "\n";
                aux.enQueue(deQueue());
            }//end while

            //dejamos la cola con los valores default
            while(!isEmpty()) enQueue(aux.deQueue());

        } catch (QueueException e) {throw new RuntimeException(e);}

        return result;
    }

}
