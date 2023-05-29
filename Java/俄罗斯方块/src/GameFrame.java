import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.*;
import javax.swing.*;
/**
 * @author key
 */
public class GameFrame extends JFrame {
    private JFrame mainFrame = null;
    public Thread mainThread = null;
    private static final int ROWS = 20;
    private static final int COLS = 15;

    //开始/停止按钮
    private JButton btnStart = null;
    //暂停/继续按钮
    private JButton btnPause = null;

    public GameFrame() {
        initComponents();
    }

    //开始新游戏按键事件监听
    private void menuItem1ActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    //退出事件监听
    private void menuItem2ActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    //操作说明事件监听
    private void menuItem3ActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    //失败判定事件监听
    private void menuItem4ActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    //暂停按键事件监听
    private void button1ActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        menuItem1 = new JMenuItem();
        menuItem2 = new JMenuItem();
        menu2 = new JMenu();
        menuItem3 = new JMenuItem();
        menuItem4 = new JMenuItem();
        panel3 = new JPanel();
        panel4 = new JPanel();
        button1 = new JButton();

        //======== this ========
        setTitle("\u4fc4\u7f57\u65af\u65b9\u5757");
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== menuBar1 ========
        {

            //======== menu1 ========
            {
                menu1.setText("\u6e38\u620f");

                //---- menuItem1 ----
                menuItem1.setText("\u5f00\u59cb\u65b0\u6e38\u620f");
                menuItem1.addActionListener(e -> menuItem1ActionPerformed(e));
                menu1.add(menuItem1);

                //---- menuItem2 ----
                menuItem2.setText("\u9000\u51fa");
                menuItem2.addActionListener(e -> menuItem2ActionPerformed(e));
                menu1.add(menuItem2);
            }
            menuBar1.add(menu1);

            //======== menu2 ========
            {
                menu2.setText("\u5e2e\u52a9");

                //---- menuItem3 ----
                menuItem3.setText("\u64cd\u4f5c\u8bf4\u660e");
                menuItem3.addActionListener(e -> menuItem3ActionPerformed(e));
                menu2.add(menuItem3);

                //---- menuItem4 ----
                menuItem4.setText("\u5931\u8d25\u5224\u5b9a");
                menuItem4.addActionListener(e -> menuItem4ActionPerformed(e));
                menu2.add(menuItem4);
            }
            menuBar1.add(menu2);
        }
        setJMenuBar(menuBar1);

        //======== panel3 ========
        {
            panel3.setLayout(new FlowLayout());
        }
        contentPane.add(panel3);
        panel3.setBounds(0, 25, 335, 410);

        //======== panel4 ========
        {
            panel4.setLayout(null);

            //---- button1 ----
            button1.setText("\u6682\u505c");
            button1.setFont(new Font("\u5b8b\u4f53", Font.BOLD, button1.getFont().getSize() + 10));
            button1.addActionListener(e -> button1ActionPerformed(e));
            panel4.add(button1);
            button1.setBounds(5, 280, 95, 45);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for (int i = 0; i < panel4.getComponentCount(); i++) {
                    Rectangle bounds = panel4.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel4.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel4.setMinimumSize(preferredSize);
                panel4.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel4);
        panel4.setBounds(345, 25, 170, 410);

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for (int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        setSize(520, 490);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public void end() {
        button1.setText("重开");
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JMenuItem menuItem1;
    private JMenuItem menuItem2;
    private JMenu menu2;
    private JMenuItem menuItem3;
    private JMenuItem menuItem4;
    private JPanel panel3;
    private JPanel panel4;
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //绘制网格
        drawGrid(g);
        //绘制边框
        drawBorder(g);
        //右侧辅助区域
        drawBorderRight(g);
        //绘制积分区域
        drawCount(g);
        //绘制下一个区域
        drawNext(g);
    }

    //绘制网格
    private void drawGrid(Graphics g) {
        Graphics2D g_2d = (Graphics2D) g;
        g_2d.setColor(new Color(255, 255, 255, 150));
        int x1 = 12;
        int y1 = 20;
        int x2 = 312;
        int y2 = 20;
        for (int i = 0; i <= ROWS; i++) {
            y1 = 12 + 20 * i;
            y2 = 12 + 20 * i;
            g_2d.drawLine(x1, y1, x2, y2);
        }

        y1 = 12;
        y2 = 412;
        for (int i = 0; i <= COLS; i++) {
            x1 = 12 + 20 * i;
            x2 = 12 + 20 * i;
            g_2d.drawLine(x1, y1, x2, y2);
        }
    }

    //绘制边框
    private void drawBorder(Graphics g) {
        BasicStroke bs_2 = new BasicStroke(12L, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        Graphics2D g_2d = (Graphics2D) g;
        g_2d.setColor(new Color(128, 128, 128));
        g_2d.setStroke(bs_2);

        RoundRectangle2D.Double rect = new RoundRectangle2D.Double(10, 60, 312, 412, 1, 1);
        g_2d.draw(rect);
    }

    //右侧辅助区域
    private void drawBorderRight(Graphics g) {
        BasicStroke bs_2 = new BasicStroke(12L, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        Graphics2D g_2d = (Graphics2D) g;
        g_2d.setColor(new Color(128, 128, 128));
        g_2d.setStroke(bs_2);

        RoundRectangle2D.Double rect = new RoundRectangle2D.Double(340, 60, 140 - 1, 413 - 1, 1, 1);
        g_2d.draw(rect);
    }


    //绘制积分区域
    private void drawCount(Graphics g) {
        BasicStroke bs_2 = new BasicStroke(2L, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        Graphics2D g_2d = (Graphics2D) g;
        g_2d.setColor(new Color(10, 10, 10));
        g_2d.setStroke(bs_2);
        g_2d.drawRect(350, 120, 110, 120);

        //得分
        g.setFont(new Font("宋体", Font.BOLD, 20));
        g.drawString("得分：", 380, 80);
    }

    //绘制下一个区域
    private void drawNext(Graphics g) {
        BasicStroke bs_2 = new BasicStroke(2L, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        Graphics2D g_2d = (Graphics2D) g;
        g_2d.setColor(new Color(0, 0, 0));
        g_2d.setStroke(bs_2);
        g_2d.drawRect(350, 120, 110, 120);

        g.setFont(new Font("宋体", Font.BOLD, 20));
        g.drawString("下一个：", 360, 160);
    }
}
