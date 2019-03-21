package pc.service;

import static java.lang.Integer.parseInt;

import java.io.*;
import java.util.*;

import pc.service.exception.*;
import pc.vo.*;

public class PCServiceImple implements PCService {
	List<PC> pcAllList = new ArrayList<>();
	List<PCReserve> res = new ArrayList<>(); // 고객 관리 객체

	static String userID; //

	Scanner scanner;

	// 초기화 블럭
	{
		init();
//		pcAllListLoad();
//		reserveLoad();

	} // dummy data

	// ID 입력
	@Override
	public void inputID() throws NameException {
		scanner = new Scanner(System.in);
		// ID 입력
		System.out.println("ID를 입력해 주세요. (종료 : q)");
		System.out.print("> ");
		userID = scanner.nextLine();

		validateString("영문과 숫자로 이루어진 1~8자리 사이의 ID를 입력해 주세요.", userID);

		if (inputString(userID)) {
			return;
		}
		
		//reserveSave();
	}

	// int 오름차순
	static class CompareIdxAsc implements Comparator<Integer> {
		@Override
		public int compare(Integer o1, Integer o2) {
			return o1 - o2;
		}
	}

	// 역 index 검색 후 중복 제거 후 정렬
	public void searchSubIdx() {
		// 역 번호 + 역 이름 목록
		List<Integer> subIdxList = new ArrayList<>();
		for (PC p : pcAllList) {
			if (!subIdxList.contains(p.getPcLocIdx())) {
				subIdxList.add(p.getPcLocIdx());
			}
		}
		// Collections.sort(subIdxList, new CompareIdxAsc());
		subIdxList.sort(new CompareIdxAsc());

		for (Integer i : subIdxList) {
			PC p = getPCLoc(i);
			if (p.getPcLocIdx() == i) {
				System.out.println(i + ". " + p.getPcLoc());
			}
		}
	}

	// 기능 1-1
	// 검색 역 명과 관련한 PC방 정보 조회
	@Override
	public boolean search() throws IndexNotFoundException {
		searchSubIdx();

		// 역 검색
		System.out.println("검색할 위치 번호를 입력해 주세요. (종료 : q)");
		System.out.print("> ");
		String inputIdx = scanner.nextLine();
		if (inputString(inputIdx)) {
			return true;
		}

		int subwayIdx = parseInt(inputIdx);

		boolean bl = false;

		// 역에 해당하는 PC방 목록 출력
		for (PC p : pcAllList) {
			if (p.getPcLocIdx() == subwayIdx) {
				System.out.println(p);
				bl = true;
			}
		}

		if (!bl) {
			throw new IndexNotFoundException("역 검색", subwayIdx);
		}

		System.out.println();
		return false;
	}

	// 옵션 1
	// 전체 PC방들의 각각의 좌석
	public void about(int pcIdx) throws IndexNotFoundException {
		boolean bl = false;

		for (PC p : pcAllList) {
			if (pcIdx == p.getPcIdx()) {
				System.out.print(p.getPcName() + " : ");
				for (int i = 0; i < p.getSeatList().size(); i++) {
					String sl = p.getSeatList().get(i).equals("-") ? ("[" + (i + 1) + "] ") : "[X] ";
					System.out.print(sl);
				}
				bl = true;
			}
		}

		if (!bl) {
			throw new IndexNotFoundException("PC방 좌석 조회", pcIdx);
		}

		System.out.println();
	}

	// 기능 1-2
	// 입력 받은 PC방 index와 일치하는 잔여 좌석 조회 및 좌석 예약
	@Override
	public void reserveSeat() throws IndexNotFoundException {
		scanner = new Scanner(System.in);

		// 기능 1 호출
		if (search()) {
			return;
		}
		
		// 예약할 PC방 입력
		System.out.println("예약할 PC방 번호를 입력해 주세요. (종료 : q)");
		System.out.print("> ");
		String inputPCIdx = scanner.nextLine();
		if (inputString(inputPCIdx)) {
			return;
		}

		int pcIdx = parseInt(inputPCIdx);

		PC p = getPC(pcIdx);

		// 옵션 1 호출
		if (p.getPcIdx() == pcIdx) {
			about(pcIdx);
		}

		System.out.println("좌석 번호를 입력해 주세요. (종료 : q)");
		System.out.print("> ");
		String in = scanner.nextLine();
		if (inputString(in)) {
			return;
		}

		int si = Integer.parseInt(in); // 좌석 번호
		
		if (!(si >= 1 && si <= p.getSeatList().size())) {
			System.out.println("좌석 번호가 없습니다.");
			return;
		}

		if (p.getPcIdx() == pcIdx) {
			if (p.getSeatList().get(si - 1).equals("-")) {
				p.getSeatList().set(si - 1, userID);

				int idx = 1;
				if (!(res == null || res.size() == 0)) {
					idx = res.get(res.size() - 1).getIdx() + 1; // 예약 번호 생성
				}

				PCReserve pc = new PCReserve(idx, userID, pcIdx, si, null, p.getPrice());
				res.add(pc);

				System.out.println("좌석 예약이 완료되었습니다.");
				System.out.println("기본금은 " + p.getPrice() + "원입니다. 추가 요금 발생 시 현장 결제 부탁드립니다.");
			} else {
				System.out.println("이미 예약된 좌석입니다.");
				throw new IndexNotFoundException("PC방 예약", pcIdx);
			}
		}
		System.out.println();
		
		//pcAllListSave();
		//reserveSave();
	}

	// 기능 2
	// 예약 취소 + 만약 푸드 구매를 했으면 그 푸드 목록 PC방 재고에 반환
	@Override
	public void reserveSeatCancel() {
		scanner = new Scanner(System.in);

		// 예약 좌석 취소와 가격 환불
		if (reserveList()) {
			return;
		}

		System.out.println("취소할 PC방 예약 번호를 입력해 주세요. (종료 : q)");
		System.out.print("> ");
		String cancelResvIdx = scanner.nextLine();
		if (inputString(cancelResvIdx)) {
			return;
		}
		int resvIdx = parseInt(cancelResvIdx);
		PCReserve reserve = getPCReserve(resvIdx);

		// if (reserve == null) {
		// System.out.println("예약 정보가 없습니다."); // << 예외 처리 포인트
		// return;
		// }

		PC currentPc = getPC(reserve.getPcIdx()); // PC방 정보
		for (int i = 0; i < res.size(); i++) {
			if (resvIdx == res.get(i).getIdx()) {
				currentPc.getSeatList().set(reserve.getSeat()-1, "-");
				emptyFood(res.get(i).getIdx()); // 좌석 취소되면 푸드 내역도 같이 수정
				res.remove(i);
				System.out.println("예약이 취소되었습니다.");
				return;
			}
		}
		//pcAllListSave();
		//reserveSave();
	}
	
	// 옵션 2
	// 구매했던 푸드 수량을 PC방 재고에 반환
	public void emptyFood(int no) {
		PCReserve p = getPCReserve(no);
		PC pc = getPC(p.getPcIdx());
		List<Food> userFoods = p.getFoods();
		List<Food> pcFoods = pc.getInventories();
		
		if(userFoods == null) {
			return;
		}
		
		for(Food f : userFoods) {
			for (Food c : pcFoods) {
				if(c.getIdx() == f.getIdx()) {
					c.setAmount(c.getAmount() +f.getAmount());
				}
			}
		}
		//pcAllListSave();
		//reserveSave();
	}

	// 기능 3-1
	// 좌석을 예약해야지만 푸드 구매 가능
	// 입력 받은 예약 번호로 그 PC방 index에 해당하는 메뉴 출력
	@Override
	public void reserveFood() throws IndexNotFoundException {
		scanner = new Scanner(System.in);
		
		// 1. res 안에 예약에 대한 정보가 있어야 구매, 수정
		if (reserveList()) {
			return;
		}
		
		boolean bl = false;
		
		System.out.println("예약 번호를 입력해 주세요. (종료 : q)");
		System.out.print("> ");
		String input = scanner.nextLine();
		if (inputString(input)) {
			return;
		}
		int resvIdx = parseInt(input);
		PCReserve reserve = getPCReserve(resvIdx);

		printFoodList(reserve.getPcIdx());

		// 2. 수량 수정이나 삭제 s
		System.out.println("구매할 품목 번호를 입력해 주세요. (메뉴 취소 : s , 종료 : q)");
		System.out.print("> ");
		input = scanner.nextLine();
		if (inputString(input, resvIdx)) {
			return;
		}
		int foodIdx = Integer.parseInt(input);
		// 3-1. inventory -- // 장바구니 ++ // sum //
		List<Food> list = getPC(reserve.getPcIdx()).getInventories();

		for (Food c : list) { // c == getFood(foodIdx)
			if (foodIdx == c.getIdx()) {
				List<Food> foodList = reserve.getFoods();
				Food foodClone = getFood(c.getIdx(), list).clone(); // 복제
				// Food foodFromInventory = getFood(foodIdx); // PC방 푸드
				Food foodFromList = getFood(foodIdx, foodList); // 예약 푸드

				if (c.getAmount() == 0) {
					System.out.println("이 항목은 현재 품절입니다.");
					bl = false; // ?
					break;
				}

				if (foodList == null) { // 신규 주문
					foodList = new ArrayList<>();
				}
				if (foodFromList == null || c.getIdx() != foodFromList.getIdx()) { // 기존 주문 다른 푸드 추가
					foodClone.setAmount(1);
					foodList.add(foodClone); // 리스트에 추가
				} else { // 기존 주문 같은 푸드 추가
					foodFromList.increAmount();
				}

				c.decreAmount(); // 재고 수량 감소
				reserve.setMoney(reserve.getMoney() + c.getPrice()); // 푸드 가격을 예약 객체에 추가
				reserve.setFoods(foodList); // 장바구니에 해당 객체 담기
				System.out.println("구매가 완료되었습니다.");
				bl = true;
				
				break;
			}
		}
		if (!bl) {
			throw new IndexNotFoundException("구매할 품목 번호", foodIdx);
		}
		
		//pcAllListSave();
		//reserveSave();
	}

	// 기능 3-2
	// 푸드 수량 하나씩 수정될 메서드
	@Override
	public void reserveFoodModify(int no) {
		scanner = new Scanner(System.in);
		PCReserve reserve = getPCReserve(no);

		List<Food> list = reserve.getFoods();
		list.forEach(System.out::println);

		System.out.println("취소할 품목을 입력해 주세요. (수정 완료 : q)");
		System.out.print("> ");
		String input = scanner.nextLine();
		if (inputString(input)) {
			return;
		}

		int foodIdx = parseInt(input);
		
		Food f = getFood(foodIdx, list); // 사용자가 가지고 있는 푸드
		f.decreAmount(); // 사용자의 항목에서 제거
		reserve.setMoney(reserve.getMoney()-f.getPrice()); // 예약 금액 감소 조정
		
		PC p = getPC(reserve.getPcIdx()); // 예약된 pc방 정보 가져오기
		Food c = getFood(foodIdx, p.getInventories()); // pc방의 재고에 있는 푸드 한 객체 
		c.increAmount(); // 수량 증가
		
		// 사용자의 푸드 주문 목록 조정
		list = reserve.getFoods();
		for(int i = 0 ; i < list.size() ; i++) {
			if(list.get(i).getAmount() == 0) {
				list.remove(i);
			}
		}
		
		// 최종적으로 주문 목록이 없을 때 null로 전환
		if(list.size() == 0) {
			reserve.setFoods(null);
		}
		// 예외 상황 발생 가능(food index)

		System.out.println("취소되었습니다.");
		
		//pcAllListSave();
		//reserveSave();
	}

	// 기능 4
	// 총 예약 리스트
	@Override
	public boolean reserveList() {
		boolean b = false;

		System.out.println("[ \" " + userID + " \" 님의 예약 정보 ]");
		for (int i = 0; i < res.size(); i++) {
			// ID에 해당하는 좌석
			if (res.get(i).getUserID().equals(userID)) {
				PC p = getPC(res.get(i).getPcIdx());
				String str = "";
				str += "" + res.get(i).getIdx() + ". :: " + p.getPcName() + " :: " + p.getPcLoc() + "점 :: "
						+ res.get(i).getSeat() + "번 좌석 :: " + p.getPrice() + "원 ::";
				if (res.get(i).getFoods() != null) {
					str += res.get(i).getFoods();
				}

				System.out.print(str);
				System.out.println(" $ 총액 : " + res.get(i).getMoney() + "원 $");

				b = true;
			}
		}

		if (!b) {
			System.out.println("예약 내역이 없습니다.");
			return true;
		}
		//pcAllListSave();
		//reserveSave();
		
		return false;
	}

	// 옵션
	// PC방 index에 해당하는 푸드 리스트 출력
	public void printFoodList(int idx) {
		for (PC p : pcAllList) {
			if (idx == p.getPcIdx()) {
				System.out.println("[ " + p.getPcName() + " PC방의 푸드 리스트 ]");
				List<Food> fc = p.getInventories();
				for (Food f : fc) {
					System.out.println(f);
				}

				System.out.println();
			}
		}
	}

	// q를 입력했을 경우
	public boolean inputString(String in) {
		if (in.equals("q")) {
			return true;
		}
		return false;
	}

	// q를 입력했을 경우
	// s를 입력했을 경우 reserveFoodModify() 호출
	// 오버 로딩
	public boolean inputString(String in, int no) {
		if (in.equals("q")) {
			return true;
		} else if (in.equals("s")) {
			reserveFoodModify(no);
			return true;
		}
		return false;
	}

	// 예약한 PC방 리스트에서 idx와 일치하는 예약 번호에 해당하는 객체 뽑고 리턴
	public PCReserve getPCReserve(int idx) {
		for (PCReserve e : res) {
			if (e.getIdx() == idx) {
				return e;
			}
		}
		return null;
	}

	// 전체 PC방 리스트에서 idx와 일치하는 각 PC방의 PC방 위치 index에 해당하는 객체 뽑고 리턴
	public PC getPCLoc(int idx) {
		for (PC p : pcAllList) {
			if (idx == p.getPcLocIdx()) {
				return p;
			}
		}
		return null;
	}

	// 전체 PC방 리스트에서 idx와 일치하는 각 PC방의 PC방 index에 해당하는 객체 뽑고 리턴
	public PC getPC(int idx) {
		for (PC p : pcAllList) {
			if (idx == p.getPcIdx()) {
				return p;
			}
		}
		return null;
	}

	// 전체 PC방 리스트에서 str과 일치하는 각 PC방의 PC방 이름에 해당하는 객체 뽑고 리턴
	// 오버 로딩
	public PC getPC(String str) {
		for (PC p : pcAllList) {
			if (str.equals(p.getPcName())) {
				return p;
			}
		}
		return null;
	}

	// list가 null이면 리턴 null
	// 전체 푸드 리스트에서 idx와 일치하는 각 푸드의 푸드 index에 해당하는 객체 뽑고 리턴
	public Food getFood(int idx, List<Food> list) {
		if (list == null)
			return null;
		for (Food f : list) {
			if (idx == f.getIdx()) {
				return f;
			}
		}
		return null;
	}
	
	// dummy data
	public void init() {
		List<Food> inventories = new ArrayList<>();

		List<String> seatList = new ArrayList<>();
		inventories.add(new Food(1, "햄버거", 2, 3000));
		inventories.add(new Food(2, "핫바", 3, 2000));
		inventories.add(new Food(3, "콜라", 4, 1000));
		inventories.add(new Food(4, "라면", 5, 3500));

		for (int i = 0; i < 8; i++) {
			seatList.add("-");
		}
		pcAllList.add(new PC(1, SubwayInfo.HWAGOKIdx, SubwayInfo.HWAGOK, "D", 2000, seatList, inventories));

		inventories = new ArrayList<>();
		inventories.add(new Food(1001, "불닭볶음면", 2, 3000));
		inventories.add(new Food(1002, "오징어집", 3, 2000));
		inventories.add(new Food(1003, "사이다", 5, 3500));

		seatList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			seatList.add("-");
		}
		pcAllList.add(new PC(2, SubwayInfo.APGUJEONGIdx, SubwayInfo.APGUJEONG, "E", 2500, seatList, inventories));

		inventories = new ArrayList<>();
		inventories.add(new Food(100, "초콜릿", 2, 3000));
		inventories.add(new Food(101, "우유", 5, 3500));

		seatList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			seatList.add("-");
		}
		pcAllList.add(new PC(3, SubwayInfo.HWAGOKIdx, SubwayInfo.HWAGOK, "F", 3000, seatList, inventories));

		inventories = new ArrayList<>();
		inventories.add(new Food(1, "냉면", 0, 5000));
		inventories.add(new Food(2, "핫바", 5, 1500));
		inventories.add(new Food(3, "라면", 3, 2000));
		inventories.add(new Food(4, "사이다", 5, 2000));
		inventories.add(new Food(5, "아메리카노", 5, 2500));

		seatList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			seatList.add("-");
		}
		pcAllList.add(new PC(4, SubwayInfo.BUCHEONIdx, SubwayInfo.BUCHEON, "B", 5000, seatList, inventories));

		//pcAllListSave();
	}
	
	// ID 입력 값 제약 조건
	// 소문자 a-z 대문자 A-Z 숫자 0-9 1자리부터 8자리까지
	private void validateString(String msg, String target) throws NameException {
		if (!target.matches("^(?=.*[a-zA-Z]+)(?=.*[0-9]+).{1,8}$")) {
			throw new NameException(msg, target);
		}
	}

	// ↓ 파일 입출력 ↓
	private void pcAllListSave() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("pcAllList.txt"));
			oos.writeObject(pcAllList);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void reserveSave() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("reserve.txt"));
			oos.writeObject(res);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void pcAllListLoad() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("pcAllList.txt"));
			pcAllList = (List<PC>) ois.readObject();
		} catch (FileNotFoundException e) {
			pcAllList = new ArrayList<>();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void reserveLoad() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("reserve.txt"));
			res = (List<PCReserve>) ois.readObject();
		} catch (FileNotFoundException e) {
			res = new ArrayList<>();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}