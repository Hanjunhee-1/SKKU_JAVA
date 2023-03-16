package PA1;
import java.awt.geom.IllegalPathStateException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BlackJack {

	static void printFirstStatus(House house, Player player, ArrayList<Computer> computers, int capacity) {
		house.firstShowMyDeck();
		System.out.println(player.returnMyDeck(0)); 	// 아무 값이나 줘도 상관없음.
		
		// capacity 가 2 이상이라는 것은 사용자를 제외한 다른 컴퓨터가 존재하는 것.
		if (capacity >= 2) {
			for (int i=0; i<capacity-1; i++) {
				System.out.println(computers.get(i).returnMyDeck(i));
			}
		}
	}
	
	static void printResult(House house, Player player, ArrayList<Computer> computers, int capacity, boolean isImmediately) {
		String houseResult = new String();
		String playerResult = new String();
		ArrayList<String> computerResult = new ArrayList<>();
		
		// House 가 바로 21 을 만들어서 게임이 끝나는 경우
		if (isImmediately) {
			houseResult += house.returnMyDeck(0);
			playerResult += "[Lose] " + player.returnMyDeck(0);
			if (capacity >= 2) {
				for (int i=0; i<computers.size(); i++) {
					computerResult.add("[Lose] " + computers.get(i).returnMyDeck(i));
				}
			}
			
			System.out.println("--- Game Results ---");
			System.out.println(houseResult);
			System.out.println(playerResult);
			if (capacity >= 2) {
				for (int i=0; i<computers.size(); i++) {
					System.out.println(computerResult.get(i));
				}
			}
		}
		if (house.isBusted) {
			houseResult += house.returnMyDeck(0) + " - Bust!";
			
			// 사용자에 대한 문구
			if (player.isBusted) {
				// 사용자가 Bust 상태일때
				playerResult += "[Lose] " + player.returnMyDeck(0) + " - Bust!";
			} else {
				playerResult += "[Win]  " + player.returnMyDeck(0);
			}
			
			// 컴퓨터에 대한 문구
			if (capacity >= 2) {
				for (int i=0; i<computers.size(); i++) {
					if (computers.get(i).isBusted) {
						// 컴퓨터가 Bust 상태일때
						computerResult.add("[Lose] " + computers.get(i).returnMyDeck(i) + " - Bust!");
					} else {
						computerResult.add("[Win]  " + computers.get(i).returnMyDeck(i));
					}
				}
			}
		} else {
			houseResult += house.returnMyDeck(0);
			
			// 사용자에 대한 문구
			if (player.isBusted) {
				// 사용자가 Bust 상태일때
				playerResult += "[Lose] " + player.returnMyDeck(0) + " - Bust!";
			} else {
				if (player.getTotalValue() > house.getTotalValue()) {
					playerResult += "[Win]  " + player.returnMyDeck(0);
				} else if (player.getTotalValue() < house.getTotalValue()) {
					playerResult += "[Lose] " + player.returnMyDeck(0);
				} else {
					playerResult += "[Draw] " + player.returnMyDeck(0);
				}
			}
			
			// 컴퓨터에 대한 문구
			if (capacity >= 2) {
				for (int i=0; i<computers.size(); i++) {
					if (computers.get(i).isBusted) {
						computerResult.add("[Lose] " + computers.get(i).returnMyDeck(i) + " - Bust!");
					} else {
						if (computers.get(i).getTotalValue() > house.getTotalValue()) {
							computerResult.add("[Win]  " + computers.get(i).returnMyDeck(i));
						} else if (computers.get(i).getTotalValue() < house.getTotalValue()) {
							computerResult.add("[Lose] " + computers.get(i).returnMyDeck(i));
						} else {
							computerResult.add("[Draw] " + computers.get(i).returnMyDeck(i));
						}
					}
				}
			}
		}
		
		if (!isImmediately) {
			System.out.println("--- Game Results ---");
			System.out.println(houseResult);
			System.out.println(playerResult);
			if (capacity >= 2) {
				for (int i=0; i<computers.size(); i++) {
					System.out.println(computerResult.get(i));
				}
			}
		}
	}

	public static void main(String[] args) {
		int seed = Integer.parseInt(args[0]);		// 카드 갯수(52)
		int players = Integer.parseInt(args[1]);	// 사람 수 (min: 1, max: 5)
		Scanner sc = new Scanner(System.in);
		
		if (players > 5) {
			System.out.println("Only up to 5 users are allowed.");
			sc.close();
			return;
		} else if (players < 1) {
			System.out.println("Requires at least 1 user");
			sc.close();
			return;
		}
		Deck deck = new Deck();
		deck.shuffle(seed);
		
		// 사용자와 함께 플레이하는 컴퓨터들
		// 사람 수를 1로 입력받았다면 해당 list 는 사용하지 않음.
		// 비어있는 list 의 size 는 항상 0.
		ArrayList<Computer> computers = new ArrayList<>();
		
		// 기본적으로 존재하는 실제 사용자.
		Player player1 = new Player(deck.dealCard());	// 기본 카드 1장
		
		// 사람 수를 5로 입력받았다면 그것은 실제 플레이를 하는 본인을 포함한 5명임.
		if (players >= 2) {
			int realCapacity = players - 1;
			for (int i=0; i<realCapacity; i++) {
				Computer c = new Computer(deck.dealCard());	// 기본 카드 1장
				computers.add(c);
			}
		}
		
		// 기본적으로 존재하는 House.
		House house = new House(deck.dealCard());	// 기본 카드 1장
		
		// Player1 -> Player2 -> ... -> House 순으로 1장씩 가졌으니 다시 이 순서로 1장씩 가지기
		player1.addCard(deck.dealCard());	// Player1 먼저 주기
		if (players >= 2) {
			for (int i=0; i<computers.size(); i++) {
				computers.get(i).addCard(deck.dealCard());	// Player2 부터 차례대로 1장씩 추가
			}
		}
		house.addCard(deck.dealCard());		// house 도 마저 1장 가지기
		
		// house 가 처음 2장의 카드에서 블랙잭을 성공시켰다면 house 의 승리로 즉시 게임 종료
		if (house.getTotalValue() == 21) {
			printResult(house, player1, computers, players, true);
			sc.close();
			return;
		}

		// House 를 포함한 모든 플레이어의 첫 덱 상태 출력
		printFirstStatus(house, player1, computers, players);
		System.out.println();
		System.out.println("--- Player1 turn ---");

		// Bust 가 아니거나 Stand 가 아닐 경우 무한반복
		while (true) {
			System.out.println(player1.returnMyDeck(0));
			String choice = sc.nextLine();
			if (choice.equals("Stand")) {
				break;
			} else if (choice.equals("Hit")) {
				player1.addCard(deck.dealCard());
			} else {
				System.out.println("Please enter only between \"Hit\" and \"Stand\". ");
			}
			
			if (player1.getTotalValue() > 21) {
				System.out.println(player1.returnMyDeck(0) + " - Bust!");
				player1.isBusted = true;
				break;
			}
		}
		System.out.println();	// 형식상 개행
		
		// 컴퓨터 플레이어가 존재한다면
		if (computers.size() != 0) {
			
			// 존재하는 컴퓨터 플레이어 수 만큼 반복
			for (int i=0; i<computers.size(); i++) {
				
				// 정보 출력해주기
				System.out.println("--- Player" + Integer.toString(i+2) + " turn ---");
				
				// Bust 가 아니거나 Stand 가 아닐 경우 무한반복
				while (true) {
					System.out.println(computers.get(i).returnMyDeck(i));
					String choice = computers.get(i).decideAction();
					System.out.println(choice);
					if (choice.equals("Stand")) {
						break;
					} else if (choice.equals("Hit")) {
						computers.get(i).addCard(deck.dealCard());
					} else {
						System.out.println("Please enter only between \"Hit\" and \"Stand\". ");
					}
					
					if (computers.get(i).getTotalValue() > 21) {
						System.out.println(computers.get(i).returnMyDeck(i) + " - Bust!");
						computers.get(i).isBusted = true;
						break;
					}
				}
				
				System.out.println();	// 형식상 개행
			}
		}
		
		System.out.println("--- House turn ---");
		
		while(true) {
			System.out.println(house.returnMyDeck(0));
			String choice = house.decideAction();
			System.out.println(choice);
			if (choice.equals("Stand")) {
				break;
			} else if (choice.equals("Hit")) {
				house.addCard(deck.dealCard());
			} else {
				System.out.println("Please enter only between \"Hit\" and \"Stand\". ");
			}
			
			if (house.getTotalValue() > 21) {
				System.out.println(house.returnMyDeck(0) + " - Bust!");
				house.isBusted = true;
				break;
			}
		}
		System.out.println();
		
		printResult(house, player1, computers, players, false);
		sc.close();
		return;
	}

}


class Card {
	int theValue;		// 카드 정보 중에 숫자. 예를 들어 다이아몬드 Ace 라면 1 이 저장됨.
	String theSuit;		// 카드 정보 중에 무늬. 예를 들어 다이아몬드 Ace 라면 "d" 가 저장됨.
	public Card() {}	// 기본 생성자.
	
	// 생성시 카드의 숫자와 무늬 정보를 저장.
	public Card(int theValue, String theSuit) {
		this.theValue = theValue;
		this.theSuit = theSuit;
	}
}

class Deck {
	private Card[] deck;		// 카드 정보를 담고 있는 배열
	private int cardsUsed;		// 카드가 총 몇 개 사용되었는지
	
	// 덱 생성
	public Deck() {
		deck = new Card[52];
		String[] suits = {"c", "h", "d", "s"};
		int count = 0;
		for (int i=0; i<suits.length; i++) {
			for (int j=1; j<=13; j++) {
				
				// 11 = J, 12 = Q, 13 = K 로 두고 나중에 변환
				Card card = new Card(j, suits[i]);
				deck[count] = card;
				count++;
			}
		}
	}
	
	// seed 값에 따라 카드를 섞어주는 함수.
	public void shuffle(int seed) {
		Random random = new Random(seed);
		
		for (int i=deck.length-1; i>0; i--) {
			int rand = (int)(random.nextInt(i+1));
			Card temp = deck[i];
			deck[i] = deck[rand];
			deck[rand] = temp;
		}
		cardsUsed = 0;
	}
	
	
	// 카드 나눠주는 함수.
	public Card dealCard() {
		if (cardsUsed == deck.length)
			throw new IllegalPathStateException("No cards are left in the deck.");
		cardsUsed++;
		return deck[cardsUsed - 1];
	}
}

class Hand {
	
	// 죽었는지 살았는지 판별하는 변수
	boolean isBusted = false;

	// 자신이 갖고 있는 카드 정보를 저장할 변수.
	ArrayList<Card> myDeck = new ArrayList<>();
	
	public void addCard(Card c) {
		// 카드를 받아 myDeck 에 추가
		myDeck.add(c);
	}
	
	public int getTotalValue() {
		// myDeck 에서 카드 정보 중 숫자의 총 합을 반환. Ace 는 1 또는 11 인데 기본은 11.
		// 만약 손에 있는 카드의 합이 21 을 초과하면 에이스 카드는 1로 계산.
		int sum = 0;
		
		// 특정 상황을 위한 Ace 카운터.
		int aceCount = 0;
		for (int i=0; i<myDeck.size(); i++) {
			if (myDeck.get(i).theValue >= 10) {
				// 10보다 크거나 같은 경우는 10,J,Q,K 밖에 없는데 
				// 그냥 10으로 통일해서 점수추가
				sum += 10;
			} else if (myDeck.get(i).theValue == 1) {
				// Ace 는 기본적으로 11로 계산
				sum += 11;
				aceCount++;
			} else {
				sum += myDeck.get(i).theValue;
			}
		}
		
		// 총합이 21 을 넘고 aceCount 가 한번이라도 나왔다면
		if (sum > 21 && aceCount > 0) {
			if (sum / 11 == aceCount) {
				// 흔한 상황은 아니지만 Ace 만 나온 상황에는
				// 하나는 11로 쳐주고 나머지는 1점으로 쳐야함.
				sum -= 10*(aceCount-1);
			} else {
				// Ace 만 나온 상황이 아닌데 총점이 21 점을 넘길경우에는
				// 모든 Ace 를 1점으로 쳐야함.
				sum -= 10*aceCount;
			}
		}
		return sum;
	}
	
	public String returnMyDeck(int index) {
		// 자식 클래스에서 오버라이딩하기.
		return "";
	}
	
	public String convert(int theValue, String theSuit, boolean isTotal, int total) {
		// 스페이드 5 라면 5s 라고 합쳐서 하나의 문자열로 반환해줌.
		// 만약 isTotal 이 true 라면 합계 점수에 대한 것을 (합계점수) 해당 포맷으로 맞추어 반환해줌.
		// 따로 로직에서 구현해줘도 되지만 자주 사용할 것 같아서 부모클래스의 함수로 생성.
		String convertResult = new String();
		if (!isTotal) {
			if (theValue == 11) {
				// 11 은 J 로 취급
				convertResult = (char)(theValue+63) + theSuit;
			} else if (theValue == 12) {
				// 12 는 Q 로 취급
				convertResult = (char)(theValue+69) + theSuit;
			} else if (theValue == 13) {
				// 13 은 K 로 취급
				convertResult = (char)(theValue+62) + theSuit;
			} else if (theValue == 1) {
				// 1 은 A 로 취급
				convertResult = (char)(theValue+64) + theSuit;
			} else {
				convertResult = Integer.toString(theValue) + theSuit;
			}
		} else {
			convertResult = "(" + Integer.toString(total) + ")";
		}
		
		return convertResult;
	}
}

class Computer extends Hand {
	
	// 기본 생성자
	public Computer() {}
	
	// 생성될때 카드 1장 받음
	public Computer(Card c1) {
		this.addCard(c1);
	}
	
	// Hit 할지 Stand 할지 정하는 것.
	public String decideAction() {
		if (this.getTotalValue() < 14) {
			return "Hit";
		} else if (this.getTotalValue() > 17) {
			return "Stand";
		} else {
			Random random = new Random();
			int isHit = (int)(random.nextInt(2));
			if (isHit == 1) {
				return "Hit";
			} else {
				return "Stand";
			}
		}
	}
	
	
	// myDeck 의 모든 카드 정보 보여주기
	@Override
	public String returnMyDeck(int index) {
		// +2 의 의미는 main() 함수에서 넘겨줄때 사용하기 위함.
		String stringIndex = Integer.toString(index+2);
		String result = new String("Player" + stringIndex + ": ");
		int i = 0;
		
		// myDeck 의 사이즈가 2일때
		// 해당 for 문을 한 번만 돌고 i 는 1로 끝남
		for (i=0; i<this.myDeck.size()-1; i++) {
			result += this.convert(
						this.myDeck.get(i).theValue, 
						this.myDeck.get(i).theSuit, 
						false, 
						0	// 어처피 total 값을 위한 convert 가 아니기 때문에 아무 값이나 넣음
					) + ", ";
		}
		
		// myDeck 에서 맨 마지막 카드 정보는 "," 가 아닌 " " 로 끝남.
		result += this.convert(
				this.myDeck.get(i).theValue, 
				this.myDeck.get(i).theSuit, 
				false, 
				0
			) + " ";
		
		// 현재 점수 합계도 포함. 
		result += this.convert(
				0, 
				"", 
				true, 
				this.getTotalValue()
			);
		
		return result;
	}
}

class Player extends Hand {
	
	// 기본 생성자
	public Player() {}
	
	// 생성될때 카드 2장 받음
	public Player(Card c1) {
		this.addCard(c1);
	}
	
	// myDeck 의 모든 카드 정보 보여주기
	// Player 는 index 값 필요없음.
	@Override
	public String returnMyDeck(int index) {
		String result = new String("Player1: ");
		int i = 0;
		
		// myDeck 의 사이즈가 2일때
		// 해당 for 문을 한 번만 돌고 i 는 1로 끝남
		for (i=0; i<this.myDeck.size()-1; i++) {
			result += this.convert(
						this.myDeck.get(i).theValue, 
						this.myDeck.get(i).theSuit, 
						false, 
						0	// 어처피 total 값을 위한 convert 가 아니기 때문에 아무 값이나 넣음
					) + ", ";
		}
		
		// myDeck 에서 맨 마지막 카드 정보는 "," 가 아닌 " " 로 끝남.
		result += this.convert(
				this.myDeck.get(i).theValue, 
				this.myDeck.get(i).theSuit, 
				false, 
				0
			) + " ";
		
		// 현재 점수 합계도 포함. 
		result += this.convert(
				0, 
				"", 
				true, 
				this.getTotalValue()
			);
		
		return result;
	}
}

class House extends Hand {
	// 기본 생성자
	public House() {}
	
	// 생성될때 카드 2장 받음
	public House(Card c1) {
		this.addCard(c1);
	}
	
	// Hit 할지 Stand 할지 정하는 것.
	public String decideAction() {
		if (this.getTotalValue() < 14) {
			return "Hit";
		} else if (this.getTotalValue() > 17) {
			return "Stand";
		} else {
			Random random = new Random();
			int isHit = (int)(random.nextInt(2));
			if (isHit == 1) {
				return "Hit";
			} else {
				return "Stand";
			}
		}
	}
	
	// myDeck 의 모든 카드 정보 보여주기
	// House 는 index 값이 필요없긴한데...
	// House 는 맨 처음 시작할 때 첫번째 카드 정보와 합계를 표시하지 않음. --> 참고하기
	@Override
	public String returnMyDeck(int index) {
		String result = new String("House: ");
		int i = 0;
		
		// myDeck 의 사이즈가 2일때
		// 해당 for 문을 한 번만 돌고 i 는 1로 끝남
		for (i=0; i<this.myDeck.size()-1; i++) {
			result += this.convert(
						this.myDeck.get(i).theValue, 
						this.myDeck.get(i).theSuit, 
						false, 
						0	// 어처피 total 값을 위한 convert 가 아니기 때문에 아무 값이나 넣음
					) + ", ";
		}
		
		// myDeck 에서 맨 마지막 카드 정보는 "," 가 아닌 " " 로 끝남.
		result += this.convert(
				this.myDeck.get(i).theValue, 
				this.myDeck.get(i).theSuit, 
				false, 
				0
			) + " ";
		
		// 현재 점수 합계도 포함. 
		result += this.convert(
				0, 
				"", 
				true, 
				this.getTotalValue()
			);
		
		return result;
	}
	
	
	// 게임이 시작되고 House 의 처음 Deck 상태를 위한 함수
	public void firstShowMyDeck() {
		
		// 첫 카드는 무조건 HIDDEN.
		String result = new String("House: HIDDEN, ");
		int i = 0;

		// 두 번째 카드는 보여줌
		result += this.convert(
				this.myDeck.get(1).theValue, 
				this.myDeck.get(i).theSuit, 
				false, 
				0	// 아무 값이나 상관없음.
			);
		System.out.println(result);
	}
}