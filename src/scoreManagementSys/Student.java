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
import javax.swing.JPasswordField;
import javax.swing.JTextField;


/**
 *@author 罗文凯
 *@version 2019/12/4
 */
@SuppressWarnings("serial")
class Student extends JFrame{
	String stdNum;//学号
	String name;
	String sex;
	String birth_month_year;//出生年月
	String faculty; //学院
	String major; //专业	
	String password;
	Student loginStd;
	
	ArrayList<Student> allStuList;
	int loginStuIndex;
	
	JLabel stdIDTips;
	JTextField stdIDInput;
	JLabel stdPwTips;
	JTextField stdPwInput;
	JButton login;
	
	Student () {
        this.stdNum = "Unknown";
        this.name = "Unknown";
        this.sex = "Unknown";
        this.birth_month_year = "Unknown";
        this.faculty = "Unknown";
        this.major = "Unknown";
        this.password = "0000";
        
        this.setBounds(300, 150, 500, 450);//位置参数
	    this.setTitle("学生登录");//title
	    this.setLayout(null);//布局
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
	    this.setVisible(true);
	    
	    Login();
    }
	
	
	void SetData(String stdNum, String name, String sex, String birth_month_year, String faculty, String major, String password){
        this.stdNum = stdNum;
        this.name = name;
        this.sex = sex;
        this.birth_month_year = birth_month_year;
        this.faculty = faculty;
        this.major = major;
        this.password = password;
    }
	
	
	ArrayList<Student> Get_students(){
        ArrayList<Student> students = new ArrayList<>();
        String line;
        try {
            @SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader("./student.txt"));
            while (true) {
                try {
                    if ((line = br.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                    return students;
                }
                @SuppressWarnings("resource")
				Scanner scan = new Scanner(line).useDelimiter("\\s+");
                String[] info = new String[7];
                for (int i=0;i<7;i++){
                    info[i] = scan.next();
                }
                Student student = new Student();
                student.SetData(info[0], info[1], info[2], info[3], info[4], info[5], info[6]);
                students.add(student);
            }
        }catch (FileNotFoundException notfound){
            File file = new File("./student.txt");
            try {
                file.createNewFile();
            }catch (IOException io){
                io.printStackTrace();
            }
        }
        return students;
    }

	
    void Login(){
    	stdIDTips = new JLabel("学生编号:");
		stdIDTips.setBounds(50, 60, 150, 50);
			
		stdIDInput = new JTextField("",30);
		stdIDInput.setBounds(200, 76, 180, 30); 
			
		stdPwTips = new JLabel("密码:");
		stdPwTips.setBounds(50, 100, 150, 50);
			
		stdPwInput = new JPasswordField("",30);
		stdPwInput.setBounds(200, 117, 180, 30);
			
		login = new JButton("登录"); 
		login.setBounds(150, 250, 180, 30);
		login.setForeground(Color.BLUE);
			
		this.add(stdIDTips);
		this.add(stdIDInput);
		this.add(stdPwTips);
		this.add(stdPwInput);
		this.add(login);
			
		login.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		          verify();
		          }});

    }
    
    
    @SuppressWarnings("unused")
	void verify() {       
		ArrayList<Student> students = this.Get_students();//得到所有学生对象的ArrayList
		this.allStuList = students;  //保存待用
        String stdNum = null;
        String password = null;
        boolean ID_exist = false;
        int std_index = -1; //登录学生的下标
        
        Student temp_student = new Student();
        //输入是否为空
        if (isBlank()) {
			
        	stdNum = stdIDInput.getText();
        	password = stdPwInput.getText();
		}
        //查找学生账号
        for (Student student : students) {
            if (stdNum.equals(student.stdNum)) {
                ID_exist = true;
                temp_student = student;
                std_index = students.indexOf(student);//获取下标
                this.loginStuIndex = std_index;//保存下标待用
            }
        }
        
        if (!ID_exist){
        	JOptionPane.showMessageDialog(null, "账号输入有误，请重新输入！如输入无误，请联系教务员");
        }
        //验证密码
        else {
            if (temp_student.password.equals(password))
            {              
            	if (password.equals("0000")){
                	int op = JOptionPane.showConfirmDialog(null, "您正在使用初始密码登录，是否现在修改密码！","安全提示",JOptionPane.YES_NO_CANCEL_OPTION);      	
                	//选择了“是”，修改初始密码
                	if(op == JOptionPane.YES_OPTION) {
                		String newPassWord = changePWConfirm();//输入两次，确认密码
                		temp_student.password = newPassWord;//更新密码
                		this.allStuList.set(this.loginStuIndex, temp_student);//将students列表中的student更新为更改过密码的temp_student
                		//TODO 保存更改
                        try {
                            BufferedWriter bw = new BufferedWriter(new FileWriter("./student.txt"));
                            for (Student student : this.allStuList) { // 写入student.txt保存更改
                                bw.write(String.format("%s\t\t%s\t\t%s\t\t%s\t\t%s\t\t%s\t\t%s\n", student.stdNum, student.name, student.sex, student.birth_month_year, student.faculty, student.major, student.password));
                            }
                            bw.close();
        
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                		JOptionPane.showMessageDialog(null, "密码已更新，修改成功");
                    }

                }               
                
            	this.loginStd = temp_student;
    			JOptionPane jO = null;
    			int option = JOptionPane.YES_OPTION;
    			JOptionPane.showMessageDialog(null, temp_student.name + " 登录成功"); 			
    			new StdMenu();
    			               
            }
            else {
            	JOptionPane.showMessageDialog(null, "密码错误，请重试！");
            }
        }
           

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
    
    
    public boolean isBlank() {
		if(stdIDInput.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "账号输入有误 请重新输入！");
			return false;			
		}
		if(stdPwInput.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "密码输入有误 请重新输入！");
			return false;			
		}
		return true;
	}
    
	//内部类菜单
	class StdMenu extends JFrame{
		public StdMenu() {
	
			this.setBounds(300, 100, 550, 430);//位置参数
		    this.setTitle("学生"+ Student.this.loginStd.name);//title
		    this.setLayout(null);//布局
		    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //想要只关闭子窗口，方法如下：子窗口设置为setDefaultCloseOption(Jframe.DISPOSE_ON_CLOSE)     
		    this.setVisible(true);
		    		    
		    JLabel labWelcome = new JLabel("欢迎您，同学"+Student.this.loginStd.name);
		    labWelcome.setBounds(50, 2, 550, 45);
		      
		    JLabel labChoosefunc = new JLabel("选择一个功能");
		    labChoosefunc.setBounds(50, 100, 100, 50);
		    
		    JButton btntapInfo = new JButton("个人信息维护"); 
		    btntapInfo.setBounds(50, 185, 200, 30);
		    btntapInfo.setForeground(Color.BLUE);
		    
		    JButton btntapSearchCourse = new JButton("课程查询"); 
		    btntapSearchCourse.setBounds(50, 225, 200, 30);
		    btntapSearchCourse.setForeground(Color.BLUE);
		    
		    JButton btntapGradeSearch = new JButton("成绩查询"); 
		    btntapGradeSearch.setBounds(50, 265, 200, 30);
		    btntapGradeSearch.setForeground(Color.BLUE);
		    
		    this.add(labWelcome);
		    this.add(labChoosefunc);
		    this.add(btntapInfo);
		    this.add(btntapSearchCourse);
		    this.add(btntapGradeSearch);
		    
		    btntapInfo.addActionListener(new ActionListener() {
		          public void actionPerformed(ActionEvent e) {
		          new PersonalInfo();
		          }
		          } );
		    btntapSearchCourse.addActionListener(new ActionListener() {
		          public void actionPerformed(ActionEvent e) {
		          new CourseSearch();
		          }
		          } );
		    btntapGradeSearch.addActionListener(new ActionListener() {
		          public void actionPerformed(ActionEvent e) {
		          new GradeSearch();
		          }
		          } );
		}
		
	}
	
	//个人信息维护
	class PersonalInfo extends JFrame{
		public PersonalInfo() {
			this.setBounds(300, 100, 500, 400);//位置参数
		    this.setTitle("学生"+ Student.this.loginStd.name);//title
		    this.setLayout(null);//布局
		    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  //想要只关闭子窗口，方法如下：子窗口设置为setDefaultCloseOption(Jframe.DISPOSE_ON_CLOSE)     
		    this.setVisible(true);
		    
		    JLabel labWelcome = new JLabel("同学"+Student.this.loginStd.name+"个人信息如下：");
		    labWelcome.setBounds(50, 5, 400, 50);
		    
		    JLabel labStdNum = new JLabel("学号："+Student.this.loginStd.stdNum);
		    labStdNum.setBounds(50, 35, 400, 50);
		    
		    JLabel labName = new JLabel("姓名："+Student.this.loginStd.name);
		    labName.setBounds(50, 65, 400, 50);
		    
		    JLabel labSex = new JLabel("性别："+Student.this.loginStd.sex);
		    labSex.setBounds(50, 95, 400, 50);
		    
		    JLabel labBirth = new JLabel("出生年月："+Student.this.loginStd.birth_month_year);
		    labBirth.setBounds(50, 125, 400, 50);
		    
		    JLabel labFac = new JLabel("学院："+Student.this.loginStd.faculty);
		    labFac.setBounds(50, 155, 400, 50);
		    
		    JLabel labMajor = new JLabel("专业："+Student.this.loginStd.major);
		    labMajor.setBounds(50, 185, 400, 50);
		    
		    JLabel labTips = new JLabel("选择下列功能");
		    labTips.setBounds(50, 215, 400, 50);
		    
		    JButton buttPW = new JButton("更改密码");
		    buttPW.setBounds(50, 285, 200, 30);

		    this.add(labWelcome);
		    this.add(labStdNum);
		    this.add(labName);
		    this.add(labSex);
		    this.add(labBirth);
		    this.add(labFac);
		    this.add(labMajor);
		    this.add(labTips);
		    this.add(buttPW);
		    
		    buttPW.addActionListener(new ActionListener() {
		          public void actionPerformed(ActionEvent e) {
		        	String newPassWord = changePWConfirm();//输入两次，确认密码
		        	if (newPassWord == null) {
						return;
					}
              		Student.this.loginStd.password = newPassWord;//更新密码
              		Student.this.allStuList.set(Student.this.loginStuIndex, Student.this.loginStd);//将students列表中的student更新为更改过密码的temp_student   
              		
                    try {
                        BufferedWriter bw = new BufferedWriter(new FileWriter("./student.txt"));
                        for (Student student : Student.this.allStuList) { // 写入student.txt保存更改
                            bw.write(String.format("%s\t\t%s\t\t%s\t\t%s\t\t%s\t\t%s\t\t%s\n", student.stdNum, student.name, student.sex, student.birth_month_year, student.faculty, student.major, student.password));
                        }
                        bw.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
		          }
		          } );
		}
	}
	
	//课程查询
	class CourseSearch extends JFrame{
		String course_to_search;
		boolean isFound = Boolean.FALSE;
		
		JLabel labWelcome;
		JTextField textCourse;
		JButton buttFind;
		
		public CourseSearch() {
			String line;
			this.setBounds(300, 100, 500, 400);//位置参数
		    this.setTitle("学生"+ Student.this.loginStd.name);//title
		    this.setLayout(null);//布局
		    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  //想要只关闭子窗口，方法如下：子窗口设置为setDefaultCloseOption(Jframe.DISPOSE_ON_CLOSE)     
		    this.setVisible(true);
		    
		    labWelcome = new JLabel("请输入要查询的课程名称");
		    labWelcome.setBounds(50, 5, 400, 50);
		    
		    textCourse = new JTextField("",30);
		    textCourse.setBounds(50, 65, 150, 30); 
		    
		    buttFind = new JButton("查询");
		    buttFind.setBounds(50, 165, 200, 30); 
		    
		    this.add(labWelcome);
		    this.add(textCourse);
		    this.add(buttFind);
		    
		    buttFind.addActionListener(new ActionListener() {
		          public void actionPerformed(ActionEvent e) {
		        	  //TODO 验证搜索输入正确性
		        	  //System.out.print("nal");
		        	  new Student.CourseSearch.VerifyInput();
		        	  
		          }
		          } );		   
		}
		
		private class VerifyInput{
			String courseFound;
			
			public VerifyInput() {
				// TODO 自动生成的构造函数存根
				if(this.verifyInput()) {
					Student.CourseSearch.VerifyInput.this.findCourse();
				}				
			}
			
			boolean verifyInput() {
				//TODO
				if(Student.CourseSearch.this.textCourse.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "输入有误 请重新输入！");
					return false;			
				}
				this.courseFound = Student.CourseSearch.this.textCourse.getText(); //从输入框中接收输入
				//System.out.println("获取文字"+Student.CourseSearch.this.course_to_search);
				return true;
			}
			
			void findCourse() {
			    String line;		    
			    try 
			    {
	                BufferedReader br = new BufferedReader(new FileReader("./Course.txt"));
	                while (true) 
	                {
	                    try 
	                    {
	                        if ((line = br.readLine()) == null) {
	                        	JOptionPane.showMessageDialog(null, "无此课程，请检查输入");
	                        	break;
	                        } 
	                    } 
	                    catch (IOException e) 
	                    {
	                        e.printStackTrace();
	                        return;
	                    }
	                    finally {}
	                    Scanner scan = new Scanner(line).useDelimiter("\\s+");
	                    String[] info = new String[5];
	                    for (int i=0;i<5;i++)
	                    {
	                        info[i] = scan.next();
	                    }
	                    
	                    if (info[1].equals(this.courseFound)){ //按课程名称搜索
	                    	JFrame frame = new JFrame();
	                    	frame.setBounds(300, 100, 500, 400);//位置参数
	                    	frame.setTitle("课程信息查询"+this.courseFound);//title
	                    	frame.setLayout(null);//布局
	                    	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  //想要只关闭子窗口而不退出
	                    	frame.setVisible(true);
	                    	
	                    	JLabel labWelcome = new JLabel("课程"+this.courseFound+"个人信息如下：");
	            		    labWelcome.setBounds(50, 5, 400, 50);
	            		    
	            		    JLabel labclsNum = new JLabel("课程编号: " + info[0]);
	            		    labclsNum.setBounds(50, 35, 400, 50);
	            		    
	            		    JLabel labclsName = new JLabel("名称: " + info[1]);
	            		    labclsName.setBounds(50, 65, 400, 50);
	            		    
	            		    JLabel labclsCredit = new JLabel("学分: " + info[2]);
	            		    labclsCredit.setBounds(50, 95, 400, 50);
	            		    
	            		    JLabel labclsHour = new JLabel("学时数: " + info[3]);
	            		    labclsHour.setBounds(50, 125, 400, 50);
	            		    
	            		    JLabel labclsTea = new JLabel("授课老师: " + info[4]);
	            		    labclsTea.setBounds(50, 155, 400, 50);
	            		    
	            		    JLabel labclsquit = new JLabel("点击右上角X退出");
	            		    labclsquit.setBounds(50, 185, 400, 50);
	                       
	            		    frame.add(labWelcome);
	            		    frame.add(labclsNum);
	            		    frame.add(labclsName);
	            		    frame.add(labclsCredit);
	            		    frame.add(labclsHour);
	            		    frame.add(labclsTea);
	            		    frame.add(labclsquit);
	            		    
	            		    break;
	                    }
	                }
			    } catch (FileNotFoundException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			    finally {}			    
			}
		}		
	}
	
	class GradeSearch extends JFrame{
		String line;
		String course_to_search;
		boolean grade_found = Boolean.FALSE; //判断是否查询到该学生的指定课程的布尔值
		
		JLabel labWelcome;
		JTextField textGrade;
		JButton buttFind;
		
		public GradeSearch() {

			this.setBounds(300, 100, 500, 400);//位置参数
		    this.setTitle("学生"+ Student.this.loginStd.name);//title
		    this.setLayout(null);//布局
		    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  //想要只关闭子窗口，方法如下：子窗口设置为setDefaultCloseOption(Jframe.DISPOSE_ON_CLOSE)     
		    this.setVisible(true);
		    
		    labWelcome = new JLabel("请输入要查询的成绩课程编号");
		    labWelcome.setBounds(50, 5, 400, 50);
		    
		    textGrade = new JTextField("",30);
		    textGrade.setBounds(50, 65, 150, 30); 
		    
		    buttFind = new JButton("查询");
		    buttFind.setBounds(50, 165, 200, 30); 
		    
		    this.add(labWelcome);
		    this.add(textGrade);
		    this.add(buttFind);
		    
		    buttFind.addActionListener(new ActionListener() {
		          public void actionPerformed(ActionEvent e) {
		        	  //TODO 验证搜索输入正确性
		        	  new Student.GradeSearch.VerifyInput();
		        	  
		          }
		          } );		   
		}
		
		private class VerifyInput{
			String courseFound;
			
			public VerifyInput() {
				// TODO 自动生成的构造函数存根
				if(this.verifyInput()) {
					Student.GradeSearch.VerifyInput.this.findGrade();
				}				
			}
			
			boolean verifyInput() {
				//TODO
				if(Student.GradeSearch.this.textGrade.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "输入有误 请重新输入！");
					return false;			
				}
				this.courseFound = Student.GradeSearch.this.textGrade.getText(); //从输入框中接收输入
				//System.out.println("获取文字"+Student.CourseSearch.this.course_to_search);
				return true;
			}
			
			void findGrade() {
			    String line;		    
			    try 
			    {
	                BufferedReader br = null;
					try {
						br = new BufferedReader(new InputStreamReader(new FileInputStream(String.format("./Grade/%s.txt", this.courseFound)), "UTF-8"));
					} catch (UnsupportedEncodingException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
	                while (true) 
	                {
	                    try 
	                    {
	                        if ((line = br.readLine()) == null) {
	                        	JOptionPane.showMessageDialog(null, "无此课程，请检查输入");
	                        	break;
	                        } 
	                    } 
	                    catch (IOException e) 
	                    {
	                        e.printStackTrace();
	                        return;
	                    }
	                    finally {}
	                    Scanner scan = new Scanner(line).useDelimiter("\\s+");
	                    String[] info = new String[6];
	                    for (int i=0;i<6;i++)
	                    {
	                        info[i] = scan.next();
	                    }
	                    
	                    if (info[0].equals(Student.this.loginStd.stdNum)){ //按课程名称搜索
	                    	JFrame frame = new JFrame();
	                    	frame.setBounds(300, 100, 500, 400);//位置参数
	                    	frame.setTitle("成绩查询"+this.courseFound);//title
	                    	frame.setLayout(null);//布局
	                    	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  //想要只关闭子窗口而不退出
	                    	frame.setVisible(true);
	                    	
	                    	JLabel labWelcome = new JLabel("课程"+this.courseFound+"成绩信息如下：");
	            		    labWelcome.setBounds(50, 5, 400, 50);
	            		    
	            		    JLabel labstdNum = new JLabel("学号: " + info[0]);
	            		    labstdNum.setBounds(50, 35, 400, 50);
	            		    
	            		    JLabel labstdName = new JLabel("姓名: " + info[1]);
	            		    labstdName.setBounds(50, 65, 400, 50);
	            		    
	            		    JLabel labclsNum = new JLabel("课程编号: " + info[2]);
	            		    labclsNum.setBounds(50, 95, 400, 50);
	            		    
	            		    JLabel labclsName1 = new JLabel("课程名称: " + info[3]);
	            		    labclsName1.setBounds(50, 125, 400, 50);
	            		    
	            		    JLabel labclsTea = new JLabel("授课老师: " + info[4]);
	            		    labclsTea.setBounds(50, 155, 400, 50);
	            		    
	            		    JLabel labclsGrade = new JLabel("成绩: " + info[5]);
	            		    labclsGrade.setBounds(50, 185, 400, 50);

	            		    
	            		    JLabel labclsquit = new JLabel("点击右上角X退出");
	            		    labclsquit.setBounds(50, 265, 400, 50);
	                       
	            		    frame.add(labWelcome);
	            		    frame.add(labstdNum);
	            		    frame.add(labstdName);
	            		    frame.add(labclsNum);
	            		    frame.add(labclsName1);
	            		    frame.add(labclsTea);
	            		    frame.add(labclsGrade);
	            		    frame.add(labclsquit);
	            		    
	            		    break;
	                    }
	                }
			    } catch (FileNotFoundException e1) {

					e1.printStackTrace();
				}
			    finally {}			    
			}
		}	
	}
}


		