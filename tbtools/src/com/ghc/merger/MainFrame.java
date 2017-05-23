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
import javax.swing.JPanel;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 4346020506177860993L;

	private FileTextPane origFileCh = null;

	private FileTextPane xkbFileCh = null;

	private FileTextPane ysbtFileCh = null;

	private JButton doMergebt = null;

	public MainFrame() {
		initComp();
	}

	private void initComp() {
		setTitle("合成工具");
		JPanel centPan = new JPanel(new GridLayout(4, 1, 10, 10));
		centPan.add(getOrigFile());
		centPan.add(getXkbFile());
		centPan.add(getYsbtFile());
		JPanel btnPan = new JPanel();
		btnPan.setLayout(new BorderLayout());
		btnPan.add(getDoMerge(), BorderLayout.EAST);
		centPan.add(btnPan);
		getContentPane().add(centPan);
		centPan.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 40));
		setPreferredSize(new Dimension(800, 260));
		setSize(800, 260);
		onCenter();
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				MainFrame.this.dispose();
				System.exit(0);
			}

			@Override
			public void windowOpened(WindowEvent e) {
				// setAutoRequestFocus(true);
				toFront();
			}
		});
	}

	public FileTextPane getOrigFile() {
		if (origFileCh == null) {
			origFileCh = new FileTextPane("要填的表目录或文件(注意备份)：", 2);
		}
		return origFileCh;
	}

	public FileTextPane getXkbFile() {
		if (xkbFileCh == null) {
			xkbFileCh = new FileTextPane("选款表文件：", 0);
		}
		return xkbFileCh;
	}

	public FileTextPane getYsbtFile() {
		if (ysbtFileCh == null) {
			ysbtFileCh = new FileTextPane("预设表头目录：", 1);
		}
		return ysbtFileCh;
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

	public JButton getDoMerge() {
		if (doMergebt == null) {
			doMergebt = new JButton("开始合并");
			// doMergebt.setBorder(BorderFactory.createEmptyBorder(10, 260, 10,
			// 260));
			doMergebt.setPreferredSize(new Dimension(120, 30));
			doMergebt.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String origFile = getOrigFile().getSelectPath();
					if (isEmpty(origFile)) {
						MessageDialog.showMessage(MainFrame.this,
								"请选择要填的表目录或文件");
						return;
					}
					String xkbFile = getXkbFile().getSelectPath();
					if (isEmpty(xkbFile)) {
						MessageDialog.showMessage(MainFrame.this, "请选择选款表文件");
						return;
					}
					String ysbtFile = getYsbtFile().getSelectPath();
					if (isEmpty(ysbtFile)) {
						MessageDialog.showMessage(MainFrame.this, "请选择预设表头目录");
						return;
					}
					new BatchFileMerger(MainFrame.this, xkbFile, ysbtFile,
							origFile).fileStartMerge();
					MessageDialog.showMessage(MainFrame.this, "合并成功");

				}
			});
		}
		return doMergebt;
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.length() <= 0) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		int doCheck = LicenseTools.doCheck();
		if (doCheck == 0) {
			new MainFrame().setVisible(true);
		} else {
			if (doCheck == 1) {
				MessageDialog
						.showMessage(null, "该软件还未被授权！请进行购买！");
			}
			if (doCheck == 2) {
				MessageDialog
						.showMessage(null, "该软件是从别的电脑复制过来的，属于盗版软件 ，请进行购买！");
			}
			if (doCheck == 3) {
				MessageDialog
						.showMessage(null, "该软件已超出授权期限！请续费！");
			}
			new LicenseFrame().setVisible(true);
		}
	}

}
