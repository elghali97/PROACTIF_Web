import fr.insalyon.dasi.proactif.metier.OM.Animal;
import fr.insalyon.dasi.proactif.metier.OM.Client;
import fr.insalyon.dasi.proactif.metier.OM.Incident;
import fr.insalyon.dasi.proactif.metier.OM.Intervention;
import fr.insalyon.dasi.proactif.metier.OM.Livraison;
import fr.insalyon.dasi.proactif.metier.Service.ServiceMetier;
import fr.insalyon.dasi.proactif.metier.Service.ServiceUtile;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ActionIntervention extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        
        HttpSession session=request.getSession(true);
        
        String type = request.getParameter("type");
        String objet = request.getParameter("objet");
        String entreprise = request.getParameter("entreprise");
        String animal = request.getParameter("animal");
        String description = request.getParameter("description");
        int id= (int) session.getAttribute("id");
        Client client = ServiceUtile.chercherClientId(id);
        Date currentDate = new Date();
        Intervention i;
        switch (type) {
            case "Livraison":
                i = new Livraison (objet, entreprise, description, currentDate, client);
                break;
            case "Animal":
                i = new Animal (animal, description, currentDate, client);
                break;
            default:
                i = new Incident (description, currentDate, client);
                break;
        }
        boolean conf = ServiceMetier.creerIntervention(i);
        
        request.setAttribute("resultIntervention", conf);
        
    }
       
}

