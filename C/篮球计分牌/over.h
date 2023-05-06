#ifndef OVER_H
#define OVER_H

#include <QWidget>
#include <train2.h>
namespace Ui {class over;}

class over : public QWidget
{
    Q_OBJECT

public:
    explicit over(QWidget *parent = nullptr);
    ~over();
    QString win;
private slots:
    void on_newgame_Button_clicked();

    void on_quit_Button_clicked();

private:
    Ui::over *ui;
};

#endif // OVER_H
