import fr.insalyon.dasi.proactif.metier.Service.ServiceMetier;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

public class ActionCloturerIntervention extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        
        try{
            String idIntervention=request.getParameter("idIntervention");
            int id=Integer.parseInt(idIntervention);
            String heure=request.getParameter("hour"); 
            String commentaire=request.getParameter("commentaire");
            Date d= new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String s= sdf.format(d);
            s=s+" "+heure;
            SimpleDateFormat sdfh = new SimpleDateFormat("dd/MM/yyyy HH");
            Date date = sdfh.parse(s);
            boolean cloture=ServiceMetier.clotureIntervention(id, date, commentaire);
            request.setAttribute("resultCloture", cloture);
        }catch(Exception e){
            request.setAttribute("resultCloture", false);
        }
        
    }
       
}


