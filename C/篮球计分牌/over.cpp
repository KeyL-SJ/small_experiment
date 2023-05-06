#include "over.h"
#include "ui_over.h"

over::over(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::over)
{
    ui->setupUi(this);

    extern int home_score;
    extern int guest_score;
    extern int newGame_flag;
    extern int newGame_flag;
    extern double countDown;
    extern double qtr_countDown;
    extern int qtr_num;
    extern bool start_stop;
    extern int home_stop_num;
    extern int guest_stop_num;
    extern bool home_stop_Enable;
    extern bool guest_stop_Enable;
    extern int home_foul_num;
    extern int guest_foul_num;

    this->setWindowTitle("game over");

    extern void system_init(void);

    if(home_score > guest_score){
        win = "主场";
    }
    else{
        win = "客场";
    }
    ui->information->setText("获胜方:"+win+"\n"+"总比分："+QString::number(home_score,3,0)+":"+QString::number(guest_score,3,0));
}

over::~over()
{
    delete ui;
}


void over::on_newgame_Button_clicked()
{
    system_init();
    this->close();
}

void over::on_quit_Button_clicked()
{
    exit(0);
}
