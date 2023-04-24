package PA2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

/**
 * 
 * @author 2022310288_Hanjunhee
 * 
 * Aircraft (size: 6, number: 1)
 * 
 * BattleShip (size: 4, number: 2)
 * 
 * Submarine (size: 3, number: 2)
 * 
 * Destroyer (size: 3, number: 1)
 * 
 * Patrol Boat (size: 2, number: 4)
 * 
 */
public class Battleship {
	
	// 게임을 진행할 테이블 생성 및 빈칸으로 초기화
	static String [][] gameBoard = new String[10][10];
	
	// 폭탄 개수
	static int bomb = 0;
	
	// mode 정보 - ([d, D]: Debug, [r, R]: Release)
	static String mode = new String("");
	
	// input 파일 이름
	static String fileName = new String("");

	// 나중에 점수 계산할 때 사용할 변수
	static ArrayList<Ship> shipScore = new ArrayList<Ship>();
	
	// memoization 에 사용
	static int[][] memo = new int[10][10];
	
	public static void main(String[] args) throws BombInputException, ModeInputException, HitException {
		
		// 현재 디렉토리 경로
		String path = System.getProperty("user.dir");
		
		Scanner scan = new Scanner(System.in);
		
		// 각 배에 대한 객체 생성
		AircraftCarrier a = new AircraftCarrier("A", 6);
		Battle b = new Battle("B", 4);
		Submarine s = new Submarine("S", 3);
		Destroyer d = new Destroyer("D", 3);
		PatrolBoat p = new PatrolBoat("P", 2);
		
		// 객체들을 저장해놓을 ArrayList 생성.
		// ships 변수는 input 파일이 없을 경우 무작위로 배를 배치할 때만 쓰임.
		ArrayList<Ship> ships = new ArrayList<Ship>();
		
		// 실제 필요한 배의 갯수만큼 저장.
		ships.add(a); ships.add(b); ships.add(b); ships.add(s); ships.add(s);
		ships.add(d); ships.add(p); ships.add(p); ships.add(p); ships.add(p);
		
		
		for (int i=0; i<10; i++) {
			for (int j=0; j<10; j++) {
				gameBoard[i][j] = " ";
			}
		}
		
		try {
			String userInput = scan.nextLine();
			
			// 나중에 gameBoard 에 값을 넣을 때 사용할 위치용 변수.
			int x=0, y=0;
			
			// 사용자에게 입력받은 숫자중 폭탄 개수 부분만 따로 추출
			bomb = Integer.parseInt(userInput.substring(0, userInput.indexOf(" ")));
			if (bomb < 0) {
				throw new BombInputException("BombInputException");
			}
			
			// 폭탄 개수 부분을 제외한 mode 와 fileName 이 담겨있는 문자열만을 남겨둠.
			userInput = userInput.substring(userInput.indexOf(" ") + 1);
			
			// mode 부분만 따로 추출.
			mode = userInput.substring(0, userInput.indexOf(" "));
			if (!(mode.equals("r") || mode.equals("R") || mode.equals("d") || mode.equals("D"))) {
				throw new ModeInputException("ModeInputException");
			}
			
			// 폭탄 개수, mode 부분을 제외한 나머지 부분은 모두 파일이름
			fileName = userInput.substring(userInput.indexOf(" ") + 1);
			

			// 파일을 입력받았을 때 주어진 파일이 존재할 경우 try 문으로 
			// 존재하지 않을 경우 catch 문으로
			try {
				
				FileInputStream fis = new FileInputStream(path + "\\" + fileName);
				InputStreamReader isr = new InputStreamReader (fis);
				BufferedReader br = new BufferedReader(isr);
				
				// 파일이 존재하는 경우 계속해서 try 문 안의 것들을 실행.
				
				// chunks 라는 ArrayList 에 라인 별로 저장할 것.
				ArrayList<String> chunks = new ArrayList<String>();
				String chunk = new String("");
				
				while ((chunk = br.readLine()) != null) {
					chunks.add(chunk);
				}

				for (int i=0; i<chunks.size(); i++) {
					for (int j=0; j<chunks.get(i).length(); j++) {
						gameBoard[x][y++] = "" + chunks.get(i).charAt(j);
					}
					x++;
					y=0;
				}
				
				// gameBoard 확인용
				// printGameBoard();
				
				// 게임 진행 부분
				// Release 모드
				if (mode.equals("r") || mode.equals("R")) {
					for (int i=0; i<bomb; i++) {
						userInput = scan.nextLine();

						String tempCol = userInput.substring(0, 1);
						String tempRow = userInput.substring(1);

						int row = Integer.parseInt(tempRow) - 1;
						int col = tempCol.charAt(0) - 65;

						try {
							if (findBomb(row, col).equals("Try again")) {
								throw new HitException(findBomb(row, col));
							} else if (findBomb(row, col).equals("Hit")) {
								System.out.println(findBomb(row, col) + " " + gameBoard[row][col]);

								String shipType = "" + (char) (gameBoard[row][col].charAt(0) + 32);

								if (shipType.equals("a")) {
									a.hits += 1;
								} else if (shipType.equals("b")) {
									b.hits += 1;
								} else if (shipType.equals("s")) {
									s.hits += 1;
								} else if (shipType.equals("d")) {
									d.hits += 1;
								} else if (shipType.equals("p")) {
									p.hits += 1;
								}

								gameBoard[row][col] = "X" + shipType;

							} else {
								System.out.println(findBomb(row, col));

								gameBoard[row][col] = "X";
							}
						} catch (HitException e) {
							System.out.println(e.getMessage());
							i--;
						}
					}

					printGameBoard();
				} 
				
				// Debug 모드
				else if (mode.equals("d") || mode.equals("D")) {
					printGameBoard();
					for (int i=0; i<bomb; i++) {
						userInput = scan.nextLine();

						String tempCol = userInput.substring(0, 1);
						String tempRow = userInput.substring(1);

						int row = Integer.parseInt(tempRow) - 1;
						int col = tempCol.charAt(0) - 65;

						try {
							if (findBomb(row, col).equals("Try again")) {
								throw new HitException(findBomb(row, col));
							} else if (findBomb(row, col).equals("Hit")) {
								System.out.println(findBomb(row, col) + " " + gameBoard[row][col]);

								String shipType = "" + (char) (gameBoard[row][col].charAt(0) + 32);

								if (shipType.equals("a")) {
									a.hits += 1;
								} else if (shipType.equals("b")) {
									b.hits += 1;
								} else if (shipType.equals("s")) {
									s.hits += 1;
								} else if (shipType.equals("d")) {
									d.hits += 1;
								} else if (shipType.equals("p")) {
									p.hits += 1;
								}

								gameBoard[row][col] = "X" + shipType;

							} else {
								System.out.println(findBomb(row, col));

								gameBoard[row][col] = "X";
							}
						} catch (HitException e) {
							System.out.println(e.getMessage());
							i--;
						}

						printGameBoard();
					}
				}
				// 점수 계산
				
				// 점수를 계산할 때는 모든 배의 종류만 들어있으면 됨.
				shipScore.add(a); shipScore.add(b); shipScore.add(s); shipScore.add(d); shipScore.add(p);
				printGameResult();
				
			} catch (Exception e) {
				// 파일이 존재하지 않는 경우에는 gameBoard 를 랜덤하게 만들어줘야함.
				Random random = new Random();
				
				// 배를 배치하는 방향을 어느 방향으로 할지 정할 때 사용.
				String[] direction = {"U", "R", "D", "L"};
				
				
				for (int i=0; i<10; i++) {
					for (int j=0; j<10; j++) {
						memo[i][j] = 0;
					}
				}
				
				// 모든 배에 대해서 gameBoard 에 배치적용.
				for (int i=0; i<ships.size(); i++) {

					// 배치되었는지 확인하기 위한 플래그
					boolean isLocated = false;
					
					int _x = random.nextInt(10);
					int _y = random.nextInt(10);
					
					while (memo[_x][_y] == 1) {
						_x = random.nextInt(10);
						_y = random.nextInt(10);
					}
					
					
					for (int j=0; j<direction.length; j++) {
						
//						System.out.print(_x);
//						System.out.print(" ");
//						System.out.println(_y);
						
						boolean check = checkLocation(_x, _y, direction[j], ships.get(i).size);

						if (check) {
							if (direction[j].equals("U")) {
								
								int row = 0;
								int maxIndex = _x - ships.get(i).size+1;
								for (row = _x; row >= maxIndex; row--) {
									gameBoard[row][_y] = ships.get(i).type;
									fillMemoCross(row, _y);
								}

								isLocated = true;

							} else if (direction[j].equals("D")) {
								
								int row = 0;
								int maxIndex = _x + ships.get(i).size - 1;
								for (row = _x; row <= maxIndex; row++) {
									gameBoard[row][_y] = ships.get(i).type;
									fillMemoCross(row, _y);
								}

								isLocated = true;

							} else if (direction[j].equals("R")) {
								
								int col = 0;
								int maxIndex = _y + ships.get(i).size - 1;
								for (col = _y; col <= maxIndex; col++) {
									gameBoard[_x][col] = ships.get(i).type;
									fillMemoCross(_x, col);
								}

								isLocated = true;
							} else if (direction[j].equals("L")) {
								
								int col = 0;
								int maxIndex = _y - ships.get(i).size + 1;
								for (col = _y; col >= maxIndex; col--) {
									gameBoard[_x][col] = ships.get(i).type;
									fillMemoCross(_x, col);
								}

								isLocated = true;
							}
						}

						if (isLocated) {
							break;
						}
					}

					if (!isLocated) {
						i--;
					}
				}
				
				// gameBoard 체크용
				// printGameBoard();


				// 게임 진행 부분
				// Release 모드
				if (mode.equals("r") || mode.equals("R")) {
					for (int i=0; i<bomb; i++) {
						userInput = scan.nextLine();

						String tempCol = userInput.substring(0, 1);
						String tempRow = userInput.substring(1);

						int row = Integer.parseInt(tempRow) - 1;
						int col = tempCol.charAt(0) - 65;

						try {
							if (findBomb(row, col).equals("Try again")) {
								throw new HitException(findBomb(row, col));
							} else if (findBomb(row, col).equals("Hit")) {
								System.out.println(findBomb(row, col) + " " + gameBoard[row][col]);

								String shipType = "" + (char) (gameBoard[row][col].charAt(0) + 32);

								if (shipType.equals("a")) {
									a.hits += 1;
								} else if (shipType.equals("b")) {
									b.hits += 1;
								} else if (shipType.equals("s")) {
									s.hits += 1;
								} else if (shipType.equals("d")) {
									d.hits += 1;
								} else if (shipType.equals("p")) {
									p.hits += 1;
								}

								gameBoard[row][col] = "X" + shipType;

							} else {
								System.out.println(findBomb(row, col));

								gameBoard[row][col] = "X";
							}
						} catch (HitException h) {
							System.out.println(h.getMessage());
							i--;
						}
					}

					printGameBoard();
				} 
				
				// Debug 모드
				else if (mode.equals("d") || mode.equals("D")) {
					printGameBoard();
					for (int i=0; i<bomb; i++) {
						userInput = scan.nextLine();

						String tempCol = userInput.substring(0, 1);
						String tempRow = userInput.substring(1);

						int row = Integer.parseInt(tempRow) - 1;
						int col = tempCol.charAt(0) - 65;

						try {
							if (findBomb(row, col).equals("Try again")) {
								throw new HitException(findBomb(row, col));
							} else if (findBomb(row, col).equals("Hit")) {
								System.out.println(findBomb(row, col) + " " + gameBoard[row][col]);

								String shipType = "" + (char) (gameBoard[row][col].charAt(0) + 32);

								if (shipType.equals("a")) {
									a.hits += 1;
								} else if (shipType.equals("b")) {
									b.hits += 1;
								} else if (shipType.equals("s")) {
									s.hits += 1;
								} else if (shipType.equals("d")) {
									d.hits += 1;
								} else if (shipType.equals("p")) {
									p.hits += 1;
								}

								gameBoard[row][col] = "X" + shipType;

							} else {
								System.out.println(findBomb(row, col));

								gameBoard[row][col] = "X";
							}
						} catch (HitException h) {
							System.out.println(h.getMessage());
							i--;
						}

						printGameBoard();
					}
				}
				// 점수 계산
				
				// 점수를 계산할 때는 모든 배의 종류만 들어있으면 됨.
				shipScore.add(a); shipScore.add(b); shipScore.add(s); shipScore.add(d); shipScore.add(p);
				printGameResult();
			}
			
		} catch (BombInputException | ModeInputException e) {
			System.out.println(e.getMessage());
		} finally {
			scan.close();
		}
	}
	
	static void printGameResult() {
		int sum = 0;

		for (int i=0; i<shipScore.size(); i++) {
			sum += shipScore.get(i).hits * shipScore.get(i).size;
		}

		System.out.println("Score: " + Integer.toString(sum));
	}

	static String findBomb(int row, int col) {
		// 밑의 return 은 형식적인 것.
		if (gameBoard[row][col].equals(" ")) {
			return "Miss";
		} else if (gameBoard[row][col].contains("X")) {
			return "Try again";
		} else {
			return "Hit";
		}
	}
	
	static void fillMemoCross(int x, int y) {
		memo[x][y] = 1;
		if (x == 0) {
			if (y == 0) {
				memo[x+1][y] = 1;
				memo[x][y+1] = 1;
			} else if (y == 9) {
				memo[x+1][y] = 1;
				memo[x][y-1] = 1;
			} else {
				memo[x][y-1] = 1;
				memo[x][y+1] = 1;
				memo[x+1][y] = 1;
			}
		} else if (x == 9) {
			if (y == 0) {
				memo[x-1][y] = 1;
				memo[x][y+1] = 1;
			} else if (y == 9) {
				memo[x-1][y] = 1;
				memo[x][y-1] = 1;
			} else {
				memo[x][y-1] = 1;
				memo[x][y+1] = 1;
				memo[x-1][y] = 1;
			}
		} else {
			if (y == 0) {
				memo[x][y+1] = 1;
				memo[x-1][y] = 1;
				memo[x+1][y] = 1;
			} else if (y == 9) {
				memo[x][y-1] = 1;
				memo[x-1][y] = 1;
				memo[x+1][y] = 1;
			} else {
				memo[x][y-1] = 1;
				memo[x][y+1] = 1;
				memo[x-1][y] = 1;
				memo[x+1][y] = 1;
			}
		}
	}
	
	// 형식에 맞추어 gameBoard 를 출력
	static void printGameBoard() {
		System.out.println("     A  B  C  D  E  F  G  H  I  J  ");
		System.out.println("     -  -  -  -  -  -  -  -  -  -  ");
		for (int i=0; i<10; i++) {
			if (i<9) {
				System.out.print("" + (i+1) + "  | ");
			} else {
				System.out.print("" + (i+1) + " | ");
			}
			for (int j=0; j<10; j++) {
				if (gameBoard[i][j].length() >= 2) {
					System.out.print(gameBoard[i][j] + " ");
				} else {
					System.out.print(gameBoard[i][j] + "  ");
				}
			}
			System.out.println();
		}
	}
	
	// 넘겨받은 위치와 방향을 기준으로 배를 쭉 놓을 수 있는지 확인하는 함수
	static boolean checkLocation(int x, int y, String d, int size) {
		boolean flag = false;
		
		if (size == 0) {
			// size 가 0 이 될때까지 재귀호출을 했다는 것은 이전에 계속해서 true 가 반환되었다는 것.
			// 처음 x 와 y 위치에 대한 공백확인 여부는 아래에서 했으므로 무사히 통과했다는 것.
			// 때문에 true 만 반환해주면 됨.
			return true;
		}
		
		if (gameBoard[x][y].equals(" ")) {
			if (d.equals("U")) {
				
				// 위와 왼쪽, 오른쪽으로는 계속해서 검사할 것이기 때문에 첫 위치의 아래로만 검사
				if ((x < 9 && gameBoard[x + 1][y].equals(" ")) || x == 10) flag = true;

				// 처음 인덱스에서 행의 위 방향으로 올라간다면 기존 행 번호에서 size 만큼 빼주고 +1.
				int maxIndex = x - size + 1;
				
				// 처음 위치에서 아래쪽이 빈칸이고 index 의 범위를 넘지 않는다면 검사 실행
				if (flag && maxIndex >= 0) {

					// 해당 부분이 빈칸이 아니라면 검사 중지.
					if (gameBoard[maxIndex][y].equals(" ")) {
						// maxIndex 가 0 이라면 행을 기준으로 가장 끝 부분에 위치할 것이므로 위에 대한 검사를 하지 않아도 됨.
						if (maxIndex == 0) {
							// 왼쪽 끝에 딱 붙어있는 경우라면 오른쪽만 빈칸인지 검사하면됨.
							if (y-1 == -1) {

								// 오른쪽이 빈칸이라면 다음 검사를 실시
								if (gameBoard[maxIndex][y+1].equals(" ")) {
									return true && checkLocation(x, y, d, size - 1);
								}

								// 오른쪽이 빈칸이 아니라면 검사 중지
								else {
									return false;
								}
							} 

							// 왼쪽 끝에 딱 붙어있지 않은 경우라면 오른쪽에 딱 붙어있는지, 왼쪽과 오른쪽 둘 다 비었는지 검사해야됨.
							else {
								// 오른쪽에 딱 붙어있는 경우라면 왼쪽만 빈칸인지 검사하면됨.
								if (y+1 == 10) {

									// 왼쪽이 빈칸이라면 다음 검사 실시.
									if (gameBoard[maxIndex][y-1].equals(" ")) {
										return true && checkLocation(x, y, d, size - 1);
									} 

									// 왼쪽이 빈칸이 아니라면 검사 중지.
									else {
										return false;
									}
								}

								// 오른쪽에도 딱 붙어있지 않은 경우라면 왼쪽과 오른쪽 둘 다 비었는지 검사
								else {

									// 왼쪽 오른쪽 둘 다 비어있다면 다음 검사 실행
									if (gameBoard[maxIndex][y+1].equals(" ") && gameBoard[maxIndex][y-1].equals(" ")) {
										return true && checkLocation(x, y, d, size-1);
									}

									// 그렇지 않을 경우 검사 중지
									else {
										return false;
									}
								}
							}
						} 
						
						// maxIndex 가 0 이 아니라는 것은 위의 칸이 남아있다는 것이고 해당 칸이 빈 칸인지 확인해야됨.
						else {
							// 위의 칸이 빈칸인지 확인
							if (gameBoard[maxIndex - 1][y].equals(" ")) {
								
								// 왼쪽 끝에 딱 붙어있는 경우라면 오른쪽이 빈칸인지 검사.
								if (y-1 == -1) {
									if (gameBoard[maxIndex][y+1].equals(" ")) {
										return true && checkLocation(x, y, d, size - 1);
									}
									
									else {
										return false;
									}
								}

								// 왼쪽 끝에 딱 붙어있지 않은 경우
								else {

									// 오른쪽 끝에 딱 붙어있는 경우
									if (y+1 == 10) {

										// 왼쪽이 빈칸이라면 다음 검사 실시 
										if (gameBoard[maxIndex][y-1].equals(" ")) {
											return true && checkLocation(x, y, d, size - 1);
										}

										else {
											return false;
										}
									}

									// 오른쪽에도 딱 붙어있지 않은 경우라면 왼쪽과 오른쪽 둘 다 비었는지 검사
									else {
										
										// 왼쪽과 오른쪽 둘 다 비었다면 다음 검사 실시
										if (gameBoard[maxIndex][y+1].equals(" ") && gameBoard[maxIndex][y-1].equals(" ")) {
											return true && checkLocation(x, y, d, size-1);
										}

										else {
											return false;
										}
									}
								}
							}

							// 위쪽이 빈칸이 아니라면 검사 중지
							else {
								return false;
							}
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
				
			} else if (d.equals("D")) {

				// 아래와 왼쪽, 오른쪽으로는 계속해서 검사할 것이기 때문에 첫 위치의 위로만 검사
				if ((x > 0 && gameBoard[x - 1][y].equals(" ")) || x == 0) flag = true;

				// 처음 인덱스에서 행의 아래 방향으로 내려간다면 기존 행 번호에서 size 만큼 더해주고 -1.
				int maxIndex = x + size - 1;

				// 처음 위치에서 위쪽이 빈칸이고 index 의 범위를 넘지않는다면 검사 실시
				if (flag && maxIndex <= 9) {

					if (gameBoard[maxIndex][y].equals(" ")) {

						// maxIndex 가 9라면 가장 아래쪽에 위치한 것이므로 아래에 대한 검사는 실시하지 않음.
						if (maxIndex == 9) {
							// 왼쪽 끝에 딱 붙어있는 경우
							if (y-1 == -1) {

								// 오른쪽이 빈칸인지만 검사
								if (gameBoard[maxIndex][y+1].equals(" ")) {
									return true && checkLocation(x, y, d, size-1);
								}

								else {
									return false;
								}
							}

							// 왼쪽 끝에 딱 붙어있지 않는 경우
							else {

								// 우선 오른쪽 끝에 딱붙어있는지 검사
								if (y+1 == 10) {

									// 왼쪽이 빈칸인지만 검사
									if (gameBoard[maxIndex][y-1].equals(" ")) {
										return true && checkLocation(x, y, d, size-1);
									}

									else {
										return false;
									}
								}

								// 오른쪽 끝에도 딱붙어있지 않은 경우
								else {
									
									// 왼쪽과 오른쪽 둘다 비어있는지 검사
									if (gameBoard[maxIndex][y+1].equals(" ") && gameBoard[maxIndex][y-1].equals(" ")) {
										return true && checkLocation(x, y, d, size-1);
									}

									else {
										return false;
									}
								}
							}
						} 
						
						// 가장 아래쪽에 위치한 것이 아닐 경우
						else {

							// 바로 아래 칸이 빈칸인지 확인
							if (gameBoard[maxIndex + 1][y].equals(" ")) {

								// 왼쪽 끝에 붙어있는 경우
								if (y-1 == -1) {
									if (gameBoard[maxIndex][y+1].equals(" ")) {
										return true && checkLocation(x, y, d, size-1);
									}
									else {
										return false;
									}
								}

								// 그렇지 않은 경우
								else {

									// 오른쪽 끝에 붙어있는 경우
									if (y+1 == 10) {
										if (gameBoard[maxIndex][y-1].equals(" ")) {
											return true && checkLocation(x, y, d, size - 1);
										}
										else {
											return false;
										}
									}

									// 왼쪽 끝, 오른쪽 끝도 아닌 경우
									else {
										if (gameBoard[maxIndex][y+1].equals(" ") && gameBoard[maxIndex][y-1].equals(" ")) {
											return true && checkLocation(x, y, d, size - 1);
										}
										else {
											return false;
										}
									}
								}
							}

							// 빈칸이 아니라면 검사 중지.
							else {
								return false;
							}
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
				
			} else if (d.equals("R")) {

				// 오른쪽과 위, 아래로는 계속해서 검사할 것이기 때문에 첫 위치의 왼쪽으로만 검사
				if ((y > 0 && gameBoard[x][y - 1].equals(" ")) || y == 0) flag = true;

				// 처음 인덱스에서 열의 방향으로 오른쪽으로 간다면 기존 열 번호에서 size 만큼 더해주고 -1.
				int maxIndex = y + size - 1;

				// 처음 위치에서 왼쪽이 빈칸 혹은 왼쪽 끝부분이고 index 의 범위를 넘지 않는다면 검사 실시.
				if (flag && maxIndex <= 9) {

					if (gameBoard[x][maxIndex].equals(" ")) {

						// 오른쪽에 딱 붙어있는 경우
						if (maxIndex == 9) {
							
							// 위쪽 끝에 딱 붙어있는 경우
							if (x-1 == -1) {

								// 아래쪽이 빈칸인지만 검사
								if (gameBoard[x+1][maxIndex].equals(" ")) {
									return true && checkLocation(x, y, d, size - 1);
								}

								else {
									return false;
								}
							}

							// 위쪽 끝에 딱 붙어있지 않은 경우
							else {

								// 아래쪽 끝에 딱 붙어있는지 검사
								if (x+1 == 10) {

									// 위쪽이 빈칸인지만 검사
									if (gameBoard[x-1][maxIndex].equals(" ")) {
										return true && checkLocation(x, y, d, size-1);
									}

									else {
										return false;
									}
								}

								// 위쪽 끝, 아래쪽 끝도 딱 붙어있지 않은 경우
								else {
									
									// 위, 아래로 다 비어있는지 검사
									if (gameBoard[x+1][maxIndex].equals(" ") && gameBoard[x-1][maxIndex].equals(" ")) {
										return true && checkLocation(x, y, d, size - 1);
									}

									else {
										return false;
									}
								}
							}
						}

						// 가장 오른쪽에 위치한 것이 아닐 경우
						else {

							// 바로 오른쪽이 빈칸인지 확인
							if (gameBoard[x][maxIndex + 1].equals(" ")) {

								// 위쪽 끝에 붙어있는 경우
								if (x-1 == -1) {

									// 아래쪽이 빈칸인지만 확인
									if (gameBoard[x+1][maxIndex].equals(" ")) {
										return true && checkLocation(x, y, d, size - 1);
									}

									else {
										return false;
									}
								}

								// 위쪽 끝에 붙어있지 않은 경우
								else {
									// 아래쪽 끝에 붙어있는 경우
									if (x+1 == 10) {
										if (gameBoard[x-1][maxIndex].equals(" ")) {
											return true && checkLocation(x, y, d, size - 1);
										}
										else {
											return false;
										}
									}

									// 위쪽 끝, 아래쪽 끝도 아닌 경우
									else {

										// 위, 아래로 모두 검사
										if (gameBoard[x+1][maxIndex].equals(" ") && gameBoard[x-1][maxIndex].equals(" ")) {
											return true && checkLocation(x, y, d, size - 1);
										}

										else {
											return false;
										}
									}
								}
							} 
							
							// 바로 오른쪽이 빈칸이 아니라면 검사 중지.
							else {
								return false;
							}
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else if (d.equals("L")) {

				// 왼쪽과 위, 아래로는 계속해서 검사할 것이기 때문에 첫 위치의 오른쪽만 검사
				if ((y < 9 && gameBoard[x][y+1].equals(" ")) || y == 9) flag = true;

				// 처음 인덱스에서 열의 방향으로 왼쪽으로 간다면 기존 열 번호에서 size 만큼 빼주고 +1.
				int maxIndex = y - size + 1;

				// 처음 위치에서 오른쪽이 빈칸이거나 끝 칸이고 index 의 범위를 넘지 않는다면 검사 실행.
				if (flag && maxIndex >= 0) {

					if (gameBoard[x][maxIndex].equals(" ")) {

						// 왼쪽에 딱 붙어있는 경우
						if (maxIndex == 0) {

							// 위쪽 끝에 딱 붙어있는 경우
							if (x-1 == -1) {

								// 아래쪽만 빈칸인지 검사
								if (gameBoard[x+1][maxIndex].equals(" ")) {
									return true && checkLocation(x, y, d, size - 1);
								}

								else {
									return false;
								}
							}

							// 위쪽 끝에 딱 붙어있지 않은 경우
							else {

								// 아래쪽 끝에 딱 붙어있는지 검사
								if (x+1 == 10) {

									// 위쪽만 빈칸인지 검사
									if (gameBoard[x-1][maxIndex].equals(" ")) {
										return true && checkLocation(x, y, d, size - 1);
									}
									else {
										return false;
									}
								}

								// 위쪽끝, 아래쪽 끝도 아닌 경우
								else {
									
									// 위, 아래로 다 비어있는지 검사
									if (gameBoard[x+1][maxIndex].equals(" ") && gameBoard[x-1][maxIndex].equals(" ")) {
										return true && checkLocation(x, y, d, size - 1);
									}

									else {
										return false;
									}
								}
							}
						}

						// 가장 왼쪽에 위치한 것이 아닐 경우
						else {

							// 바로 왼쪽이 빈칸인지 확인
							if (gameBoard[x][maxIndex - 1].equals(" ")) {

								// 위쪽 끝에 붙어있는 경우
								if (x-1 == -1) {

									// 아래쪽이 빈칸인지만 확인
									if (gameBoard[x+1][maxIndex].equals(" ")) {
										return true && checkLocation(x, y, d, size - 1);
									}

									else {
										return false;
									}
								}

								// 위쪽 끝에 붙어있지 않은 경우
								else {

									// 아래쪽 끝에 붙어있는 경우
									if (x+1 == 10) {
										if (gameBoard[x-1][maxIndex].equals(" ")) {
											return true && checkLocation(x, y, d, size - 1);
										}
										else {
											return false;
										}
									}

									// 위쪽 끝, 아래쪽 끝도 아닌 경우
									else {

										// 위, 아래로 모두 검사
										if (gameBoard[x+1][maxIndex].equals(" ") && gameBoard[x-1][maxIndex].equals(" ")) {
											return true && checkLocation(x, y, d, size - 1);
										}

										else {
											return false;
										}
									}
								}
							}

							// 바로 왼쪽이 비어있지 않은 경우에는 검사 중지.
							else {
								return false;
							}
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		} else {
			return false;
		}

		// 에러 방지를 위한 return 문.
		// 위에서 다 걸러줄 것이기 때문에 아래 return 문은 실행이 안될 것임.
		return false;
	}
}

class Ship {
	String type = new String("");
	int size = 0;
	
	// hits 는 나중에 점수 계산할 때 사용.
	int hits = 0;
	
	public Ship() {}
	
	public Ship(String type, int size) {
		this.type = type;
		this.size = size;
	}
}

final class AircraftCarrier extends Ship {
	public AircraftCarrier(String type, int size) {
		super(type, size);
	}
}

final class Battle extends Ship {
	public Battle(String type, int size) {
		super(type, size);
	}
}

final class Submarine extends Ship {
	public Submarine(String type, int size) {
		super(type, size);
	}
}

final class Destroyer extends Ship {
	public Destroyer(String type, int size) {
		super(type, size);
	}
}

final class PatrolBoat extends Ship {
	public PatrolBoat(String type, int size) {
		super(type, size);
	}
}

class BombInputException extends Exception {
	public BombInputException(String message) {
		super(message);
	}
}

class ModeInputException extends Exception {
	public ModeInputException(String message) {
		super(message);
	}
}

class HitException extends Exception {
	public HitException(String message) {
		super(message);
	}
}