package com.ghc.merger;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

public class FileTextPane extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4024343551143356404L;

	private String name = null;

	private int fileTyle = 0;// 0:CSV ，1: 目录 ， 3:不限制

	private JTextField filePath = null;

	private JButton fileChBtn = null;

	private JFileChooser chooser = null;

	public FileTextPane(String name, int fileType) {
		this.fileTyle = fileType;
		this.name = name;
		initComp();
	}

	private void initComp() {
		setLayout(new BorderLayout(20, 20));
		JLabel label = new JLabel(name);
		// label.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
		add(label, BorderLayout.WEST);
		add(getFilePathComp(), BorderLayout.CENTER);
		add(getFileChBtn(), BorderLayout.EAST);
		// setPreferredSize(new Dimension(580, 50));

	}

	public String getSelectPath() {
		return getFilePathComp().getText();
	}

	private JButton getFileChBtn() {
		if (fileChBtn == null) {
			fileChBtn = new JButton("选择");
			// fileChBtn.setBorder(BorderFactory.createEmptyBorder(10, 50, 10,
			// 50));
			fileChBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					int result = getFileCh().showDialog(FileTextPane.this, "选择");
					if(result == JFileChooser.APPROVE_OPTION){
						File selectedFile = getFileCh().getSelectedFile();
						if (selectedFile != null) {
							getFilePathComp().setText(selectedFile.getPath());
						}
					}
				}

			});

		}
		return fileChBtn;
	}

	private JTextField getFilePathComp() {
		if (filePath == null) {
			filePath = new JTextField();
			// filePath.setBorder(BorderFactory.createEmptyBorder(10, 50, 10,
			// 50));
		}
		return filePath;
	}

	private JFileChooser getFileCh() {
		if (chooser == null) {
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
					if (fileTyle == 0) {
						if (f.getName().toLowerCase().endsWith(".csv")) {
							return true;
						}
					}
					if (fileTyle == 1) {
						if (f.isDirectory()) {
							return true;
						}
					}
					if (fileTyle == 2) {
						return true;
					}
					return false;
				}
			});
			if (fileTyle == 0) {
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			}
			if (fileTyle == 1) {
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			}
			if (fileTyle == 2) {
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			}
		}
		return chooser;
	}

}
