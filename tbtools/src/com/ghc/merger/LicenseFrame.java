package com.ghc.merger;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

public class LicenseFrame extends JFrame {
	private static final long serialVersionUID = -3960893570252181973L;

	private JButton hardKeyBtn ;
	
	private JButton licenseFileBtn ;

	private JFileChooser chooser;

	public LicenseFrame() {
		initComp();
	}

	private void initComp() {
		JPanel centPane = new JPanel(new BorderLayout());
		centPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 10));
		JLabel comp = new JLabel("<html>该软件还未授权！！！<br/>" +
				"请点击【生成客户标识】按钮生成您的唯一标识码。<br/>" +
				"联系客服,并提供【唯一标识码】购买使用权。" +
				"<br/><br/>" +
				"如果您已经购买该软件<br/>" +
				"请您点击【导入授权】按钮导入授权文件。</html>");
		comp.setForeground(Color.RED);
		centPane.add(comp);
		
		JPanel buttonPan = new JPanel(new BorderLayout(20,20));
		buttonPan.add(getHardKeyBtn());
		buttonPan.add(getLicenseFileBtn(),BorderLayout.SOUTH);
		centPane.add(buttonPan,BorderLayout.SOUTH);
		
		getContentPane().add(centPane);
		setPreferredSize(new Dimension(400, 260));
		setSize(400, 260);
		onCenter();
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				LicenseFrame.this.dispose();
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

	public JButton getHardKeyBtn() {
		if(hardKeyBtn == null){
			hardKeyBtn = new JButton("生成客户标识");
			hardKeyBtn.setPreferredSize(new Dimension(120, 35));
			hardKeyBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String uniCode;
					try {
						uniCode = LicenseTools.getUniCode();
						MessageDialog.showMessage(LicenseFrame.this,"您的唯一标识码", uniCode);
					} catch (Exception e1) {
						MessageDialog.showMessage(LicenseFrame.this,e1.getMessage());
					}
					
				}
			});
		}
		return hardKeyBtn;
	}

	public JButton getLicenseFileBtn() {
		if(licenseFileBtn == null){
			licenseFileBtn = new JButton("导入授权");
			licenseFileBtn.setPreferredSize(new Dimension(120, 35));
			licenseFileBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {

					int result = getFileCh().showDialog(LicenseFrame.this,"选择授权文件");
					if(result == JFileChooser.APPROVE_OPTION){
						File selectedFile = getFileCh().getSelectedFile();
						if(selectedFile != null){
							String messge = LicenseTools.importLic(selectedFile);
							if(messge == null){
								MessageDialog.showMessage(LicenseFrame.this,"授权成功，请您尽情使用!");
								new MainFrame().setVisible(true);
								LicenseFrame.this.dispose();
							}else{
								MessageDialog.showMessage(LicenseFrame.this,messge);	
							}
						}else{
							MessageDialog.showMessage(LicenseFrame.this,"请选择授权文件！");
						}
					}
				}
			});
		}
		return licenseFileBtn;
	}
	
	private JFileChooser getFileCh() {
		if(chooser == null){
			chooser = new JFileChooser();
			chooser.setFileFilter(new FileFilter() {
				
				@Override
				public String getDescription() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public boolean accept(File f) {
					if (f.isDirectory()) {
						return true;
					}
					if(f.getName().toLowerCase().endsWith(".ghc")){
						return true;
					}
					return false;
				}
			});
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);  
		}
		return chooser;
	}
	
	


}
