package it.polito.tdp.lab04.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.lab04.model.Corso;
import it.polito.tdp.lab04.model.Studente;

public class CorsoDAO {

	/*
	 * Ottengo tutti i corsi salvati nel Db
	 */
	public List<Corso> getTuttiICorsi() {

		final String sql = "SELECT * FROM corso";

		List<Corso> corsi = new LinkedList<Corso>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				/*
				 * String codins = rs.getString("codins"); int numeroCrediti =
				 * rs.getInt("crediti"); String nome = rs.getString("nome"); int
				 * periodoDidattico = rs.getInt("pd");
				 * 
				 * System.out.println(codins + " " + numeroCrediti + " " + nome + " " +
				 * periodoDidattico);
				 */
				Corso c = new Corso(rs.getString("codins"), rs.getInt("crediti"), rs.getString("nome"),
						rs.getInt("pd"));
				corsi.add(c);
			}
			st.close();
			conn.close();

			return corsi;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db", e);
		}
	}

	/*
	 * Data la matricola ottengo lo studente corrispondente
	 */
	public Studente getStudente(Integer matricola) {

		final String sql = "SELECT * FROM studente WHERE matricola=?";

		Studente s = null;

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, matricola);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				String nome = rs.getString("nome");
				String cognome = rs.getString("cognome");
				String cds = rs.getString("cds");
				s = new Studente(matricola, nome, cognome, cds);

			}
			st.close();
			conn.close();

			return s;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db", e);
		}
	}

	/*
	 * Ottengo tutti gli studenti iscritti al Corso
	 */
	public List<Studente> getStudentiIscrittiAlCorso(Corso corso) {

		final String sql = "SELECT i.matricola, cognome, nome, cds FROM iscrizione AS i,studente AS s WHERE i.matricola=s.matricola and codins=?";
		// String codins = corso.getCodins();
		List<Studente> studenti = new LinkedList<Studente>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setString(1, corso.getCodins());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				/*
				 * Integer matricola = rs.getInt("matricola"); String nome =
				 * rs.getString("nome"); String cognome = rs.getString("cognome"); String cds =
				 * rs.getString("cds"); Studente s = new Studente(matricola, nome, cognome,
				 * cds);
				 */
				// System.out.println(matricola + " " + cognome + " " + nome + " " + cds);
				studenti.add(new Studente(rs.getInt("matricola"), rs.getString("nome"), rs.getString("cognome"),
						rs.getString("cds")));

			}
			st.close();
			conn.close();

			return studenti;

		} catch (SQLException e) { // e.printStackTrace(); throw new
			throw new RuntimeException("Errore Db", e);
		}

	}

	/*
	 * Ottengo tutti i corsi a cui è iscritto uno studente
	 */
	public List<Corso> getCorsiDelloStudente(Integer matricola) {

		final String sql = "SELECT c.codins, crediti, nome, pd FROM iscrizione AS i,corso AS c WHERE i.codins=c.codins AND matricola=?";

		List<Corso> corsi = new LinkedList<Corso>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, matricola);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				String codins = rs.getString("codins");
				int numeroCrediti = rs.getInt("crediti");
				String nome = rs.getString("nome");
				int periodoDidattico = rs.getInt("pd");

				System.out.println(codins + " " + numeroCrediti + " " + nome + " " + periodoDidattico);
				Corso c = new Corso(codins, numeroCrediti, nome, periodoDidattico);
				corsi.add(c);

			}
			st.close();
			conn.close();

			return corsi;

		} catch (SQLException e) { // e.printStackTrace();
			throw new RuntimeException("Errore Db", e);
		}

	}

	/*
	 * Data una matricola ed il codice insegnamento, iscrivi lo studente al corso.
	 */
	public boolean iscriviStudenteACorso(Studente studente, Corso corso) {

		final String sql = "INSERT INTO iscrizione values(?, ?)";

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, studente.getMatricola());
			st.setString(2, corso.getCodins());
			int rs = st.executeUpdate();

			st.close();
			conn.close();

			if (rs == 1) {
				return true;
			}
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db", e);
		}
		// ritorna true se l'iscrizione e' avvenuta con successo
		return false;
	}

	public boolean isStudenteIscrittoACorso(Studente s, Corso cSelezionato) {

			final String sql = "SELECT * FROM iscrizione where codins=? and matricola=?";
			boolean returnValue = false;

			try {
				Connection conn = ConnectDB.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				st.setString(1, cSelezionato.getCodins());
				st.setInt(2, s.getMatricola());

				ResultSet rs = st.executeQuery();

				if (rs.next())
					returnValue = true;

				conn.close();

			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("Errore Db");
			}

			return returnValue;
		}
	

}
