package domain.queue;

import domain.Node;

/**
 * Implementación de una Cola con Prioridad utilizando una lista enlazada
 * Los elementos con mayor prioridad son atendidos primero
 */
public class PriorityLinkedQueue implements Queue {

    private Node front; // Apunta al frente de la cola
    private Node rear; // Apunta al final de la cola
    private int counter; // Contador de elementos encolados

    /**
     * Constructor por defecto
     */
    public PriorityLinkedQueue() {
        front = rear = null;
        counter = 0;
    }

    /**
     * Devuelve el tamaño actual de la cola
     * @return Número de elementos en la cola
     */
    @Override
    public int size() {
        return counter;
    }

    /**
     * Elimina todos los elementos de la cola
     */
    @Override
    public void clear() {
        front = rear = null;
        counter = 0;
    }

    /**
     * Verifica si la cola está vacía
     * @return true si la cola está vacía, false en caso contrario
     */
    @Override
    public boolean isEmpty() {
        return front == null;
    }

    /**
     * Busca la posición de un elemento en la cola
     * @param element Elemento a buscar
     * @return Posición del elemento (1-based) o -1 si no se encuentra
     * @throws QueueException Si la cola está vacía
     */
    @Override
    public int indexOf(Object element) throws QueueException {
        if (isEmpty())
            throw new QueueException("Priority Linked Queue is Empty");

        PriorityLinkedQueue aux = new PriorityLinkedQueue(); // Cola auxiliar
        int pos1 = 1; // Contador de posición (comienza en 1)
        int pos2 = -1; // Resultado final, -1 indica que no existe

        while (!isEmpty()) {
            if (util.Utility.compare(front(), element) == 0) {
                pos2 = pos1; // Si lo encuentra, guarda su posición
            }
            aux.enQueue(deQueue()); // Mueve el elemento a la cola auxiliar
            pos1++;
        }

        // Restaura la cola original
        while (!aux.isEmpty()) {
            Node temp = aux.front;
            enQueue(aux.deQueue(), temp.priority);
        }

        return pos2;
    }

    /**
     * Encola un elemento con prioridad por defecto (baja=1)
     * @param element Elemento a encolar
     * @throws QueueException Si hay un error al encolar
     */
    @Override
    public void enQueue(Object element) throws QueueException {
        // Para elementos sin prioridad especificada, asigna prioridad baja (1)
        enQueue(element, 1);
    }

    /**
     * Encola un elemento con una prioridad específica
     * @param element Elemento a encolar
     * @param priority Prioridad (1=baja, 2=media, 3=alta)
     * @throws QueueException Si hay un error al encolar
     */
    public void enQueue(Object element, int priority) throws QueueException {
        Node newNode = new Node(element, priority);

        // Si la cola está vacía
        if (isEmpty()) {
            front = rear = newNode;
        } else {
            // Si el nuevo nodo tiene mayor prioridad que el frente
            if (priority > front.priority) {
                newNode.next = front;
                front = newNode;
            } else {
                // Busca la posición correcta basada en la prioridad
                Node current = front;
                Node prev = null;

                // Encuentra la posición correcta basada en la prioridad (valor más alto = mayor prioridad)
                while (current != null && priority <= current.priority) {
                    prev = current;
                    current = current.next;
                }

                if (current == null) { // Insertar al final
                    prev.next = newNode;
                    rear = newNode;
                } else { // Insertar en el medio
                    prev.next = newNode;
                    newNode.next = current;
                }
            }
        }
        counter++;
    }

    /**
     * Desencola el elemento con mayor prioridad (el del frente)
     * @return El elemento desencolado
     * @throws QueueException Si la cola está vacía
     */
    @Override
    public Object deQueue() throws QueueException {
        if (isEmpty()) throw new QueueException("Priority Linked Queue is Empty");
        Object result = front.data;
        if (front == rear) {
            clear();
        } else {
            front = front.next;
        }
        counter--;
        return result;
    }

    /**
     * Verifica si un elemento existe en la cola
     * @param element Elemento a buscar
     * @return true si el elemento existe, false en caso contrario
     * @throws QueueException Si la cola está vacía
     */
    @Override
    public boolean contains(Object element) throws QueueException {
        if (isEmpty()) throw new QueueException("Priority Linked Queue is Empty");
        Boolean found = false;
        PriorityLinkedQueue auxQueue = new PriorityLinkedQueue();
        while (!isEmpty()) {
            Node temp = front;
            Object current = deQueue();
            if (util.Utility.compare(current, element) == 0) found = true;
            auxQueue.enQueue(current, temp.priority);
        }
        // Restaura la cola original
        while (!auxQueue.isEmpty()) {
            Node temp = auxQueue.front;
            enQueue(auxQueue.deQueue(), temp.priority);
        }

        return found;
    }

    /**
     * Devuelve el elemento del frente sin desencolarlo
     * @return Elemento del frente
     * @throws QueueException Si la cola está vacía
     */
    @Override
    public Object peek() throws QueueException {
        if (isEmpty()) throw new QueueException("Priority Linked Queue is Empty");
        return front.data;
    }

    /**
     * Devuelve el elemento del frente sin desencolarlo (igual que peek)
     * @return Elemento del frente
     * @throws QueueException Si la cola está vacía
     */
    @Override
    public Object front() throws QueueException {
        if (isEmpty()) throw new QueueException("Priority Linked Queue is Empty");
        return front.data;
    }

    /**
     * Devuelve una representación en cadena de la cola
     * @return Cadena con el contenido de la cola
     */
    @Override
    public String toString() {
        if (isEmpty()) return "Priority Linked Queue is Empty";
        String result = "Priority Linked Queue content\n";
        PriorityLinkedQueue aux = new PriorityLinkedQueue();

        try {
            while (!isEmpty()) {
                Node temp = front;
                result += front() + " [Priority: " + temp.priority + "]\n";
                aux.enQueue(deQueue(), temp.priority);
            }

            // Restaura la cola a su estado original
            while (!aux.isEmpty()) {
                Node temp = aux.front;
                enQueue(aux.deQueue(), temp.priority);
            }

        } catch (QueueException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}