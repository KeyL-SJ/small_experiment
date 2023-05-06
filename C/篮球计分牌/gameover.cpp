#include "gameover.h"
#include "ui_gameover.h"
#include <tra
gameOver::gameOver(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::gameOver)
{
    ui->setupUi(this);
}

gameOver::~gameOver()
{
    delete ui;
}

void gameOver::on_newgame_button_clicked()
{

}
