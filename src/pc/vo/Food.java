package pc.vo;


public class Food implements Cloneable, java.io.Serializable {

	private int idx;
	private String food;
	private int price;
	private int amount; // 재고값

	public Food() {}
	

	public Food(int idx, String food, int amount, int price) {
		super();
		this.idx = idx;
		this.food = food;
		this.amount = amount;
		this.price = price;
	}


	public String getFood() {
		return food;
	}

	public void setFood(String food) {
		this.food = food;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void increAmount() {
		amount++;
	}
	
	public void decreAmount() {
		amount--;
	}
	@Override
	public String toString() {
		return idx + ". " + food + "\t" + price + "원" + "\t수량 : " + amount;
	}


	@Override
	public Food clone() {
		Food f = null;
		try {
			f = (Food)super.clone();
		}catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return f;
	}
	
	
	
}
