package it.polito.tdp.lab04;

import java.util.List;
import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.lab04.model.Corso;
import it.polito.tdp.lab04.model.Model;
import it.polito.tdp.lab04.model.Studente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ComboBox<Corso> CBCorsi;

	@FXML
	private Button btnIscrittiCorso;

	@FXML
	private TextField txtMatricola;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtCognome;

	@FXML
	private Button btnCercaCorsi;

	@FXML
	private Button btnIscrivi;

	@FXML
	private Button btnCompletamento;

	@FXML
	private TextArea txtRisultato;

	@FXML
	private Button btnReset;

	@FXML
	void doActivation(ActionEvent event) {
	}

	@FXML
	void stampaIscrittiCorso(ActionEvent event) {
		this.doReset(event);

		try {
			Corso c = CBCorsi.getValue();

			// NB -> controlla prima se è uguale a null!!
			if (c == null || c.getNome().equals("")) {
				txtRisultato.setText("Devi scegliere un corso!");
				return;
			}

			List<Studente> studenti = this.model.getStudentiIscrittiAlCorso(c);

			if (studenti.isEmpty()) {
				txtRisultato.setText("Nessuno studente è attualmente iscritto a questo corso");
				return;
			}
			/*
			 * String risultato = ""; for (Studente s : studenti) { if
			 * (risultato.equals("")) { risultato += String.format("%-30s",
			 * s.getMatricola()) + String.format("%-30s", s.getCognome()) +
			 * String.format("%-30s", s.getNome()) + String.format("%-30s", s.getCds()); }
			 * else { risultato += "\n" + String.format("%-30s", s.getMatricola()) +
			 * String.format("%-30s", s.getCognome()) + String.format("%-30s", s.getNome())
			 * + String.format("%-30s", s.getCds()); } }
			 */

			StringBuilder sb = new StringBuilder();

			for (Studente studente : studenti) {

				sb.append(String.format("%-10s ", studente.getMatricola()));
				sb.append(String.format("%-20s ", studente.getCognome()));
				sb.append(String.format("%-20s ", studente.getNome()));
				sb.append(String.format("%-10s ", studente.getCds()));
				sb.append("\n");
			}

			txtRisultato.setText(sb.toString());

		} catch (RuntimeException e) {
			txtRisultato.setText("ERRORE DI CONNESSIONE AL DATABASE!");
		}

	}

	@FXML
	void doCercaCorsi(ActionEvent event) {

		txtRisultato.clear();

		Integer matricola = null;
		try {
			matricola = Integer.parseInt(txtMatricola.getText());
		} catch (NumberFormatException e) {
			txtRisultato.setText("La matricola è composta solo da numeri");
			return;
		}
		if (matricola.toString().isEmpty()) {
			txtRisultato.setText("Inserisci una matricola!");
			return;
		}

		Studente s = this.model.getStudente(matricola);
		if (s == null) {
			txtRisultato.setText("Lo studente non è presente nel database");
			return;
		}

		List<Corso> corsi = this.model.getCorsiDelloStudente(matricola);

		/*
		 * String risultato = ""; for (Corso c : corsi) { if (risultato.equals("")) {
		 * risultato += c.toStringEsteso(); } else { risultato += "\n" +
		 * c.toStringEsteso(); } }
		 */

		// PER FORMATTARE:
		StringBuilder risultato = new StringBuilder();
		for (Corso corso : corsi) {
			risultato.append(String.format("%-8s ", corso.getCodins()));
			risultato.append(String.format("%-4s ", corso.getNumeroCrediti()));
			risultato.append(String.format("%-45s ", corso.getNome()));
			risultato.append(String.format("%-4s ", corso.getPeriodoDidattico()));
			risultato.append("\n");
		}
		txtRisultato.setText(risultato.toString());
	}

	@FXML
	void doCompletamentoAutomatico(ActionEvent event) {
		txtRisultato.clear();

		Integer matricola = null;
		try {
			matricola = Integer.parseInt(txtMatricola.getText());
		} catch (NumberFormatException e) {
			txtRisultato.setText("La matricola è composta solo da numeri");
			return;
		}
		try {
			if (matricola.toString().isEmpty()) {
				txtRisultato.setText("Inserisci una matricola!");
				return;
			}

			Studente s = this.model.getStudente(matricola);
			if (s == null) {
				txtRisultato.setText("Lo studente non è presente nel database");
				return;
			}

			txtNome.setText(s.getNome());
			txtCognome.setText(s.getCognome());
		} catch (RuntimeException e) {
			txtRisultato.setText("ERRORE DI CONNESSIONE AL DATABASE!");
		}
	}

	@FXML
	void doIscrivi(ActionEvent event) {
		txtRisultato.clear();

		try {
			Corso cSelezionato = CBCorsi.getValue();
			if (cSelezionato == null || cSelezionato.getNome().equals("")) {
				txtRisultato.setText("Selezionare un corso!");
				return;
			}
			/*
			 * String matricola = txtMatricola.getText();
			 * if(!this.controlloMatricola(matricola)) { return; }
			 */
			Integer matricola = null;

			matricola = Integer.parseInt(txtMatricola.getText());

			if (matricola.toString().isEmpty()) {
				txtRisultato.setText("Inserire una matricola!");
				return;
			}

			Studente s = this.model.getStudente(matricola);
			if (s == null) {
				txtRisultato.setText("Lo studente non è presente nel database");
				return;
			}

			/*
			 * DA FARE NEL MODEL!!!! List<Corso> corsi =
			 * this.model.getCorsiDelloStudente(matricola); for (Corso c : corsi) if
			 * (cSelezionato.equals(c)) {
			 * txtRisultato.setText("Studente già iscritto a questo corso"); return; }
			 */
			
			if (model.isStudenteIscrittoACorso(s, cSelezionato)) {
				txtRisultato.appendText("Studente già iscritto a questo corso");
				return;
			}
			boolean x = this.model.doIscrivi(s, cSelezionato);

			if (x == true) {
				txtRisultato.setText("Studente iscritto al corso!");
			} else {
				txtRisultato.setText("Errore nell'iscrizione!");
			}
		}catch (NumberFormatException e) {
			txtRisultato.setText("La matricola è composta solo da numeri");
			return;
		}catch (RuntimeException e) {
			txtRisultato.setText("ERRORE DI CONNESSIONE AL DATABASE!");
		}
	}

	@FXML
	void doReset(ActionEvent event) {
		txtCognome.clear();
		txtMatricola.clear();
		txtNome.clear();
		txtRisultato.clear();
		
		//COME CANCELLARE LA SELEZIONE nella choice box
		CBCorsi.getSelectionModel().clearSelection();
	}

	/*
	 * private boolean controlloMatricola(String mat) { try { Integer matricola =
	 * Integer.parseInt(mat); } catch (NumberFormatException e) {
	 * txtRisultato.setText("La matricola è composta solo da numeri"); return false;
	 * } if (mat.isEmpty()) { txtRisultato.setText("Inserisci una matricola!");
	 * return false; } return true; }
	 */

	@FXML
	void initialize() {
		assert btnCompletamento != null : "fx:id=\"btnCompletamento\" was not injected: check your FXML file 'Scene.fxml'.";
		assert CBCorsi != null : "fx:id=\"CBCorsi\" was not injected: check your FXML file 'Scene.fxml'.";
		assert btnIscrittiCorso != null : "fx:id=\"btnIscrittiCorso\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtMatricola != null : "fx:id=\"txtMatricola\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtNome != null : "fx:id=\"txtNome\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtCognome != null : "fx:id=\"txtCognome\" was not injected: check your FXML file 'Scene.fxml'.";
		assert btnCercaCorsi != null : "fx:id=\"btnCercaCorsi\" was not injected: check your FXML file 'Scene.fxml'.";
		assert btnIscrivi != null : "fx:id=\"btnIscrivi\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtRisultato != null : "fx:id=\"txtRisultato\" was not injected: check your FXML file 'Scene.fxml'.";
		assert btnReset != null : "fx:id=\"btnReset\" was not injected: check your FXML file 'Scene.fxml'.";

	}

	public void setModel(Model model) {
		this.model = model;
		CBCorsi.getItems().addAll(this.model.getElencoCorsi());
	}

}
