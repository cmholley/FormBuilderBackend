package dash.filters;

public class AppConstants {

	public static final int GENERIC_APP_ERROR_CODE = 5001;
	public static final String DASH_POST_URL = "here will be the dash post url";
	public static final String APPLICATION_UPLOAD_LOCATION_FOLDER = "/var/uploads/form_builder";
	public static final String APPLICATION_NAME = "FormBuilder";

	public enum QuestionType {
		SHORT_TEXT
	}

	public enum InputValidation {
		NONE, NUMBER, EMAIL, URL, PHONE
	}

	public enum TOKEN_TYPE {
		PASSWORD_RESET, EMAIL_ACTIVATION
	}
}
