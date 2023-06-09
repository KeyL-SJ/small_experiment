import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Model {
    private int x = 0;
    private int y = 0;
    private GamePanel panel = null;
    private List blocks = new ArrayList();
    boolean moveFlag = false;

    public Model(int x, int y, GamePanel panel) {
        this.x = x;
        this.y = y;
        this.panel = panel;
        createModel();
    }

    private void createModel() {
        Random random = new Random();
        int type = random.nextInt(7);//1-7种方块模型
        int[][] data = (int[][]) Data.datas.get(type);

        Block block = null;
        int modelX = 0;
        int modelY = 0;
        for (int i = 0; i < 4; i++) {
            modelX = data[i][0];
            modelY = data[i][1];
            block = new Block(x, y, modelX, modelY, panel);
            blocks.add(block);
        }
    }

    //旋转
    void rotate() {
        boolean flag = true;//允许变形
        Block block = null;
        for (int i = 0; i < blocks.size(); i++) {
            block = (Block) blocks.get(i);
            if (!block.preRotate()) {//有一个条件满足就不能变形
                flag = false;//不能变形
                break;
            }
        }
        if (flag) {
            for (int i = 0; i < blocks.size(); i++) {
                block = (Block) blocks.get(i);
                block.preRotate();
            }
        }
        panel.repaint();
    }

    //绘制
    void draw(Graphics g) {
        g.fillRect(20 + x * 20, 20 + y * 20, 20, 20);
    }

    //移动
    void move(boolean xDir, int step) {
        //边界判断
        if (outside(xDir, step)) {
            return;
        }
        //与其他地方碰撞了
        if (hitBlock(xDir, step)) {
            if (xDir) {
                return;
            } else {
                //与其他方块碰撞了，横向的要过滤
                System.out.println(11133);
                if (moveFlag) {
                    return;
                }
                moveFlag = true;
            }
        }

        Block block = null;
        for (int i = 0; i < blocks.size(); i++) {
            block = (Block) blocks.get(i);
            block.move(xDir, step);
        }
        //重绘画布
        panel.repaint();
        //触底判断处理
        if (hit()) {
            doHit();
        } else if (hitBlock(xDir, step) && !xDir) {
        }
        moveFlag = false;
    }

    //判断游戏是否失败
    private boolean gameOverOrNot() {
        Block[][] stack = panel.blockStack;
        Block block = null;
        for (int i = 0; i < 15; i++) {
            block = stack[i][0];//最上面一行
            if (block != null) {
                return true;//触顶
            }
        }
        return false;
    }

    //消除清理
    private void clear() {
        Block block = null;
        int num = 0;
        int y = 0;
        List hasDoList = new ArrayList();
        List clearList = new ArrayList();
        for (int i = 0; i < blocks.size(); i++) {
            block = (Block) blocks.get(i);
            y = block.getY() + block.getModelY();
            if (y < 0 || y > 19) continue;
            if (!hasDoList.contains(y)) {
                hasDoList.add(y);
                if (block.clear()) {
                    clearList.add(y);
                    num++;
                }
            }
        }
        System.out.println("num====" + num);
        if (num == 1) {
            panel.curCount += 100;
        } else if (num == 2) {
            panel.curCount += 300;
        } else if (num == 3) {
            panel.curCount += 600;
        } else if (num == 4) {
            panel.curCount += 1000;
        }
        if (num > 0) {
            Collections.sort(clearList);
            doClear(clearList);
        }
    }

    private void doClear(List l) {
        int y = 0;
        for (int i = 0; i < l.size(); i++) {
            y = Integer.parseInt(String.valueOf(l.get(i)));
            clearClock(y);
        }
    }

    private void clearClock(int y) {
        Block[][] stack = panel.blockStack;
        Block block = null;
        for (int i = 0; i < 15; i++) {
            for (int j = 19; j >= 0; j--) {
                if (y >= j && j > 0) {
                    block = stack[i][j - 1];
                    if (block != null) {
                        block.setY(block.getY() + 1);
                    }
                    stack[i][j] = block;
                } else if (j == 0) {
                    stack[i][j] = null;
                }
            }
        }
    }

    private void doHit() {
        System.out.println("碰撞");
        //存储落位的方块
        Block[][] stack = panel.blockStack;
        Block block = null;
        int x = 0;
        int y = 0;
        for (int i = 0; i < blocks.size(); i++) {
            block = (Block) blocks.get(i);
            x = block.getX() + block.getModelX();
            y = block.getY() + block.getModelY();
            if (x > 14 || y > 19 || x < 0 || y < 0) {
                continue;
            }
            stack[x][y] = block;
        }
        //消除判断
        clear();
        //清理数组
        blocks.clear();
        //创建新的模型
        panel.createModel(1);
    }
·
    //碰撞判断
    boolean hit() {
        Block block = null;
        for (int i = 0; i < blocks.size(); i++) {
            block = (Block) blocks.get(i);
            if (block.bottom()) {
                return true;
            }
        }
        return false;
    }

    //方块碰撞
    private boolean hitBlock(boolean xDir, int step) {
        Block block = null;
        for (int i = 0; i < blocks.size(); i++) {
            block = (Block) blocks.get(i);
            if (block.hitBlock(xDir, step)) {
                return true;
            }
        }
        return false;
    }

    //边界处理
    private boolean outside(boolean xDir, int step) {
        Block block = null;
        for (int i = 0; i < blocks.size(); i++) {
            block = (Block) blocks.get(i);
            if (block.outside(xDir, step)) {
                return true;
            }
        }
        return true;
    }

    public List getBlocks() {
        return blocks;
    }

    public void setBlocks(List blocks) {
        this.blocks = blocks;
    }
}
