module com.example.shopexpensetracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.ooxml;
    requires org.apache.poi.poi;
    requires org.apache.logging.log4j;
    requires org.apache.commons.lang3;
    requires de.jensd.fx.glyphs.fontawesome;


    opens com.example.shopexpensetracker to javafx.fxml, org.apache.poi.poi, org.apache.poi.ooxml, org.apache.commons.lang3;
    exports com.example.shopexpensetracker;
    exports com.example.shopexpensetracker.Models;
    opens com.example.shopexpensetracker.Models to javafx.fxml, org.apache.commons.lang3, org.apache.poi.ooxml, org.apache.poi.poi;
}