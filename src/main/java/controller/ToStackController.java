package controller;

import domain.Climate;
import domain.Place;
import domain.Weather;
import domain.queue.LinkedQueue;
import domain.queue.QueueException;
import domain.stack.LinkedStack;
import domain.stack.StackException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import util.FXUtility;

/**
 * Controlador para la vista de conversión entre Cola y Pila
 * Maneja la funcionalidad de encolar lugares con clima y convertir entre estructuras
 */
public class ToStackController {
    @FXML
    private TableView<Climate> tvStack; // Tabla para mostrar la pila
    @FXML
    private Pane mainPain; // Panel principal
    @FXML
    private Pane buttonPane111; // Panel de botones
    @FXML
    private Pane buttonPane11; // Panel de botones
    @FXML
    private TableView<Climate> tvQueue; // Tabla para mostrar la cola
    @FXML
    private Pane buttonPane; // Panel de botones
    @FXML
    private TextField tfName; // Campo para ingresar el lugar
    @FXML
    private ComboBox<String> cbWeather; // Combo para seleccionar clima

    private LinkedQueue queue; // Cola para almacenar climas
    private LinkedStack stack; // Pila para almacenar climas
    private ObservableList<Climate> queueList; // Lista observable para la tabla de cola
    private ObservableList<Climate> stackList; // Lista observable para la tabla de pila

    /**
     * Inicializa los componentes de la vista y las estructuras de datos
     */
    @FXML
    public void initialize() {
        // Inicializar colecciones
        queue = new LinkedQueue();
        stack = new LinkedStack();

        // Inicializar listas observables
        queueList = FXCollections.observableArrayList();
        stackList = FXCollections.observableArrayList();

        // Configurar combo de clima
        cbWeather.getItems().addAll("Rainy", "Thunderstorm", "Sunny", "Cloudy", "Foggy");
        cbWeather.setValue("Sunny");

        // Configurar tablas
        setupQueueTableView();
        setupStackTableView();
    }

    /**
     * Configura la tabla de la cola con columnas de tamaño adecuado
     */
    private void setupQueueTableView() {
        // Limpiar cualquier columna existente
        tvQueue.getColumns().clear();

        TableColumn<Climate, String> placeColumn = new TableColumn<>("Place");
        placeColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPlace().getName()));
        placeColumn.setPrefWidth(tvQueue.getPrefWidth() * 0.5); // 50% del ancho

        TableColumn<Climate, String> weatherColumn = new TableColumn<>("Weather Condition");
        weatherColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getWeather().getDescription()));
        weatherColumn.setPrefWidth(tvQueue.getPrefWidth() * 0.5); // 50% del ancho

        tvQueue.getColumns().addAll(placeColumn, weatherColumn);
        tvQueue.setItems(queueList);

        // Hacer que las columnas se ajusten automáticamente
        tvQueue.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Configura la tabla de la pila con columnas de tamaño adecuado
     */
    private void setupStackTableView() {
        // Limpiar cualquier columna existente
        tvStack.getColumns().clear();

        TableColumn<Climate, String> placeColumn = new TableColumn<>("Place");
        placeColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPlace().getName()));
        placeColumn.setPrefWidth(tvStack.getPrefWidth() * 0.5); // 50% del ancho

        TableColumn<Climate, String> weatherColumn = new TableColumn<>("Weather Condition");
        weatherColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getWeather().getDescription()));
        weatherColumn.setPrefWidth(tvStack.getPrefWidth() * 0.5); // 50% del ancho

        tvStack.getColumns().addAll(placeColumn, weatherColumn);
        tvStack.setItems(stackList);

        // Hacer que las columnas se ajusten automáticamente
        tvStack.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Maneja la conversión entre cola y pila
     * @param actionEvent Evento del botón
     */
    @FXML
    public void toConvertion(ActionEvent actionEvent) {
        try {
            if (!queue.isEmpty() && stack.isEmpty()) {
                // Mover de cola a pila
                while (!queue.isEmpty()) {
                    stack.push(queue.deQueue());
                }
                FXUtility.showMessage("Éxito", "¡Elementos movidos de Cola a Pila!");
            } else if (queue.isEmpty() && !stack.isEmpty()) {
                // Mover de pila a cola
                while (!stack.isEmpty()) {
                    queue.enQueue(stack.pop());
                }
                FXUtility.showMessage("Éxito", "¡Elementos movidos de Pila a Cola!");
            } else if (queue.isEmpty() && stack.isEmpty()) {
                FXUtility.showMessage("Información", "¡Ambas estructuras están vacías!");
            } else {
                FXUtility.showMessage("Información", "¡Una de las estructuras debe estar vacía para realizar la conversión!");
            }

            updateQueueTableView();
            updateStackTableView();
        } catch (Exception e) {
            FXUtility.showErrorAlert("Error", e.getMessage());
        }
    }

    /**
     * Encola automáticamente 20 lugares con climas aleatorios
     * @param actionEvent Evento del botón
     */
    @FXML
    public void autoEnQueue(ActionEvent actionEvent) {
        try {
            int count = 0;
            for (int i = 0; i < 20 && count < 20; i++) {
                String place = util.Utility.getPlace();
                String weather = util.Utility.getWeather();

                Climate climate = new Climate(new Place(place), new Weather(weather));

                // Verifica si el lugar con el mismo clima ya existe
                if (!containsSameClimate(climate)) {
                    queue.enQueue(climate);
                    count++;
                }
            }

            updateQueueTableView();
            FXUtility.showMessage("Éxito", count + " lugares han sido encolados!");
        } catch (QueueException e) {
            FXUtility.showErrorAlert("Error", e.getMessage());
        }
    }

    /**
     * Verifica si un lugar con el mismo clima ya existe en la cola
     * @param climate Clima a verificar
     * @return true si ya existe, false en caso contrario
     */
    private boolean containsSameClimate(Climate climate) {
        try {
            if (queue.isEmpty()) return false;

            LinkedQueue tempQueue = new LinkedQueue();
            boolean found = false;

            while (!queue.isEmpty()) {
                Climate current = (Climate) queue.deQueue();
                if (current.getPlace().getName().equals(climate.getPlace().getName()) &&
                        current.getWeather().getDescription().equals(climate.getWeather().getDescription())) {
                    found = true;
                }
                tempQueue.enQueue(current);
            }

            // Restaurar la cola
            while (!tempQueue.isEmpty()) {
                queue.enQueue(tempQueue.deQueue());
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
        queue.clear();
        stack.clear();
        queueList.clear();
        stackList.clear();
        FXUtility.showMessage("Éxito", "¡Cola y Pila limpias!");
    }

    /**
     * Encola un lugar con los datos ingresados manualmente
     * @param actionEvent Evento del botón
     */
    @FXML
    public void enQueue(ActionEvent actionEvent) {
        try {
            String place = tfName.getText().trim();
            if (place.isEmpty()) {
                FXUtility.showErrorAlert("Error", "¡El lugar no puede estar vacío!");
                return;
            }

            String weather = cbWeather.getValue();

            Climate climate = new Climate(new Place(place), new Weather(weather));

            // Verifica si el clima ya existe
            if (containsSameClimate(climate)) {
                FXUtility.showErrorAlert("Error", "¡Este lugar con el mismo clima ya existe en la cola!");
                return;
            }

            queue.enQueue(climate);
            updateQueueTableView();
            tfName.clear();
            FXUtility.showMessage("Éxito", "¡Lugar añadido a la cola!");
        } catch (QueueException e) {
            FXUtility.showErrorAlert("Error", e.getMessage());
        }
    }

    /**
     * Actualiza la vista de la tabla de cola
     */
    private void updateQueueTableView() {
        try {
            queueList.clear();

            if (queue.isEmpty()) {
                return;
            }

            LinkedQueue tempQueue = new LinkedQueue();

            while (!queue.isEmpty()) {
                Climate climate = (Climate) queue.deQueue();
                queueList.add(climate);
                tempQueue.enQueue(climate);
            }

            // Restaurar la cola
            while (!tempQueue.isEmpty()) {
                queue.enQueue(tempQueue.deQueue());
            }
        } catch (QueueException e) {
            FXUtility.showErrorAlert("Error", e.getMessage());
        }
    }

    /**
     * Actualiza la vista de la tabla de pila
     */
    private void updateStackTableView() {
        try {
            stackList.clear();

            if (stack.isEmpty()) {
                return;
            }

            LinkedStack tempStack = new LinkedStack();

            while (!stack.isEmpty()) {
                Climate climate = (Climate) stack.pop();
                stackList.add(climate);
                tempStack.push(climate);
            }

            // Restaurar la pila
            while (!tempStack.isEmpty()) {
                stack.push(tempStack.pop());
            }
        } catch (StackException e) {
            FXUtility.showErrorAlert("Error", e.getMessage());
        }
    }
}