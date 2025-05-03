package controller;

import domain.Person;
import domain.queue.PriorityLinkedQueue;
import domain.queue.QueueException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import util.FXUtility;

/**
 * Controlador para la vista de Cola con Prioridad
 * Maneja la funcionalidad de encolar personas con diferentes prioridades y atenderlas
 */
public class PriorityController {
    @FXML
    private Text txtMessage; // Texto para mostrar mensajes
    @FXML
    private TableView<Person> tvBoard; // Tabla para mostrar la cola de personas
    @FXML
    private Pane mainPain; // Panel principal
    @FXML
    private Pane buttonPane111; // Panel de botones
    @FXML
    private Pane buttonPane11; // Panel de botones
    @FXML
    private Pane buttonPane; // Panel de botones
    @FXML
    private TextField tfName; // Campo para ingresar el nombre
    @FXML
    private TextArea taAttentionProcess; // Área de texto para mostrar proceso de atención
    @FXML
    private ComboBox<String> cbMood; // Combo para seleccionar estado de ánimo
    @FXML
    private ComboBox<String> cbPriority; // Combo para seleccionar prioridad

    private PriorityLinkedQueue priorityQueue; // Cola con prioridad para almacenar personas
    private ObservableList<Person> personList; // Lista observable para la tabla de cola

    /**
     * Inicializa los componentes de la vista y las estructuras de datos
     */
    @FXML
    public void initialize() {
        // Inicializar la cola con prioridad
        priorityQueue = new PriorityLinkedQueue();

        // Configuración del combo de estados de ánimo
        cbMood.getItems().addAll("Happiness", "Sadness", "Anger", "Sickness", "Cheerful",
                "Reflective", "Gloomy", "Romantic", "Calm", "Hopeful", "Fearful", "Tense", "Lonely");
        cbMood.setValue("Happiness");

        // Configuración del combo de prioridades
        cbPriority.getItems().addAll("Low", "Medium", "High");
        cbPriority.setValue("Low");

        // Configuración de la tabla
        setupTableView();

        // Configurar el área de texto
        taAttentionProcess.setEditable(false);
        taAttentionProcess.setWrapText(true);
    }

    /**
     * Configura la tabla con columnas de tamaño adecuado
     */
    private void setupTableView() {
        // Limpiar cualquier columna existente
        tvBoard.getColumns().clear();

        // Inicializar lista observable
        personList = FXCollections.observableArrayList();

        TableColumn<Person, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(tvBoard.getPrefWidth() * 0.25);

        TableColumn<Person, String> moodCol = new TableColumn<>("Mood");
        moodCol.setCellValueFactory(new PropertyValueFactory<>("mood"));
        moodCol.setPrefWidth(tvBoard.getPrefWidth() * 0.25);

        TableColumn<Person, Integer> timeCol = new TableColumn<>("Attention Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("attentionTime"));
        timeCol.setPrefWidth(tvBoard.getPrefWidth() * 0.25);

        TableColumn<Person, Integer> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(data -> new SimpleIntegerProperty(getPriorityValue(data.getValue().getMood())).asObject());
        priorityCol.setPrefWidth(tvBoard.getPrefWidth() * 0.25);

        tvBoard.getColumns().addAll(nameCol, moodCol, timeCol, priorityCol);
        tvBoard.setItems(personList);

        // Hacer que las columnas se ajusten automáticamente
        tvBoard.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Obtiene el valor de prioridad basado en el estado de ánimo
     * @param mood Estado de ánimo de la persona
     * @return Valor de prioridad (1-3)
     */
    private int getPriorityValue(String mood) {
        switch (mood) {
            case "Happiness": case "Cheerful": case "Calm": case "Hopeful": return 3;
            case "Romantic": case "Reflective": return 2;
            default: return 1;
        }
    }

    /**
     * Encola automáticamente 20 personas con datos aleatorios
     * @param actionEvent Evento del botón
     */
    @FXML
    public void autoEnQueue(ActionEvent actionEvent) {
        try {
            for (int i = 0; i < 20; i++) {
                String name = util.Utility.getPersonName();
                String mood = util.Utility.getMood();
                int attentionTime = util.Utility.getAttention();

                Person person = new Person(name, mood, attentionTime);

                // Verifica si la persona con el mismo nombre y estado de ánimo ya existe
                if (!containsSamePerson(person)) {
                    priorityQueue.enQueue(person, getPriorityValue(mood));
                }
            }

            updateTableView();
            FXUtility.showMessage("Éxito", "¡Se han encolado personas aleatoriamente!");
        } catch (QueueException e) {
            FXUtility.showErrorAlert("Error", e.getMessage());
        }
    }

    /**
     * Verifica si una persona con el mismo nombre y estado de ánimo ya existe en la cola
     * @param person Persona a verificar
     * @return true si ya existe, false en caso contrario
     */
    private boolean containsSamePerson(Person person) {
        try {
            if (priorityQueue.isEmpty()) return false;

            PriorityLinkedQueue tempQueue = new PriorityLinkedQueue();
            boolean found = false;

            while (!priorityQueue.isEmpty()) {
                Person current = (Person) priorityQueue.deQueue();
                if (current.getName().equals(person.getName()) && current.getMood().equals(person.getMood())) {
                    found = true;
                }
                tempQueue.enQueue(current, getPriorityValue(current.getMood()));
            }

            // Restaurar la cola
            while (!tempQueue.isEmpty()) {
                Person current = (Person) tempQueue.deQueue();
                priorityQueue.enQueue(current, getPriorityValue(current.getMood()));
            }

            return found;
        } catch (QueueException e) {
            return false;
        }
    }

    /**
     * Limpia todas las estructuras de datos y componentes
     * @param actionEvent Evento del botón
     */
    @FXML
    public void clear(ActionEvent actionEvent) {
        priorityQueue.clear();
        personList.clear();
        taAttentionProcess.clear();
        tfName.clear();
    }

    /**
     * Simula la atención de una persona desencolándola
     * @param actionEvent Evento del botón
     */
    @FXML
    public void attentionProcess(ActionEvent actionEvent) {
        try {
            if (priorityQueue.isEmpty()) {
                FXUtility.showMessage("Información", "¡La cola está vacía!");
                return;
            }

            Person person = (Person) priorityQueue.deQueue();

            // Mostrar la información de la persona atendida en el área de texto
            taAttentionProcess.appendText("Atendiendo: " + person.toString() + "\n");

            updateTableView();
        } catch (QueueException e) {
            FXUtility.showErrorAlert("Error", e.getMessage());
        }
    }

    /**
     * Encola una persona con los datos ingresados manualmente
     * @param actionEvent Evento del botón
     */
    @FXML
    public void enQueue(ActionEvent actionEvent) {
        try {
            String name = tfName.getText().trim();
            if (name.isEmpty()) {
                FXUtility.showErrorAlert("Error", "¡El nombre no puede estar vacío!");
                return;
            }

            String mood = cbMood.getValue();
            int priority = getPriorityFromString(cbPriority.getValue());
            int attentionTime = util.Utility.getAttention();

            Person person = new Person(name, mood, attentionTime);

            // Verifica si la persona ya existe
            if (containsSamePerson(person)) {
                FXUtility.showErrorAlert("Error", "¡Esta persona con el mismo estado de ánimo ya existe en la cola!");
                return;
            }

            priorityQueue.enQueue(person, priority);
            updateTableView();
            tfName.clear();
        } catch (QueueException e) {
            FXUtility.showErrorAlert("Error", e.getMessage());
        }
    }

    /**
     * Convierte una prioridad en texto a su valor numérico
     * @param priority Texto de prioridad (Low, Medium, High)
     * @return Valor numérico de prioridad (1-3)
     */
    private int getPriorityFromString(String priority) {
        switch (priority) {
            case "High": return 3;
            case "Medium": return 2;
            default: return 1;
        }
    }

    /**
     * Actualiza la vista de la tabla con los datos actuales de la cola
     */
    private void updateTableView() {
        try {
            personList.clear();

            if (priorityQueue.isEmpty()) {
                return;
            }

            PriorityLinkedQueue tempQueue = new PriorityLinkedQueue();

            while (!priorityQueue.isEmpty()) {
                Person person = (Person) priorityQueue.deQueue();
                personList.add(person);
                tempQueue.enQueue(person, getPriorityValue(person.getMood()));
            }

            // Restaurar la cola
            while (!tempQueue.isEmpty()) {
                Person person = (Person) tempQueue.deQueue();
                priorityQueue.enQueue(person, getPriorityValue(person.getMood()));
            }
        } catch (QueueException e) {
            FXUtility.showErrorAlert("Error", e.getMessage());
        }
    }
}