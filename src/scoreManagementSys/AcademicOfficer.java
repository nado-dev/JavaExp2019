package scoreManagementSys;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;


/**
 * @author Aaron
 *
 */
public class AcademicOfficer extends JFrame {
	String staffNum;
	String name;
	String school;
	String password;
	AcademicOfficer loginAO;
	ArrayList<AcademicOfficer> allAOs;
	int loginAOIndex;

	JLabel AOIDTips;
	JTextField AOIDInput;
	JLabel AOPwTips;
	JTextField AOPwInput;
	JButton login;

	public AcademicOfficer() {
		// TODO 自动生成的构造函数存根
		this.staffNum = "Undefined";
		this.name = "Undefined";
		this.school = "Undefined";

		this.setBounds(300, 150, 500, 450);// 位置参数
		this.setTitle("教务员登录");// title
		this.setLayout(null);// 布局
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setVisible(true);

		Login();
	}

	void setData(String staffNum, String name, String school, String password) {
		this.staffNum = staffNum;
		this.name = name;
		this.school = school;
		this.password = password;
	}

	void Login() {
		AOIDTips = new JLabel("教务员职工编号");
		AOIDTips.setBounds(50, 60, 150, 50);

		AOIDInput = new JTextField("", 30);
		AOIDInput.setBounds(200, 76, 180, 30);

		AOPwTips = new JLabel("密码:");
		AOPwTips.setBounds(50, 100, 150, 50);

		AOPwInput = new JPasswordField("", 30);
		AOPwInput.setBounds(200, 117, 180, 30);

		login = new JButton("登录");
		login.setBounds(150, 250, 180, 30);
		login.setForeground(Color.BLUE);

		this.add(AOIDTips);
		this.add(AOIDInput);
		this.add(AOPwTips);
		this.add(AOPwInput);
		this.add(login);

		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				verify();
			}
		});
	}

	/**
	 * 作为登录方法的一部分
	 */
	void verify() {
		ArrayList<AcademicOfficer> aos = this.getAOs();
		if (aos.size() == 0) {
			JOptionPane.showMessageDialog(null, "无教务员信息");
			return;
		}
		this.allAOs = aos;
		String AONum = null;
		String pw = null;
		boolean ID_EXIST = false;
		int aoIndex = -1;

		AcademicOfficer tempAO = new AcademicOfficer();

		if (isBlank()) {
			AONum = AOIDInput.getText();
			pw = AOPwInput.getText();
		}
		if (pw == null || AONum == null) {
			JOptionPane.showMessageDialog(null, "账号或密码不能为空");
			return;
		}
		for (AcademicOfficer ao : aos) {
			if (AONum.equals(ao.staffNum)) {
				ID_EXIST = true;
				tempAO = ao;
				aoIndex = aos.indexOf(ao);
				this.loginAOIndex = aoIndex;
			}
		}

		if (!ID_EXIST) {
			JOptionPane.showMessageDialog(null, "账号输入有误，请重新输入！");
		} else {
			if (tempAO.password.equals(pw)) {

				if (pw.equals("0000")) {
					int op = JOptionPane.showConfirmDialog(null, "您正在使用初始密码登录，是否现在修改密码！", "安全提示",
							JOptionPane.YES_NO_CANCEL_OPTION);
					if (op == JOptionPane.YES_OPTION) {
						String newPassWord = changePassWordConfirm();
						tempAO.password = newPassWord;
						this.allAOs.set(this.loginAOIndex, tempAO);

						try {
							BufferedWriter bw = new BufferedWriter(new FileWriter("./AcademicOfficer.txt"));
							for (AcademicOfficer ao : this.allAOs) {
								bw.write(String.format("%s\t\t%s\t\t%s\t\t%s\n", ao.staffNum, ao.name, ao.school,
										ao.password));
							}
							bw.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						JOptionPane.showMessageDialog(null, "密码已更新，修改成功");
					}
				}
				this.loginAO = tempAO;
				JOptionPane.showMessageDialog(null, "教务员  " + tempAO.name + " 登录成功");
				new AOMenu();
			} else {
				JOptionPane.showMessageDialog(null, "密码错误，请检查您的输入");
			}
		}
	}

	private String changePassWordConfirm() {
		String result = null;
		for (;;) {

			String newPassWord1 = JOptionPane.showInputDialog("输入新的密码");
			if (newPassWord1 == null) {
				break;
			}
			if (newPassWord1.equals("")) {
				JOptionPane.showMessageDialog(null, "请正确输入！");
				continue;
			}

			String newPassWord2 = JOptionPane.showInputDialog("再次输入以确认您的密码");
			if (newPassWord2 == null) {
				break;
			}
			if (newPassWord2.equals("")) {
				JOptionPane.showMessageDialog(null, "请正确输入！");
				continue;
			}

			if (newPassWord1.equals(newPassWord2)) {
				JOptionPane.showMessageDialog(null, "密码修改成功");
				result = newPassWord1;
				break;
			} else {
				JOptionPane.showConfirmDialog(null, "两次修改不一致，请再试一次！");
				continue;
			}
		}
		return result;
	}

	// 菜单
	class AOMenu extends JFrame {
		public AOMenu() {
			this.setBounds(300, 150, 500, 450);// 位置参数
			this.setTitle("教务员" + AcademicOfficer.this.loginAO.name);// title
			this.setLayout(null);// 布局
			this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 想要只关闭子窗口，方法如下：子窗口设置为setDefaultCloseOption(Jframe.DISPOSE_ON_CLOSE)
			this.setVisible(true);

			JLabel labWelcome = new JLabel("欢迎您，老师" + AcademicOfficer.this.loginAO.name);
			labWelcome.setBounds(50, 2, 550, 45);

			JLabel labChoosefunc = new JLabel("选择一个功能");
			labChoosefunc.setBounds(50, 100, 100, 50);

			JButton btntapInfo = new JButton("个人信息维护");
			btntapInfo.setBounds(50, 185, 200, 30);
			btntapInfo.setForeground(Color.BLUE);

			JButton btntapCourse = new JButton("课程管理");
			btntapCourse.setBounds(50, 225, 200, 30);
			btntapCourse.setForeground(Color.BLUE);

			JButton btntapGrade = new JButton("成绩管理");
			btntapGrade.setBounds(50, 265, 200, 30);
			btntapGrade.setForeground(Color.BLUE);

			btntapInfo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new PersonalInfo();
				}
			});

			btntapCourse.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new CourseMaintain();
				}
			});

			btntapGrade.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new GradeMaintain();
				}
			});

			this.add(labWelcome);
			this.add(labChoosefunc);
			this.add(btntapInfo);
			this.add(btntapCourse);
			this.add(btntapGrade);
			this.add(btntapInfo);
		}
	}

	public boolean isBlank() {
		if (AOIDInput.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "账号输入有误 请重新输入！");
			return false;
		}
		if (AOPwInput.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "密码输入有误 请重新输入！");
			return false;
		}
		return true;
	}

	ArrayList<AcademicOfficer> getAOs() {
		ArrayList<AcademicOfficer> aos = new ArrayList<>();
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader("./AcademicOfficer.txt"));
			while (true) {
				try {
					if ((line = br.readLine()) == null) {
						return aos;
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
				@SuppressWarnings("resource")
				Scanner scan = new Scanner(line).useDelimiter("\\s+");
				String[] info = new String[4];
				for (int i = 0; i < 4; i++) {
					info[i] = scan.next();
				}
				AcademicOfficer ao = new AcademicOfficer();
				ao.setData(info[0], info[1], info[2], info[3]);
				aos.add(ao);
			}
		} catch (FileNotFoundException notfound) {
			File file = new File("./AcademicOfficer.txt");
			try {
				file.createNewFile();
			} catch (IOException io) {
				io.printStackTrace();
			}
		}
		return aos;
	}

	/**
	 * 获取某个文件夹下的所有文件
	 *
	 * @param fileNameList 存放文件名称的list
	 * @param path         文件夹的路径
	 * @return 所有文件名的list
	 */
	ArrayList<String> getAllFileName(String path) {
		ArrayList<String> files = new ArrayList<String>();
		File file = new File(path);
		File[] tempList = file.listFiles();
		if (tempList.length == 0) {
			return null;
		}
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				files.add(tempList[i].getName());
			}
		}
		return files;
	}

	/**
	 * 
	 * @param clsnum
	 * @return ArrayList 当前文件下所有成绩对象组成的list
	 */
	ArrayList<Grade> getGrade(String clsnum) {
		ArrayList<Grade> grades = new ArrayList<>();
		String line;
		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(String.format("./Grade/%s.txt", clsnum)));
			while (true) {
				try {
					if ((line = br.readLine()) == null)
						break;
				} catch (IOException e) {
					e.printStackTrace();
					return grades;
				}
				Scanner scan = new Scanner(line).useDelimiter("\\s+");
				String[] info = new String[6];
				for (int i = 0; i < 6; i++) {
					info[i] = scan.next();
				}
				Grade grade = new Grade();
				grade.SetData(info[0], info[2], info[1], info[3], info[4], info[5]);
				grades.add(grade);
			}
		} catch (FileNotFoundException notfound) {
			File file = new File(String.format("./Grade/%s.txt", clsnum));
			try {
				file.createNewFile();
			} catch (IOException io) {
				io.printStackTrace();
			}
		}
		return grades;
	}

	/**
	 * 
	 * @param clsnum
	 * @return ArrayList 当前文件下所有课程对象组成的list
	 */
	ArrayList<Course> getCourse() {
		ArrayList<Course> courses = new ArrayList<>();
		String line;
		try {
			BufferedReader br = new BufferedReader(new FileReader("./Course.txt"));


			while (true) {
				try {
					if ((line = br.readLine()) == null)
						break;
				} catch (IOException e) {
					e.printStackTrace();
					return courses;
				}

				Scanner scan = new Scanner(line).useDelimiter("\\s+");
				String[] info = new String[5];
				for (int i = 0; i < 5; i++) {
					info[i] = scan.next();
				}
				Course course = new Course();
				course.SetData(info[0], info[1], info[2], info[3], info[4]);
				courses.add(course);
			}
		} catch (FileNotFoundException notfound) {
			File file = new File("./Course.txt");
			try {
				file.createNewFile();
			} catch (IOException io) {
				io.printStackTrace();
			}
		}
		return courses;
	}

	class PersonalInfo extends JFrame {

		public PersonalInfo() {
			this.setBounds(300, 150, 500, 450);// 位置参数
			this.setTitle("教务员" + AcademicOfficer.this.loginAO.name);// title
			this.setLayout(null);// 布局
			this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // 想要只关闭子窗口，方法如下：子窗口设置为setDefaultCloseOption(Jframe.DISPOSE_ON_CLOSE)
			this.setVisible(true);

			JLabel labWelcome = new JLabel("教务员" + AcademicOfficer.this.loginAO.name + "个人信息如下：");
			labWelcome.setBounds(50, 5, 400, 50);

			JLabel labteaNum = new JLabel("职工编号：" + AcademicOfficer.this.loginAO.staffNum);
			labteaNum.setBounds(50, 35, 400, 50);

			JLabel labName = new JLabel("姓名：" + AcademicOfficer.this.loginAO.name);
			labName.setBounds(50, 65, 400, 50);

			JLabel labDepa = new JLabel("学院：" + AcademicOfficer.this.loginAO.school);
			labDepa.setBounds(50, 95, 400, 50);

			JLabel labTips = new JLabel("选择下列功能");
			labTips.setBounds(50, 215, 400, 50);

			JButton buttPW = new JButton("更改密码");
			buttPW.setBounds(50, 285, 200, 30);

			this.add(labWelcome);
			this.add(labteaNum);
			this.add(labName);
			this.add(labDepa);
			this.add(labTips);
			this.add(buttPW);

			buttPW.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String newPassWord = AcademicOfficer.this.changePassWordConfirm();// 输入两次，确认密码
					if (newPassWord == null) {
						return;
					}
					AcademicOfficer.this.loginAO.password = newPassWord;// 更新密码
					AcademicOfficer.this.allAOs.set(AcademicOfficer.this.loginAOIndex, AcademicOfficer.this.loginAO);

					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter("./AcademicOfficer.txt"));
						for (AcademicOfficer ao : AcademicOfficer.this.allAOs) {
							bw.write(String.format("%s\t\t%s\t\t%s\t\t%s\n", ao.staffNum, ao.name, ao.school,
									ao.password));
						}
						bw.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
		}
	}

	class GradeMaintain {
		String course_to_enter;
		ArrayList<Grade> AllGrade;

		public GradeMaintain() {
			// 检查Grade文件夹是否存在
			File file = new File("./Grade");
			if (!file.exists()) {
				int option = JOptionPane.showConfirmDialog(null, "成绩文件夹不存在，是否新建?", "错误提示", JOptionPane.YES_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					file.mkdirs();
				} else {
					return;
				}
			}

			ArrayList<String> AllCourse = getAllFileName("./Grade");
			if (AllCourse == null) {
				int option = JOptionPane.showConfirmDialog(null, "无成绩信息，是否新建所有课程的空白成绩单?", "错误提示",
						JOptionPane.YES_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					addBlankGradeForm();
				} else {
					return;
				}
			}
			gradeMenu();// 成绩管理主菜单
		}

		void addBlankGradeForm() {
			File file = new File("./Course.txt");
			if (!file.exists()) {
				JOptionPane.showConfirmDialog(null, "课程信息文件不存在，请添加至少一项课程再进行操作！");
				return;
			}
			ArrayList<Course> allCourse = AcademicOfficer.this.getCourse();
			if (allCourse == null) {
				JOptionPane.showConfirmDialog(null, "课程信息为空，请添加至少一项课程再进行操作！");
				return;
			}
			for (Course course : allCourse) {
				File file1 = new File("./Grade/" + course.clsnum + ".txt");
				// 1.建立空白文件
				try {
					file1.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// 2.加入学生选课信息
				ArrayList<String> newinfo = new ArrayList<>();
				
				String str = JOptionPane.showInputDialog("课程:" + course.clsname + "的选课人数");
				int num = Integer.parseInt(str);
				for (int i = 0; i < num; i++) {
					String stdname = JOptionPane.showInputDialog("第" + (i + 1) + "个学生姓名");
					if (stdname == null)
						return;
					if (stdname.equals("")) {
						JOptionPane.showMessageDialog(null, "输入有误，请重新输入");
						continue;
					}
					String stdnum = JOptionPane.showInputDialog("第" + (i + 1) + "个学生学号");
					if (stdnum == null)
						return;
					if (stdnum.equals("")) {
						JOptionPane.showMessageDialog(null, "输入有误，请重新输入");
						continue;
					}
			
					String string = stdnum + "\t\t" + stdname + "\t\t" + course.clsnum + "\t\t" + course.clsname
							+ "\t\t" + course.teaname + "\t\t" + "undefined" + "\n";
					newinfo.add(string);
				}
				// 3.写入文件
				try {
					BufferedWriter bw = new BufferedWriter(new FileWriter("./Grade/" + course.clsnum + ".txt"));
					String total = "";
					for (String s1 : newinfo) {
						total += s1;
					}
					bw.write(total);
					bw.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}

		void gradeMenu() {
			JFrame frame = new JFrame();
			frame.setBounds(300, 150, 500, 450);// 位置参数
			frame.setTitle("教务员" + AcademicOfficer.this.loginAO.name);// title
			frame.setLayout(null);// 布局
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // 想要只关闭子窗口，方法如下：子窗口设置为setDefaultCloseOption(Jframe.DISPOSE_ON_CLOSE)
			frame.setVisible(true);

			JLabel labChoosefunc = new JLabel("选择一个功能");
			labChoosefunc.setBounds(50, 100, 100, 50);

			JButton btntapStat = new JButton("综合统计");
			btntapStat.setBounds(50, 185, 200, 30);
			btntapStat.setForeground(Color.BLUE);

			JButton btntapRank = new JButton("排名");
			btntapRank.setBounds(50, 225, 200, 30);
			btntapRank.setForeground(Color.BLUE);

			JButton btntapPrint = new JButton("打印");
			btntapPrint.setBounds(50, 265, 200, 30);
			btntapPrint.setForeground(Color.BLUE);

			frame.add(labChoosefunc);
			frame.add(btntapStat);
			frame.add(btntapRank);
			frame.add(btntapPrint);

			btntapStat.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					stat();
				}
			});

			btntapRank.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					rank();
				}
			});
			btntapPrint.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					print();
				}
			});
		}

		boolean chooseCourse() {
			List<Object> list = new ArrayList<Object>();
			for (;;) {
				ArrayList<String> allcls = getAllFileName("./Grade/");
				String s1 = "";
				for (String string : allcls) {
					s1 = s1 + string.substring(0, string.lastIndexOf(".")) + "\n";
					list.add(string.substring(0, string.lastIndexOf(".")));
				}

				int size = list.size();
				Object[] objects = list.toArray(new Object[size]);
				// 接收课程编号输入

				String courseNum = (String) JOptionPane.showInputDialog(null, "请选择您要统计成绩的课程编号\n", "选择课程",
						JOptionPane.PLAIN_MESSAGE, null, objects, null);
				if (courseNum == null) {
					return false;
				}
				if (courseNum == "") {
					JOptionPane.showMessageDialog(null, "请正确输入！");
					continue;
				}
				this.course_to_enter = courseNum;
				break;
			}
			ArrayList<Grade> Allgrade = null;
			Allgrade = AcademicOfficer.this.getGrade(this.course_to_enter);
			this.AllGrade = Allgrade;

			this.AllGrade.sort(new Comparator<Grade>() {

				@Override
				public int compare(Grade o1, Grade o2) {
					// TODO 自动生成的方法存根
					if (o1.grade.equals("undefined") || o1.grade.equals("") || o1.grade == null
							|| o2.grade.equals("undefined") || o2.grade.equals("") || o2.grade == null) {
						return 0;
					}
					int o1g = Integer.parseInt(o1.grade);
					int o2g = Integer.parseInt(o2.grade);
					if (o1g > o2g) {
						return 1;
					}
					if (o1g < o2g) {
						return -1;
					}
					return 0;
				}
			});
			return true;
		}

		void rank() {
			boolean isfilled;
			isfilled = chooseCourse();
			if (!isfilled) {
				return;
			}
			ArrayList<Grade> Allgrade = null;
			Allgrade = AcademicOfficer.this.getGrade(this.course_to_enter);
			this.AllGrade = Allgrade;

			this.AllGrade.sort(new Comparator<Grade>() {

				@Override
				public int compare(Grade o1, Grade o2) {
					// TODO 自动生成的方法存根
					if (o1.grade.equals("undefined") || o1.grade.equals("") || o1.grade == null
							|| o2.grade.equals("undefined") || o2.grade.equals("") || o2.grade == null) {
						return 0;
					}
					int o1g = Integer.parseInt(o1.grade);
					int o2g = Integer.parseInt(o2.grade);
					if (o1g < o2g) {
						return 1;
					}
					if (o1g > o2g) {
						return -1;
					}
					return 0;
				}
			});
			for(Grade grade :this.AllGrade) {
				if (grade.grade.equals("undefined")) {
					JOptionPane.showMessageDialog(null, "成绩尚未初始化");
					return;
				}
			}
			JOptionPane.showMessageDialog(null, "排序完成");
			JFrame frame = new JFrame();
			frame.setBounds(300, 150, 600, 600);// 位置参数
			frame.setTitle("排序结果");// title
			frame.setLayout(null);// 布局
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); 
			frame.setVisible(true);

			JLabel lab = new JLabel("排序结果如下");
			lab.setBounds(250, 5, 400, 50);

			String s = "学号" + "&emsp&emsp&emsp&emsp" + "姓名" + "&emsp&emsp&emsp&emsp" + "成绩" + "<br>";
			for (int i = 0; i < this.AllGrade.size(); i++) {
				s = s + this.AllGrade.get(i).stdnum + " &emsp&emsp&emsp&emsp " + this.AllGrade.get(i).stdname
						+ " &emsp&emsp&emsp&emsp " + this.AllGrade.get(i).grade + "<br>";
			}
			s = "<html><body>" + s + "<body><html>";
			JLabel labResult = new JLabel(s);
			labResult.setBounds(50, 70, 400, 300);
			labResult.setFont(new Font("Serif", Font.PLAIN, 16));

			frame.add(lab);
			frame.add(labResult);
		}

		void print() {
			boolean isfilled;
			isfilled = chooseCourse();
			if (!isfilled) {
				return;
			}
			ArrayList<Grade> Allgrade = null;
			Allgrade = AcademicOfficer.this.getGrade(this.course_to_enter);
			this.AllGrade = Allgrade;
			this.AllGrade.sort(new Comparator<Grade>() {
				@Override
				public int compare(Grade o1, Grade o2) {
					// TODO 自动生成的方法存根
					if (o1.grade.equals("undefined") || o1.grade.equals("") || o1.grade == null
							|| o2.grade.equals("undefined") || o2.grade.equals("") || o2.grade == null) {
						return 0;
					}
					int o1g = Integer.parseInt(o1.grade);
					int o2g = Integer.parseInt(o2.grade);
					if (o1g < o2g) {
						return 1;
					}
					if (o1g > o2g) {
						return -1;
					}
					return 0;
				}
			});
			for(Grade grade :this.AllGrade) {
				if (grade.grade.equals("undedined")) {
					JOptionPane.showMessageDialog(null, "成绩尚未初始化");
					return;
				}
			}
			new Print();// 打印窗口
		}

		void stat() {
			boolean isfilled;
			isfilled = chooseCourse();
			if (!isfilled) {
				return;
			}
			ArrayList<Grade> Allgrade = null;
			Allgrade = AcademicOfficer.this.getGrade(this.course_to_enter);
			this.AllGrade = Allgrade;
			this.AllGrade.sort(new Comparator<Grade>() {
				@Override
				public int compare(Grade o1, Grade o2) {
					// TODO 自动生成的方法存根
					if (o1.grade.equals("undefined") || o1.grade.equals("") || o1.grade == null
							|| o2.grade.equals("undefined") || o2.grade.equals("") || o2.grade == null) {
						return 0;
					}
					Integer o1g = Integer.parseInt(o1.grade);
					Integer o2g = Integer.parseInt(o2.grade);
					return o1g.compareTo(o2g);
				}
			});
			int total = 0;
			int count = 0;
			Grade highest = AcademicOfficer.GradeMaintain.this.AllGrade.get(0);
			Grade lowest = AcademicOfficer.GradeMaintain.this.AllGrade.get(0);
			for (Grade grade : AcademicOfficer.GradeMaintain.this.AllGrade) {
				if (grade.grade.equals("undefined") || grade.grade.equals("") || grade.grade == null) {
					continue;
				}
				if (Integer.parseInt(grade.grade) > Integer.parseInt(highest.grade)) {
					highest = grade;
				}
				if (Integer.parseInt(grade.grade) < Integer.parseInt(highest.grade)) {
					lowest = grade;
				}
				total += Integer.parseInt(grade.grade);
				count++;
			}
			if (count == 0) {
				JOptionPane.showMessageDialog(null, "成绩尚未初始化");
				return;
			}
			int avg = total / count;
			JFrame frame = new JFrame();
			frame.setBounds(300, 100, 600, 600);// 位置参数
			frame.setTitle("统计信息");// title
			frame.setLayout(null);// 布局
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // 想要只关闭子窗口，方法如下：子窗口设置为setDefaultCloseOption(Jframe.DISPOSE_ON_CLOSE)
			frame.setVisible(true);

			JLabel labavg = new JLabel("平均分");
			labavg.setBounds(50, 5, 400, 50);

			JLabel labdisavg = new JLabel(String.valueOf(avg));
			labdisavg.setBounds(50, 45, 400, 50);

			JLabel labhigh = new JLabel("最高分");
			labhigh.setBounds(50, 85, 400, 50);

			JLabel labhighdis = new JLabel(highest.stdname + "  " + highest.stdnum + "  " + highest.grade);
			labhighdis.setBounds(50, 125, 400, 50);

			JLabel lablow = new JLabel("最低分");
			lablow.setBounds(50, 165, 400, 50);

			JLabel lablowdis = new JLabel(lowest.stdname + "  " + lowest.stdnum + "  " + lowest.grade);
			lablowdis.setBounds(50, 205, 400, 50);

			frame.add(labavg);
			frame.add(labdisavg);
			frame.add(labhigh);
			frame.add(labhighdis);
			frame.add(lablow);
			frame.add(lablowdis);

		}

		class Print extends JFrame implements Printable, ActionListener {
			JButton printBtn = new JButton("点击此处开始打印");
			// 设置窗口的信息，添加各种面板按钮，及初始化按钮的监听器。

			public Print() {
				this.setBounds(500, 300, 100, 80);
				printBtn.setBounds(500, 300, 50, 40);
				this.add(printBtn);
				printBtn.addActionListener(this);
				setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				setVisible(true);
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				// 获取打印服务对象
				PrinterJob job = PrinterJob.getPrinterJob();
				job.setPrintable(Print.this); // 添加打印任务
				try {
					job.print(); // 执行打印任务
				} catch (PrinterException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(this, "导出打印PDF文件成功");
			}

			@Override
			public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
				Graphics2D g = (Graphics2D) graphics;
				int x = (int) pageFormat.getImageableX()+20;
				int y = (int) pageFormat.getImageableY();
				Font font1 = new Font("宋体", Font.BOLD, 16);
				Font font2 = new Font("宋体", Font.PLAIN, 14);
				switch (pageIndex) {
				case 0:
					g.setColor(Color.BLACK);
					g.setFont(font1);
					g.drawString("             课程" + AcademicOfficer.GradeMaintain.this.course_to_enter + "成绩表",
							x + 20, y + 20);
					if (AcademicOfficer.GradeMaintain.this.AllGrade.size() == 0) {
						JOptionPane.showMessageDialog(null, "成绩信息为空，打印失败");
						return  NO_SUCH_PAGE ;
					}
					Grade temp = AcademicOfficer.GradeMaintain.this.AllGrade.get(0);
					g.drawString("授课教师："+temp.teaname+"              课程名称："+temp.clsname, x+20, y+60);
					g.drawLine(x+5, y+65, x+400, y+65);
					g.drawString("学号" , x + 40, y + 85);
					g.drawString("姓名" , x + 170, y + 85);
					g.drawString("成绩" , x + 320, y + 85);
					g.drawLine(x+5, y+65, x+5, y+100);
					g.drawLine(x+120, y+65, x+120, y+100);
					g.drawLine(x+260, y+65, x+260, y+100);
					g.drawLine(x+400, y+65, x+400, y+100);
					g.drawLine(x+5, y+100, x+400, y+100);
					for (int i = 0; i < AcademicOfficer.GradeMaintain.this.AllGrade.size(); i++) {
						g.setFont(font2);
						Grade grade = AcademicOfficer.GradeMaintain.this.AllGrade.get(i);
						g.drawLine(x+5, y+100+35*i, x+5, y+100+35*(i+1));
						g.drawLine(x+120, y+100+35*i, x+120, y+100+35*(i+1));
						g.drawLine(x+260, y+100+35*i, x+260, y+100+35*(i+1));
						g.drawLine(x+400, y+100+35*i, x+400, y+100+35*(i+1));
						g.drawString(grade.stdnum, x+55, y+90+35*(i+1));
						g.drawString(grade.stdname, x+180, y+90+35*(i+1));
						g.drawString(grade.grade, x+300, y+90+35*(i+1));
						g.drawLine(x+5, y+100+35*(i+1), x+400, y+100+35*(i+1));
					}
					return PAGE_EXISTS;
				default:
					return NO_SUCH_PAGE;
				}
			}
		}
	}
	
	class CourseMaintain{
		ArrayList<Course> AllCourse;
		String course_found;
		int index;
		
		JLabel labcnum ;
		JTextField txtcnum ;
		JLabel labcname ;
		JTextField txtname ;
		JLabel labcre;
		JTextField txtcre ;
		JLabel labch ;
		JTextField txtch ;
		JLabel  labtea;
		JTextField txttea;

		public CourseMaintain() {
			// TODO 自动生成的构造函数存根
			this.AllCourse= getCourse();
			if (AllCourse.size() == 0) {
				int option = JOptionPane.showConfirmDialog(null, "无课程信息，是否新建课程信息文件?", "错误提示",
						JOptionPane.YES_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					creatNewCourse();
				} else {
					return;
				}
			}
			else {
				courseMenu();
			}

		}

		private void courseMenu() {
			// TODO 自动生成的方法存根
			JFrame frame = new JFrame();
			frame.setBounds(300, 150, 500, 450);// 位置参数
			frame.setTitle("课程信息管理");// title
			frame.setLayout(null);// 布局
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // 想要只关闭子窗口，方法如下：子窗口设置为setDefaultCloseOption(Jframe.DISPOSE_ON_CLOSE)
			frame.setVisible(true);
			
			JButton btnfind = new JButton("管理课程信息(删除、修改)");
			btnfind.setBounds(50, 185, 200, 30);
			btnfind.setForeground(Color.BLUE);
			
			JButton btnadd = new JButton("增加课程信息");
			btnadd.setBounds(50, 225, 200, 30);
			btnadd.setForeground(Color.BLUE);
			
			btnfind.addActionListener(new ActionListener() {
			      public void actionPerformed(ActionEvent e) {
			          AcademicOfficer.CourseMaintain.this.change();
			          }});

			btnadd.addActionListener(new ActionListener() {
			      public void actionPerformed(ActionEvent e) {
			          AcademicOfficer.CourseMaintain.this.add();
			          }});
			
			frame.add(btnadd);
			frame.add(btnfind);
		}

		private void creatNewCourse() {
			// TODO 自动生成的方法存根
			add();
		}
		
		private boolean chooseCourse() {
			List<Object> list = new ArrayList<Object>();
			for (;;) {
				ArrayList<String> temp = new ArrayList<>();
				for(Course co: this.AllCourse) {
					temp.add(co.clsname);
				}
				ArrayList<String> allcls = temp;
				for (String string : allcls) {
					list.add(string);
				}

				int size = list.size();
				Object[] objects = list.toArray(new Object[size]);

				String cname = (String) JOptionPane.showInputDialog(null, "请选择您要操作成绩的课程名称\n", "选择课程",
						JOptionPane.PLAIN_MESSAGE, null, objects, null);
				if (cname == null) {
					return false;
				}
				if (cname == "") {
					JOptionPane.showMessageDialog(null, "请正确输入！");
					continue;
				}
				this.course_found = cname;
				break;
			}
			return true;
		}

		private void add() {
			JFrame frame = new JFrame();
			frame.setBounds(300, 100, 600, 600);// 位置参数
			frame.setTitle("统计信息");// title
			frame.setLayout(null);// 布局
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // 想要只关闭子窗口，方法如下：子窗口设置为setDefaultCloseOption(Jframe.DISPOSE_ON_CLOSE)
			frame.setVisible(true);
			
			labcnum = new JLabel("课程编号");
			labcnum.setBounds(50, 20, 100, 50);
			
			txtcnum = new JTextField("",30);
			txtcnum.setBounds(130, 35, 180, 30); 
			
			labcname = new JLabel("课程名称");
			labcname.setBounds(50, 60, 100, 50);
			
			txtname = new JTextField("",30);
			txtname.setBounds(130, 76, 180, 30); 
			
			labcre = new JLabel("学分");
			labcre.setBounds(50, 100, 50, 50);
			
			txtcre = new JTextField("",30);
			txtcre.setBounds(130, 117, 180, 30);
			
			labch = new JLabel("课时");
			labch.setBounds(50, 140, 100, 50);
			
			txtch = new JTextField("",30);
			txtch.setBounds(130, 158, 180, 30);
			
			labtea= new JLabel("授课教师");
			labtea.setBounds(50, 189, 100, 50);
			
			txttea = new JTextField("",30);
			txttea.setBounds(130, 199, 180, 30);

			
			JButton btnsave = new JButton("保存更改");
			btnsave.setBounds(50, 385, 120, 20);
			
			btnsave.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {					
					String cnum = txtcnum.getText(); 				
					String cname = txtname.getText();										
					String cre = txtcre.getText(); 				
					String ch = txtch.getText(); 		
					String ctea = txttea.getText(); 					
					Course ctemp = new Course();
					ctemp.SetData(cnum, cname, cre, ch, ctea);
					AcademicOfficer.CourseMaintain.this.AllCourse.add(ctemp);
					save();		
				}});

			frame.add(txtch);
			frame.add(txtcnum);
			frame.add(txtcre);
			frame.add(txtname);
			frame.add(txttea);
			frame.add(labch);
			frame.add(labcname);
			frame.add(labcnum);
			frame.add(labcre);
			frame.add(labtea);
			frame.add(btnsave);
			}	
				
		private void save() {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter("./Course.txt"));
				for (Course ao : AcademicOfficer.CourseMaintain.this.AllCourse) {
					bw.write(String.format("%s\t\t%s\t\t%s\t\t%s\t\t%s\n", ao.clsnum, ao.clsname, ao.credit,
							ao.clshour,ao.teaname));
				}
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "课程信息已更新，修改成功");
		}
		
		 void change() {
			boolean isfound = chooseCourse();
			if (!isfound) {
				return;
			}
			for(int i = 0; i< AcademicOfficer.CourseMaintain.this.AllCourse.size();i++) {
				if(AllCourse.get(i).clsname.equals(AcademicOfficer.CourseMaintain.this.course_found)) {
					AcademicOfficer.CourseMaintain.this.index = i;
					JFrame frame = new JFrame();
					frame.setBounds(300, 100, 600, 600);// 位置参数
					frame.setTitle("查看课程信息");// title
					frame.setLayout(null);// 布局
					frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // 想要只关闭子窗口，方法如下：子窗口设置为setDefaultCloseOption(Jframe.DISPOSE_ON_CLOSE)
					frame.setVisible(true);
					
					labcnum = new JLabel("课程编号");
					labcnum.setBounds(50, 20, 100, 50);
					
					txtcnum = new JTextField(AllCourse.get(i).clsnum,30);
					txtcnum.setBounds(130, 35, 180, 30); 
					
					labcname = new JLabel("课程名称");
					labcname.setBounds(50, 60, 100, 50);
					
					txtname = new JTextField(AllCourse.get(i).clsname,30);
					txtname.setBounds(130, 76, 180, 30); 
					
					labcre = new JLabel("学分");
					labcre.setBounds(50, 100, 50, 50);
					
					txtcre = new JTextField(AllCourse.get(i).credit,30);
					txtcre.setBounds(130, 117, 180, 30);
					
					labch = new JLabel("课时");
					labch.setBounds(50, 140, 100, 50);
					txtch = new JTextField(AllCourse.get(i).clshour,30);
					txtch.setBounds(130, 158, 180, 30);
					
					labtea= new JLabel("授课教师");
					labtea.setBounds(50, 189, 100, 50);
					
					txttea = new JTextField(AllCourse.get(i).teaname,30);
					txttea.setBounds(130, 199, 180, 30);
					
					JButton btnsave = new JButton("保存更改");
					btnsave.setBounds(50, 385, 100, 20);
					
					JButton btndelete = new JButton("删除此记录");
					btndelete.setBounds(150, 385, 100, 20);

					btnsave.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {							
							String cnum = txtcnum.getText(); 							
							String cname = txtname.getText();						
							String cre = txtcre.getText(); 
							String ch = txtch.getText(); 						
							String ctea = txttea.getText(); 							
							Course ctemp = new Course();
							ctemp.SetData(cnum, cname, cre, ch, ctea);
							AcademicOfficer.CourseMaintain.this.AllCourse.set(AcademicOfficer.CourseMaintain.this.index, ctemp);
							save();
						}
					});

					btndelete.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							AcademicOfficer.CourseMaintain.this.AllCourse.remove(index);
							save();
							return;
						}
					});
					
					frame.add(txtch);
					frame.add(txtcnum);
					frame.add(txtcre);
					frame.add(txtname);
					frame.add(txttea);
					frame.add(labch);
					frame.add(labcname);
					frame.add(labcnum);
					frame.add(labcre);
					frame.add(labtea);
					frame.add(btndelete);
					frame.add(btnsave);
				}
			}
		}
	}
}

