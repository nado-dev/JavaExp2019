package scoreManagementSys;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;


/**
 * @author 房文宇
 *@version 2019/12/4
 */
@SuppressWarnings("serial")
class MainMenu extends JFrame{
	

   public MainMenu()
   {  
      this.setBounds(300, 100, 550, 430);//位置参数
      this.setTitle("学生成绩信息管理系统");//title
      this.setLayout(null);//布局
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
      this.setVisible(true);
      
      JLabel labWelcome = new JLabel("欢迎进入学生成绩管理系统");
      labWelcome.setBounds(50, 2, 550, 45);
      JLabel labChooseIden = new JLabel("选择您的身份");
      labChooseIden.setBounds(50, 100, 100, 50);
      
      JButton btntapStu = new JButton("学生"); 
      btntapStu.setBounds(50, 185, 80, 20);
      btntapStu.setForeground(Color.BLUE);
      
      JButton btntapTea = new JButton("教师"); 
      btntapTea.setBounds(150, 185, 80, 20);
      btntapTea.setForeground(Color.BLUE);
      
      JButton btntapAO = new JButton("教务员"); 
      btntapAO.setBounds(250, 185, 80, 20);
      btntapAO.setForeground(Color.BLUE);
      
      JButton btntapAD = new JButton("管理员"); 
      btntapAD.setBounds(350, 185, 80, 20);
      btntapAD.setForeground(Color.BLUE);
      

      this.add(labWelcome);
      this.add(labChooseIden);
      this.add(btntapStu);
      this.add(btntapTea);
      this.add(btntapAO);
      this.add(btntapAD);
      
      btntapStu.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	      new Student();
	      }
	      } );
      
      btntapTea.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          //new StudentFrame();
          }
          } );
      
      btntapAO.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          //new StudentFrame();
          }
          } );
      
      btntapAD.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          new Admin();
          }
          } );
      
      
      
   }  
}


