import javax.swing.*;
import java.awt.*;

/*
*
* 动画显示的核心
*
* */
public class LRShow {
    InnerPanel drawPanel;
    JFrame jFrame;
    public LRShow(){
        init();
        startDraw();

    }
    private void init(){
        drawPanel = new InnerPanel();
        jFrame = new JFrame("动画显示");
        jFrame.setSize(new Dimension(600,600));
        drawPanel.setPreferredSize(new Dimension(600,600));
        Container container = jFrame.getContentPane();
        container.add(drawPanel);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setVisible(true);
    }
    // 开始绘制
    private void startDraw(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        drawPanel.repaint();// 重绘
                        Thread.sleep(1000);
                    }
                }catch (InterruptedException e){
                    System.out.println(e.getMessage());
                }
            }
        }).start();
    }

    private class InnerPanel extends JPanel {
        // 绘制栈和相关的数据
        InnerPanel(){}
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            // 绘制输入串
            String title = "输入串:";
            String input = LRData.input; // 获得输入串
            int readPoint = LRData.readPoint;
            Color pointColor = Color.red; // 指针颜色
            Color originColor = Color.black;

            // 绘制标签
            g.setFont(new Font("黑体",Font.BOLD,20));
            g.setColor(originColor);
            g.drawString(title,100,40);

            // 绘制动作说明
            String actionTitle = "动作说明";
            g.drawString(actionTitle,100,80);
            String description = LRData.description; // 获得描述
            g.drawString(description,200,80);

            int x = 150;
            int y = 40;
            // 转成字符数组
            char[] printText = input.toCharArray();
            for(int i=0;i<printText.length;i++){
                x += 22;
                String printStr = String.valueOf(printText[i]);
                if(i == readPoint){
                    // 当前指向为红色
                    g.setColor(pointColor);
                }else{
                    // 其他颜色为黑色
                    g.setColor(originColor);
                }
                g.drawString(printStr,x,y);
            }
            // 绘制符号栈,从底部开始绘制
            char[] charStackArray = LRData.getCharStackString().toCharArray();
            // 绘制状态栈标签
            g.setColor(originColor);
            String charStackTitle = "符号栈";
             x = 20;
             y = 400;
             g.drawString(charStackTitle,x,y);
             y -= 20;
             for (int i=0;i<charStackArray.length;i++){
                 if(i == charStackArray.length-1){
                     // 栈顶颜色为红色
                     g.setColor(pointColor);

                 }else{
                     g.setColor(originColor);
                 }
                 // 绘制矩形
                 g.drawRect(x,y-20,60,20);
                 g.drawString(String.valueOf(charStackArray[i]),x+20,y);
                 y-=20;
             }
             // 绘制状态栈
             char[] stateStackArray = LRData.getStateStackString().toCharArray();
             g.setColor(originColor);
             String stateStackTitle = "状态栈";
             x= 100;
             y = 400;
             g.drawString(stateStackTitle,x,y);
             y -=20;
             for(int i=0;i<stateStackArray.length;i++){
                 if(i==stateStackArray.length-1){
                     g.setColor(pointColor);
                 }else{
                     g.setColor(originColor);
                 }
                 //
                 g.drawRect(x,y-20,60,20);
                 g.drawString(String.valueOf(stateStackArray[i]),x+20,y);
                 y-=20;
             }
        }
    }

}
