import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

//只有总控程序

class MainFrame {
	// GUI部分
	private JFrame frame;
	private JLabel inputlabel;
    private JTextField inputStr;
    private JButton analyzeBtn;
    private JTextArea showAnalyzeArea;
    private JScrollPane js;
    LRCore core;
    LRShow lrShow;
	public MainFrame() {
	    initGUI(); // 初始化界面
        lrShow = new LRShow();
		// initData();//初始化数据
		// 设置按钮监听
		analyzeBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
                String input = inputStr.getText();
                core = new LRCore(); // 显示框链接
                core.setShowAnalyzeArea(showAnalyzeArea);
                core.setInput(input);
				core.start(); // 开始分析
			}
		});
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
}
public class LR1 {
public static void main(String[] args) {
	new MainFrame();
}
}
