package AutoNoleggioDb;

import java.util.Scanner;
import java.sql.*;

public class AutoNoleggioDb {
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		System.out.println("Benvenuti nell'Autonoleggio Fantasylandia");
		boolean menu = true;
		while (menu) {
			System.out.println("Accedi al Menù?");
			System.out.println("Esci dal Menù");
			System.out.println("Scegli un opzione");
			int choice = sc.nextInt();
			sc.nextLine();

			switch (choice) {
			case 1:
				System.out.println("Username: ");
				String usernameOEmail = sc.nextLine();
				System.out.println("Password: ");
				String password = sc.nextLine();
				boolean accessoSuccesso = AutonoleggioDAO.accessoUtente(usernameOEmail, password);
				if (accessoSuccesso) {
					System.out.println("Accesso effettuato correttamente!");
					boolean haRuoloAdmin = AutonoleggioDAO.haRuoloAdmin(usernameOEmail);
					if (haRuoloAdmin) {
						System.out.println("Hai il ruolo Admin, puoi gestire tutte le auto da mostrare agli utenti");
						gestisciAutoAdmin(sc);
					} else {
						gestisciAutoUtente(sc, usernameOEmail);
					}
				} else {
					System.out.println("Credenziali di accesso non valide!");

				}
				break;

			case 2:
				menu = false;
				break;

			default:
				System.out.println("Scelta non valida!");
				break;

			}

		}
		System.out.println("Grazie per averci scelto, arrivederci!");
		sc.close();

	}

	public static void gestisciAutoAdmin(Scanner sc) {
		try {
			Connection connection = DriverManager.getConnection(AutonoleggioDAO.getUrl(), AutonoleggioDAO.getUsername(),
					AutonoleggioDAO.getPassword());
			while (true) {
				System.out.println("1. Visualizza elenco Auto");
				System.out.println("2 Inserisci nuova Auto");
				System.out.println("3. Modifica Auto");
				System.out.println("4. Cancella Auto");
				System.out.println("5. Esci");
				System.out.println("6. Seleziona");
				int choice = sc.nextInt();
				sc.nextLine();

				switch (choice) {
				case 1:
					System.out.println("Elenco delle auto esistenti");
					AutonoleggioDAO.visualizzaAuto();
					break;
				case 2:
					System.out.println("Inserisci una nuova Auto");
					System.out.println("Modello: ");
					String modello = sc.nextLine();
					System.out.println("Prezzo Giornaliero");
					double prezzoGiornaliero = sc.nextDouble();
					AutonoleggioDAO.inserisciAuto(modello, prezzoGiornaliero, "disponibile");
					break;
				case 3:
					System.out.println("Modifica Auto");
					System.out.println("Id dell'auto da modificare");
					int idAutoModifica = sc.nextInt();
					sc.nextLine();
					System.out.println("Nuovo modello");
					String nuovoModello = sc.nextLine();
					System.out.println("Nuovo prezzo giornaliero");
					double nuovoPrezzoGiornaliero = sc.nextDouble();
					AutonoleggioDAO.modificaAuto(idAutoModifica, nuovoModello, nuovoPrezzoGiornaliero, "disponibile");
					break;
				case 4:
					System.out.println("Cancella auto");
					System.out.println("Id dell'auto da cancellare");
					int idAutoCancella = sc.nextInt();
					AutonoleggioDAO.cancellaAuto(idAutoCancella);
					break;
				case 5:
					connection.close();
					return;
				default:
					System.out.println("Scelta non valida, riprova!");
					break;

				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void gestisciAutoUtente(Scanner sc, String username) {
		try {
			Connection connection = DriverManager.getConnection(AutonoleggioDAO.getUrl(), AutonoleggioDAO.getUsername(),
					AutonoleggioDAO.getPassword());
			while (true) {
				System.out.println("1. Visualizza auto disponibili");
				System.out.println("2. Prendi un auto");
				System.out.println("3. Restituisci un auto");
				System.out.println("4. Esci");
				System.out.println("Seleziona");
				int choice = sc.nextInt();
				switch (choice) {
				case 1:
					System.out.println(" Elenco auto disponibili");
					AutonoleggioDAO.visualizzaAuto();
					break;
				case 2:
					System.out.println(" Inserisci l'id dell'auto che vuoi prendere");
					int idAutoPrendi = sc.nextInt();
					AutonoleggioDAO.prendiAuto(idAutoPrendi, username);
					break;
				case 3:
					System.out.println("Inserisci l'id dell'auto da restituire");
					int idAutoRestituisci = sc.nextInt();
					AutonoleggioDAO.restituisciAuto(idAutoRestituisci, username);
					break;
				case 4:
					connection.close();
					return;
				default:
					System.out.println("Scelta non valida, riprova!");
					break;

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
