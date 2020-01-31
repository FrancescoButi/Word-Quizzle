import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIInterface extends Remote {

    public int registra_utente(String name, String password) throws RemoteException;
    //public int aggiungi_amico (String nickUtente, String nickAmico) throws RemoteException;
}