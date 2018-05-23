import fr.insalyon.dasi.proactif.metier.OM.Employe;
import fr.insalyon.dasi.proactif.metier.OM.Intervention;
import fr.insalyon.dasi.proactif.metier.Service.ServiceMetier;
import fr.insalyon.dasi.proactif.metier.Service.ServiceUtile;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ActionInterventionEnCours extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        
        HttpSession session=request.getSession(true);
        
        int id= (int) session.getAttribute("id");
        Employe e =ServiceUtile.chercherEmployeId(id);
        List<Intervention> historique = ServiceMetier.historiqueEmploye(e);
        if((historique!=null)&&(!historique.isEmpty())){
            request.setAttribute("resultInterventionEnCours", historique.get(historique.size()-1));
        }else{
            request.setAttribute("resultInterventionEnCours", null);
        }
        
    }
       
}

