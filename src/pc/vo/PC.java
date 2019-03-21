package pc.vo;

import java.util.List;

public class PC implements java.io.Serializable {
	
	private int pcIdx; // PC방 index
	private int pcLocIdx; // PC방 위치 index
	private String pcLoc; // PC방 위치
	private String pcName; // PC방 이름
	private int price; // PC방 가격
	private List<String> seatList; // 좌석 목록
	private List<Food> inventories; // 푸드 재고 목록

	public PC() {}
	
	public PC(int pcIdx, int pcLocIdx, String pcLoc, String pcName, int price, List<String> seatList) {
		super();
		this.pcIdx = pcIdx;
		this.pcLocIdx = pcLocIdx;
		this.pcLoc = pcLoc;
		this.pcName = pcName;
		this.price = price;
		this.seatList = seatList;
	}

	public PC(int pcIdx, int pcLocIdx, String pcLoc, String pcName, int price, List<String> seatList, List<Food> inventories) {
		super();
		this.pcIdx = pcIdx;
		this.pcLocIdx = pcLocIdx;
		this.pcLoc = pcLoc;
		this.pcName = pcName;
		this.price = price;
		this.seatList = seatList;
		this.inventories = inventories;
	}
	
	public int getPcIdx() {
		return pcIdx;
	}

	public void setPcIdx(int pcIdx) {
		this.pcIdx = pcIdx;
	}
	
	public int getPcLocIdx() {
		return pcLocIdx;
	}
	
	public void setPcLocIdx(int pcLocIdx) {
		this.pcLocIdx = pcLocIdx;
	}

	public String getPcLoc() {
		return pcLoc;
	}

	public void setPcLoc(String pcLoc) {
		this.pcLoc = pcLoc;
	}

	public String getPcName() {
		return pcName;
	}

	public void setPcName(String pcName) {
		this.pcName = pcName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public List<String> getSeatList() {
		return seatList;
	}

	public void setSeatList2(List<String> seatList) {
		this.seatList = seatList;
	}

	public List<Food> getInventories() {
		return inventories;
	}

	public void setInventories(List<Food> inventories) {
		this.inventories = inventories;
	}

	@Override
	public String toString() {
		return pcIdx + ". " + pcName + " (" + pcLoc + ") ";
	}


}
