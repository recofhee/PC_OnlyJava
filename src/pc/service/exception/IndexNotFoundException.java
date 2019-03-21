package pc.service.exception;

public class IndexNotFoundException extends Exception {
	private int idx;

	public IndexNotFoundException() {
		super();
	}

	public IndexNotFoundException(String msg, int idx) {
		super(msg);
		this.idx = idx;
	}

	@Override
	public String getMessage() {
		return super.getMessage() + " 위치에서 일치하지 않는 목록 번호가 입력되었습니다.";
	}

}