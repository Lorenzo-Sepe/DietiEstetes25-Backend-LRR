package it.unina.dietiestates25.utils;


public class Msg {

    private Msg() {
    }

    // -------------------- USER
    public static final String USER_ALREADY_PRESENT = "Email or username already present";
    public static final String USERNAME_ALREADY_PRESENT = "Username already in use";
    public static final String EMAIL_ALREADY_PRESENT = "Email already in use";

    public static final String USER_SIGNUP_FIRST_STEP = "User successfully registered. Please check your email to complete the registration";
    public static final String USER_SIGNUP_SECOND_STEP = "Your email has been verified. Please log in";
    public static final String USER_HAS_DEFAULT_AUTHORITY = "This user has default authority: the authority cannot be changed until user verification mail";

    public static final String USER_HAS_SAME_AUTHORITY = "Old and new authorities are the same";
    public static final String AUTHORITY_CHANGED = "Authority successfully updated";
    public static final String INVALID_AUTHORITY = "Invalid authority name";

    public static final String MAIL_SIGNUP_SUBJECT = "MyBlog: verification email";

    // l'endpoint presente nell'email va copiato e messo su postman con metodo PATCH.
    // Se lanciato direttamente dall'email, ovvero da browser,
    // la chiamata fallisce in quando il browser permette solo chiamate GET
    public static final String MAIL_SIGNUP_BODY = "Please click here to confirm your email : http://localhost:8081/api/v0/auth/confirm/";

    public static final String ACCESS_DENIED = "YOU ARE NOT AUTHORIZED TO PERMORM THIS ACTION";

    public static final String PASSWORD_MISMATCH = "The repeated password not match";
    public static final String PWD_CHANGED = "Password successfully updated";
    public static final String PWD_INCORRECT = "Wrong Password";

    // -------------------- UPLOAD FILE
    public static final String FILE_TOO_LARGE = "File size not allowed";
    public static final String FILE_NOT_VALID_IMAGE = "Image not valid";
    public static final String FILE_INVALID_DIMENSIONS = "Invalid image width or height";
    public static final String FILE_EXTENSION_NOT_ALLOWED = "Image type not allowed";
    public static final String FILE_EXTENSION_MISSING = "Missing image type";
    public static final String FILE_ERROR_UPLOAD = "Upload file failed";
    public static final String FILE_ERROR_DELETE = "Delete file failed";

    // -------------------- TAG
    public static final String TAG_ALREADY_PRESENT = "Tag already present";

    // -------------------- POST
    public static final String POST_TITLE_IN_USE = "Post title already in use";
    public static final String POST_UNAUTHORIZED_ACCESS = "The post can modified only by owner";

    public static final String POST_SAME_AUTHOR = "Old and new author are the same";

    public static final String POSTS_REASSIGNEMENT = "All posts have been reassigned";
    public static final String POST_REASSIGNEMENT = "The post has been reassigned";

    // -------------------- PREFERRED POST
    public static final String BOOKMARK_ADD = "The post has been bookmarked";
    public static final String BOOKMARK_REMOVE = "The post has been removed from bookmarks";

    // -------------------- COMMENT
    public static final String COMMENT_500 = "Something went wrong writing the comment";
    public static final String COMMENT_UNAUTHORIZED_ACCESS = "The comment can modified only by owner";
    public static final String COMMENT_CENSORED = "This comment has been censored";
    public static final String COMMENT_UNDER_CONTROL = "This comment is waiting for moderator decision";
    public static final String REACTION_SAME_COMMENT_AUTHOR = "You cannot add a reaction to your own comment";

    // -------------------- MADE BY YOU
    public static final String MADE_BY_YOU_EXISTS = "You already uploaded your artifact.";
    public static final String MADE_BY_YOU_CENSORED = "This artifact has been censored";
    public static final String MADE_BY_YOU_UNDER_CONTROL = "This artifact is waiting for moderator decision";
    public static final String MADE_BY_YOU_UNAUTHORIZED_ACCESS = "The artifact can modified only by owner";
    public static final String MADE_BY_YOU_DELETE_OK = "Your artifact has been removed";

    // -------------------- REPORT REASON
    public static final String REPORT_REASON_VALID_ALREADY_PRESENT = "A reason with this name is already present";

    // -------------------- REPORT
    public static final String REPORT_CREATED = "Your report has been send";
    public static final String REPORT_DOUBLE_PARAMETERS = "Either comment and madeByou ids found";
    public static final String REPORT_NO_PARAMETERS = "No comment or madeByYou ids found";
    public static final String REPORT_INVALID_REPORTER = "You cannot report yourself";
    public static final String REPORT_ALREDY_REPORTED = "You already report this";
    public static final String REPORT_UNMODIFIABLE = "You cannot change the status of this report because it was closed";
    public static final String REPORT_COME_BACK_NOT_ALLOWED = "This report cannot come back in the previous status";
    public static final String REPORT_NOT_CLOSEABLE = "You cannot close directly this report";
    public static final String REPORT_SAME_STATUS = "The old status equals to the new status";

    // -------------------- PDF
    public static final String PDF_FATAL_ERROR = "Error creating pdf from post";

}
