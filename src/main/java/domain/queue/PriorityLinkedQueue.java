package domain.queue;

import domain.Node;

public class PriorityLinkedQueue implements Queue {

    private Node front; //apunta al anterior
    private Node rear; //apunta al posterior
    private int counter; //contador de elementos encolados

    public PriorityLinkedQueue(){
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
            throw new QueueException("Priority Linked Queue is Empty");

        PriorityLinkedQueue aux = new PriorityLinkedQueue(); //Cola auxiliar
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
        if(isEmpty()) front = rear = newNode;
        rear.next = newNode;
        rear = rear.next;
        counter++;
    }

    public void enQueue(Object element, int priority) throws QueueException {
        Node newNode = new Node(element, priority);
        if(isEmpty()){
            front = rear = newNode;
        }else{
            Node aux = front;
            Node prev = front;
            
            while(aux != null && aux.priority >= priority) {
                prev = aux; //dejo un apuntador al nodo anterior
                aux = aux.next; //muevo el auxiliar al siguiente nodo
            }//end while
            //Se sale cuando alcanza nulo o la prioridad del nuevo elemento es mayor
            
            //Primero pregunto si el nuevo elemento tiene una prioridad más alta al elemento del frente de la cola
            if(aux == front) {
                newNode.next = front; //Ponemos el nodo al frente 
                front = newNode;
            }else if (aux == null){ //Se encola de forma normal al no encontrar una prioridad mayor
                prev.next = newNode; //prev está en el último nodo
                rear = newNode;
            }else{ //el nuevo elemento quedará en medio de dos nodos
                prev.next = newNode;
                newNode.next = aux;
            } //end if 2
            
        }//end if
        counter++;
    }
    
    

    @Override
    public Object deQueue() throws QueueException {
       if(isEmpty()) throw new QueueException("Priority Linked Queue is Empty");
       Object result = front.data;
        if(front == rear){clear();}else{
            front = front.next;
        }
       counter--;
       return result;
    }

    @Override
    public boolean contains(Object element) throws QueueException {
        if(isEmpty()) throw new QueueException("Priority Linked Queue is Empty");
        Boolean found = false;
        PriorityLinkedQueue auxQueue = new PriorityLinkedQueue();
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
        if (isEmpty()) throw new QueueException("Priority Linked Queue is Empty");
        return front.data;
    }

    @Override
    public Object front() throws QueueException {
        if (isEmpty()) throw new QueueException("Priority Linked Queue is Empty");
        return front.data;
    }

    @Override
    public String toString() {
        if(isEmpty()) return "Priority Linked Queue is Empty";
        String result = "Priority Linked Queue content\n";
       PriorityLinkedQueue aux = new PriorityLinkedQueue();

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
