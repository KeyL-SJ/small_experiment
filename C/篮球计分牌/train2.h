#ifndef TRAIN2_H
#define TRAIN2_H

#include <QWidget>
#include "ui_train2.h"


extern int home_score;
extern int guest_score;
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

extern void system_init(void);

QT_BEGIN_NAMESPACE
namespace Ui { class train2; }
QT_END_NAMESPACE

class train2 : public QWidget
{
    Q_OBJECT

public:
    #define TIME_NUM 24.00;
    #define QTR_TIME_NUM 36.00;
    train2(QWidget *parent = nullptr);
    ~train2();
    QTimer *timer;
protected:
    void paintEvent(QPaintEvent *);
    void button_init(void);
private slots:
    void countDown_24(void);
    void on_start_button_clicked();
    void on_stop_button_clicked();
    void on_home_add_toolButton_triggered(QAction *arg1);
    void on_return_button_clicked();
    void on_home_dec_toolButton_triggered(QAction *arg1);
    void on_guest_add_toolButton_triggered(QAction *arg1);
    void on_guest_dec_toolButton_triggered(QAction *arg1);
    void on_home_stop_button_clicked();

    void on_guest_stop_button_clicked();

    void on_home_foul_button_clicked();

    void on_guest_foul_button_clicked();

private:
    Ui::train2 *ui;
};
#endif // TRAIN2_H
