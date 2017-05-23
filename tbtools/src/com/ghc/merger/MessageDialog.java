package com.ghc.merger;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MessageDialog extends JDialog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8232807333552130690L;

	private JButton ok ;
	
	public static void showMessage(Frame parent,String messige){
		new MessageDialog(parent, messige).showMode();
	}
	
	private void showMode() {
		onCenter();
		setModal(true);
		setVisible(true);
	}

	public MessageDialog(Frame parent,String messige){
		super(parent);
		this.setTitle("提示");
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BorderLayout(20,20));
		JTextField comp = new JTextField(messige);
		comp.setText(messige);
		comp.setEditable(false);
		jPanel.add(comp,BorderLayout.CENTER);
		jPanel.add(getOk(),BorderLayout.SOUTH);
		jPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
		this.getContentPane().add(jPanel);
		setPreferredSize(new Dimension(360, 200));
		setSize(new Dimension(360, 200));
	}
	
	
	
	public JButton getOk() {
		if(ok == null){
			ok = new JButton("确定");
			ok.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					MessageDialog.this.dispose();
				}
			});
		}
		return ok;
	}

	public void onCenter(){
		 Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包 
		 Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸 
		 int screenWidth = screenSize.width/2; // 获取屏幕的宽
		 int screenHeight = screenSize.height/2; // 获取屏幕的高
		 int height = this.getHeight(); 
		 int width = this.getWidth(); 
		 setLocation(screenWidth-width/2, screenHeight-height/2);
	}

	public static void showMessage(Frame parent, String title,
			String messige) {
		MessageDialog messageDialog = new MessageDialog(parent, messige);
		messageDialog.setTitle(title);
		messageDialog.showMode();
	}

}
