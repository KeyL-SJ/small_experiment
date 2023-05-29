import java.awt.*;

public class Block {
    private int x = 0;
    private int y = 0;
    private int modelX = 0;
    private int modelY = 0;
    private GamePanel panel = null;

    public Block(int x, int y, int modelX, int modelY, GamePanel panel) {
        this.x = x;
        this.y = y;
        this.modelX = modelX;
        this.modelY = modelY;
    }

    //绘制
    void draw(Graphics g) {
        g.fillRect(12 + (modelX + x) * 20, 12 + (modelY + y) * 20, 20, 20);
    }

    //绘制下一个
    void drawNext(Graphics g) {
        g.fillRect(380 + modelX * 20 + 1, 170 + modelY * 20 + 1, 19, 19);
    }


    //预变形
    public boolean preRotate() {
        //旋转万能公式 x=-y y=x
        int x = -modelY + this.x;
        int y = modelX + this.y;
        if (x < 0 || x > 14) {
            return false;
        }
        if (y >= 19) {
            return false;
        }
        return true;
    }

    //移动
    public void move(boolean xDir, int step) {
        if (xDir) {//x方向上的移动，step为正数向左移动，为负数向右移动
            x += step;
        } else {
            y += step;
        }
        panel.repaint();
    }

    public boolean clear() {
        Block[][] blockStack = panel.blockStack;
        Block bott = null;
        for (int i = 0; i < 15; i++) {
            bott = (Block) blockStack[i][y + modelY];
            if (bott == null) {
                return false;
            }
        }
        return true;
    }


    public boolean bottom() {
        if (this.y + modelY == 19) {
            return true;
        }
        return false;
    }

    public boolean outside(boolean xDir, int step) {
        if (xDir) {
            //横向
            int x = (this.x + modelX) + step;
            if (x < 0 || x > 14) {
                //不能移动
                return true;
            }
        } else {
            int y = (this.y + modelY) + step;
            if (y > 19) {
                //不能移动
                return true;
            }
        }
        return false;
    }

    //跟其他方块有无碰撞
    public boolean hitBlock(boolean xDir, int step) {
        Block[][] blockStack = panel.blockStack;
        Block bott = null;
        int x = 0;
        int y = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 20; j++) {
                bott = (Block) blockStack[i][j];
                if (bott != null) {
                    if (xDir) {
                        //横向
                        x = (this.x + modelX) + step;
                        y = (this.y + modelY);
                    } else {
                        //加速下坠
                        x = (this.modelY + modelX);
                        y = (this.y + modelY) + step;
                    }
                    if (x == bott.x + bott.modelX && y == bott.y + bott.modelY) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getModelX() {
        return modelX;
    }

    public void setModelX(int modelX) {
        this.modelY = modelX;
    }

    public int getModelY() {
        return modelY;
    }

    public void setModelY(int modelY) {
        this.modelY = modelY;
    }
}