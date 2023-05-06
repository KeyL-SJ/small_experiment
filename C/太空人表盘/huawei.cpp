#define _CRT_SECURE_NO_WARNINGS 1
#include <stdio.h>
#include <time.h>

#include "huawei.h"
#include <mmsystem.h>
#pragma comment(lib,"winmm.lib")

#define WIN_SIZE 500
#define WIN_HALF (WIN_SIZE/2)	//窗口的一半
IMAGE spaceMan[59];
IMAGE other[6];
const char *week[7] = { "日","一","二","三","四","五","六" };
void setTextStyle(int height, int width, const char *faceName)
{
	LOGFONT f = { 0 };
	f.lfHeight = height;
	f.lfWidth = width;
	f.lfQuality = ANTIALIASED_QUALITY;
	strcpy(f.lfFaceName, faceName);
	settextstyle(&f);
}
void loadImg()
{
	//mciSendString("open ./images/heykong.mp3", NULL, 0, NULL);
	//mciSendString("play ./images/heykong.mp3 repeat", NULL, 0, NULL);

	char fileName[50] = { 0 };
	for (int i = 0; i < 30; i++)
	{
		sprintf_s(fileName, "./images/guoguoxiaoshidi (%d).jpeg", i + 1);
		loadimage(spaceMan + i, fileName, 140, 130);
	}

	loadimage(&other[0], "./images/xinlv.jpg", 60, 60);		//心率
	loadimage(&other[1], "./images/sun.jpg", 40, 40);		//太阳
	loadimage(&other[2], "./images/shoes.jpg", 40, 40);		//鞋子
	loadimage(&other[3], "./images/shang.jpg", 30, 30);		//上箭头
	loadimage(&other[4], "./images/xia.jpg", 30, 30);		//下箭头
	loadimage(&other[5], "./images/rocket.jpg", 40, 40);	//火箭
}

//太空人旋转动画
void animation()
{
	static int index = 0;	//[0~59)
	putimage(175, 210, spaceMan + index);
	static DWORD t1;
	DWORD t2 = clock();	//获取程序运行到调用该函数经过的毫秒
	if (t2 - t1 > 20)
	{
		index = (index + 1) % 30;
		t1 = t2;
	}
}
void gameDraw()
{
	setbkcolor(RGB(255, 0, 0));
	cleardevice();
	//绘制表盘
	setlinecolor(RGB(0, 0, 0));//设置边框颜色
	setlinestyle(PS_SOLID, 30);
	setfillcolor(RGB(255, 255, 255));//设置圆的填充白色
	fillellipse(0, 0, WIN_SIZE, WIN_SIZE);//绘制一个圆

	//绘制线条
	setlinestyle(PS_SOLID, 4);
	setlinecolor(BLACK);
	//最上面竖线
	line(WIN_HALF - 30, 20, WIN_HALF - 30, 130);
	//横线x2
	line(WIN_HALF - 195, WIN_HALF - 120, WIN_HALF + 195, WIN_HALF - 120);
	line(WIN_HALF - 195, WIN_HALF + 120, WIN_HALF + 195, WIN_HALF + 120);
	//下面线条x3
	line(WIN_HALF + 80, WIN_HALF + 120, WIN_HALF + 80, WIN_HALF + 175);
	line(WIN_HALF + 80, WIN_HALF + 175, WIN_HALF - 60, WIN_HALF + 175);
	line(WIN_HALF - 60, WIN_HALF + 175, WIN_HALF - 60, WIN_HALF + 175 + 48);


	setbkmode(TRANSPARENT);
	//左上空气湿度90%
	setTextStyle(55, 23, "Arial");
	settextcolor(BLACK);
	outtextxy(WIN_HALF - 155, 75, "90%");
	drawImg(other + 5, WIN_HALF - 90, 35);	//火箭
											//右上
	putimage(WIN_HALF - 90, 35, other + 5);

	setTextStyle(25, 15, "黑体");
	outtextxy(WIN_HALF - 25, 35, "空气良好");
	setTextStyle(25, 13, "宋体");
	outtextxy(WIN_HALF - 25, 65, "晴天");
	outtextxy(WIN_HALF - 25, 95, "25℃");
	outtextxy(WIN_HALF + 38, 65, "26°");
	outtextxy(WIN_HALF + 38, 95, "17°");
	drawImg(other + 4, WIN_HALF + 73, 60);	//上面的箭头
	drawImg(other + 3, WIN_HALF + 73, 90);	//下面的箭头
	drawImg(other + 1, WIN_HALF + 105, 70);	//太阳
	putimage(WIN_HALF + 73, 60, other + 4);
	putimage(WIN_HALF + 73, 90, other + 3);
	putimage(WIN_HALF + 105, 70, other + 1);
	
	// 下部分
	setTextStyle(37, 17, "宋体");
	outtextxy(100, WIN_HALF + 130, "睡眠");
	outtextxy(WIN_HALF + 90, WIN_HALF + 130, "距离");
	outtextxy(50, WIN_HALF-40, "桂林");

	setTextStyle(40, 15, "Arial");
	outtextxy(185, WIN_HALF + 125, "7h30m");
	outtextxy(215, WIN_HALF + 180, "9.88km");

	//中间
	//心率
	setTextStyle(25, 13, "宋体");
	outtextxy(60, WIN_HALF + 30, "80~128");
	drawImg(&other[0], 65, WIN_HALF + 50);	//心率图
	putimage(65, WIN_HALF + 50, other + 0);

	setTextStyle(40, 15, "Arial");
	outtextxy(135, WIN_HALF + 60, "92");
	
	// 步数
	drawImg(other + 2, WIN_HALF + 65, WIN_HALF + 65);
	putimage(WIN_HALF + 65, WIN_HALF + 65, &other[2]);
	outtextxy(WIN_HALF + 125, WIN_HALF + 75, "9527");

	//时间、日期相关
	time_t timep = time(NULL);			//获取当前时间
	struct tm* p = localtime(&timep);	//把时间转成格式化时间

	setTextStyle(25, 12, "宋体");
	outtextxy(WIN_HALF + 110, WIN_HALF - 20, "十月六日");
	char fileName[40] = { 0 };
	sprintf_s(fileName, "周%s %d-%d", week[p->tm_wday], p->tm_mon + 1, p->tm_mday);
	outtextxy(WIN_HALF + 110, WIN_HALF + 10, fileName);

	// 获取字体
	setTextStyle(100, 40, "Arial");
	char szBuf[40] = { 0 };
	sprintf_s(szBuf, "%d:%02d", p->tm_hour, p->tm_min);
	outtextxy(105, 120, szBuf);
	// 秒
	setTextStyle(55, 23, "Arial");
	sprintf(szBuf, "%02d", p->tm_sec);
	outtextxy(335, 160, szBuf);
}
int main()
{
	initgraph(WIN_SIZE, WIN_SIZE);
	SetWindowNewStyle(WIN_SIZE, WIN_SIZE);
	loadImg();
	BeginBatchDraw();//双缓冲 防止闪屏
	while (true)
	{
		gameDraw();
		animation();
		mouseEvent();
		FlushBatchDraw();
	}
	EndBatchDraw();
	return 0;
}