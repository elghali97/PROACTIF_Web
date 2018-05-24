import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.insalyon.dasi.proactif.metier.OM.Intervention;
import fr.insalyon.dasi.proactif.metier.OM.Personne;
import java.io.PrintWriter;
import java.util.List;


public class Serialisation {
    
    public static void printDetailDeconnexion(PrintWriter out, boolean result){
        Gson gson= new GsonBuilder().setPrettyPrinting().create();
        JsonObject container = new JsonObject();
        container.addProperty("deconnexion",result);
        out.println(gson.toJson(container)); 
    }
    
    public static void printDetailInscription(PrintWriter out, boolean result){
        Gson gson= new GsonBuilder().setPrettyPrinting().create();
        JsonObject container = new JsonObject();
        container.addProperty("Inscription",result);
        out.println(gson.toJson(container));
    }
    
    public static void printDetailCloture(PrintWriter out, boolean result){
        Gson gson= new GsonBuilder().setPrettyPrinting().create();
        JsonObject container = new JsonObject();
        container.addProperty("cloture",result);
        out.println(gson.toJson(container));
    }
    
    public static void printDetailIntervention(PrintWriter out, boolean conf) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject container = new JsonObject();
        container.addProperty("Intervention", conf);
        out.println(gson.toJson(container));
    }
    
    public static void printDetailConnection(PrintWriter out, String connection){
        Gson gson= new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonConnexion = new JsonObject();
        jsonConnexion.addProperty("Authentification",connection);
        JsonObject container= new JsonObject();
        container.add("Connexion", jsonConnexion);
        out.println(gson.toJson(container));
    }
    
    public static void printDetailPersonne(PrintWriter out, Personne p){
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
    
    public static void printInterventionActive(PrintWriter out, Intervention i){
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
            jsonIntervention.addProperty("idIntervention",i.getIdIntervention());
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
     
    public static void printListIntervention(PrintWriter out, List<Intervention> interventions, double lat, double lng){
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray jsonListe= new JsonArray();
        
        if(interventions!=null){
            
            for(Intervention i : interventions) {

                JsonObject jsonInterventionClient= new JsonObject();
                jsonInterventionClient.addProperty("idClient",i.getClient().getId());
                jsonInterventionClient.addProperty("civiliteClient",i.getClient().getCivilite());
                jsonInterventionClient.addProperty("nomClient",i.getClient().getNom());
                jsonInterventionClient.addProperty("prenomClient",i.getClient().getPrenom());
                jsonInterventionClient.addProperty("mailClient",i.getClient().getMail());
                jsonInterventionClient.addProperty("adresseClient",i.getClient().getAdressePost());
                jsonInterventionClient.addProperty("numtelClient",i.getClient().getNumTel());
                jsonInterventionClient.addProperty("latitude", i.getClient().getLatitudeLongitude().lat);
                jsonInterventionClient.addProperty("longitude", i.getClient().getLatitudeLongitude().lng);

                JsonObject jsonIntervention= new JsonObject();
                jsonIntervention.addProperty("idIntervention",i.getIdIntervention());
                jsonIntervention.addProperty("type",i.getType());
                jsonIntervention.addProperty("description",i.getDescription());
                jsonIntervention.add("client",jsonInterventionClient);
                jsonListe.add(jsonIntervention);
            }
        }
            
        JsonObject jsonInterventionEmploye=new JsonObject();
        jsonInterventionEmploye.addProperty("latitude", lat);
        jsonInterventionEmploye.addProperty("longitude",lng );

        JsonObject container = new JsonObject();
        container.add("interventions",jsonListe);
        container.add("employe",jsonInterventionEmploye);
        out.println(gson.toJson(container));


    }
    
    public static void printListIntervention(PrintWriter out, List<Intervention> interventions){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray jsonListe= new JsonArray();
        if(interventions!=null){
            
            for(Intervention i : interventions) {

                JsonObject jsonInterventionClient= new JsonObject();
                jsonInterventionClient.addProperty("idClient",i.getClient().getId());
                jsonInterventionClient.addProperty("civiliteClient",i.getClient().getCivilite());
                jsonInterventionClient.addProperty("nomClient",i.getClient().getNom());
                jsonInterventionClient.addProperty("prenomClient",i.getClient().getPrenom());
                jsonInterventionClient.addProperty("mailClient",i.getClient().getMail());
                jsonInterventionClient.addProperty("adresseClient",i.getClient().getAdressePost());
                jsonInterventionClient.addProperty("numtelClient",i.getClient().getNumTel());

                JsonObject jsonIntervention= new JsonObject();
                jsonIntervention.addProperty("idIntervention",i.getIdIntervention());
                jsonIntervention.addProperty("type",i.getType());
                jsonIntervention.addProperty("description",i.getDescription());
                jsonIntervention.add("client",jsonInterventionClient);
                jsonListe.add(jsonIntervention);
            }
        }
        
        JsonObject container = new JsonObject();
        container.add("interventions",jsonListe);
        out.println(gson.toJson(container));
    }
}
