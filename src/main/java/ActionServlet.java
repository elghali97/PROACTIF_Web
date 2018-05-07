import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.insalyon.dasi.proactif.DAO.JpaUtil;
import fr.insalyon.dasi.proactif.metier.OM.Animal;
import fr.insalyon.dasi.proactif.metier.OM.Client;
import fr.insalyon.dasi.proactif.metier.OM.Employe;
import fr.insalyon.dasi.proactif.metier.OM.Incident;
import fr.insalyon.dasi.proactif.metier.OM.Intervention;
import fr.insalyon.dasi.proactif.metier.OM.Livraison;
import fr.insalyon.dasi.proactif.metier.OM.Personne;
import fr.insalyon.dasi.proactif.metier.Service.ServiceMetier;
import fr.insalyon.dasi.proactif.metier.Service.ServiceUtile;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            //Service s= new Service();
            String todo =request.getParameter("action");
            
            // DEBUG:
//            Enumeration en=request.getParameterNames();
// 
//            while(en.hasMoreElements()){
//                Object objOri=en.nextElement();
//                String param=(String)objOri;
//                String value=request.getParameter(param);
//                System.out.println("Parameter Name is '"+param+"' and Parameter Value is '"+value+"'");
//            }
            
            switch (todo) {
                case "connecter":{
                    
                    String login= request.getParameter("login");
                    String password= request.getParameter("password");
                    String usertype=request.getParameter("usertype");
                    System.out.println("usertype: "+usertype);
                    Personne p=ServiceMetier.chercherPersonneMailEtMdp(login,password);
                    if((p!=null) && authentify(p,usertype)){
                        printDetailConnection(out,"OK",p.getId());
                    }else{
                        printDetailConnection(out, "KO",0);
                    }    
                    break;
                    
                }
                case "inscription":{
                    
                    String civilite = request.getParameter("civilite");
                    String name = request.getParameter("name");
                    String surname = request.getParameter("surname");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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
                    printDetailInscription(out, result);
                    break;
                }
                case "intervention":{
                    
                    String type = request.getParameter("type");
                    String objet = request.getParameter("objet");
                    String entreprise = request.getParameter("entreprise");
                    String animal = request.getParameter("animal");
                    String description = request.getParameter("description");
                    String idClient = request.getParameter("idClient");
                    int id = Integer.parseInt(idClient);
                    Client client = ServiceUtile.chercherClientId(id);
                    //!!!!!!! Définir date du jour
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date currentDate = new Date();
                    Intervention i;
                    if (type.equals("Livraison")) {
                        i = new Livraison (objet, entreprise, description, currentDate, client);
                    } else if (type.equals("Animal")) {
                        i = new Animal (animal, description, currentDate, client);
                    } else {
                        i = new Incident (description, currentDate, client);
                    }
                    boolean conf = ServiceMetier.creerIntervention(i);
                    printDetailIntervention(out, conf);
                    break;
                    // if (type.equals("Incident")) {
                    //    Incident i = new Incident (description, date, client);
                    // } else if (type.equals("Livraison")) {
                    //   Livraison i = new Livraison ();
                    //}
                    //boolean conf = ServiceMetier.creerIntervention(i);
                    
                }
                case "consulterInterventionEnCours":{
                    
                    String idEmploye=request.getParameter("IdEmploye");
                    System.out.println("idEmploye : "+idEmploye);
                    int id=Integer.parseInt(idEmploye);
                    Employe e =ServiceUtile.chercherEmployeId(id);
                    List<Intervention> historique = ServiceMetier.historiqueEmploye(e);
                    if((historique!=null)&&(!historique.isEmpty())){
                        printInterventionActive(out,historique.get(historique.size()-1));
                    }else{
                        printInterventionActive(out,null);
                    }
                    break;
                    
                }
                
//            if (q==null){
//                List<Service.Personne> personnes = s.consulterListePersonnes();
//                printListPersonne(out,personnes);
//            }else{
//                String [] w=q.split("=");
//                if (w.length==1){
//                    List<Service.Personne> personnes = s.consulterListePersonnes();
//                    printListPersonne(out,personnes);
//                }else{
//                    int id= Integer.parseInt(w[1]);
//                    Service.Personne p = s.consulterDetailPersonne(id);
//                    printDetailPersonne(out,p);
//                }
//            }
            }
            out.close();
        }
    }
    
    protected void printDetailInscription(PrintWriter out, boolean result){
        Gson gson= new GsonBuilder().setPrettyPrinting().create();
        JsonObject container = new JsonObject();
        container.addProperty("Inscription",result);
        out.println(gson.toJson(container));
    }
    
    protected void printDetailIntervention(PrintWriter out, boolean conf) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject container = new JsonObject();
        container.addProperty("Intervention", conf);
        out.println(gson.toJson(container));
    }
    
//    protected void printListPersonne(PrintWriter out, List<Service.Personne> personnes){
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        JsonArray jsonListe= new JsonArray();
//        for(Service.Personne p : personnes) {
//            JsonObject jsonPersonne= new JsonObject();
//            jsonPersonne.addProperty("id",p.getId());
//            jsonPersonne.addProperty("civilite",p.getCivilite());
//            jsonPersonne.addProperty("nom",p.getNom());
//            jsonPersonne.addProperty("prenom",p.getPrenom());
//            jsonPersonne.addProperty("mail",p.getMail());
//            jsonPersonne.addProperty("adresse",p.getAdresse());
//            jsonListe.add(jsonPersonne);
//        }
//        JsonObject container = new JsonObject();
//        container.add("personnes",jsonListe);
//        out.println(gson.toJson(container));
//    }
//    
    protected void printDetailConnection(PrintWriter out, String connection, int id){
        Gson gson= new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonConnexion = new JsonObject();
        jsonConnexion.addProperty("Authentification",connection);
        jsonConnexion.addProperty("Id",id);
        JsonObject container= new JsonObject();
        container.add("Connexion", jsonConnexion);
        out.println(gson.toJson(container));
    }
    
    protected void printDetailPersonne(PrintWriter out, Personne p){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonPersonne= new JsonObject();
        jsonPersonne.addProperty("id",p.getId());
        jsonPersonne.addProperty("civilite",p.getCivilite());
        jsonPersonne.addProperty("nom",p.getNom());
        jsonPersonne.addProperty("prenom",p.getPrenom());
        jsonPersonne.addProperty("numtel",p.getNumTel());
        jsonPersonne.addProperty("mail",p.getMail());
        jsonPersonne.addProperty("adressePost",p.getAdressePost());
        JsonObject container = new JsonObject();
        container.add("personne",jsonPersonne);
        out.println(gson.toJson(container)); 
    }
    
    protected void printInterventionActive(PrintWriter out, Intervention i){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonIntervention= new JsonObject();
        if(i!=null){
            JsonObject jsonInterventionClient= new JsonObject();
            jsonInterventionClient.addProperty("idClient",i.getClient().getId());
            jsonInterventionClient.addProperty("nomClient",i.getClient().getNom());
            jsonInterventionClient.addProperty("prenomClient",i.getClient().getPrenom());
            jsonInterventionClient.addProperty("mailClient",i.getClient().getMail());
            jsonInterventionClient.addProperty("adresseClient",i.getClient().getAdressePost());
            jsonInterventionClient.addProperty("numtelClient",i.getClient().getNumTel());
            
            jsonIntervention.add("client",jsonInterventionClient);
            jsonIntervention.addProperty("type",i.getType());
            jsonIntervention.addProperty("description",i.getDescription());
            jsonIntervention.addProperty("exist",true);
            if(i.getDateTermine()==null){
                jsonIntervention.addProperty("active",true);
            }else{
                jsonIntervention.addProperty("active",false);
            } 
        }else{
            jsonIntervention.addProperty("exist",false);
        }
        JsonObject container = new JsonObject();
        container.add("intervention",jsonIntervention);
        out.println(gson.toJson(container)); 
    }
    
    
    protected boolean authentify(Personne p, String usertype){        
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
    
    protected void printListIntervention(PrintWriter out, List<Intervention> interventions){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray jsonListe= new JsonArray();
        for(Intervention i : interventions) {
            JsonObject jsonIntervention= new JsonObject();
            jsonIntervention.addProperty("type",i.getType());
            jsonIntervention.addProperty("description",i.getDescription());
            JsonObject jsonInterventionClient= new JsonObject();
            jsonInterventionClient.addProperty("idClient",i.getClient().getId());
            jsonInterventionClient.addProperty("civiliteClient",i.getClient().getCivilite());
            jsonInterventionClient.addProperty("nomClient",i.getClient().getNom());
            jsonInterventionClient.addProperty("prenomClient",i.getClient().getPrenom());
            jsonInterventionClient.addProperty("mailClient",i.getClient().getMail());
            jsonInterventionClient.addProperty("adresseClient",i.getClient().getAdressePost());
            jsonInterventionClient.addProperty("numtelClient",i.getClient().getNumTel());
            jsonIntervention.add("client",jsonInterventionClient);
            jsonListe.add(jsonIntervention);
        }
        JsonObject container = new JsonObject();
        container.add("interventions",jsonListe);
        out.println(gson.toJson(container));
    }

    @Override
    public void init() throws ServletException {
        JpaUtil.init();
    }

    @Override
    public void destroy() {
        JpaUtil.destroy();
    }
    
    

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
