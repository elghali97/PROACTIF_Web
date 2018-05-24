
import fr.insalyon.dasi.proactif.metier.OM.Personne;
import fr.insalyon.dasi.proactif.metier.Service.ServiceMetier;
import javax.servlet.http.HttpServletRequest;

public class ActionConnection extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        String login= request.getParameter("login");
        String password= request.getParameter("password");
        String usertype=request.getParameter("usertype");
        System.out.println("usertype: "+usertype);
        Personne p=ServiceMetier.chercherPersonneMailEtMdp(login,password);
        request.setAttribute("personne", p);
    }
       
}
