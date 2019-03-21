package pc.test;

import java.util.Scanner;

import pc.service.PCServiceImple;
import pc.service.exception.*;

public class PCTest {
	public static void main(String[] args) {
		boolean flag = true;
		Scanner sc = new Scanner(System.in);
		PCServiceImple ps = new PCServiceImple();

		System.out.println(":::: PC방 예약 프로그램 ::::");
		System.out.println();

		boolean b = false;
		
		// ID 입력
		while(!b) { // ID 입력이 잘못되면 계속해서 입력 받기
			try {
				ps.inputID();
				b = true;
			} catch (NameException e) {
				System.out.println(e.getMessage());
			}
		}

		// 매치 비교
		while (flag) {
			System.out.println("1. 예약 \t 2. 예약 취소 \t 3. 푸드 구매 및 취소 \t 4. 예약 리스트 \t 5. 종료");
			try {
				String input = sc.nextLine();
				int i = Integer.parseInt(input);

				switch (i) {
				case 1:
					ps.reserveSeat();
					break;
				case 2:
					ps.reserveSeatCancel();
					break;
				case 3:
					ps.reserveFood();
					break;
				case 4:
					ps.reserveList();
					break;
				case 5:
					System.out.println("이용해 주셔서 감사합니다.");
					flag = false;
					break;

				default:
					System.out.println("1 ~ 5 사이의 숫자를 입력해 주세요.");
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("목록에 해당하는 항목을 <숫자>로만 입력해 주세요.");
			} catch (NullPointerException e) {
				System.out.println("목록에 해당하는 항목을 <숫자>로만 입력해 주세요.");
			} catch (IndexNotFoundException e) {
				System.out.println(e.getMessage());
			} catch (IndexOutOfBoundsException e) {
				System.out.println("범위를 벗어남");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
