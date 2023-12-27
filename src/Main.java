import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main extends JFrame {

    private JTextField textField1;
    private JButton createButton;
    private JPanel mainPanel;

    public Main() {
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(createButton, "Ahoj " + textField1.getText());
            }
        });
    }

    public static void main(String[] args) {
        Main m = newWindow(400, 500);
    }

    public static Main newWindow(int width, int height){
        Main m = new Main();
        m.setContentPane(m.mainPanel);
        m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        m.setSize(400, 500);
        m.setLocationRelativeTo(null);
        m.setVisible(true);
        return m;
    }
}