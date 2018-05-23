import fr.insalyon.dasi.proactif.DAO.JpaUtil;
import fr.insalyon.dasi.proactif.metier.OM.Client;
import fr.insalyon.dasi.proactif.metier.OM.Employe;
import fr.insalyon.dasi.proactif.metier.OM.Intervention;
import fr.insalyon.dasi.proactif.metier.OM.Personne;
import fr.insalyon.dasi.proactif.metier.Service.ServiceMetier;
import fr.insalyon.dasi.proactif.metier.Service.ServiceUtile;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * @author LENOVO
 **/
@WebServlet(name="ActionServletSession",urlPatterns = {"/ActionServlet"})
public class ActionServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.text.ParseException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        
        HttpSession session=request.getSession(true);
        request.setCharacterEncoding("UTF-8");
        
        response.setContentType("application/json");
        
        try (PrintWriter out = response.getWriter()) {
            
            String todo =request.getParameter("action");
            switch (todo) {
                
                case "connecter":{
                    String usertype=request.getParameter("usertype");
                    Action action= new ActionConnection();
                    action.execute(request);
                    Personne p= (Personne) request.getAttribute("personne");
                    if((p!=null) && authentify(p,usertype)){
                        session.setAttribute("id", p.getId());
                        Serialisation.printDetailConnection(out,"OK");
                    }else{
                        Serialisation.printDetailConnection(out, "KO");
                    }    
                    break;
                }
                
                case "inscription":{
                    Action action= new ActionInscription();
                    action.execute(request);
                    boolean result= (boolean) request.getAttribute("resultInscription");
                    Serialisation.printDetailInscription(out, result);
                    break;
                }
                
                case "intervention":{
                    Action action= new ActionIntervention();
                    action.execute(request);
                    boolean result= (boolean) request.getAttribute("resultIntervention");
                    Serialisation.printDetailIntervention(out, result);
                    break;
                }
                
                case "consulterInterventionEnCours":{
                    Action action= new ActionInterventionEnCours();
                    action.execute(request);
                    Intervention result= (Intervention) request.getAttribute("resultInterventionEnCours");
                    Serialisation.printInterventionActive(out,result);
                    break;  
                }
                
                case "consulterHistoriqueClient" :{
                    int id= (int) session.getAttribute("id");
                    Client c = ServiceUtile.chercherClientId(id);
                    List<Intervention> historique = ServiceMetier.historiqueClient(c);
                    Serialisation.printListIntervention(out,historique);
                    break;
                }
                
                case "cloturerIntervention":{
                    Action action= new ActionCloturerIntervention();
                    action.execute(request);
                    boolean result= (boolean) request.getAttribute("resultCloture");
                    Serialisation.printDetailCloture(out,result);
                    break;
                }
                
                case "consulterHistoriqueEmploye":{
                    Action action= new ActionConsulterHistoriqueEmploye();
                    action.execute(request);
                    List<Intervention> historique= (List<Intervention>) request.getAttribute("resultHistorique");
                    double lngEmploye= (double) request.getAttribute("lngEmploye");
                    double latEmploye= (double) request.getAttribute("latEmploye");
                    if((historique!=null)){
                        Serialisation.printListIntervention(out,historique,latEmploye,lngEmploye);
                    }else{
                        Serialisation.printListIntervention(out,null,latEmploye,lngEmploye);
                    }
                    break;
                }
                
                case "deconnexion":{
                    session.setAttribute("id", null);
                    Serialisation.printDetailDeconnexion(out, true);
                    break;
                }
                
            }
            out.close();
        }
    }
    
    public static boolean authentify(Personne p, String usertype){        
        boolean authentification;
        if(usertype.equals("employe")){
            Employe e= ServiceUtile.chercherEmployeId(p.getId());
            authentification= (p.getNom().equals(e.getNom()))&&(p.getPrenom().equals(e.getPrenom()))&&(p.getDateNaiss()==e.getDateNaiss());
            return authentification;
        }else{
            Client c= ServiceUtile.chercherClientId(p.getId());
            authentification= (p.getNom().equals(c.getNom()))&&(p.getPrenom().equals(c.getPrenom()))&&(p.getDateNaiss()==c.getDateNaiss());
            return authentification;
        }
    }
    
    @Override
    public void init() throws ServletException {
        JpaUtil.init();
    }

    @Override
    public void destroy() {
        JpaUtil.destroy();
    }
    
    //page 37/102
    //Appel ajax qui actionServlet si attribut employé true ou false... Si false redirige vers page connexion

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
