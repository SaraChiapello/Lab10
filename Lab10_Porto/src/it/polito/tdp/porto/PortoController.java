package it.polito.tdp.porto;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import it.polito.tdp.porto.model.Paper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {
	Model model;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCoautori(ActionEvent event) {
    	txtResult.clear();
    	Author autore=boxPrimo.getValue();
    	if(autore==null) {
    		txtResult.setText("seleziona un autore");
    		return;
    	}
    	List<Author> s;
    	s=model.displayNeighbours(autore);
    	txtResult.appendText(s.toString());
    	
        boxSecondo.getItems().addAll(model.getIdMapAutore2(autore).values());


    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	txtResult.clear();
    	Author autore1=boxPrimo.getValue();
    	Author autore2=boxSecondo.getValue();

    	if(autore1==null||autore1==null) {
    		txtResult.setText("seleziona un autore");
    		return;
    	}
    	List<Paper> s;
    	s=model.getShortestPath(autore1, autore2);
    	for(Paper p:s)
    		txtResult.appendText(p.toString());
    	

    	

    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";
    }
    void setModel(Model model) {
    	this.model=model;
    	model.createGraph();
    	List<Author> lista=new ArrayList<Author>(model.getIdMapAutore().values());
    	Collections.sort(lista);
        boxPrimo.getItems().addAll(lista);

    }
}
