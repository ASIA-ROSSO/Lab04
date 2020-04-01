package it.polito.tdp.lab04.model;

import java.util.*;

import it.polito.tdp.lab04.DAO.CorsoDAO;

public class Model {

	CorsoDAO dao = new CorsoDAO();

	List<Corso> corsi = dao.getTuttiICorsi();

	public List<Corso> getElencoCorsi() {
		Corso vuoto = new Corso("nessun corso in particolare", -1, "", -1);
		corsi.add(vuoto);
		return corsi;
	}

	public Studente getStudente(String matricola) {
		return dao.getStudente(matricola);
	}
	
	public List<Studente> getStudentiIscrittiAlCorso(Corso corso){
		return dao.getStudentiIscrittiAlCorso(corso);
	}
	
	public List<Corso> getCorsiDelloStudente(String matricola) {
		return dao.getCorsiDelloStudente(matricola);
	}
	
	public boolean doIscrivi(Studente studente, Corso corso) {
		return dao.inscriviStudenteACorso(studente, corso);
	}
}
