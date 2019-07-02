import javax.swing.*;

/*
* 总控程序的核心
* */
public class LRCore extends Thread {
    String input ="i*i+i#";
    boolean exit = false;
    public JTextArea showAnalyzeArea;
    public LRCore(){
    }
    public void setShowAnalyzeArea(JTextArea showAnalyzeArea) {
        this.showAnalyzeArea = showAnalyzeArea;
    }
    public void setInput(String input){
        this.input = input;
    }

    @Override
    public void run() {
        super.run();
        while (!exit){
            System.out.println("线程开始");
            startAnalyze();
            LRData.threadExit = exit;
        }
    }
    public void startAnalyze() {
        try {
            LRData.initStack(); // 初始化符号栈
            int step=1;// 步骤
//        String input=inputStr.getText();//获得输入字符
            // valified 校验
            if(input.charAt(input.length()-1) != '#'){
                JOptionPane.showMessageDialog(null,"输入格式错误");
                exit = true;
                return;
            }
            // 设置数据
            LRData.input = input;
            int readpoint=0;				//读取输入符号的指针
            boolean flag=true;				//判断识别是否成功
            char a = input.charAt(readpoint);//当前正在读取的字符
            // 设置数据
            LRData.readPoint = readpoint;
//        int s= stateStack.peek();//状态栈的栈顶状态
            int s = LRData.stateStack.peek();
            //输出分析表头
            showAnalyze("步骤","状态栈","符号栈","输入串","动作说明");
            showAnalyze("0","0","#",input,"初始化");
            // 线程休眠1s
            Thread.sleep(1000);

//        while(!action(s, a).equals("acc")) //当前状态没有识别成功
            while (!LRData.action(s,a).equals("acc"))
            {
                a = input.charAt(readpoint);//当前正在读取的字符
//            s = stateStack.peek();//状态栈的栈顶状态
                s = LRData.stateStack.peek();
//            String ac=action(s, a);
                String ac = LRData.action(s,a);
                if(ac.equals("^"))//识别错误
                {
                    ERROR();
                    flag = false; //识别标志置成错误
                    exit = true; // 退出线程
                    break;//退出循环
                }
                else if (ac.charAt(0)=='S') {//如果识别到Sn
                    //把a移入符号栈
                    //把n移入状态栈
                    String nString=ac.substring(1);//获得Sn中n的字符串
                    int n=Integer.parseInt(nString);//转换成数字
                    String description = "ACTION("+s+","+a+")="+ac+",状态"+n+"入栈";
                    LRData.description = description;
                    Thread.sleep(2000);

                    LRData.description = "符号:"+a+"入栈";
                    Thread.sleep(2000);
                    LRData.charStack.add(a);

                    LRData.description = "状态:"+n+"入栈";
                    Thread.sleep(2000);
                    LRData.stateStack.add(n);

                    showAnalyze(String.valueOf(step),
                            LRData.getStateStackString(),
                            LRData.getCharStackString(),
                            input.substring(readpoint),
                            description);
                    step++;
                    /////
                    //读取下一个字符
                    readpoint=readpoint+1;
                    LRData.description = "读取下一个字符";
                    Thread.sleep(2000);
                    LRData.readPoint = readpoint;
                }else if (ac.charAt(0)=='r') { //如果是ACTION(S,a)=rn;归约
                    //使用产生式n归约符号栈
                    String nString=ac.substring(1);
                    int n=Integer.parseInt(nString);
                    String pros = LRData.pro[n-1];

                    LRData.description ="归约:"+pros;
                    Thread.sleep(2000);

                    char head=pros.charAt(0);//获得左部的符号
                    int popn=pros.substring(3).length();//产生式右部的长度
                    //符号栈出栈对应长度的数据
                    for(int pop=1; pop<=popn; pop++)
                    {
//                    charStack.pop();//符号栈出栈对应长度
//                    stateStack.pop();//状态栈出栈对应长度
                        char pop1 = LRData.charStack.peek();
                        LRData.description = String.valueOf(pop1)+"出栈";
                        Thread.sleep(2000);

                        LRData.charStack.pop();
                        int pop2 = LRData.stateStack.peek();
                        LRData.description = String.valueOf(pop2)+"出栈";
                        Thread.sleep(2000);
                        LRData.stateStack.pop();
                    }

                    //把产生式的左部进栈
                    LRData.description="符号:"+head+"进栈";
                    Thread.sleep(2000);
                    LRData.charStack.add(head);
                    //把goto(s,a)放进状态栈
                    s=LRData.stateStack.peek();//s指向当前状态栈顶
                    int st = LRData.GOTO(s, head);

                    LRData.description="状态:"+st+"进栈";
                    Thread.sleep(2000);
                    LRData.stateStack.add(st);
                    //输出
                    String description = ac+":"+pros+" 归约,GOTO("+s+","+head+")="+st+"入栈";

                    LRData.description = description;
                    showAnalyze(String.valueOf(step),LRData.getStateStackString(),LRData.getCharStackString(),input.substring(readpoint),description);
                    Thread.sleep(2000);
                    step++;
                    ///
                }
            }
            if(flag) // 在循环的时候没有错误
            {
                showAnalyzeArea.append("Acc:识别成功！\n");
                LRData.description = "识别成功";
                exit = true;
                System.out.println("识别成功！");
            }
        } catch (Exception e2) {
            System.out.println("产生异常了");
            System.out.println(e2.getMessage());
            showAnalyzeArea.append("识别错误");
        }
    }

    private void showAnalyze(String step, String stateStack, String charStack,String inputStr,String description){
        String output = step +"\t"+stateStack+"\t\t"+charStack+"\t"+inputStr+"\t"+description+"\n";
        showAnalyzeArea.append(output);
    }

    private void ERROR()
    {
        System.out.println("分析错误");
        JOptionPane.showMessageDialog(null,"分析错误");
    }

}
