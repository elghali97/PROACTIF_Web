import fr.insalyon.dasi.proactif.metier.OM.Employe;
import fr.insalyon.dasi.proactif.metier.OM.Intervention;
import fr.insalyon.dasi.proactif.metier.Service.ServiceMetier;
import fr.insalyon.dasi.proactif.metier.Service.ServiceUtile;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ActionConsulterHistoriqueEmploye extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        
        HttpSession session=request.getSession(true);
        
        int id= (int) session.getAttribute("id");
        Employe e =ServiceUtile.chercherEmployeId(id);
        double latEmploye=e.getLatitudeLongitude().lat;
        double lngEmploye=e.getLatitudeLongitude().lng;
        List<Intervention> historique = ServiceMetier.historiqueEmploye(e);
        request.setAttribute("resultHistorique", historique);
        request.setAttribute("latEmploye",latEmploye);
        request.setAttribute("lngEmploye",lngEmploye);
        
    }
       
}
