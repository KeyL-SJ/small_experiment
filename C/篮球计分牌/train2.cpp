#include "train2.h"
#include "ui_train2.h"
#include <QtGui>
#include <QPainter>
#include <QPixmap>
#include <QMenu>
#include "over.h"

int home_score;
int guest_score;
int newGame_flag;
double countDown;
double qtr_countDown;
int qtr_num;
bool start_stop;
int home_stop_num;
int guest_stop_num;
bool home_stop_Enable;
bool guest_stop_Enable;
int home_foul_num;
int guest_foul_num;

void system_init(void);

train2::train2(QWidget *parent)
    : QWidget(parent)
    , ui(new Ui::train2)
{
    ui->setupUi(this);
    this->resize(1400,800);
    this->setWindowTitle("篮球记分牌");
    timer = new QTimer(this);
    system_init();

    ui->time_LCD->display(QString::number(countDown,'f',2));
    ui->qtr_time_lcd->display(QString::number(qtr_countDown,'f',2));
    ui->qtr_LCDNumber->display(qtr_num);
    ui->guest_stop_num_lcd->display(guest_stop_num);
    ui->home_stop_num_lcd->display(home_stop_num);

    button_init();
    QObject::connect(timer,SIGNAL(timeout()),this,SLOT(countDown_24()));
}

train2::~train2()
{
    delete ui;
}

void train2::paintEvent(QPaintEvent *){
    QPainter* painter = new QPainter(this);
    QPixmap pixmap = QPixmap(":/球队主场/勇士.jpg");
    painter->drawPixmap(0,0,this->width(),this->height(),pixmap);
    painter->end();
}

void train2::countDown_24(void){
    countDown -= 0.01;
    qtr_countDown -= 0.01;

    if(countDown <= 0){
        countDown = 0;
        start_stop = 0;
        timer->stop();
    }

    if(qtr_countDown <= 0){
        qtr_countDown = 0;
        qtr_num++;
        qtr_countDown = QTR_TIME_NUM;
        countDown = TIME_NUM;
        if(qtr_num > 4){
            if(home_score == guest_score){
                qtr_num = 5;
            }
            else{
                qtr_num = 9;
                countDown = 0;
                qtr_countDown = 0;
                over* overWindow = new over;
                overWindow->show();
            }
        }
        start_stop = 0;
        timer->stop();

    }
    ui->home_LCDNumber->display(home_score);
    ui->guest_LCDNumber->display(guest_score);
    ui->time_LCD->display(QString::number(countDown,'f',2));
    ui->qtr_time_lcd->display(QString::number(qtr_countDown,'f',2));
    ui->qtr_LCDNumber->display(qtr_num);
    ui->guest_stop_num_lcd->display(guest_stop_num);
    ui->home_stop_num_lcd->display(home_stop_num);
    ui->home_foul_num_lcd->display(home_foul_num);
    ui->guest_foul_num_lcd->display(guest_foul_num);
}

void train2::on_start_button_clicked()
{
    if(start_stop == 0){
        if(countDown == 0){
            if(qtr_countDown < 24){
                countDown = qtr_countDown;
            }
            else{
                countDown = TIME_NUM;
            }
        }
        home_stop_Enable = 1;
        guest_stop_Enable = 1;
        start_stop = 1;
        timer->start(10);
    }
}

void train2::on_stop_button_clicked()
{
    if(start_stop == 1){
        start_stop = 0;
        timer->stop();
    }
}

void train2::on_return_button_clicked()
{
    if(start_stop == 0){
        countDown = TIME_NUM;
        if(countDown>qtr_countDown && qtr_countDown <= 24){
            countDown = qtr_countDown;
        }
    }
    ui->time_LCD->display(QString::number(countDown,'f',2));
}

void train2::button_init(void){
    QMenu* menu_add = new QMenu();
    QMenu* menu_dec = new QMenu();

    menu_add->addAction("+1");
    menu_add->addAction("+2");
    menu_add->addAction("+3");

    menu_dec->addAction("-1");
    menu_dec->addAction("-2");
    menu_dec->addAction("-3");

    ui->home_add_toolButton->setMenu(menu_add);
    ui->guest_add_toolButton->setMenu(menu_add);

    ui->home_dec_toolButton->setMenu(menu_dec);
    ui->guest_dec_toolButton->setMenu(menu_dec);

}

void train2::on_home_add_toolButton_triggered(QAction *arg1){
    if(arg1->toolTip() == "+1"){
        home_score++;
        if(start_stop == 1){
            start_stop = 0;
            timer->stop();
        }
    }
    if(arg1->toolTip() == "+2"){
        home_score+=2;
        if(start_stop == 1){
            start_stop = 0;
            timer->stop();
        }
    }
    if(arg1->toolTip() == "+3"){
        home_score+=3;
        if(start_stop == 1){
            start_stop = 0;
            timer->stop();
        }
    }
    ui->home_LCDNumber->display(home_score);
}


void train2::on_home_dec_toolButton_triggered(QAction *arg1)
{
    if(arg1->toolTip() == "-1"){
        home_score--;
        if(home_score<=0){
            home_score=0;
        }
        start_stop = 0;
        timer->stop();
    }
    if(arg1->toolTip() == "-2"){
        home_score-=2;
        if(home_score<=0){
            home_score=0;
        }
        start_stop = 0;
        timer->stop();
    }
    if(arg1->toolTip() == "-3"){
        home_score-=3;
        if(home_score<=0){
            home_score=0;
        }
        start_stop = 0;
        timer->stop();
    }
    ui->home_LCDNumber->display(home_score);
}

void train2::on_guest_add_toolButton_triggered(QAction *arg1)
{
    if(arg1->toolTip() == "+1"){
        guest_score++;
        if(start_stop == 1){
            start_stop = 0;
            timer->stop();
        }
    }
    if(arg1->toolTip() == "+2"){
        guest_score+=2;
        if(start_stop == 1){
            start_stop = 0;
            timer->stop();
        }
    }
    if(arg1->toolTip() == "+3"){
        guest_score+=3;
        if(start_stop == 1){
            start_stop = 0;
            timer->stop();
        }
    }
    ui->guest_LCDNumber->display(guest_score);
}

void train2::on_guest_dec_toolButton_triggered(QAction *arg1)
{
    if(arg1->toolTip() == "-1"){
        guest_score--;
        if(guest_score<=0){
            guest_score = 0;
        }
        start_stop = 0;
        timer->stop();
    }
    if(arg1->toolTip() == "-2"){
        guest_score-=2;
        if(guest_score<=0){
            guest_score = 0;
        }
        start_stop = 0;
        timer->stop();
    }
    if(arg1->toolTip() == "-3"){
        guest_score-=3;
        if(guest_score<=0){
            guest_score = 0;
        }
        start_stop = 0;
        timer->stop();
    }
    ui->guest_LCDNumber->display(guest_score);
}

void system_init(void){
    countDown = TIME_NUM;
    qtr_countDown = QTR_TIME_NUM;
    qtr_num = 1;
    start_stop = 0;
    home_score = 0;
    guest_score = 0;
    newGame_flag = 0;
    home_stop_num = 5;
    guest_stop_num = 5;
    home_stop_Enable = 1;
    guest_stop_Enable = 1;
    home_foul_num = 0;
    guest_foul_num = 0;
}

void train2::on_home_stop_button_clicked()
{
    if(home_stop_Enable == 1 && home_stop_num > 0){
        guest_stop_Enable = 0;
        if(start_stop == 1){
            home_stop_num--;
            if(home_stop_num >= 0){
                start_stop = 0;
                timer->stop();
            }
            else{
                home_stop_num = 0;
            }
        }
    }
    ui->home_stop_num_lcd->display(home_stop_num);
}

void train2::on_guest_stop_button_clicked()
{
    if(guest_stop_Enable == 1 && guest_stop_num > 0){
        home_stop_Enable = 0;
        if(start_stop == 1){
            guest_stop_num--;
            if(guest_stop_num >= 0){
                start_stop = 0;
                timer->stop();
            }
            else{
                guest_stop_num = 0;
            }
        }
    }
    ui->guest_stop_num_lcd->display(guest_stop_num);
}

void train2::on_home_foul_button_clicked()
{
    home_foul_num++;
    ui->home_foul_num_lcd->display(home_foul_num);
    start_stop = 0;
    timer->stop();
}

void train2::on_guest_foul_button_clicked()
{
    guest_foul_num++;
    ui->guest_foul_num_lcd->display(guest_foul_num);
    start_stop = 0;
    timer->stop();
}
