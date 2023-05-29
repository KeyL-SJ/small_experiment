import javax.swing.*;

public class main {
    public static void main(String[] args) {
        GameFrame frame = new GameFrame();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GamePanel gamePanel = new GamePanel(frame);
        frame.add(gamePanel);
    }
}
