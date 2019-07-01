import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

import javax.swing.*;

//只有总控程序

class MainFrame {
    private int statenum = 12;//状态个数
    private char[] endcharz ={'i','+','*','(',')','#'};					//终结符数组
	private char[] noendcharz = {'E','T','F'};							//非终结符数组
	private String[] pro = {"E->E+T","E->T","T->T*F","T->F","F->(E)","F->i"};//产生式数组
	private String[][] action = new String[statenum][endcharz.length]; // action 表
	private int[][] Goto = new int[statenum][noendcharz.length]; // goto表
    private Stack<Integer> stateStack=new Stack<>();//状态栈
    private Stack<Character> charStack=new Stack<>();//符号栈
	//GUI部分
	private JFrame frame;
	private JLabel inputlabel;
    private JTextField inputStr;
    private JButton analyzeBtn;
    private JTextArea showAnalyzeArea;
    private JScrollPane js; // 下拉框
	public MainFrame() {
	    initGUI(); // 初始化界面
		initData();//初始化数据
		// 设置按钮监听
		analyzeBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				startAnalyze(); // 开始分析
			}
		});
		//
		/////////////////////
	}
    // 初始化界面
    private void initGUI(){
        frame=new JFrame("LR(1)分析");
        frame.setLocation(100, 100);
        frame.setSize(674,600);
        frame.setVisible(true);
        frame.setBackground(Color.BLACK);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container=frame.getContentPane();
        container.setLayout(null);
        //标签
        inputlabel=new JLabel("字符串输入");
        container.add(inputlabel);
        inputlabel.setBounds(5, 0, 200, 40);
        //输入框
        inputStr = new JTextField();
        inputStr.setFont(new Font("黑体",Font.BOLD,20));
        inputStr.setText("i*i+i#");
        container.add(inputStr);
        inputStr.setBounds(5, 40, 510, 40);
        //分析按钮
        analyzeBtn =new JButton("开始分析");
        container.add(analyzeBtn);

        analyzeBtn.setBounds(520, 40,144, 40);
        //分析结果显示框
        showAnalyzeArea =new JTextArea();
        // showAnalyzeArea.setFont(new Font("黑体",Font.BOLD,12));
        //滚动条
        js=new JScrollPane(showAnalyzeArea);
        js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        container.add(js);
        js.setBounds(5, 80,660,485);
        //----endGUI----
    }
//初始化数据函数
private void initData()
{
	//action表
	for(int state=0; state < statenum; state++)
		for(int i=0;i<endcharz.length;i++)
		{
			action[state][i]="^";//默认^为空符号
		}
	//按照实验表构建
	action[0][0]="S5";
	action[0][3]="S4";
	action[1][1]="S6";
	action[1][5]="acc";
	action[2][1]="r2";
	action[2][2]="S7";
	action[2][4]="r2";
	action[2][5]="r2";
	action[3][1]="r4";
	action[3][2]="r4";
	action[3][4]="r4";
	action[3][5]="r4";
	action[4][0]="S5";
	action[4][3]="S4";
	action[5][1]="r6";
	action[5][2]="r6";
	action[5][4]="r6";
	action[5][5]="r6";
	action[6][0]="S5";
	action[6][3]="S4";
	action[7][0]="S5";
	action[7][3]="S4";
	action[8][1]="S1";
	action[8][4]="S11";
	action[9][1]="r1";
	action[9][2]="S7";
	action[9][4]="r1";
	action[9][5]="r1";
	action[10][1]="r3";
	action[10][2]="r3";
	action[10][4]="r3";
	action[10][5]="r3";
	action[11][1]="r5";
	action[11][2]="r5";
	action[11][4]="r5";
	action[11][5]="r5";
	//构建GOTO表
	for(int state = 0; state < statenum; state ++)
		for(int i = 0;i < noendcharz.length; i++)
		{
			Goto[state][i]=-1;//初始化为-1是空的
		}
	Goto[0][0]=1;
	Goto[0][1]=2;
	Goto[0][2]=3;
	Goto[4][0]=8;
	Goto[4][1]=2;
	Goto[4][2]=3;
	Goto[6][1]=9;
	Goto[6][2]=3;
	Goto[7][2]=10;

	// console ation表 和 goto表
	for(int i=0;i<12;i++)
	{	for(int j=0;j<endcharz.length;j++)
		{
			System.out.print(action[i][j]+"\t");
		}
		for(int j = 0;j < noendcharz.length; j++)
		{
			System.out.print(Goto[i][j]+"\t");

		}
		System.out.println();


	}
}
    // 初始化两个栈
    private void initStack(){
	    // 清空栈
        stateStack.clear();
        charStack.clear();
	    //初始化状态栈
        stateStack.add(0);
        //初始化符号栈
        charStack.add('#');
    }

// action值
String action(int state,char in)//根据输入的状态和识别的符号在action表中寻找对应的值
{
	int charindex = getEndCharIndex(in);//获得终结对应的下标
	return action[state][charindex];
}

// 获得终结符对应的下标
int getEndCharIndex(char in)
{
	for(int i=0;i<endcharz.length;i++)
	{
		if(endcharz[i] == in)
		{
			return i;//返回对应的下标
		}
	}
    return -1; //找不见返回错误标记
}

// goto表的状态转换
int GOTO(int state,char A)
{
	int char_index=getNoEndCharIndex(A); // 获得符号的下标
	return Goto[state][char_index]; // 返回对应的goto表的值
}

//
int getNoEndCharIndex(char A)//获得非终结符的下标
{
	for(int i=0;i<noendcharz.length;i++)
	{
		if(noendcharz[i]==A)
			return i;
	}
	return -1;
}

// 错误信息
void ERROR()
{
   System.out.println("分析错误");
    JOptionPane.showMessageDialog(null,"分析错误");
}

// 开始分析
private void startAnalyze() {
    try {
        initStack(); // 初始化符号栈
        int step=1;// 步骤
        String input=inputStr.getText();//获得输入字符
        // valified 校验
        if(input.charAt(input.length()-1) != '#'){
            JOptionPane.showMessageDialog(null,"输入格式错误");
            return;
        }
        int readpoint=0;				//读取输入符号的指针
        boolean flag=true;				//判断识别是否成功
        char a = input.charAt(readpoint);//当前正在读取的字符
        int s= stateStack.peek();//状态栈的栈顶状态
        //输出分析表头
        showAnalyze("步骤","状态栈","符号栈","输入串","动作说明");
        while(!action(s, a).equals("acc")) //当前状态没有识别成功
        {
            a = input.charAt(readpoint);//当前正在读取的字符
            s = stateStack.peek();//状态栈的栈顶状态
            String ac=action(s, a);
            if(ac.equals("^"))//识别错误
            {
                ERROR();
                flag = false; //识别标志置成错误
                break;//退出循环
            }
            else if (ac.charAt(0)=='S') {//如果识别到Sn
                //把a移入符号栈
                charStack.add(a);
                //把n移入状态栈
                String nString=ac.substring(1);//获得Sn中n的字符串
                int n=Integer.parseInt(nString);//转换成数字
                stateStack.add(n);//n入栈
                // 说明
                String description = "ACTION("+s+","+a+")="+ac+",状态"+n+"入栈";
                showAnalyze(String.valueOf(step),
                        getStateStackString(),
                        getCharStackString(),
                        input.substring(readpoint),
                        description);
                step++;
                /////
                // System.out.println("ACTION("+s+","+a+")="+ac+",状态"+n+"入栈");
                //读取下一个字符
                readpoint=readpoint+1;
            }else if (ac.charAt(0)=='r') { //如果是ACTION(S,a)=rn;归约
                //使用产生式n归约符号栈
                String nString=ac.substring(1);
                int n=Integer.parseInt(nString);
                String pros=pro[n-1];//获得产生式产生式数组从0开始
                char head=pros.charAt(0);//获得左部的符号
                int popn=pros.substring(3).length();//产生式右部的长度
                //符号栈出栈对应长度的数据
                for(int pop=1; pop<=popn; pop++)
                {
                    charStack.pop();//符号栈出栈对应长度
                    stateStack.pop();//状态栈出栈对应长度
                }
                //把产生式的左部进栈
                charStack.add(head);
                //把goto(s,a)放进状态栈
                s=stateStack.peek();//s指向当前状态栈顶
                int st = GOTO(s, head);
                stateStack.add(st);
                //输出
                String description = ac+":"+pros+" 归约,GOTO("+s+","+head+")="+st+"入栈";
                showAnalyze(String.valueOf(step),getStateStackString(),getCharStackString(),input.substring(readpoint),description);
                step++;
                ////
                // System.out.println(ac+":"+pros+"归约,GOTO("+s+","+head+")="+st+"入栈");
            }
        }
        if(flag) // 在循环的时候没有错误
        {
            showAnalyzeArea.append("Acc:识别成功！\n");

            System.out.println("识别成功！");
        }
    } catch (Exception e2) {
        System.out.println("产生异常了");
        System.out.println(e2.getMessage());
        showAnalyzeArea.append("识别错误");
        // TODO: handle exception
    }
}
    // 获得状态栈的字符串
    private String getStateStackString(){
	    String re = "";
        for (int item:stateStack) {
            re = re + item + " ";
        }
	    return re;
    }
    // 获得符号栈函数
    private String getCharStackString() {
	    // return charStack.toString();
        String re ="";
        for (char item :
                charStack) {
            re += item; // 拼接
        }
        return re;
    }


// 在文本栏中显示相关信息
private void showAnalyze(String step, String stateStack, String charStack,String inputStr,String description){
	    String output = step +"\t"+stateStack+"\t\t"+charStack+"\t"+inputStr+"\t"+description+"\n";
	    showAnalyzeArea.append(output);
    }
}




public class LR1 {
public static void main(String[] args) {
	new MainFrame();
}
}
