package com.ghc.merger;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LicenseExportFrame extends JFrame {
	private static final long serialVersionUID = 2472966257812525758L;

	private JTextField uniCodeText;
	
	private JTextField timeText;

	private JButton expLicenseBtn;

	private FileTextPane chooser;
	
	public LicenseExportFrame() {
		initComp();
	}

	private void initComp() {
		JPanel centPane = new JPanel(new GridLayout(4,1));
		centPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 10));
		
		JPanel unitCodePane = new JPanel(new BorderLayout());
		unitCodePane.add(new JLabel("客户唯一标识码："),BorderLayout.WEST);
		unitCodePane.add(getUniCodeText(),BorderLayout.CENTER);
		centPane.add(unitCodePane);
		//时间
		JPanel timePane = new JPanel(new BorderLayout());
		timePane.add(new JLabel("授权时长(永久授权请填写负壹【-1】)："),BorderLayout.WEST);
		timePane.add(getTimeText());
		timePane.add(new JLabel("月"),BorderLayout.EAST);
		centPane.add(timePane);
		
		centPane.add(getChooser());
		
		JPanel buttonPan = new JPanel(new BorderLayout());
		buttonPan.add(getExpLicenseBtn(),BorderLayout.EAST);
		centPane.add(buttonPan);
		
		getContentPane().add(centPane);
		
		setPreferredSize(new Dimension(800, 260));
		setSize(800, 260);
		onCenter();
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				LicenseExportFrame.this.dispose();
				System.exit(0);
			}

			@Override
			public void windowOpened(WindowEvent e) {
//				setAutoRequestFocus(true);
				toFront();
			}
		});
	}

	public void onCenter() {
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width / 2; // 获取屏幕的宽
		int screenHeight = screenSize.height / 2; // 获取屏幕的高
		int height = this.getHeight();
		int width = this.getWidth();
		setLocation(screenWidth - width / 2, screenHeight - height / 2);
	}
	
	
	public JTextField getTimeText() {
		if(timeText == null){
			timeText = new JTextField();
		}
		return timeText;
	}

	public JTextField getUniCodeText() {
		if(uniCodeText == null){
			uniCodeText = new JTextField();
		}
		return uniCodeText;
	}

	public JButton getExpLicenseBtn() {
		if(expLicenseBtn == null){
			expLicenseBtn = new JButton("授权");
			expLicenseBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String uniCode = getUniCodeText().getText();
					if(isEmpty(uniCode)){
						MessageDialog.showMessage(LicenseExportFrame.this,"请填写 【客户唯一标识码】");
						return;
					}
					String time = getTimeText().getText();
					if(isEmpty(time)){
						MessageDialog.showMessage(LicenseExportFrame.this,"请填写 【授权时长】");
						return;
					}
					
					String targetDir = getChooser().getSelectPath();
					if(isEmpty(targetDir)){
						MessageDialog.showMessage(LicenseExportFrame.this,"请填写 【授权时长】");
						return;
					}
					String exportLic = LicenseTools.exportLic(targetDir.trim(), time.trim(), uniCode.trim());
					if(isEmpty(exportLic)){
						MessageDialog.showMessage(LicenseExportFrame.this,"授权导出成功！");
					}else{
						MessageDialog.showMessage(LicenseExportFrame.this,exportLic);
					}
				}
			});
		}
		return expLicenseBtn;
	}
	
	public static boolean isEmpty(String str) {
		if (str == null || str.length() <= 0) {
			return true;
		}
		return false;
	}

	public FileTextPane getChooser() {
		if(chooser == null){
			chooser = new FileTextPane("授权文件存放目录：", 1);
		}
		return chooser;
	}
	
	public static void main(String[] args) {
		new LicenseExportFrame().setVisible(true);

	}
	
	
}
