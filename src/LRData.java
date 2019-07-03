import java.util.Stack;

public class LRData {
    private static int statenum = 12;//状态个数
    private static char[] endcharz ={'i','+','*','(',')','#'};					//终结符数组
    private static char[] noendcharz = {'E','T','F'};							//非终结符数组
    public static String[] pro = {"E->E+T","E->T","T->T*F","T->F","F->(E)","F->i"};//产生式数组
    private static String[][] action = new String[statenum][endcharz.length]; // action 表
    private static int[][] Goto = new int[statenum][noendcharz.length]; // goto表
    public static Stack<Integer> stateStack=new Stack<>();//状态栈
    public static Stack<Character> charStack=new Stack<>();//符号栈
    public static String input = "i*i+i#"; // 初始化字符串
    public static int readPoint =0;  // 当前读取字符串的指针
    public static boolean threadExit = false;
    public static String description = "初始化";
    static {
        initData();
        initStack();
    }

    // 初始化数据
    public static void initData(){
        for(int state=0; state < statenum; state++) {
            for (int i = 0; i < endcharz.length; i++) {
                action[state][i] = "^";//默认^为空符号
            }
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
        for(int state = 0; state < statenum; state ++) {
            for (int i = 0; i < noendcharz.length; i++) {
                Goto[state][i] = -1; //初始化为-1是空的
            }
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

    // 数据处理相关函数
    public static String action(int state,char in)//根据输入的状态和识别的符号在action表中寻找对应的值
    {
        int charindex = getEndCharIndex(in);//获得终结对应的下标
        return action[state][charindex];
    }

    // 获得终结符对应的下标
    private static int getEndCharIndex(char in)
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
    public static int GOTO(int state,char A)
    {
        int char_index=getNoEndCharIndex(A); // 获得符号的下标
        return Goto[state][char_index]; // 返回对应的goto表的值
    }

    //
    private static int getNoEndCharIndex(char A)//获得非终结符的下标
    {
        for(int i=0;i<noendcharz.length;i++)
        {
            if(noendcharz[i]==A) {
                return i;
            }
        }
        return -1;
    }

    // 初始化两个栈
    public static void initStack(){
        // 清空栈
        stateStack.clear();
        charStack.clear();
        //初始化状态栈
        stateStack.add(0);
        //初始化符号栈
        charStack.add('#');
    }

    // 获得状态栈顶值
    public static int getStateStackPeek(){
        return stateStack.peek();
    }

    // 获得符号栈顶的值
    public static char getCharStackPeek(){
        return charStack.peek();
    }
    // 获得状态栈的字符串
    public static String getStateStackString(){
        String re = "";
        for (int item:stateStack) {
//            re = re + item + " ";
            re = re + item;
        }
        return re;
    }
    // 获得符号栈函数
    public static String getCharStackString() {
        // return charStack.toString();
        String re ="";
        for (char item :
                charStack) {
            re += item; // 拼接
        }
        return re;
    }

}
