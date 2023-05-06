#define _CRT_SECURE_NO_WARNINGS 1
#undef UNICODE
#undef _UNICODE

#include<stdio.h>
#include<time.h>
#include<graphics.h>
#include<Windows.h>
#include<stdlib.h>
#include<conio.h>

#include<mmsyscom.h>
#pragma comment(lib,"winmm.lib")

#define WIN_WIDTH 640
#define WIN_HEIGHT 480
#define MAX_SNAKE 100
enum DIR
{
	UP,
	DOWN,
	LEFT,
	RIGHT,
};

struct Snake_tlg//贪吃蛇结构体
{
	int num;
	int dir;
	int score;
	int size;
	POINT coor[MAX_SNAKE];
}snake;

struct Food_tlg //食物的出现 结构体
{
	POINT fd;//食物的坐标
	int flag;//是否被吃掉
	DWORD color;//定义食物的颜色
}food;

void Gameinit()
{
	srand((unsigned int)time(NULL));
	snake.num = 3;
	snake.dir = RIGHT;
	snake.score = 0;
	snake.size = 10;
	snake.coor[2].x = 0;
	snake.coor[2].y = 0;
	snake.coor[1].x = 0 + snake.size;
	snake.coor[1].y = 0;
	snake.coor[0].x = 0 + 2 * snake.size;
	snake.coor[0].y = 0;

	food.fd.x = rand() % (WIN_WIDTH / 10) * 10;
	food.fd.y = rand() % (WIN_HEIGHT / 10) * 10;
	food.flag = 1;//初始化食物存在
	food.color = RGB(rand() % 256, rand() % 256, rand() % 256);//随机产生食物的颜色

	mciSendString("open ./heykong.mp3", NULL, 0, NULL);
	mciSendString("play ./heykong.mp3 repeat", NULL, 0, NULL);
}

void GameDraw()
{
	setbkcolor(RGB(105, 160, 141));
	cleardevice();//清除其他元素

	//画蛇
	setfillcolor(YELLOW);
	int i;
	for (i = 0; i < snake.num; i++)//每次都画一个矩形，画三次
	{
		setfillcolor(BLACK);
		fillrectangle(snake.coor[i].x,
			snake.coor[i].y,
			snake.coor[i].x + snake.size,
			snake.coor[i].y + snake.size);
	}
	//画食物
	if (food.flag == 1)
	{
		setfillcolor(food.color);
		fillellipse(food.fd.x, food.fd.y, food.fd.x + 10, food.fd.y + 10);
	}
	char temp[20] = "";
	sprintf(temp, "分数:%d", snake.score);
	outtextxy(20, 20, temp);
}

void KeyControl()
{
	if (GetAsyncKeyState(VK_UP) && snake.dir != DOWN)
	{
		snake.dir = UP;
	}
	if (GetAsyncKeyState(VK_DOWN) && snake.dir != UP)
	{
		snake.dir = DOWN;
	}
	if (GetAsyncKeyState(VK_LEFT) && snake.dir != RIGHT)
	{
		snake.dir = LEFT;
	}
	if (GetAsyncKeyState(VK_RIGHT) && snake.dir != LEFT)
	{
		snake.dir = RIGHT;
	}
}

void snakeMove()
{
	int i;
	for (i = snake.num - 1; i > 0; i--)
	{
		snake.coor[i].x = snake.coor[i - 1].x;
		snake.coor[i].y = snake.coor[i - 1].y;
	}
	switch (snake.dir)
	{
	case UP:snake.coor[0].y -=10;
		if (snake.coor[0].y + 10 <= 0)
		{
			snake.coor[0].y =WIN_HEIGHT;
		}
		break;
	case DOWN:snake.coor[0].y += 10;
		if (snake.coor[0].y >= WIN_HEIGHT)
		{
			snake.coor[0].y = 0;
		}
		break;
	case LEFT:snake.coor[0].x -= 10;
		if (snake.coor[0].x + 10 <= 0)
		{
			snake.coor[0].x = WIN_WIDTH;
		}
		break;
	case RIGHT:snake.coor[0].x += 10;
		if (snake.coor[0].x >= WIN_WIDTH)
		{
			snake.coor[0].x = 0;
		}
		break;
	default:printf("错误操作，请重新操作"); break;
	}
}

void eatFood()
{
	if (snake.coor[0].x == food.fd.x && snake.coor[0].y == food.fd.y && food.flag == 1)
	{
		snake.num++;
		snake.score += 10;
		food.flag = 0;
	}
	if (food.flag == 0)
	{
		food.fd.x = rand() % (WIN_WIDTH / 10) * 10;
		food.fd.y = rand() % (WIN_HEIGHT / 10) * 10;
		food.flag = 1;
		food.color = RGB(rand() % 256, rand() % 256, rand() % 256);
	}
}

void DontEatSelf()
{
	for (int i = 4; i < snake.num; i++)
	{
		if (snake.coor[0].x == snake.coor[i].x && snake.coor[0].y == snake.coor[i].y)
		{
			outtextxy(200, 200, "Game Over!");
			_getch();
			exit(666);
		}
	}
}

void Pause()
{
	if (_getch() == 32)
	{
		while (1)
		{
			_getch();
		}
	}
}


int main(void)
{
	initgraph(WIN_WIDTH, WIN_HEIGHT, SHOWCONSOLE);
	printf("\tWelcome!\n");
	printf("Let Me Play a Game");
	Gameinit();
	while (1)
	{
		snakeMove();
		GameDraw();
		eatFood();
		KeyControl();
		DontEatSelf();
		Sleep(50);
		Pause();
	}
	getchar();
	closegraph();

}
