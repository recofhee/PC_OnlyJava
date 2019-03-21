package pc.service.exception;

public class NameException extends Exception {
	String name;

	public NameException(String msg, String name) {
		super(msg);
		this.name = name;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getMessage() {
		return super.getMessage() + " \n-> [" + name + "]\n (영문과 숫자로 이루어진 1~8자리 사이가 아닌 값이 입력되었습니다.)";
	}

}