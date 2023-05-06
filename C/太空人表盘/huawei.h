#ifndef _HUAWEI_H_
#define _HUAWEI_H_
#include<graphics.h>

void SetWindowNewStyle(int w, int h)
{
	// 去掉标题
	SetWindowLong(GetHWnd(), GWL_STYLE, GetWindowLong(GetHWnd(), GWL_STYLE) & ~WS_CAPTION);
	// 初始化界面为圆形
	SetWindowRgn(GetHWnd(), CreateEllipticRgn(0, 0, w, h), true);
}
//png透明贴图
void drawImg(IMAGE* pimg, int x, int y)
{
	// 变量初始化
	DWORD* dst = GetImageBuffer();
	DWORD* src = GetImageBuffer(pimg);
	int dst_width = ::getwidth();			//窗口宽高
	int dst_height = ::getheight();
	int _w = pimg->getwidth();
	int _h = pimg->getheight();

	// 计算贴图的实际长宽
	int iwidth = (x + _w > dst_width) ? dst_width - x : _w;		// 处理超出右边界
	int iheight = (y + _h > dst_height) ? dst_height - y : _h;	// 处理超出下边界
	if (x < 0) { src += -x;				iwidth -= -x;	x = 0; }				// 处理超出左边界
	if (y < 0) { src += (_w * -y);	iheight -= -y;	y = 0; }				// 处理超出上边界
																		// 修正贴图起始位置
	dst += (dst_width * y + x);

	// 实现透明贴图
	for (int iy = 0; iy < iheight; iy++)
	{
		for (int ix = 0; ix < iwidth; ix++)
		{
			byte a = (byte)(src[ix] >> 24);//计算透明通道的值[0,256) 0为完全透明 255为完全不透明
			if (a > 100)
			{
				dst[ix] = src[ix];
			}
		}
		//换到下一行
		dst += dst_width;
		src += _w;
	}
}
//去掉窗口标题以后，能够点击移动窗口
void mouseEvent()
{
	//求鼠标相对于当前窗口左上角的坐标(即鼠标距离窗口左上的的宽度和高度)
	static POINT WndPtSize;
	static bool isMove = false;
	if (MouseHit())
	{
		MOUSEMSG msg = GetMouseMsg();
		if (msg.uMsg == WM_LBUTTONDOWN)	//左键按下
		{
			//获取窗口相对与屏幕左上角的 左上角坐标，和右下角坐标
			RECT wndRect;
			GetWindowRect(GetHWnd(), &wndRect);


			//获取鼠标光标相对于屏幕的坐标
			POINT curPos;
			GetCursorPos(&curPos);

			//求鼠标相对于当前窗口左上角的坐标
			WndPtSize.x = curPos.x - wndRect.left;
			WndPtSize.y = curPos.y - wndRect.top;

			isMove = true;
		}
		else if (msg.uMsg == WM_LBUTTONUP)//左键弹起
		{
			isMove = false;
		}
		else if (msg.uMsg == WM_MOUSEMOVE)//鼠标移动
		{
			if (isMove)
			{
				POINT CursorPos;
				GetCursorPos(&CursorPos);
				/*把窗口移动到屏幕的x，y的位置
				* @hwnd:窗口句柄
				* @hwndInsertAfter:窗口的z顺序  HWND_TOPMOST {在前面, 位于任何顶部窗口的前面}
				* @X，Y: 窗口左上角的新位置（相对于屏幕）
				* @cx，xy: 窗口大小
				* @uFlags:SWP_NOSIZE {忽略 cx、cy, 保持大小}
				*/
				SetWindowPos(GetHWnd(), HWND_TOPMOST, CursorPos.x - WndPtSize.x, CursorPos.y - WndPtSize.y, 0, 0, SWP_NOSIZE);
				//CursorPos.x - WndPtSize.x	//获取当前窗口左上角相对于屏幕的坐标
				//CursorPos.y - WndPtSize.y

				//printf("%d %d\n", CursorPos.x - WndPtSize.x, CursorPos.y - WndPtSize.y);
			}
		}
		else if (msg.uMsg == WM_RBUTTONDOWN)//右键按下
		{
			exit(0);
		}
	}
}

#endif // !_TOOLS_H_

