package optimal.ghost.dto;

public class Response {

	private String string = "";
	private String message = "";
	// We want to set always in our response this field as null
	private final Character play = null;

	public Response() {
	}

	public Response(String string, String message) {
		this.string = string;
		this.message = message;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Character getPlay() {
		return play;
	}

}
