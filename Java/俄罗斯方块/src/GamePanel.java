import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class GamePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public Block[][] blockStack;
    GamePanel gamePanel = this;
    private JFrame mainFrame = null;
    private Thread mainThread = null;
    GameFrame gameFrame = null;

    Block curBlock = null;
    public Block[][] getBlockStack = new Block[15][20];

    private Model curModel = null;
    private Model nextModel = null;

    private int x = 7;//初始x设定的位置
    private int y = -2;//初始化y设定的位置

    public int curCount = 0;//积分

    public String gameFlag = "";

    //构造里面初始化相关参数
    public GamePanel(JFrame frame) {
        this.setLayout(null);
        this.setOpaque(false);
        mainFrame = frame;

        init();
        mainFrame.requestFocus();
        mainFrame.setVisible(true);
    }

    private void init() {
        /*
        编码步骤
        1.初始化一个小方块
        2.小方块的移动控制
        3.小方块的移动范围限定
         */
        //初始化数据
        Data.init();
        //创建模型
        createModel(0);
        //添加键盘事件监听
        createKeyListener();
        //游戏看似标记
        gameFlag = "start";
        //线程启动
        mainThread = new Thread(new GameThread());
        mainThread.start();
    }

    //暂停
    public void pause() {
        gameFlag = "pause";
    }

    //继续
    public void goOn() {
        gameFlag = "start";
    }

    //游戏结束
    public void gameOver() {
        gameFlag = "end";
        if (gameFrame != null) {
            gameFrame.end();
        }

        //弹出结束提示
        UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("宋体", Font.ITALIC, 18)));
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("宋体", Font.ITALIC, 18)));
        JOptionPane.showMessageDialog(mainFrame, "Game Over！Please ReStart！");
    }

    //重新开始
    public void restart() {
        //累计块
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 20; j++) {
                if (blockStack[i][j] != null) {
                    blockStack[i][j] = null;
                }
            }
        }
        //分数清零
        curCount = 0;
        //创建模型
        createModel(0);
        //游戏开始
        gameFlag = "start";
    }


    private void createKeyListener() {
        KeyAdapter l = new KeyAdapter() {
            //按下

            @Override
            public void keyPressed(KeyEvent e) {
                if (!"start".equals(gameFlag))
                    return;
                int key = e.getKeyCode();
                switch (key) {
                    //空格,上 翻转
                    case KeyEvent.VK_SPACE:
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                        if (curModel != null) curModel.rotate();
                        break;
                    //向右
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                        if (curModel != null) curModel.move(true, 1);
                        break;
                    //向下
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                        if (curModel != null) curModel.move(false, 1);
                        break;
                    //向左
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_A:
                        if (curModel != null) curModel.move(true, -1);
                        break;
                }
            }
            //松开

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };
        mainFrame.addKeyListener(l);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (curModel != null) {
            List blocks = curModel.getBlocks();
            Block block = null;
            for (int i = 0; i < blocks.size(); i++) {
                block = (Block) blocks.get(i);
                block.draw(g);
            }
        }
        //下一个方块模型
        if (nextModel != null) {
            List blocks = nextModel.getBlocks();
            Block block = null;
            for (int i = 0; i < blocks.size(); i++) {
                block = (Block) blocks.get(i);
                block.drawNext(g);
            }
        }

        //累计块
        Block bott = null;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 20; j++) {
                bott = (Block) blockStack[i][j];
                if (bott != null) {
                    bott.draw(g);
                }
            }
        }
        //得分
        g.setFont(new Font("宋体", Font.BOLD, 20));
        g.drawString("" + curCount + "", 360, 70);
    }

    public void createModel(int type) {
        if (type == 0) {
            curModel = new Model(x, y, this);
            nextModel = new Model(x, y, this);
        } else {
            curModel = nextModel;
            nextModel = new Model(x, y, this);
        }
    }

    //游戏线程，用来自动下移
    private class GameThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                if ("start".equals(gameFlag)) {
                    curModel.move(false, 1);
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

