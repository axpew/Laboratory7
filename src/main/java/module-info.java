module ucr.laboratory7 {
    requires javafx.controls; // Requiere los controles de JavaFX
    requires javafx.fxml; // Requiere FXML para las interfaces gráficas

    // Abre el paquete principal a JavaFX
    opens ucr.laboratory7 to javafx.fxml;
    exports ucr.laboratory7;

    // Abre el paquete de controladores a JavaFX
    exports controller;
    opens controller to javafx.fxml;

    // Abre los paquetes de dominio para enlace de propiedades
    opens domain to javafx.base;
    opens domain.queue to javafx.base;
}