package scoreManagementSys;

import java.io.*;
import java.util.*;
import javax.swing.*;

import scoreManagementSys.Student.CourseSearch;
import scoreManagementSys.Student.PersonalInfo;
import scoreManagementSys.Student.StdMenu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 *@author 李子桐
 *@version 2019/12/8
 */
public class Teacher extends JFrame{
	String staffNum;
	String name;
	String school;
	String department;
    String password;
    
    Teacher loginTea;
    ArrayList<Teacher> allTeaList;
    int loginTeaIndex;
    
    JLabel teaIDTips;
	JTextField teaIDInput;
	JLabel teaPwTips;
	JTextField teaPwInput;
	JButton login;
	
	
	Teacher(){
		this.staffNum = "Unknown";
		this.name = "Unknown";
		this.school = "Unknown";
		this.department = "Unknown";
		this.password = "0000";
		
		this.setBounds(300, 150, 500, 450);//位置参数
	    this.setTitle("教师登录");//title
	    this.setLayout(null);//布局
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
	    this.setVisible(true);
	    
	    Login();
	}
	
	
    void SetData(String staffNum, String name, String school,String department,String password){
		this.staffNum = staffNum;
		this.name = name;
		this.school = school;
		this.department = department;
		this.password = password;
    }
    
    
    ArrayList<Teacher> Get_teachers(){
        ArrayList<Teacher> teachers = new ArrayList<>();
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader("./teacher.txt"));
            while (true) {
                try {
                    if ((line = br.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                    return teachers;
                }
                Scanner scan = new Scanner(line).useDelimiter("\\s+");
                String[] info = new String[5];
                for (int i=0;i<5;i++){
                    info[i] = scan.next();
                }
                Teacher teacher = new Teacher();
                teacher.SetData(info[0], info[1], info[2], info[3], info[4]);
                teachers.add(teacher);
            }
        }catch (FileNotFoundException notfound){
            File file = new File("./teacher.txt");
            try {
                file.createNewFile();
            }catch (IOException io){
                io.printStackTrace();
            }
        }
        return teachers;
    } 
    
    void Login() {
    	teaIDTips = new JLabel("教师职工编号:");
		teaIDTips.setBounds(50, 60, 150, 50);
			
		teaIDInput = new JTextField("",30);
		teaIDInput.setBounds(200, 76, 180, 30); 
			
		teaPwTips = new JLabel("密码:");
		teaPwTips.setBounds(50, 100, 150, 50);
			
		teaPwInput = new JPasswordField("",30);
		teaPwInput.setBounds(200, 117, 180, 30);
			
		login = new JButton("登录"); 
		login.setBounds(150, 250, 180, 30);
		login.setForeground(Color.BLUE);
			
		this.add(teaIDTips);
		this.add(teaIDInput);
		this.add(teaPwTips);
		this.add(teaPwInput);
		this.add(login);
		
		login.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		          verify();
		          }});
    }
    
    void verify() { 
    	ArrayList<Teacher> teachers = this.Get_teachers();
    	this.allTeaList = teachers;
    	String teaNum = null;
    	String password = null;
    	boolean ID_exist = false;
    	int tea_index = -1;
    	
    	Teacher temp_teacher = new Teacher();
    	
    	if (isBlank()) {			
        	teaNum = teaIDInput.getText();
        	password = teaPwInput.getText();
		}
    	
    	for (Teacher teacher : teachers) {
            if (teaNum.equals(teacher.staffNum)) {
                ID_exist = true;
                temp_teacher = teacher;
                tea_index = teachers.indexOf(teacher);//获取下标
                this.loginTeaIndex = tea_index;//保存下标待用
            }
        }
    	
    	if (!ID_exist){
        	JOptionPane.showMessageDialog(null, "账号输入有误，请重新输入！如输入无误，请联系教务员");
        }
    	
    	//验证密码
    	else {
    		if(temp_teacher.password.equals(password)) {
    			if(password.equals("0000")) {
    				int op = JOptionPane.showConfirmDialog(null, "您正在使用初始密码登录，是否现在修改密码！","安全提示",JOptionPane.YES_NO_CANCEL_OPTION);      	
    				if (op == JOptionPane.YES_OPTION) {
						String newPassWord = changePWConfirm();
						temp_teacher.password = newPassWord;
						this.allTeaList.set(this.loginTeaIndex, temp_teacher);
						
						try {
							BufferedWriter bw = new BufferedWriter(new FileWriter("./teacher.txt"));
                            for (Teacher teacher : this.allTeaList) { // 写入student.txt保存更改
                                bw.write(String.format("%s\t\t%s\t\t%s\t\t%s\t\t%s\n", teacher.staffNum, teacher.name, teacher.school, teacher.department, teacher.password));
                            }
                            bw.close();
						}catch (IOException e) {
							e.printStackTrace();
						}
						JOptionPane.showMessageDialog(null, "密码已更新，修改成功");
					}
    			}
    			this.loginTea = temp_teacher;
    			JOptionPane jO = null;
    			int option = JOptionPane.YES_OPTION;
    			JOptionPane.showMessageDialog(null, temp_teacher.name + " 登录成功"); 			
    			new TeaMenu();
    		}
    	}
    }
    
    public boolean isBlank() {
		if(teaIDInput.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "账号输入有误 请重新输入！");
			return false;			
		}
		if(teaPwInput.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "密码输入有误 请重新输入！");
			return false;			
		}
		return true;
	}
    
    String changePWConfirm() {
    	String result = null;
		for(;;) {
    		//TODO 修改密码

    		String newPassWord1 = (String)JOptionPane.showInputDialog("输入新的密码");
    		if (newPassWord1 == null ) {
    			break;
			}
    		if (newPassWord1.equals("")){
    			JOptionPane.showMessageDialog(null, "请正确输入！");
    			continue;
    		}
    		
    		String newPassWord2 = (String)JOptionPane.showInputDialog("再次输入以确认您的密码");
    		if (newPassWord2 == null ) {
    			break;
			}
    		if (newPassWord2.equals("")){
    			JOptionPane.showMessageDialog(null, "请正确输入！");
    			continue;
    		}
    		
    		if (newPassWord1.equals(newPassWord2)) {
    			JOptionPane.showMessageDialog(null, "密码修改成功");
				result = newPassWord1;
				break;
			}
    		else {
    			JOptionPane.showConfirmDialog(null, "两次修改不一致，请再试一次！");
    			continue;
    		}
    	}
		return result;
    }
    
  //内部类菜单
  	class TeaMenu extends JFrame{
  		public TeaMenu() {  	
  			this.setBounds(300, 100, 550, 430);//位置参数
  		    this.setTitle("学生"+ Teacher.this.loginTea.name);//title
  		    this.setLayout(null);//布局
  		    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //想要只关闭子窗口，方法如下：子窗口设置为setDefaultCloseOption(Jframe.DISPOSE_ON_CLOSE)     
  		    this.setVisible(true);
  		    		    
  		    JLabel labWelcome = new JLabel("欢迎您，同学"+Teacher.this.loginTea.name);
  		    labWelcome.setBounds(50, 2, 550, 45);
  		      
  		    JLabel labChoosefunc = new JLabel("选择一个功能");
  		    labChoosefunc.setBounds(50, 100, 100, 50);
  		    
  		    JButton btntapInfo = new JButton("个人信息维护"); 
  		    btntapInfo.setBounds(50, 185, 200, 30);
  		    btntapInfo.setForeground(Color.BLUE);
  		    
  		    JButton btntapLoginCourse = new JButton("成绩登录"); 
  		    btntapLoginCourse.setBounds(50, 225, 200, 30);
  		    btntapLoginCourse.setForeground(Color.BLUE);
  		    
  		    this.add(labWelcome);
		    this.add(labChoosefunc);
		    this.add(btntapInfo);
		    this.add(btntapLoginCourse);
		    
		    btntapInfo.addActionListener(new ActionListener() {
		          public void actionPerformed(ActionEvent e) {
		        //TODO
		          //new PersonalInfo();
		          }
		          } );
		    
		    btntapLoginCourse.addActionListener(new ActionListener() {
		          public void actionPerformed(ActionEvent e) {
		        //TODO
		          //new CourseSearch();
		          }
		          } );
  		}
  	}
}
