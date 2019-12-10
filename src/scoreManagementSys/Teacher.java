package scoreManagementSys;

import java.io.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Grade{
	String stdnum;
	String clsnum;
	String stdname;
	String clsname;
	String teaname;
	String grade;
	
	void SetData(String stdnum, String clsnum, String stdname,String clsname,String teaname,String grade){
		this.stdnum = stdnum;
		this.clsnum = clsnum;
		this.stdname = stdname;
		this.clsname = clsname;
		this.teaname = teaname;
		this.grade = grade;
    }
}


/**
 *@author 李子桐 房文宇
 *@version 2019/12/8
 */
@SuppressWarnings("serial")
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
    
    /**
     * 
     * @param clsnum
     * @return ArrayList 当前文件下所有成绩对象组成的list
     */
    ArrayList<Grade> getGrade(String clsnum){
        ArrayList<Grade> grades = new ArrayList<>();
        String line;
        try {
            @SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(String.format("./Grade/%s.txt", clsnum)));
            while (true) {
                try {
                    if ((line = br.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                    return grades;
                }
                @SuppressWarnings("resource")
				Scanner scan = new Scanner(line).useDelimiter("\\s+");
                String[] info = new String[6];
                for (int i=0;i<6;i++){
                    info[i] = scan.next();
                }
                Grade grade = new Grade();
                grade.SetData(info[0], info[2], info[1], info[3], info[4],info[5]);
                grades.add(grade);
            }
        }catch (FileNotFoundException notfound){
            File file = new File(String.format("./Grade/%s.txt", clsnum));
            try {
                file.createNewFile();
            }catch (IOException io){
                io.printStackTrace();
            }
        }
        return grades;
    }
    
    
    /**
              * 获取某个文件夹下的所有文件
     *
     * @param fileNameList 存放文件名称的list
     * @param path 文件夹的路径
     * @return 所有文件名的list
     */
    ArrayList<String> getAllFileName(String path) {
        ArrayList<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                files.add(tempList[i].getName());
            }
        }
        return files;
    }

    //成绩登录 ArrayList版本
    class GradeInput_V2{
 		String line;
        String course_to_enter;
        String grade;
        int n = 0;
        int index;
        boolean grade_enter = Boolean.FALSE; // 判断是否查询到该学生的指定课程的布尔值
        ArrayList<Grade> AllGrade;
        
        public GradeInput_V2() {
        	
        List<Object> list=new ArrayList<Object>();
       	for(;;) {
            ArrayList<String> allcls = getAllFileName("./Grade/");
            String s1 = "";
            for(String string : allcls) {
            	s1 = s1+string.substring(0,string.lastIndexOf("."))+"\n";
            	list.add(string.substring(0,string.lastIndexOf(".")));
            }
            
            int size = list.size();
   		 	Object[] objects = (Object[])list.toArray(new Object[size]);
       		//接收课程编号输入
       		
       		String courseNum = (String) JOptionPane.showInputDialog(null,"请选择您要录入成绩的课程编号\n"
       				,"选择课程",JOptionPane.PLAIN_MESSAGE, null,objects, objects[0]);
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
       	
       	//加载成绩信息
       	ArrayList<Grade> Allgrade = null;
        Allgrade = Teacher.this.getGrade(this.course_to_enter);
        this.AllGrade = Allgrade;
        //选择：对个人或是对所有成绩
        Object[] options = {"按学号登录","登录所有成绩"};
        int m = JOptionPane.showOptionDialog(null, "请选择成绩登录方式", "成绩登录", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        //按成绩查询
        List<Object> list1=new ArrayList<Object>();
        String s3 = "";
       	if (m == 0) {     		
	       	for(int i = 0; i< Allgrade.size(); i++) {
	         	s3 = s3+"学生学号："+Allgrade.get(i).stdnum+"学生姓名: "+Allgrade.get(i).stdname+"\n";
	         	list1.add(Allgrade.get(i).stdnum);
	         }        
	        int size = list1.size();
   		 	Object[] objects1 = (Object[])list1.toArray(new Object[size]);   		
	       	//遍历查询该学生       	
	       	outerLoop:while(true) {
	       		String stdnum = (String) JOptionPane.showInputDialog(null, "请输入学生学号", "成绩登录", JOptionPane.PLAIN_MESSAGE, null, objects1, objects1[0]);
	       		if (stdnum == null) {
					return;
				}
	       		int index = 0;//下标
	       		for(;index < Allgrade.size(); index++) {
	           		if (Allgrade.get(index).stdnum.equals(stdnum)) {
						JOptionPane.showMessageDialog(null, "查找成功！");
						this.index = index;
						break outerLoop;
					}           		
	           	}
	       		JOptionPane.showMessageDialog(null, "无此学生信息，请检查输入");
	       	}
	       	
	       	int op = JOptionPane.showConfirmDialog(null
	       			, "警告！\n输入开始后请按照提示进行输入直至程序正常退出\n随意取消或关闭输入将可能会导致数据丢失\n是否继续"
	       			,"警告",JOptionPane.YES_NO_CANCEL_OPTION);
	        if (op != JOptionPane.YES_OPTION) 
				return;	           
	        else {

	        	JFrame frame = new JFrame();
	        	frame.setBounds(300, 100, 700, 700);//位置参数
	        	frame.setTitle("课程成绩信息查询"+this.course_to_enter);//title
	        	frame.setLayout(null);//布局
	        	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  //想要只关闭子窗口而不退出
	        	frame.setVisible(true);
	        	
	        	JLabel labWelcome = new JLabel("学号"+Allgrade.get(this.index).stdnum+"	课程"+this.course_to_enter+"成绩信息如下：");
	    	    labWelcome.setBounds(50, 5, 400, 50);
	    	    
	    	    String s4 = "";
	            s4 = s4+"学生学号："+Allgrade.get(this.index).stdnum+"<br>"+
	            "学生姓名: "+Allgrade.get(this.index).stdname+"<br>"+
	            "课程名称："+Allgrade.get(this.index).clsname+"<br>"+
	            "授课教师："+Allgrade.get(this.index).teaname+"<br>"+
	            "成绩："+Allgrade.get(this.index).grade;            
	            s4 = "<html><body>" + s4 + "<html><body>";
	            
	            JLabel labDetail = new JLabel(s4);
	    	    labDetail.setBounds(50, 70, 400, 300);
	    	    labDetail.setForeground(Color.RED);
	    	    labDetail.setFont(new Font("Serif", Font.PLAIN, 16));

	    	    JButton isChangeGrade = new JButton("更新此学生成绩");
	    	    isChangeGrade.setBounds(150, 450, 180, 30);
	    	    isChangeGrade.setForeground(Color.RED);
	    	    
	    	    frame.add(labWelcome);
	    	    frame.add(labDetail);
	    	    frame.add(isChangeGrade);
	    	    
	    	    isChangeGrade.addActionListener(new ActionListener() {
			          public void actionPerformed(ActionEvent e) {
			        	  String newGrade = null;
			        	  while(true) {
						         newGrade = (String)JOptionPane.showInputDialog("请输入新成绩");
						         if (newGrade ==null) {
									return;//取消
								}
						        if (newGrade.equals("")) {
									JOptionPane.showMessageDialog(null, "输入有误，请重试");
									continue;
								}
						        break;
			        	  }
			        	  Teacher.GradeInput_V2.this.AllGrade.get(Teacher.GradeInput_V2.this.index).grade = newGrade;
			        	  writeNewGrade(Teacher.GradeInput_V2.this.AllGrade, Teacher.GradeInput_V2.this.course_to_enter);
			        	  JOptionPane.showMessageDialog(null, "修改成功！");
				     }
				 } );
	        }
        }
        else {
        	 ArrayList<Grade> Allgrade1 = null;
             Allgrade1 = getGrade(this.course_to_enter);
             int i = 0;
             for(; i < Allgrade1.size();i++) {
            	 JOptionPane.showMessageDialog(null, "您将要登录的成绩的课程信息"+"\n"
                         						+"学号: " + Allgrade1.get(i).stdnum+ "\n" 
                         						+"姓名: " + Allgrade1.get(i).stdname +"\n"
                         						+"课程编号: " + Allgrade1.get(i).clsnum+"\n"
                         						+"课程名称: " + Allgrade1.get(i).clsname+"\n"
                         						+"教师: " +Allgrade1.get(i).teaname+"\n"
            	 								+"已录入的成绩（null为未录入）"+Allgrade1.get(i).grade);
             	for(;;) {
            		//接收具体学生成绩输入
            		String grade = (String) JOptionPane.showInputDialog(String.format("请输入您输入学生  %s 的  %s 成绩",  Allgrade1.get(i).stdnum, Teacher.GradeInput_V2.this.course_to_enter));
            		if(grade == null) {
            			break;
            		}
            		if (grade =="") {
            			JOptionPane.showMessageDialog(null, "请正确输入！");
            			continue;
    				}
            		Allgrade1.get(i).grade = grade;
                    break;
            	} 
        	}
             
             writeNewGrade(Allgrade1, this.course_to_enter);
        }
    }
}

    /**
     * @function 写入所有的成绩对象
     * @param grades
     * @param clsname
     */
    void writeNewGrade(ArrayList<Grade> grades, String clsname) {
    	 try {
             BufferedWriter bw = new BufferedWriter(new FileWriter(String.format("./Grade/%s.txt", clsname)));
             for (Grade grade : grades) { // 写入student.txt保存更改
            	 if (grade.grade == null) {
            		 JOptionPane.showMessageDialog(null, String.format("学生%s的成绩录入有误，请重新输入", grade.stdnum));
            		 for(;;) {
                 		//接收具体学生重新成绩输入
                 		String grade1 = (String) JOptionPane.showInputDialog(String.format("请输入您输入学生  %s 的成绩",  grade.stdnum));
                 		if(grade1 == null) {
                 			break;
                 		}
                 		if (grade1 =="") {
                 			JOptionPane.showMessageDialog(null, "请正确输入！");
                 			continue;
         				}
                 		grade.grade = grade1;
                        break;
                 	} 
				}
                 bw.write(String.format("%s\t\t%s\t\t%s\t\t%s\t\t%s\t\t%s\n"
                		 , grade.stdnum, grade.stdname, grade.clsnum, grade.clsname, grade.teaname, grade.grade));
             }
             bw.close();

         } catch (IOException e) {
             e.printStackTrace();
         }
 		JOptionPane.showMessageDialog(null, "成绩信息已更新，修改成功");
    }
    
    
    ArrayList<Teacher> Get_teachers(){
        ArrayList<Teacher> teachers = new ArrayList<>();
        String line;
        try {
            @SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader("./teacher.txt"));
            while (true) {
                try {
                    if ((line = br.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                    return teachers;
                }
                @SuppressWarnings("resource")
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
    
    
    /**
     * @see 作为Login()方法的一部分
     */
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
  		    
  		    JButton btntapQruryCourse = new JButton("成绩查询"); 
  		    btntapQruryCourse.setBounds(50, 265, 200, 30);
  		    btntapQruryCourse.setForeground(Color.BLUE);
		    
  		    this.add(labWelcome);
		    this.add(labChoosefunc);
		    this.add(btntapInfo);
		    this.add(btntapLoginCourse);
		    this.add(btntapQruryCourse);
		    
		    btntapInfo.addActionListener(new ActionListener() {
		          public void actionPerformed(ActionEvent e) {
		          new PersonalInfo();
		          }
		          } );
		    
		    btntapLoginCourse.addActionListener(new ActionListener() {
		          public void actionPerformed(ActionEvent e) {
		          new GradeInput_V2();
		          }
		          } );
		    btntapQruryCourse.addActionListener(new ActionListener() {
		          public void actionPerformed(ActionEvent e) {
		          new GradeQrury();
		          }
		          } );
  		}
  	}
  	
  	class GradeQrury{
 		String line;
        String course_to_enter;
        String grade;
        int n = 0;
        boolean grade_enter = Boolean.FALSE; // 判断是否查询到该学生的指定课程的布尔值

               
        public GradeQrury() {
        List<Object> list=new ArrayList<Object>();	
       	for(;;) {
            ArrayList<String> allcls = getAllFileName("./Grade/");
            String s1 = "";
            for(String string : allcls) {
            	s1 = s1+string.substring(0,string.lastIndexOf("."))+"\n";
            	list.add(string.substring(0,string.lastIndexOf(".")));
            }
            
            int size = list.size();
   		 	Object[] objects = (Object[])list.toArray(new Object[size]);   
   
       		//接收课程编号输入
       		String courseNum = (String) JOptionPane.showInputDialog(null, "请选择您要查找成绩的课程编号", "成绩查询", JOptionPane.PLAIN_MESSAGE, null, objects, objects[0]);;
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
       	
       	//加载成绩信息
       	ArrayList<Grade> Allgrade = null;
        Allgrade = Teacher.this.getGrade(this.course_to_enter);
        //选择：对个人查询或是对所有成绩查询
        Object[] options = {"按学号查询","查询所有成绩"};
        int m = JOptionPane.showOptionDialog(null, "请选择查询方式", "成绩查询", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        //按个人查
        List<Object> list1=new ArrayList<Object>();	
        String s3 = "";
       	if (m == 0) {     		
       		for(int i = 0; i< Allgrade.size(); i++) {
         	s3 = s3+"学生学号："+Allgrade.get(i).stdnum+"学生姓名: "+Allgrade.get(i).stdname+"\n";
         	list1.add(Allgrade.get(i).stdnum);
         }        
       	 int size = list1.size();
		 Object[] objects1 = (Object[])list1.toArray(new Object[size]);   	
       	Grade resultGrade = new Grade();	
       	//遍历查询该学生
       	outerLoop:while(true) {
       		String stdnum =  (String) JOptionPane.showInputDialog(null, "请选择您要查找成绩的学生学号编号", "成绩查询", JOptionPane.PLAIN_MESSAGE, null, objects1, objects1[0]);;
       		if (stdnum == null) {
				return;
			}
       		for(Grade grade: Allgrade) {
           		if (grade.stdnum.equals(stdnum)) {
					JOptionPane.showMessageDialog(null, "查找成功！");
					resultGrade = grade;
					break outerLoop;
				}           		
           	}
       		JOptionPane.showMessageDialog(null, "无此成绩信息，请检查输入");
       	}
       		
       		JFrame frame = new JFrame();
        	frame.setBounds(300, 100, 500, 400);//位置参数
        	frame.setTitle("课程成绩信息查询"+this.course_to_enter);//title
        	frame.setLayout(null);//布局
        	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  //想要只关闭子窗口而不退出
        	frame.setVisible(true);
        	
        	JLabel labWelcome = new JLabel("学号"+resultGrade.stdnum+"	课程"+this.course_to_enter+"成绩信息如下：");
    	    labWelcome.setBounds(50, 5, 400, 50);
    	    
    	    String s4 = "";
            s4 = s4+"学生学号："+resultGrade.stdnum+"<br>"+
            "学生姓名: "+resultGrade.stdname+"<br>"+
            "课程名称："+resultGrade.clsname+"<br>"+
            "授课教师："+resultGrade.teaname+"<br>"+
            "成绩："+resultGrade.grade;            
            s4 = "<html><body>" + s4 + "<html><body>";
            
            JLabel labDetail = new JLabel(s4);
    	    labDetail.setBounds(50, 70, 400, 300);
    	    labDetail.setForeground(Color.RED);
    	    labDetail.setFont(new Font("Serif", Font.PLAIN, 16));
    	    frame.add(labWelcome);
    	    frame.add(labDetail);
		}
       	
       	else 
       	{
	       	JFrame frame = new JFrame();
	    	frame.setBounds(300, 100, 500, 400);//位置参数
	    	frame.setTitle("课程成绩信息查询"+this.course_to_enter);//title
	    	frame.setLayout(null);//布局
	    	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  //想要只关闭子窗口而不退出
	    	frame.setVisible(true);
	    	
	    	JLabel labWelcome = new JLabel("课程"+this.course_to_enter+"课程成绩信息如下：");
		    labWelcome.setBounds(50, 5, 400, 50);
		    
		    
	        String s2 = "";
	        for(int i = 0; i< Allgrade.size(); i++) {
	        	s2 = s2+"学生学号："+Allgrade.get(i).stdnum+"	学生姓名: "+Allgrade.get(i).stdname+"	分数："+Allgrade.get(i).grade+"<br>";
	        }
	        s2 = "<html><body>" + s2 + "<html><body>";
	        
		    JLabel labDetail = new JLabel(s2);
		    labDetail.setBounds(50, 70, 400, 300);
		    labDetail.setFont(new Font("Serif", Font.PLAIN, 16));
		    
		    frame.add(labWelcome);
		    frame.add(labDetail);
       	}
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
}

