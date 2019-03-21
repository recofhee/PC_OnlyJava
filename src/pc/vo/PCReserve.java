package pc.vo;

import java.util.List;

public class PCReserve implements Comparable<PCReserve>, java.io.Serializable {
	private int idx; // 예약번호
	private String userID; // 이용자 아이디
	private int pcIdx; // PC방 index
	private int seat; // 선택 좌석
	private int money; // 예약자의 총 계산금액
	
	//private List<PC> pcs; 
	private List<Food> foods;

	public PCReserve() {}
	
	public PCReserve(int idx, String userID, int pcIdx, int seat, List<Food> foods, int money) {
		super();
		this.idx = idx;
		this.userID = userID;
		this.pcIdx = pcIdx;
		this.seat = seat;
		this.foods = foods;
		this.money = money;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public int getPcIdx() {
		return pcIdx;
	}

	public void setPcIdx(int pcIdx) {
		this.pcIdx = pcIdx;
	}

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}


	public List<Food> getFoods() {
		return foods;
	}

	public void setFoods(List<Food> foods) {
		this.foods = foods;
	}

	@Override
	public String toString() {
		return "PCReserve [userID=" + userID + ", 예약 번호=" + idx + ", pcIdx=" + pcIdx + ", seat=" + seat + ", foods="
				+ foods + ", 계산 금액 : " + money + "]";
	}

	@Override
	public int compareTo(PCReserve o) {
		// TODO Auto-generated method stub
		return this.compareTo(o);
	}

}
