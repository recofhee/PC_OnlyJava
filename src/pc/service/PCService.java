package pc.service;

import pc.service.exception.*;
import pc.vo.*;

public interface PCService {
	// ID 입력
	public void inputID() throws NameException;
	
	// 검색
	public boolean search() throws IndexNotFoundException;
		
	// 예약 및 구매
	// 좌석 예약
	public void reserveSeat() throws IndexNotFoundException;
	
	// 푸드 구매
	public void reserveFood() throws CloneNotSupportedException, IndexNotFoundException;
	
	// 총 예약 정보 확인
	public boolean reserveList();
		
	// 좌석 예약 취소
	public void reserveSeatCancel();
		
	// 푸드 구매 수정
	public void reserveFoodModify(int no) throws IndexNotFoundException;
	
}
