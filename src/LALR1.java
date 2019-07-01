import java.awt.EventQueue;  
import java.awt.GridBagConstraints;  
import java.awt.GridBagLayout;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.io.BufferedReader;  
import java.io.File;  
import java.io.FileNotFoundException;  
import java.io.FileReader;  
import java.io.IOException;  
import java.text.SimpleDateFormat;  
import java.util.Calendar;  
import java.util.Date;  
import java.util.Stack;  
import java.util.Timer;  
import java.util.TimerTask;  
  
import javax.swing.BorderFactory;  
import javax.swing.JButton;  
import javax.swing.JFileChooser;  
import javax.swing.JFrame;  
import javax.swing.JLabel;  
import javax.swing.JScrollPane;  
import javax.swing.JTable;  
import javax.swing.JTextArea;  
import javax.swing.JTextField;  
import javax.swing.UIManager;  
import javax.swing.filechooser.FileFilter;  
import javax.swing.filechooser.FileNameExtensionFilter;  
import javax.swing.table.DefaultTableCellRenderer;  
  
public class LALR1 extends JFrame {  
    /** 
     *  
     */  
    private static final long serialVersionUID = 1L;  
    private char VN[]={'S','E','T','F'};//非终结符  
    private char VT[]={'+','*','(',')','i','$','ε'};//终结符  
    private char Total[]={'S','E','T','F','+','*','(',')','i','$'};  
    /*private char Total[]={'S','E','C','c','d','$'}; 
    private char VN[]={'S','E','C'};//非终结符 
    private char VT[]={'c','d','$','ε'};//终结符*/  
    private String M[][]=new String[100][100];  
    private String M2[][]=new String[100][100];  
    private String[] First=new String[VN.length];//单个非终结符的first集  
    private String M1[][]=new String[100][100];  
    private int N[][]=new int[100][100];  
    private int Goto[][]=new int[100][Total.length];  
    private String ACtion[][]=new String[100][VT.length-1];  
    private boolean Visited[]=new boolean[100];  
    private String G[]={"S->E","E->E+T","E->T","T->T*F","T->F","F->(E)","F->i"};//文法  
    //private String G[]={"S->E","E->CC","C->cC","C->d"};//文法  
      
    //建立表格  
        private Object[] colname = {"步骤","分析栈","分析符号","剩余字符串","动作"};  
        private Object[][] data=new Object[50][5];  
        private JTable table;  
        private JTextField j3;  
        private JLabel j8;  
        private JLabel j9;  
        private JLabel j10;  
        private JTextArea j5;  
        private JScrollPane jsPan;  
        private JScrollPane jsPane;  
        private File imgFile = null;// 声明所选择文件  
        private BufferedReader in1;  
        private GridBagLayout layout;  
        private GridBagConstraints s;  
        private int h = 0;  
        private String getstr=null;  
        private int ONE_SECOND = 1000;  
        private String DEFAULT_TIME_FORMAT = "HH:mm:ss";  
        private String time;  
          
      //获取系统的日期  
        public String Get_Date() {  
             Date date = new Date();      
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");    
             String dateNowStr = sdf.format(date);    
             return dateNowStr;  
        }  
          
        public void Get_Time() {  
               
             Timer tmr = new Timer();  
             tmr.scheduleAtFixedRate(new JLabelTimerTask(),new Date(), ONE_SECOND);  
        }  
          
        //将表格清空  
                public void Get_Clear() {  
                    int i;  
                    for(i=0;i<50;i++) {  
                        data[i]=new Object[]{"","","","",""};  
                    }  
                }  
                  
      
    public void Init() {//初始化follow集，将#放到开始符号集中去  
        M[0][0]="S->.E";  
        M2[0][0]="S->.E,$";  
        int i=0,j=0;  
        for(i=0;i<100;i++) {  
            for(j=0;j<100;j++) {  
                N[i][j]=0;  
            }  
        }  
    /*  for(i=0;i<100;i++) { 
            for(j=0;j<100;j++) { 
                M1[i][j]="love"; 
            } 
        }*/  
        M1[0][0]="S->.E";  
          
        for(i=0;i<100;i++) {  
            Visited[i]=false;  
        }  
    }  
      
    public int IsInTotal(char c) {//判断是否在总的字符中  
        int i;  
        int locate=999;  
        for(i=0;i<Total.length;i++) {  
            if(Total[i]==c) {  
                locate=i;  
            }  
        }  
        return locate;  
    }  
      
    public int IsInVn(char ch) {//判断字符是否属于非终结符  
        int judge=999;  
        for(int i=0;i<VN.length;i++) {  
            if(ch==VN[i]) {  
                judge=i;  
                break;  
            }  
        }  
        return judge;  
    }  
      
      
    public int IsInVt(char ch) {//判断字符是否属于终结符  
        int judge=999;  
        for(int i=0;i<VT.length;i++) {  
            if(ch==VT[i]) {  
                judge=i;  
                break;  
            }  
        }  
        return judge;  
    }  
      
    public int IsInFirst(String s,char ch) {//判断字符是否在first集中  
        int judge=999;  
        for(int i=0;i<s.length();i++) {  
            if(ch==s.charAt(i)) {  
                judge=i;  
                break;  
            }  
        }  
        return judge;  
    }  
      
    public  String Simply(String s) {//去除重复的字符  
        StringBuffer sb = new StringBuffer();  
        int len = s.length();  
        int i = 0;  
        boolean flag = false;  
   
        for (i = 0; i < len; i++) {  
            char c = s.charAt(i);  
            if(c!='n'&&c!='u'&&c!='l') {  
   
            if (s.indexOf(c) != s.lastIndexOf(c)) {  
                flag = false;  
            }else{  
                flag = true;  
            }  
            if(i==s.indexOf(c))  
                flag=true;  
            if (flag) {  
                sb.append(c);  
            }  
        }  
        }  
        return sb.toString();  
    }  
      
    public boolean Check(int a[],int b[]) {//检查两个数组是否相等  
        int i;  
        int count=0;  
        for(i=0;i<VN.length;i++) {  
            if(a[i]==b[i]) {  
                count++;  
            } else {  
                break;  
            }  
        }  
        return (count==VN.length);  
    }  
      
    public void First_Call(char s[],int j,int i) {//子过程  
            if(IsInVn(s[j])!=999) {//如果推导式后面的是非终结符  
                if(First[IsInVn(s[j])]!=null) {//如果它的first集为非空  
                    if(IsInFirst(First[IsInVn(s[j])],'ε')==999) {//如果ε不在这个非终结符中  
                        First[IsInVn(s[0])]+=First[IsInVn(s[j])];  
                    } else {//ε在这个非终结符中  
                        First[IsInVn(s[0])]+=First[IsInVn(s[j])];  
                        j=j+1;//取下一个符号  
                        while(j<G[i].length()&&s[j]!='|') {  
                            First[IsInVn(s[0])]+=First[IsInVn(s[j])];  
                            if(IsInFirst(First[IsInVn(s[j])],'ε')!=999) {  
                                j++;  
                            } else {  
                                break;  
                            }  
                        }  
                          
                    }//步骤2中的过程  
                }  
        } else {//如果第一个后面的是终结符  
            First[IsInVn(s[0])]+=s[j];  
        }  
    }  
  
      
    public void Get_First() {//求解单个非终结符first集  
        int i,j;  
        for(i=0;i<G.length;i++) {  
            char s[]=G[i].toCharArray();  
            j=0;  
            if(IsInVn(s[j])!=999) {//如果第一个是非终结符  
                j=j+3;  
                First_Call(s,j,i);  
                while(j<s.length) {  
                    if(s[j]=='|') {//判断|,继续后续的操作  
                        j++;  
                        First_Call(s,j,i);  
                    } else {  
                        j++;  
                    }  
                }  
            }  
        }  
    }  
      
    public void First_Finally() {  
        int i;  
        int Count[]=new int[VN.length];//存放当前first集中元素的个数  
        int Count1[]=new int[VN.length];//存放执行后first集中元素的个数  
        Count[0]=1;//不等进行循环  
          
        while(Check(Count,Count1)==false) {  
            for(i=0;i<VN.length;i++) {  
                if(First[i]!=null) {  
                    Count[i]=First[i].length();  
                } else {  
                    Count[i]=0;  
                }  
            }  
              
            Get_First();  
            for(i=0;i<VN.length;i++) {  
                if(First[i]!=null) {  
                    First[i]=Simply(First[i]);  
                }  
            }  
            for(i=0;i<VN.length;i++) {  
                if(First[i]!=null) {  
                    Count1[i]=First[i].length();  
                } else {  
                    Count1[i]=0;  
                }  
            }  
        }  
          
    }  
      
    public String DeleteCharString(String sourceString, char chElemData) {//删除特定的字符从字符串中  
        String tmpString = "";  
        tmpString += chElemData;  
        StringBuffer stringBuffer = new StringBuffer(sourceString);  
        int iFlag = -1;  
        do {  
            iFlag = stringBuffer.indexOf(tmpString);  
            if (iFlag != -1) {  
                stringBuffer.deleteCharAt(iFlag);  
            }  
        } while (iFlag != -1);  
        return stringBuffer.toString();  
    }  
      
      
    public String Get_First_String(String t) {//求字符串的First集  
        char s[]=t.toCharArray();  
        int i=0;  
        String temp="";  
        if(IsInVn(s[i])!=999) {//如果是非终结符  
            if(IsInFirst(First[IsInVn(s[i])],'ε')!=999) {  
                temp+=First[IsInVn(s[i])];  
                temp=DeleteCharString(temp, 'ε');  
            } else {  
                temp+=First[IsInVn(s[i])];  
            }  
        } else {//否则是终结符  
            if(s[i]!='ε') {  
                temp+=s[i];  
            }  
        }  
          
          
            while(i<s.length) {//循环执行  
                if(IsInVn(s[i])==999) {  
                    break;  
                } else {  
                    if(IsInFirst(First[IsInVn(s[i])],'ε')!=999) {  
                        i++;  
                        if(i<s.length) {  
                            if(IsInVn(s[i])!=999) {//如果是非终结符  
                                if(IsInFirst(First[IsInVn(s[i])],'ε')!=999) {  
                                    temp+=First[IsInVn(s[i])];  
                                    temp=DeleteCharString(temp, 'ε');  
                                } else {  
                                    temp+=First[IsInVn(s[i])];  
                                    break;  
                                }  
                            } else {//否则是终结符  
                                if(s[i]!='ε') {  
                                    temp+=s[i];  
                                    break;  
                                }  
                            }  
                              
                        } else {  
                            break;  
                        }  
                    } else {  
                        break;  
                    }  
                }  
                  
            }  
          
        if(s[0]=='ε'||i==s.length) {//每个字符的first集中都含有ε  
            temp+="ε";  
        }  
        return temp;  
    }  
      
      
      
    public String Get_Xiang(String temp,char c) {//得到项集  
        StringBuilder d=new StringBuilder();  
        int j;  
        for(j=0;j<3;j++) {  
            d.append(temp.charAt(j));  
        }  
        d.append('.');  
        for(j=3;j<temp.length();j++) {  
            d.append(temp.charAt(j));  
        }  
        d.append(',');  
        d.append(c);  
          
        return d.toString();  
    }  
      
    public String Get_Zhong(String temp[][],int I,int i) {//获得first集  
        int j=3;  
        StringBuilder d=new StringBuilder();  
          
        while(temp[I][i].charAt(j)!='.'&&j<temp[I][i].length()) {  
            j++;  
        }  
        j++;  
        if(IsInVn(temp[I][i].charAt(j))!=999&&j<temp[I][i].length()) {  
            j++;  
            while(temp[I][i].charAt(j)!=','&&j<temp[I][i].length()) {  
                d.append(temp[I][i].charAt(j));  
                j++;  
            }  
            j++;  
            d.append(temp[I][i].charAt(j));  
        } else {  
            if(temp[I][i].charAt(j)==',') {  
                j++;  
                d.append(temp[I][i].charAt(j));  
            }  
        }  
          
          
        return Get_First_String(d.toString());  
          
    }  
    //求解CLOSURE  
    public void CLOSURE_LR1(int I) {  
        //System.out.println(I);  
        //System.out.println(M2[I][0]);  
        int i;  
        int j,k,m;  
        int h=0;  
        for(i=0;i<100;i++) {//I中的每个项[A->A.Bb,a]  
            if(M2[I][i]!=null) {//如果字符串非空  
                j=3;  
                while(M2[I][i].charAt(j)!='.'&&j<M2[I][i].length()) {  
                    j++;  
                }  
                j++;  
                if(IsInVn(M2[I][i].charAt(j))!=999) {  
                    for(k=0;k<G.length;k++) {  
                        if(G[k].charAt(0)==M2[I][i].charAt(j)) {  
                            String temp=Get_Zhong(M2,I,i);  
                            for(m=0;m<temp.length();m++) {  
                                if(IsInI(Get_Xiang(G[k],temp.charAt(m)),M2,I)==false) {  
                                    h=Get_NotNull(M2,I);  
                                    M2[I][h]=Get_Xiang(G[k],temp.charAt(m));  
                                    //System.out.println(M2[I][h]);  
                                }  
                            }  
                              
                        }  
                    }  
                }  
            }  
        }  
          
          
    }  
      
      
      
      
    public int Get_NotNull(String temp[][],int k) {//返回最小为空的下标  
        int i;  
        int sum=0;  
        for(i=0;i<100;i++) {  
            if(temp[k][i]!=null) {  
                sum++;  
            }  
        }  
        return sum;  
    }  
      
    public boolean IsInI(String temp,String temp1[][],int i) {  
        boolean judge=false;  
        int k;  
        for(k=0;k<100;k++) {  
            if(temp.equals(temp1[i][k])) {  
                judge=true;  
            }  
        }  
        return judge;  
    }  
      
    public String Get_Xiang(String temp) {//得到项集  
        StringBuilder d=new StringBuilder();  
        int j;  
        for(j=0;j<3;j++) {  
            d.append(temp.charAt(j));  
        }  
        d.append('.');  
        for(j=3;j<temp.length();j++) {  
            d.append(temp.charAt(j));  
        }  
          
        return d.toString();  
    }  
      
      
    //求解CLOSURE  
    public void CLOSURE(int I) {  
        int i;  
        int j,k;  
        int h=0;  
        for(i=0;i<M[I].length;i++) {//I中的每个项[A->A.Bb,a]  
            if(M[I][i]!=null) {//如果字符串非空  
                j=3;  
                while(M[I][i].charAt(j)!='.'&&j<M[I][i].length()) {  
                    j++;  
                }  
                j++;  
                if(IsInVn(M[I][i].charAt(j))!=999) {  
                    for(k=0;k<G.length;k++) {  
                        if(G[k].charAt(0)==M[I][i].charAt(j)) {  
                                if(IsInI(Get_Xiang(G[k]),M,I)==false) {  
                                    h=Get_NotNull(M,I);  
                                    M[I][h]=Get_Xiang(G[k]);  
                                }  
                              
                        }  
                    }  
                }  
            }  
        }  
    }  
      
    //备用  
      
    public int Get_NotNull_B(String str[]) {//返回最小为空的下标  
        int i;  
        int sum=0;  
        for(i=0;i<100;i++) {  
            if(str[i]!=null) {  
                sum++;  
            }  
        }  
        return sum;  
    }  
      
    public boolean IsInI(String temp,String str[]) {  
        boolean judge=false;  
        int k;  
        for(k=0;k<100;k++) {  
            if(temp.equals(str[k])) {  
                judge=true;  
            }  
        }  
        return judge;  
    }  
      
      
    public void CLOSURE_B(String str[]) {  
        int i;  
        int j,k;  
        int h=0;  
        for(i=0;i<str.length;i++) {//I中的每个项[A->A.Bb,a]  
            if(str[i]!=null) {//如果字符串非空  
                j=3;  
                while(str[i].charAt(j)!='.'&&j<str[i].length()) {  
                    j++;  
                }  
                j++;  
                if(j<str[i].length()) {  
                      
                    if(IsInVn(str[i].charAt(j))!=999) {  
                        for(k=0;k<G.length;k++) {  
                            if(G[k].charAt(0)==str[i].charAt(j))   
                                    if(IsInI(Get_Xiang(G[k]),str)==false) {  
                                        h=Get_NotNull_B(str);  
                                        str[h]=Get_Xiang(G[k]);  
                                    }  
                                  
                        }  
                    }  
                      
                }  
            }  
        }  
    }  
      
      
    public String Get_X(String temp[][],int i) {  
        int j,k;  
        StringBuilder d=new StringBuilder();  
        for(j=0;j<temp[i].length;j++) {  
            if(temp[i][j]!=null) {  
                k=3;  
                while(temp[i][j].charAt(k)!='.'&&k<temp[i][j].length()) {  
                    k++;  
                }  
                k++;  
                if(k<temp[i][j].length()&&temp[i][j].charAt(k)!=',') {  
                    d.append(temp[i][j].charAt(k));  
                }  
            }  
  
        }  
        return d.toString();  
          
    }  
      
    public String[] Get_New(String temp[][],int i,char X) {//新的产生式  
        String tem[]=new String[100];  
        int j,k,f=0;  
        StringBuilder d=new StringBuilder();  
        j=0;  
        while(j<temp[i].length) {  
            if(temp[i][j]!=null) {  
                k=0;  
                while(temp[i][j].charAt(k)!='.'&&k<temp[i][j].length()) {  
                    d.append(temp[i][j].charAt(k));  
                    k++;  
                }  
                k++;  
                if(k<temp[i][j].length()&&temp[i][j].charAt(k)==X) {  
                    d.append(temp[i][j].charAt(k));  
                    d.append('.');  
                    k++;  
                    while(k<temp[i][j].length()) {  
                        d.append(temp[i][j].charAt(k));  
                        k++;  
                    }  
                    tem[f]=d.toString();  
                    d.replace(0,d.length(),"");  
                    f++;  
                } else {  
                    d.replace(0,d.length(),"");//清空  
                }  
            }  
            j++;  
        }  
        return tem;  
    }  
      
    public int Get_N(String temp) {//获得元素个数  
        return temp.length();  
    }  
      
    public int Get_J(int k,int h) {  
        int sum=0;  
        int i,j;  
        for(i=0;i<k;i++) {  
            for(j=0;j<100;j++) {  
                sum+=N[i][j];  
            }  
        }  
        for(j=0;j<h;j++) {  
            sum+=N[k][j];  
        }  
        return sum;  
    }  
      
    public int Get_Same(String temp[]) {//判断求出的项集是否已经存在  
        int i=0,j;  
        int correct = 999;  
        int sum = 0;  
        while(i<100) {  
            sum=0;  
            int len=Get_NotNull(M,i);  
            int len1=Get_NotNull_B(temp);  
            if(len==len1) {  
                for(j=0;j<len;j++) {  
                    if(M[i][j].equals(temp[j])) {  
                        sum++;  
                    }  
                }  
                  
                if(sum==len) {  
                    correct=i;  
                    break;  
                } else {  
                    i++;  
                }  
                  
            } else {  
                i++;  
            }  
        }  
        return correct;  
    }  
      
    public int Get_In_X(int k,char X) {//获得字符在序列中的位置  
        int locate=999;  
        int i=0;  
        for(i=0;i<Get_X(M,k).length();i++) {  
            if(X==Get_X(M,k).charAt(i)) {  
                locate=i;  
            }  
        }  
        return locate;  
    }  
      
    public void Copy(int k,String str[]) {//将得到的项集赋值到M中去  
        int i;  
        for(i=0;i<100;i++) {  
            M[k][i]=str[i];  
        }  
    }  
      
    public int GOTO(int I,char X) {//求解goto函数  
        String str[]=new String[100];  
        str=Get_New(M,I,X);  
        /*for(int n=0;n<10;n++) { 
            if(str[n]!=null) { 
                System.out.print(X+" "+str[n]+ " "); 
            } 
        }*/  
        System.out.println();  
        CLOSURE_B(str);  
        int same=Get_Same(str);  
        if(same==999) {//不存在这个新集合  
            int locate=Get_In_X(I,X);  
            N[I][locate]++;  
            int locate1=Get_J(I,locate)+1;  
        //  System.out.print("&"+locate1+" ");  
            Copy(locate1,str);  
            Goto[I][IsInTotal(X)]=locate1;  
            return locate1;  
              
        } else {//如果存在这个M中  
            Goto[I][IsInTotal(X)]=same;  
            return same;  
        }  
    }  
      
    public void Items() {//求closure ,goto 的主例程  
        int i,j;  
        String temp;  
        CLOSURE(0);  
        for(i=0;i<100;i++) {  
            if(Visited[i]==false) {//如果没有访问  
                temp=Get_X(M,i);  
                temp=Simply(temp);  
                if(temp!=null) {  
                    for(j=0;j<temp.length();j++) {  
                        GOTO(i,temp.charAt(j));  
                    }  
                }  
            }  
            Visited[i]=true;  
        }  
          
    }  
      
      
    public void Get_LALR1() {  
        int i,j,g,h,k;  
        char c;  
        CLOSURE_LR1(0);  
        for(i=0;i<100;i++) {  
            for(j=0;j<100;j++) {  
                if(M2[i][j]!=null) {  
                    k=0;  
                    StringBuilder d=new StringBuilder();  
                    while(M2[i][j].charAt(k)!='.') {  
                        d.append(M2[i][j].charAt(k));  
                        k++;  
                    }  
                    k++;  
                    if(M2[i][j].charAt(k)!=',') {  
                        c=M2[i][j].charAt(k);  
                        d.append(c);  
                        d.append('.');  
                        k++;  
                        while(k<M2[i][j].length()) {  
                            d.append(M2[i][j].charAt(k));  
                            k++;  
                        }  
                          
                        g=Goto[i][IsInTotal(c)];  
                        if(IsInI(d.toString(),M2,g)==false) {//判断是否已经存在  
                            h=Get_NotNull(M2,g);  
                            M2[g][h]=d.toString();  
                            //System.out.println(M2[g][h]);  
                            CLOSURE_LR1(g);  
                        }  
                          
                    }  
                }  
            }  
        }  
    }  
      
      
    public int Get_Locate(String temp) {//返回要规约的位置  
        int i;  
        int locate=999;  
        for(i=0;i<G.length;i++) {  
            if(temp.equals(G[i])){  
                locate=i;  
            }  
        }  
        return locate;  
    }  
      
    public void Get_Action() {//获得Action表  
        int i,j,k;  
        for(i=0;i<100;i++) {  
            for(j=0;j<100;j++) {  
                if(M2[i][j]!=null) {  
                    k=0;  
                    StringBuilder d=new StringBuilder();  
                    while(k<M2[i][j].length()&&M2[i][j].charAt(k)!='.') {  
                        d.append(M2[i][j].charAt(k));  
                        k++;  
                    }  
                    k++;  
                    if(IsInVt(M2[i][j].charAt(k))!=999) {//a是终结符  
                        if(Goto[i][IsInTotal(M2[i][j].charAt(k))]!=0) {  
                            ACtion[i][IsInVt(M2[i][j].charAt(k))]="s"+String.valueOf(Goto[i][IsInTotal(M2[i][j].charAt(k))]);  
                        }  
                    } else {  
                        if(M2[i][j].charAt(k)==',') {//如果是逗号  
                            k++;  
                            if(M2[i][j].charAt(0)!='S') {  
                                ACtion[i][IsInVt(M2[i][j].charAt(k))]="r"+String.valueOf(Get_Locate(d.toString()));  
                            } else {  
                                ACtion[i][IsInVt(M2[i][j].charAt(k))]="acc";  
                            }  
                        }  
                    }  
                }  
            }  
        }  
    }  
      
    public char Before_temp(String temp) {//返回.号前面的第一个字符  
        int i=0;  
        while(i<temp.length()&&temp.charAt(i)!='.') {  
            i++;  
        }  
        i--;  
        return temp.charAt(i);  
    }  
      
      
    public void From_M_To_M1() {  
        int i,j,k;  
        char c;  
        for(i=1;i<100;i++) {  
            for(j=0;j<100;j++) {  
                if(M[i][j]!=null) {  
                    if(j==0) {  
                        M1[i][j]=M[i][j];  
                    } else {  
                        c=Before_temp(M[i][0]);  
                        k=0;  
                        while(M[i][j].charAt(k)!='.') {  
                            k++;  
                        }  
                        k--;  
                        if(c==M[i][j].charAt(k)) {  
                            M1[i][j]=M[i][j];  
                        }  
                    }  
                }  
            }  
        }  
    }  
      
    public int Get_Number(int k) {//返回B中的个数  
        int sum=G[k].length()-3;  
        return sum;  
    }  
      
      
    //输出堆栈中的内容  
    public String Print_Stack(Stack<Integer> stack) {  
        return stack.toString();  
    }  
      
      
        //输出堆栈中的内容  
                public String Print_Stack1(Stack<Character> stack) {  
                    String s=stack.toString();  
                    int i;  
                    StringBuilder t=new StringBuilder();  
                    for(i=0;i<s.length();i++) {  
                        if(s.charAt(i)!='['&&s.charAt(i)!=','&&s.charAt(i)!=']'&&s.charAt(i)!=' ') {  
                            t.append(s.charAt(i));//将其中的字符相加为所要求的字符串  
                        }  
                    }  
                    return t.toString();  
                }  
                  
        //输出剩余字符串  
        public String Print_Buffer(String s,int j) {  
            String t=s.substring(j,s.length());  
            return t;  
        }  
      
    public void Get_Run() {  
        getstr = j5.getText();  
        int i=0,j,k;  
        int t;  
        Stack<Integer> stack = new Stack<Integer>(); // 创建堆栈对象   
        Stack<Character> stack1 = new Stack<Character>();  
        stack.push(0);  
        while(true) {  
            int s=stack.peek();  
            if(ACtion[s][IsInVt(getstr.charAt(i))].charAt(0)=='s') {  
                  
                System.out.print(Print_Stack(stack));  
                System.out.print("         ");  
                System.out.print(Print_Stack1(stack1));  
                System.out.print("         ");  
                System.out.print(Print_Buffer(getstr,i));  
                System.out.println();  
                  
                data[h++]=new Object[]{h,Print_Stack(stack),Print_Stack1(stack1),Print_Buffer(getstr,i),"移入"};  
                  
                stack1.push(getstr.charAt(i));  
                stack.push(Integer.parseInt(ACtion[s][IsInVt(getstr.charAt(i))].substring(1,ACtion[s][IsInVt(getstr.charAt(i))].length())));  
                i++;  
            } else {  
                if(ACtion[s][IsInVt(getstr.charAt(i))].charAt(0)=='r') {  
                      
                    System.out.print(Print_Stack(stack));  
                    System.out.print("         ");  
                    System.out.print(Print_Stack1(stack1));  
                    System.out.print("         ");  
                    System.out.print(Print_Buffer(getstr,i));  
                    System.out.print("         ");  
                      
                    j=Integer.parseInt(ACtion[s][IsInVt(getstr.charAt(i))].substring(1,ACtion[s][IsInVt(getstr.charAt(i))].length()));  
                    data[h++]=new Object[]{h,Print_Stack(stack),Print_Stack1(stack1),Print_Buffer(getstr,i),"根据"+G[j]+"归约"};  
                    k=Get_Number(j);  
                    while(k>0) {  
                        stack.pop();  
                        stack1.pop();  
                        k--;  
                    }  
                    stack1.push(G[j].charAt(0));  
                    t=stack.peek();  
                    stack.push(Goto[t][IsInTotal(G[j].charAt(0))]);  
                    System.out.print(G[j]);  
                    System.out.println();  
                } else {  
                    if(ACtion[s][IsInVt(getstr.charAt(i))].equals("acc")) {  
                        System.out.print(Print_Stack(stack));  
                        System.out.print("         ");  
                        System.out.print(Print_Stack1(stack1));  
                        System.out.print("         ");  
                        System.out.print(Print_Buffer(getstr,i));  
                        System.out.print("         ");  
                        System.out.print(G[0]);  
                        System.out.println();  
                        data[h++]=new Object[]{h,Print_Stack(stack),Print_Stack1(stack1),Print_Buffer(getstr,i),"接受"};  
                        break;  
                    } else {  
                        System.out.println("ERROR");  
                    }  
                }  
                  
            }  
                  
        }  
          
    }  
      
    public void Print1() {  
        for(int i=0;i<100;i++) {  
            for(int j=0;j<100;j++) {  
                if(M[i][j]!=null){  
                    System.out.print(i+ " "+j+M[i][j]+" ");  
            }  
        }  
            System.out.println();  
    }  
    }  
      
    public void Print2() {  
        for(int i=0;i<100;i++) {  
            for(int j=0;j<Total.length;j++) {  
                if(Goto[i][j]!=0)  
                    System.out.print(i+","+j+" "+Goto[i][j]+" ");  
        }  
            System.out.println();  
    }  
    }  
      
    public void Print3() {  
        for(int i=0;i<100;i++) {  
            for(int j=0;j<VT.length-1;j++) {  
                if(ACtion[i][j]!=null)  
                    System.out.print(i+","+j+" "+ACtion[i][j]+" ");  
        }  
            System.out.println();  
    }  
    }  
      
    public void Print4() {  
        for(int i=0;i<100;i++) {  
            for(int j=0;j<100;j++) {  
                if(M2[i][j]!=null){  
                    System.out.print(i+ " "+j+M2[i][j]+" ");  
            }  
        }  
            System.out.println();  
    }  
    }  
      
    LALR1() {  
          
          
        setTitle("LALR1语法分析器");  
        final JButton j4 = new JButton("浏览");  
        j4.addActionListener(new ActionListener() {  
            public void actionPerformed(final ActionEvent e) {  
                JFileChooser fileChooser = new JFileChooser();// 创建文件选择器  
                FileFilter filter = new FileNameExtensionFilter(  
                        "文件(txt/docx/doc/java/cpp/asm)", "txt","docx","doc","java","cpp","asm");// 创建过滤器  
                fileChooser.setFileFilter(filter);// 设置过滤器  
                int flag = fileChooser.showOpenDialog(null);// 显示打开对话框  
                if (flag == JFileChooser.APPROVE_OPTION) {  
                    imgFile = fileChooser.getSelectedFile(); // 获取选中文件的File对象  
                }  
                if (imgFile != null) {  
                    j3.setText(imgFile.getAbsolutePath());// 文件完整路径  
                }  
            }  
        });  
          
        final JButton j1 = new JButton("显示");   
        j1.addActionListener(new ActionListener() {  
            public void actionPerformed(final ActionEvent e) {  
                try {  
                    in1=new BufferedReader(new FileReader  
                            (imgFile.getAbsoluteFile()));  
                } catch (FileNotFoundException e2) {  
                    // TODO Auto-generated catch block  
                    e2.printStackTrace();  
                }//获得绝对路径  
                String s;  
                  
                try {  
                    while((s=in1.readLine())!=null) {  
                        j5.append(s);  
                        j5.append("\n");  
                    }  
                    in1.close();  
                } catch (IOException e1) {  
                    // TODO Auto-generated catch block  
                    e1.printStackTrace();  
                }  
            }  
        });  
          
        final JButton j2 = new JButton("分析");   
        j2.addActionListener(new ActionListener() {  
            public void actionPerformed(final ActionEvent e) {  
                Get_Clear();  
                h=0;  
                Init();  
                Items();  
                From_M_To_M1();  
                Get_LALR1();  
                //CLOSURE_LR1(0);  
                //Print();  
                Get_Action();  
                Get_Run();  
            //  Print1();  
                //Print2();  
                //Print3();  
                Print4();  
                jsPane.validate();   
                jsPane.repaint();  
                Get_Date();  
            }  
        });  
          
        j3 = new JTextField();  
        j5 = new JTextArea();  
        //model = new DefaultTableModel(data, colname);  
        //table = new JTable(model);  
        table = new JTable(data,colname);  
        //表头居中  
        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);  
        //表格内容居中  
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();// 设置table内容居中  
        renderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);  
          table.setDefaultRenderer(Object.class, renderer);  
  
        jsPan = new JScrollPane(j5);  
        jsPane = new JScrollPane(table);  
  
  
        layout = new GridBagLayout();   
          
        j8=new JLabel("LALR1",JLabel.CENTER);  
        j8.setBorder(BorderFactory.createEtchedBorder());  
        j9=new JLabel(Get_Date(),JLabel.CENTER);  
        j9.setBorder(BorderFactory.createEtchedBorder());  
        j10=new JLabel("54",JLabel.CENTER);  
        j10.setBorder(BorderFactory.createEtchedBorder());  
        this.setLayout(layout);   
        this.add(j1);  
        this.add(j2);   
        this.add(j3);   
        this.add(j4);   
        this.add(jsPan);  
        this.add(jsPane);  
        this.add(j8);  
        this.add(j9);  
        this.add(j10);  
  
        s= new GridBagConstraints();//定义一个GridBagConstraints，   
        //是用来控制添加进的组件的显示位置   
        s.fill = GridBagConstraints.BOTH;   
        //该方法是为了设置如果组件所在的区域比组件本身要大时的显示情况   
        //NONE：不调整组件大小。   
        //HORIZONTAL：加宽组件，使它在水平方向上填满其显示区域，但是不改变高度。   
        //VERTICAL：加高组件，使它在垂直方向上填满其显示区域，但是不改变宽度。   
        //BOTH：使组件完全填满其显示区域。   
        s.gridwidth=1;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个   
        s.weightx = 0;//该方法设置组件水平的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间   
        s.weighty=0;//该方法设置组件垂直的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间   
        layout.setConstraints(j1, s);//设置组件   
        s.gridwidth=1;   
        s.weightx = 0;   
        s.weighty=0;   
        layout.setConstraints(j2, s);   
        s.gridwidth=4;   
        s.weightx = 1;   
        s.weighty=0;   
        layout.setConstraints(j3, s);   
        s.gridwidth=0;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个   
        s.weightx = 0;//不能为1，j4是占了4个格，并且可以横向拉伸，   
        //但是如果为1，后面行的列的格也会跟着拉伸,导致j7所在的列也可以拉伸   
        //所以应该是跟着j6进行拉伸   
        s.weighty=0;   
        layout.setConstraints(j4, s);  
        s.gridwidth=0;   
        s.weightx = 1;   
        s.weighty=1;   
        layout.setConstraints(jsPan, s);   
        s.gridwidth=0;   
        s.weightx = 1;   
        s.weighty=1;   
        layout.setConstraints(jsPane, s);  
          
        s.gridwidth=2;   
        s.weightx = 1;   
        s.weighty=0;   
        layout.setConstraints(j8, s);  
          
        s.gridwidth=1;   
        s.weightx = 1;   
        s.weighty=0;   
        layout.setConstraints(j9, s);  
          
        s.gridwidth=0;   
        s.weightx = 1;   
        s.weighty=0;   
        layout.setConstraints(j10, s);  
    }  
      
    protected class JLabelTimerTask extends TimerTask{  
         SimpleDateFormat dateFormatter = new SimpleDateFormat(DEFAULT_TIME_FORMAT);  
         @Override  
         public void run() {  
          time = dateFormatter.format(Calendar.getInstance().getTime());  
          j10.setText(time);  
         }  
         }  
      
public static void main(String args[]) {  
          
    try {//使用默认窗口形式  
        UIManager  
                .setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");  
    } catch (Throwable e) {  
        e.printStackTrace();  
    }  
      
    EventQueue.invokeLater(new Runnable() {  
        public void run() {  
            try {  
                LALR1 alr = new LALR1();  
                alr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
                alr.setBounds(300,200,400,400);  
                alr.setVisible(true);  
                alr.Get_Time();//动态显示时间  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    });  
}  
}  
