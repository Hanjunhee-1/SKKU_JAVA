import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class GUI {
    public static void main(String args[]) {
        new MyFrame();
    }
}

class MyFrame extends JFrame {
    String id = "admin";
    String pw = "admin1234";

    // 사용자에게 입력받을 필드 선언
    JTextField idField;
    JPasswordField pwField;

    // 버튼을 표시해줄 버튼 선언
    JButton loginButton;

    // 사용자에게 입력받을 필드를 안내할 라벨 선언
    JLabel idLabel;
    JLabel pwLabel;

    // 라벨과 필드를 한 번에 묶어서 하위 컴포넌트로 만들어줄 패널 선언
    JPanel idPanel;
    JPanel pwPanel;

    // 버튼 또한 패널로 묶어줘야 GUI 상에서 예쁘게 표시됨.
    JPanel buttonPanel;

    MyFrame() {
        // GUI 상에 보여줄 제목을 설정
        setTitle("로그인 창");

        // layout 과 GUI 창을 닫는 옵션 관련 설정
        setResizable(false);                // 사용자가 GUI 창의 크기를 조절하는 것을 방지합니다.
        setDefaultCloseOperation(EXIT_ON_CLOSE);      // GUI 창을 닫는 옵션
        setLayout(new GridLayout(3,1));     // GridLayout 으로 설정.


        // id 라벨과 id 필드에 대해 설정해주기
        idLabel = new JLabel("Id");                                 // 라벨이 표시해줄 텍스트 설정
        idLabel.setPreferredSize(new Dimension(80, 10));    // 라벨의 크기 설정
        idField = new JTextField();                                      // id 필드 사용을 위한 JTextField 객체 생성
        idField.setPreferredSize(new Dimension(150, 20));   // id 필드 크기 설정
        idPanel = new JPanel();                                          // id 라벨과 id 필드를 한번에 묶어줄 패널 사용을 위한 객체 생성
        idPanel.add(idLabel);                                            // id 라벨 먼저 추가
        idPanel.add(idField);                                            // id 필드 추가
        add(idPanel);                                                    // Frame 에 패널 추가

        // pw 라벨과 pw 필드에 대해 설정해주기
        pwLabel = new JLabel("Password");                           // 라벨이 표시해줄 텍스트 설정
        pwLabel.setPreferredSize(new Dimension(80, 10));    // 라벨의 크기 설정
        pwField = new JPasswordField();                                  // pw 필드 사용을 위한 JPasswordField 객체 생성
        pwField.setPreferredSize(new Dimension(150, 20));   // pw 필드 크기 설정
        pwPanel = new JPanel();                                          // pw 라벨과 pw 필드를 한번에 묶어줄 패널 사용을 위한 객체 생성
        pwPanel.add(pwLabel);                                            // pw 라벨 먼저 추가
        pwPanel.add(pwField);                                            // pw 필드 추가
        add(pwPanel);                                                    // Frame 에 패널 추가

        // login 버튼 관련 설정
        loginButton = new JButton("로그인");
        loginButton.setPreferredSize(new Dimension(150, 20));
        loginButton.addActionListener(new ButtonClickListener());
        buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        add(buttonPanel);


        // Frame 의 크기를 패널들의 크기에 맞게 자동으로 설정해준다.
        pack();

        // setSize() 를 통해 pack() 을 안사용할 수도 있다.
        // setSize(300, 200);

        // GUI 실행 창이 화면의 가운데에 뜨도록 설정
        setLocationRelativeTo(null);

        // 이렇게 해야 GUI 창이 보인다.
        setVisible(true);
    }

    	// button 을 클릭했을 때를 위한 ClickListener 클래스
	class ButtonClickListener implements ActionListener {
		
		public void actionPerformed (ActionEvent e) {
			String tempId = idField.getText();
			// String tempPassword = pwField.getText();
            String tempPassword = pwField.getText();
			
			if (tempId.equals(id) && tempPassword.contentEquals(pw)) {
				JOptionPane.showMessageDialog(null, "Success");
			} else {
				JOptionPane.showConfirmDialog(null, "Fail");
			}
		}
	}
}