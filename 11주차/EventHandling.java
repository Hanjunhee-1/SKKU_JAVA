import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EventHandling {
    public static void main(String args[]) {
        Frame f = new Frame();
    }
}

class Frame extends JFrame {
    private JLabel label;
    private int cnt = 0;

    Frame() {
        setTitle("Example of event handling");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        ImageIcon icon1 = new ImageIcon("img/plus.png");
        Image image1 = icon1.getImage();
        Image newImage1 = image1.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JButton b1 = new JButton(new ImageIcon(newImage1));

        ImageIcon icon2 = new ImageIcon("img/minus.png");
        Image image2 = icon2.getImage();
        Image newImage2 = image2.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JButton b2 = new JButton(new ImageIcon(newImage2));


        ImageIcon icon3 = new ImageIcon("img/reset.png");
        Image image3 = icon3.getImage();
        Image newImage3 = image3.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JButton b3 = new JButton(new ImageIcon(newImage3));

        JPanel panel = new JPanel();

        b1.setActionCommand("plus");
        b1.addActionListener(new BclickListener());
        b2.setActionCommand("minus");
        b2.addActionListener(new BclickListener());
        b3.setActionCommand("reset");
        b3.addActionListener(new BclickListener());

        label = new JLabel("0");

        // label 의 폰트 설정
        Font font = label.getFont();
        label.setFont(new Font(font.getName(), Font.PLAIN, 30));

        Container c = getContentPane();
        
        panel.add(b1);
        panel.add(label);
        panel.add(b2);
        panel.add(b3);

        setLayout(new BorderLayout());
        c.add(panel, BorderLayout.NORTH);
        c.add(b3, BorderLayout.SOUTH);
        pack();

        setSize(300, 200);
        setVisible(true);
    }

    class BclickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            
            if (command.equals("plus")) {
                label.setText(Integer.toString(++cnt));
            } else if (command.equals("minus")) {
                label.setText(Integer.toString(--cnt));
            } else {
                cnt = 0;
                label.setText(Integer.toString(cnt));
            }
        }
    }
}