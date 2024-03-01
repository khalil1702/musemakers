package entities;
import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class sendSMS {

    public static final String ACCOUNT_SID = "ACab788b2f8b9f90445ebce762f0e6dcf2";
    public static final String AUTH_TOKEN = "453e869a55635272e321eafdb3b947f9";

    public static void sendSMS(Reclamation r) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("+21627163524"),
                        new com.twilio.type.PhoneNumber("+18154733136"),
                        "Cher(e) "+ r.getNom_user() + " " + r.getUser().getPrenom_user()+ "\nLe statut de ta réclamation : " + r.getDescriRec() + " est : " + r.getStatutRec())
                .create();

        System.out.println(message.getSid());
    }
}
