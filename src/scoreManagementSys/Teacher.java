package scoreManagementSys;

import java.io.*;
import java.util.*;
import javax.swing.*;
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
  		    		    
  		    JLabel labWelcome = new JLabel("欢迎您，老师"+Teacher.this.loginTea.name);
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
		          new PersonalInfo();
		          }
		          } );
		    
		    btntapLoginCourse.addActionListener(new ActionListener() {
		          public void actionPerformed(ActionEvent e) {
		          new GradeInput();
		          }
		          } );
  		}
  	}
  	
  	class PersonalInfo extends JFrame{
		public PersonalInfo() {
			this.setBounds(300, 100, 500, 400);//位置参数
		    this.setTitle("老师"+ Teacher.this.loginTea.name);//title
		    this.setLayout(null);//布局
		    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  //想要只关闭子窗口，方法如下：子窗口设置为setDefaultCloseOption(Jframe.DISPOSE_ON_CLOSE)     
		    this.setVisible(true); 
		    
		    JLabel labWelcome = new JLabel("老师"+ Teacher.this.loginTea.name+"个人信息如下：");
		    labWelcome.setBounds(50, 5, 400, 50);
		    
		    JLabel labteaNum = new JLabel("职工编号："+Teacher.this.loginTea.staffNum);
		    labteaNum.setBounds(50, 35, 400, 50);
		    
		    JLabel labName = new JLabel("姓名："+Teacher.this.loginTea.name);
		    labName.setBounds(50, 65, 400, 50);
		    
		    JLabel labDepa = new JLabel("学院："+Teacher.this.loginTea.department);
		    labDepa.setBounds(50, 95, 400, 50);
		    
		    JLabel labSch = new JLabel("系："+Teacher.this.loginTea.school);
		    labSch.setBounds(50, 125, 400, 50);
		    
		    JLabel labTips = new JLabel("选择下列功能");
		    labTips.setBounds(50, 215, 400, 50);
		    
		    JButton buttPW = new JButton("更改密码");
		    buttPW.setBounds(50, 285, 200, 30);
		    
		    this.add(labWelcome);
		    this.add(labteaNum);
		    this.add(labName);
		    this.add(labDepa);
		    this.add(labSch);
		    this.add(labTips);
		    this.add(buttPW);
		    
		    buttPW.addActionListener(new ActionListener() {
		          public void actionPerformed(ActionEvent e) {
		        	String newPassWord = changePWConfirm();//输入两次，确认密码
		        	if (newPassWord == null) {
						return;
					}
            		Teacher.this.loginTea.password = newPassWord;//更新密码
            		Teacher.this.allTeaList.set(Teacher.this.loginTeaIndex, Teacher.this.loginTea);
            		
                  try {
                      BufferedWriter bw = new BufferedWriter(new FileWriter("./teacher.txt"));
                      for (Teacher teacher : Teacher.this.allTeaList) { // 写入teacher.txt保存更改
                    	  bw.write(String.format("%s\t\t%s\t\t%s\t\t%s\t\t%s\n", teacher.staffNum, teacher.name, teacher.school, teacher.department, teacher.password));
                      }
                      bw.close();
                  } catch (IOException e1) {
                      e1.printStackTrace();
                  }
		          }
		          } );
				}
  			}
  	//成绩登录
  	class GradeInput{
  		 String line;
         String course_to_enter;
         String grade;
         int n = 0;
         boolean grade_enter = Boolean.FALSE; // 判断是否查询到该学生的指定课程的布尔值
         final String[] info = new String[600];
         public GradeInput() {
        	for(;;) {
        		//接收课程编号输入
        		String courseNum = (String) JOptionPane.showInputDialog("请输入您要录入成绩的课程编号");
        		if(courseNum == null) {
        			break;
        		}
        		if (courseNum =="") {
        			JOptionPane.showMessageDialog(null, "请正确输入！");
        			continue;
				}
        		this.course_to_enter = courseNum;
        		break;
        	} 
        	
        	int op = JOptionPane.showConfirmDialog(null
        			, "警告！\n输入开始后请按照提示进行输入直至程序正常退出\n随意取消或关闭输入将会导致数据丢失\n是否继续"
        			,"警告",JOptionPane.YES_NO_CANCEL_OPTION);
            if (op != JOptionPane.YES_OPTION) {
				return;
            }
            else {
        	try {
                final BufferedReader br = new BufferedReader(
                new FileReader(String.format("./Grade/%s.txt", course_to_enter)));
                while (true) {
                    try 
                    {
                      if ((line = br.readLine()) == null)
                          break;
                    } 
                    catch (final IOException e) 
                    {
                        e.printStackTrace();
                        return;
                    }
                    final Scanner scan4 = new Scanner(line).useDelimiter("\\s+");
                    for (int i = 0; i < 6; i++) {
                        info[n + i] = scan4.next();
                    }
                    if (info[2].equals(course_to_enter)) { // 打印匹配到的成绩信息
                        grade_enter = Boolean.TRUE;
                        //System.out.println("学号: " + info[n]);
                        //System.out.println("姓名: " + info[n + 1]);
                        //System.out.println("课程编号: " + info[n + 2]);
                        //System.out.println("课程名称: " + info[n + 3]);
                        //System.out.println("教师: " + info[n + 4]);
                        JOptionPane.showMessageDialog(null, "您将要登录的成绩的课程信息"+"\n"
                        +"学号: " + info[n]+ "\n" 
                        +"姓名: " + (String)info[n+1] +"\n"
                        +"课程编号: " + info[n + 2]+"\n"
                        +"课程名称: " + info[n + 3]+"\n"
                        +"教师: " + info[n + 4]);
                        
                        //final Scanner scan6 = new Scanner(System.in);
                        //System.out.println(String.format("Please enter %s's grade of %s :", info[1], course_to_enter));
                        //grade = scan6.next();
                        
                    	for(;;) {
                    		//接收具体学生成绩输入
                    		String grade = (String) JOptionPane.showInputDialog(String.format("请输入您输入学生  %s 的  %s 成绩",  info[1], this.course_to_enter));
                    		if(grade == null) {
                    			break;
                    		}
                    		if (grade =="") {
                    			JOptionPane.showMessageDialog(null, "请正确输入！");
                    			continue;
            				}
                            info[n + 5] = grade;
                            n += 6;
                            break;
                    	} 
                    }
                }

                try {
                    int num1 = 0, num2 = 0;
                    final BufferedWriter bw = new BufferedWriter(
                    new FileWriter(String.format("./Grade/%s.txt", course_to_enter)));
                    while (num1 < n) {
                        while (num2 < 5) {
                            bw.write(info[num1] + "\t");
                            num1++;
                            num2++;
                        }
                        bw.write(info[num1] + "\n");
                        num1++;
                        num2 = 0;
                    }
                    bw.flush();
                    bw.close();
                } catch (final IOException io) {
                    io.printStackTrace();
                }
            } catch (final FileNotFoundException notfound) {
                final File dir = new File("./Grade");
                if (!dir.exists()) {
                    if (dir.mkdir())
                        //System.out.println("Folder not found, have create a new folder");
                    	JOptionPane.showMessageDialog(null, "成绩文件不存在，请联系管理员，已临时新建文件夹");
                }
                final File file = new File(String.format("./Grade/%s.txt", course_to_enter));
                try {
                    if (file.createNewFile())
                        //System.out.println(String.format("%s.txt not found, have created new file.", course_to_enter));
                    	JOptionPane.showMessageDialog(null, "该课程成绩文件不存在，请联系管理员，已临时文件");
                } catch (final IOException io) {
                    io.printStackTrace();
                }
            }
        	
            if (!grade_enter) {
                //System.out.println(String.format("Grade of %s don't enter.", course_to_enter));
            	JOptionPane.showMessageDialog(null, String.format("课程  %s 的成绩未录入.", course_to_enter));
            }
            }
        }
    }
}

