package it.unina.dietiestates25.utils;


public class Msg {


    private Msg() {
    }

    // -------------------- USER
    public static final String USER_ALREADY_PRESENT = "Email o nome utente già presenti";
    public static final String USERNAME_ALREADY_PRESENT = "Nome utente già in uso";
    public static final String EMAIL_ALREADY_PRESENT = "Email già in uso";
    public static final String PASSWORD_CHANGED ="Password cambiata con successo"; ;

    public static final String USER_SIGNUP_FIRST_STEP = "Utente registrato con successo. Effettua il login";
    public static final String USER_HAS_DEFAULT_AUTHORITY = "Questo utente ha l'autorizzazione predefinita: l'autorizzazione non può essere modificata fino alla verifica dell'email";

    public static final String USER_HAS_SAME_AUTHORITY = "Le vecchie e nuove autorizzazioni sono uguali";
    public static final String AUTHORITY_CHANGED = "Autorizzazione aggiornata con successo";
    public static final String INVALID_AUTHORITY = "Nome autorizzazione non valido";

    public static final String MAIL_SIGNUP_SUBJECT = "DietiEstates25: email di verifica";

    // l'endpoint presente nell'email va copiato e messo su postman con metodo PATCH.
    // Se lanciato direttamente dall'email, ovvero da browser,
    // la chiamata fallisce in quando il browser permette solo chiamate GET
    public static final String MAIL_SIGNUP_BODY = "Clicca qui per confermare la tua email : http://localhost:8081/api/pb/auth/confirm/";

    public static final String ACCESS_DENIED = "NON SEI AUTORIZZATO A ESEGUIRE QUESTA AZIONE";

    public static final String PASSWORD_MISMATCH = "La password ripetuta non corrisponde";
    public static final String PWD_CHANGED = "Password aggiornata con successo";
    public static final String PWD_INCORRECT = "Password errata";

    // -------------------- UPLOAD FILE
    public static final String FILE_TOO_LARGE = "Dimensione file non consentita";
    public static final String FILE_NOT_VALID_IMAGE = "Immagine non valida";
    public static final String FILE_INVALID_DIMENSIONS = "Larghezza o altezza dell'immagine non valide";
    public static final String FILE_EXTENSION_NOT_ALLOWED = "Tipo di immagine non consentito";
    public static final String FILE_EXTENSION_MISSING = "Tipo di immagine mancante";
    public static final String FILE_ERROR_UPLOAD = "Caricamento file fallito";
    public static final String FILE_ERROR_DELETE = "Eliminazione file fallita";

    // -------------------- TAG
    public static final String TAG_ALREADY_PRESENT = "Tag già presente";

    // -------------------- POST
    public static final String POST_TITLE_IN_USE = "Titolo del post già in uso";
    public static final String POST_UNAUTHORIZED_ACCESS = "Il post può essere modificato solo dal proprietario";

    public static final String POST_SAME_AUTHOR = "Il vecchio e il nuovo autore sono gli stessi";

    public static final String POSTS_REASSIGNEMENT = "Tutti i post sono stati riassegnati";
    public static final String POST_REASSIGNEMENT = "Il post è stato riassegnato";

    // -------------------- PREFERRED POST
    public static final String BOOKMARK_ADD = "Il post è stato aggiunto ai preferiti";
    public static final String BOOKMARK_REMOVE = "Il post è stato rimosso dai preferiti";

    // -------------------- COMMENT
    public static final String COMMENT_500 = "Qualcosa è andato storto durante la scrittura del commento";
    public static final String COMMENT_UNAUTHORIZED_ACCESS = "Il commento può essere modificato solo dal proprietario";
    public static final String COMMENT_CENSORED = "Questo commento è stato censurato";
    public static final String COMMENT_UNDER_CONTROL = "Questo commento è in attesa della decisione del moderatore";
    public static final String REACTION_SAME_COMMENT_AUTHOR = "Non puoi aggiungere una reazione al tuo commento";

    // -------------------- MADE BY YOU
    public static final String MADE_BY_YOU_EXISTS = "Hai già caricato il tuo artefatto";
    public static final String MADE_BY_YOU_CENSORED = "Questo artefatto è stato censurato";
    public static final String MADE_BY_YOU_UNDER_CONTROL = "Questo artefatto è in attesa della decisione del moderatore";
    public static final String MADE_BY_YOU_UNAUTHORIZED_ACCESS = "L'artefatto può essere modificato solo dal proprietario";
    public static final String MADE_BY_YOU_DELETE_OK = "Il tuo artefatto è stato rimosso";

    // -------------------- REPORT REASON
    public static final String REPORT_REASON_VALID_ALREADY_PRESENT = "Una motivazione con questo nome è già presente";

    // -------------------- REPORT
    public static final String REPORT_CREATED = "La tua segnalazione è stata inviata";
    public static final String REPORT_DOUBLE_PARAMETERS = "Trovati sia ID del commento che del madeByYou";
    public static final String REPORT_NO_PARAMETERS = "Nessun ID del commento o madeByYou trovato";
    public static final String REPORT_INVALID_REPORTER = "Non puoi segnalare te stesso";
    public static final String REPORT_ALREDY_REPORTED = "Hai già segnalato questo contenuto";
    public static final String REPORT_UNMODIFIABLE = "Non puoi cambiare lo stato di questa segnalazione perché è stata chiusa";
    public static final String REPORT_COME_BACK_NOT_ALLOWED = "Questa segnalazione non può tornare allo stato precedente";
    public static final String REPORT_NOT_CLOSEABLE = "Non puoi chiudere direttamente questa segnalazione";
    public static final String REPORT_SAME_STATUS = "Il vecchio stato è uguale al nuovo stato";

    // -------------------- PDF
    public static final String PDF_FATAL_ERROR = "Errore durante la creazione del PDF dal post";

}
