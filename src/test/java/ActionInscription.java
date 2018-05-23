
import fr.insalyon.dasi.proactif.metier.OM.Client;
import fr.insalyon.dasi.proactif.metier.Service.ServiceMetier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

public class ActionInscription extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        
        try {
            String civilite = request.getParameter("civilite");
            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String d = request.getParameter("borndate");
            Date borndate = sdf.parse(d);
            
            String postcode = request.getParameter("postcode");
            String city = request.getParameter("city");
            String street = request.getParameter("street");
            String telephone = request.getParameter("telephone");
            String mail = request.getParameter("mail");
            String password = request.getParameter("password");
            Client p = new Client (civilite, name, surname, borndate, postcode, city, street, telephone, mail, password);
            boolean result = ServiceMetier.creerPersonne(p);
            request.setAttribute("resultInscription", result);
        } catch (ParseException ex) {
            request.setAttribute("resultInscription", false);
        }
        
    }
       
}
