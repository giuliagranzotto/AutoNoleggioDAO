package AutoNoleggioDb;

import java.sql.*;

public class AutonoleggioDAO {
	private static final String URL = "jdbc:mysql://localhost:3306/autonoleggio_db";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "";

	public static boolean accessoUtente(String usernameOEmail, String password) {
		try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
			String query = "SELECT * FROM utenti WHERE (username = ? OR email = ?) AND password = ?";
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setString(1, usernameOEmail);
				statement.setString(2, usernameOEmail);
				statement.setString(3, password);
				try (ResultSet resultSet = statement.executeQuery()) {
					return resultSet.next();

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	public static boolean haRuoloAdmin(String usernameOEmail) {
		try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
			String query = "SELECT utenti.*, ruoli.nome_ruolo FROM utenti INNER JOIN ruoli ON utenti.id_ruolo = ruoli.id_ruolo WHERE (username = ? OR email = ?) AND ruoli.nome_ruolo = 'admin'";
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setString(1, usernameOEmail);
				statement.setString(2, usernameOEmail);
				try (ResultSet resultSet = statement.executeQuery()) {
					return resultSet.next();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void visualizzaAuto() {
		try {
			Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			String query = "SELECT id, modello, prezzo_giornaliero, stato FROM auto";
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				int idAuto = resultSet.getInt("id");
				String modello = resultSet.getString("modello");
				double prezzo_giornaliero = resultSet.getDouble("prezzo_giornaliero");
				String stato = resultSet.getString("Stato");
				System.out.println("Id: " + idAuto + " modello: " + modello + " Prezzo Giornaliero "
						+ prezzo_giornaliero + " Stato " + stato);
			}
			resultSet.close();
			statement.close();
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void prendiAuto(int idAuto, String username) {
		try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
			String query = "UPDATE auto SET stato = 'occupata', id_utente_preso = (SELECT id FROM utenti WHERE username = ?) WHERE id = ? AND stato = 'disponibile'";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, username);
			statement.setInt(2, idAuto);

			int rowsUpdated = statement.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Auto presa con successo!");

			} else {
				System.out.println("L'auto non è disponibile, oppure non esiste l'id dell'auto");

			}

			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void restituisciAuto(int idAuto, String username) {
		try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

			String query = "UPDATE auto SET stato = 'disponibile', id_utente_preso = NULL WHERE id = ? AND stato = 'occupata' AND id_utente_preso = (SELECT id FROM utenti WHERE username = ?)";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, idAuto);
			statement.setString(2, username);

			int rowsUpdated = statement.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Auto restituita con successo!");

			} else {
				System.out.println("L'auto non è stata presa da questo utente, oppure id l'auto non esiste");
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void inserisciAuto(String modello, double prezzoGiornaliero, String stato) {
		try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
			String query = "INSERT INTO auto (modello, prezzo_giornaliero, stato) VALUES(?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, modello);
			statement.setDouble(2, prezzoGiornaliero);
			statement.setString(3, stato);

			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("Auto inserita correttamente");
			} else {
				System.out.println("errore durante l'inserimento");
			}

			statement.close();
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void modificaAuto(int id, String nuovoModello, double nuovoPrezzoGiornaliero, String stato) {
		try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
			String query = "UPDATE auto SET modello = ?, prezzo_giornaliero = ?, stato = ? WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, nuovoModello);
			statement.setDouble(2, nuovoPrezzoGiornaliero);
			statement.setString(3, stato);
			statement.setInt(4, id);

			/*
			 * Nella query SQL specificata abbiamo 4 segnaposto (?) per i parametri
			 * richiesti, dobbiamo passare il numero dei parametri sullo statement in base
			 * al valore richiesto Es: id occuperà iil 4 posto (?) Per tanto il metodo
			 * setInt(4, id) imiposterà il valore dell'id dell'auto nel quarto segnaposto
			 * nella query, consentendo di eseguire l'aggiornamento dei dati per l'auto
			 * specificata tramite l'id.
			 */
			int rowsUpdated = statement.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Dati dell'auto aggironati con successo");
			} else {
				System.out.println("Nessun auto trovata con questo id!");
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	public static void cancellaAuto(int idAuto) {
		try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
			String query = "DELETE FROM auto WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, idAuto);

			int rowsDeleted = statement.executeUpdate();
			if (rowsDeleted > 0) {
				System.out.println("Auto cancellata con successo!");

			} else {
				System.out.println("Nessuna auto cancellata con id specificato!");
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static String getUrl() {
		return URL;

	}

	public static String getUsername() {
		return USERNAME;

	}

	public static String getPassword() {
		return PASSWORD;

	}
}
