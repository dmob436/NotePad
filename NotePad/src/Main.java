
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

public class Main extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTabbedPane tabPane;


	public static void main(String[] args) {
		new Main().setVisible(true);
	}

	private Main() {
		super("NotePad_V0.05");
		setSize(800, 600);
		setLocationRelativeTo(null);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Icon.png")));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitSafely();
			}
		});
		init();
	}

	private void init() {

		tabPane = new JTabbedPane();

		WordDocument doc = new WordDocument(true);
		tabPane.add(doc.getName(), doc);

		JMenuBar bar = new JMenuBar();

		// Edit Items
		JMenu edit = new JMenu("Edit");
		JMenuItem copy = new JMenuItem(new DefaultEditorKit.CopyAction());
		JMenuItem paste = new JMenuItem(new DefaultEditorKit.PasteAction());
		JMenuItem cut = new JMenuItem(new DefaultEditorKit.CutAction());
		JMenuItem undo = new JMenuItem("Undo");
		JMenuItem redo = new JMenuItem("Redo");

		copy.setText("Copy");
		paste.setText("Paste");
		cut.setText("Cut");

		
		// File Items
		JMenu file = new JMenu("File");
		JMenuItem newDoc = new JMenuItem("New");
		JMenuItem open = new JMenuItem("Open");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem saveas = new JMenuItem("Save as");
		JMenuItem print = new JMenuItem("Print");
		JMenuItem exit = new JMenuItem("Exit");

		// File Key ShortCuts
		newDoc.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		open.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		save.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		print.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

		// Edit Key ShortCuts
		copy.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		paste.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

		JMenuItem[] items = { newDoc, open, save, saveas, print, exit, undo, redo, copy, paste, cut };
		for (JMenuItem item : items) {
			item.addActionListener(this);
		}
		// File Buttons
		file.add(newDoc);
		file.add(open);
		file.add(save);
		file.add(saveas);
		file.addSeparator();
		file.add(print);
		file.addSeparator();
		file.add(exit);

		// Edit Buttons
		edit.add(undo);
		edit.add(redo);
		edit.addSeparator();
		edit.add(copy);
		edit.add(paste);
		edit.add(cut);

		// Menu bar items
		bar.add(file);
		bar.add(edit);

	

 		add(tabPane);
		setJMenuBar(bar);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("New")) {
			newDoc();
		} else if (e.getActionCommand().equals("Open")) {
			open();
		} else if (e.getActionCommand().equals("Save")) {
			save();
		} else if (e.getActionCommand().equals("Save as")) {
			saveAs();
		} else if (e.getActionCommand().equals("Print")) {
			Printer();
		} else if (e.getActionCommand().equals("Exit")) {
			exitSafely();
		} else if (e.getActionCommand().equals("Paste")) {

		} else if (e.getActionCommand().equals("Copy")) {

		} else if (e.getActionCommand().equals("Cut")) {

		} else if (e.getActionCommand().equals("Undo")) {
			
		} else if (e.getActionCommand().equals("Redo")) {
		
		}

	}

	private void newDoc() {

		WordDocument doc = new WordDocument(true);
		tabPane.addTab(doc.getName(), doc);
		tabPane.setSelectedComponent(doc);
		
	}

	private void open() {
		JFileChooser chooser = new JFileChooser("./");

		int returned = chooser.showOpenDialog(this);

		if (returned == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();

			WordDocument doc = new WordDocument(file.getName(), file.getAbsolutePath(), new JTextArea());

			tabPane.addTab(file.getName(), doc);
			tabPane.setSelectedComponent(doc);

			try {
				BufferedReader br = new BufferedReader(new FileReader(file));

				String line;
				while ((line = br.readLine()) != null) {
					doc.getText().append(line + "\n");
				}

				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void save() {
		WordDocument doc = (WordDocument) tabPane.getSelectedComponent();

		if (doc.isNewDocument()) {
			saveAs();
		} else {
			doc.save();
		}
	}

	private void saveAs() {
		JFileChooser chooser = new JFileChooser("./");

		int returned = chooser.showSaveDialog(this);

		if (returned == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();

			WordDocument doc = (WordDocument) tabPane.getSelectedComponent();
			if (doc.saveAs(file.getAbsolutePath())) {

				tabPane.setTitleAt(tabPane.getSelectedIndex(), file.getName());
			}

		}
	}

	private void exitSafely() {
		for (int i = 0; i < tabPane.getTabCount(); i++) {
			WordDocument doc = (WordDocument) tabPane.getComponentAt(i);

			if (doc.isUnsaved()) {
				setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				int value = JOptionPane.showConfirmDialog(null,
						"Are you sure you wish to exit? \n You will lose all of your unsaved changes!", "Warning",
						JOptionPane.YES_NO_OPTION);

				if (value == JOptionPane.NO_OPTION) {
					return;
				} else {
					dispose();
				}

			} else {
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		}
	}

	private void Printer() {
		WordDocument doc = new WordDocument();
		Print pri = new Print((Component)doc.getText());
		pri.doPrint();
	}

}
