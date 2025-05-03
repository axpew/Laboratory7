package domain.queue;

public class ArrayQueue implements Queue {

    private int front; //anterior
    private int rear; //posterior
    private Object queue[]; //arreglo para guardar los elementos de la cola
    private int n; //cantidad max de elementos a encolar

    public ArrayQueue(int n) {
        if(n<=0) System.exit(1);
        this.n = n;
        this.queue = new Object[n];
        this.rear = n-1; //último elemento
        this.front = rear;
    }

    @Override
    public int size() {
        return rear - front;
    }

    @Override
    public void clear() {
        this.queue = new Object[n];
        front = rear = n - 1;
    }

    @Override
    public boolean isEmpty() {
        return rear == front;
    }

    @Override
    public int indexOf(Object element) throws QueueException {
        if(isEmpty())throw new QueueException("Array Queue is Empty");
        ArrayQueue aux = new ArrayQueue(size());
        int index1=0;
        int index2=-1; //si es -1 no existe
        while(!isEmpty()){
            if(util.Utility.compare(front(), element)==0) index2 = index1; //guarda la posición si lo encuentra
            aux.enQueue(deQueue());
            index1++;
        }//end while
        //Restaura la original
        while(!aux.isEmpty()){
            enQueue(aux.deQueue());
        }
        return index2;
    }

    @Override
    public void enQueue(Object element) throws QueueException {
        if (size() == queue.length) throw new QueueException("Array Queue is full");
        for (int i = front; i < rear; i++)
            queue[i] = queue[i + 1];
        queue[rear] = element;
        front--;
    }

    @Override
    public Object deQueue() throws QueueException {
        if(isEmpty()) throw new QueueException("Array Queue is Empty");
        return queue[++front];
    }

    @Override
    public boolean contains(Object element) throws QueueException {
        if(isEmpty())throw new QueueException("Array Queue is Empty");
        ArrayQueue aux = new ArrayQueue(size());
        boolean found = false;
        while(!isEmpty()){
            if(util.Utility.compare(front(), element)==0) found = true;

            aux.enQueue(deQueue()); //encola la auxiliar con lo sacado de la original
        }

        while(!aux.isEmpty()) enQueue(aux.deQueue()); //restaurar original

        return found;
    }

    @Override
    public Object peek() throws QueueException {
        if(isEmpty())  throw new QueueException("Array Queue is Empty");

        return queue[front+1];
    }

    @Override
    public Object front() throws QueueException {
        if(isEmpty())  throw new QueueException("Array Queue is Empty");

        return queue[front+1];
    }

    @Override
    public String toString() {
        if(isEmpty()) return "Array Queue is Empty";
        String result = "Array Queue content\n";
        ArrayQueue aux = new ArrayQueue(size());

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

}//END CLASS
