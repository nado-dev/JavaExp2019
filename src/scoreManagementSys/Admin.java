package scoreManagementSys;

import java.util.ArrayList;   
import java.util.Scanner;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;




//系统管理员
/**
* @author 房文宇
*@version 2019/12/4
*/
class Admin extends JFrame{
	String  adminId = "admin";
	String  adminPW = "admin";
	Boolean isVerify;
	JLabel adminIDTips;
	JTextField adminIDInput;
	JLabel adminPwTips;
	JTextField adminPwInput;
	JButton login;
	
	public Admin() {
		this.setBounds(300, 150, 500, 450);//位置参数
	    this.setTitle("管理员登录");//title
	    this.setLayout(null);//布局
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
	    this.setVisible(true);
	    
	    AdminLogin();
	   
	}
	void AdminLogin() {
		adminIDTips = new JLabel("管理员账户(admin):");
		adminIDTips.setBounds(50, 60, 150, 50);
			
		adminIDInput = new JTextField("",30);
		adminIDInput.setBounds(200, 76, 180, 30); 
			
		adminPwTips = new JLabel("管理员密码(admin):");
		adminPwTips.setBounds(50, 100, 150, 50);
			
		adminPwInput = new JTextField("",30);
		adminPwInput.setBounds(200, 117, 180, 30);
			
		login = new JButton("登录"); 
		login.setBounds(150, 250, 180, 30);
		login.setForeground(Color.BLUE);
			
		this.add(adminIDTips);
		this.add(adminIDInput);
		this.add(adminPwTips);
		this.add(adminPwInput);
		this.add(login);
			
		login.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		          verify();
		          }});
	}
		
	
	
	@SuppressWarnings("static-access")
	void verify() {
		String idInput = null;
		String pwInput = null;
		if (isBlank()) {
			
			idInput = adminIDInput.getText();
			pwInput = adminPwInput.getText();
		}
			
		if (idInput.equals(this.adminId) && pwInput.equals(this.adminPW)) {
			
			JOptionPane jO = null;
			int option = JOptionPane.YES_OPTION;
			jO.showMessageDialog(null, "登录成功");
			if(option == jO.YES_OPTION) {
				new AdminMenu();
			}
			this.isVerify = true;
		}
		else {
			JOptionPane.showMessageDialog(null, "账号或密码错误，请重试");
		}
	}
	
	
	public boolean isBlank() {
		if(adminIDInput.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "账号输入有误 请重新输入！");
			return false;			
		}
		if(adminPwInput.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "密码输入有误 请重新输入！");
			return false;			
		}
		return true;
	}
	
	//内部类菜单
	@SuppressWarnings("serial")
	private class AdminMenu extends JFrame{
		public AdminMenu() {
			this.setBounds(300, 100, 550, 430);//位置参数
		    this.setTitle("管理员admin");//title
		    this.setLayout(null);//布局
		    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
		    this.setVisible(true);
		    
		    
		    JLabel labWelcome = new JLabel("欢迎您，admin");
		    labWelcome.setBounds(50, 2, 550, 45);
		      
		    JLabel labChoosefunc = new JLabel("选择一个功能");
		    labChoosefunc.setBounds(50, 100, 100, 50);
		    
		    JButton btntapBackups = new JButton("备份"); 
		    btntapBackups.setBounds(50, 185, 80, 20);
		    btntapBackups.setForeground(Color.BLUE);
		    
		    this.add(labWelcome);
		    this.add(labChoosefunc);
		    this.add(btntapBackups);
		    
		    btntapBackups.addActionListener(new ActionListener() {
		          public void actionPerformed(ActionEvent e) {
		          //new SystemAdminLogin();
		          }
		          } );
		}
	}
}
