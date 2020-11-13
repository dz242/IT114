import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class PracticalUI3 extends JFrame {
    CardLayout card;
    PracticalUI3 self;
    JPanel textArea;

    public PracticalUI3(String title) {
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setPreferredSize(new Dimension(400, 400));
	setLocationRelativeTo(null);
	self = this;
	setTitle(title);
	card = new CardLayout();
	setLayout(card);
	createConnectionScreen();
	createUserInputScreen();
	createPanelRoom();
	createSidebar();
	JPanel pl = new JPanel();
	pl.setSize(10, 400);
	pl.setMinimumSize(new Dimension(10, 400));
	pl.setPreferredSize(new Dimension(10, 400));
	textArea.getParent().getParent().getParent().add(pl, BorderLayout.WEST);
	JPanel t = new JPanel();
	t.setSize(400, 10);
	t.setPreferredSize(new Dimension(10, 400));
	t.setMinimumSize(new Dimension(10, 400));
	textArea.getParent().getParent().getParent().add(t, BorderLayout.NORTH);
	showUI();
    }

    void createConnectionScreen() {
	JPanel panel = new JPanel();
	// yes it's a bit weird setting layout and passing in the reference as to the
	// LayoutManager.
	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	JLabel hostLabel = new JLabel("Host:");
	JTextField host = new JTextField("127.0.0.1");
	panel.add(hostLabel);
	panel.add(host);
	JLabel portLabel = new JLabel("Port:");
	JTextField port = new JTextField("3000");
	panel.add(portLabel);
	panel.add(port);
	JButton button = new JButton("Next");
	button.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		// TODO trigger connection; let a callback change our page
		String _host = host.getText();
		String _port = port.getText();
		if (_host.length() > 0 && _port.length() > 0) {
		    self.next();
		}
	    }

	});
	panel.add(button);
	this.add(panel);
    }

    void createUserInputScreen() {
	JPanel panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	JLabel userLabel = new JLabel("Username:");
	JTextField username = new JTextField();
	panel.add(userLabel);
	panel.add(username);
	JButton button = new JButton("Next");// TODO rename to something like Start or Login
	button.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		// TODO trigger username payload
		String name = username.getText();
		if (name != null && name.length() > 0) {
		    self.next();
		}
	    }

	});
	panel.add(button);
	this.add(panel);
    }

    void createPanelRoom() {
	JPanel panel = new JPanel();
	panel.setLayout(new BorderLayout());

	textArea = new JPanel();
	textArea.setLayout(new BoxLayout(textArea, BoxLayout.Y_AXIS));
	textArea.setAlignmentY(Component.BOTTOM_ALIGNMENT);
	JScrollPane scroll = new JScrollPane(textArea);
	scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	panel.add(scroll, BorderLayout.CENTER);

	JPanel input = new JPanel();
	input.setLayout(new BoxLayout(input, BoxLayout.X_AXIS));
	JTextField text = new JTextField();
	input.add(text);
	JButton button = new JButton("Send");
	button.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		// TODO trigger message payload
		if (text.getText().length() > 0) {
		    self.addMessage(text.getText());
		    text.setText("");
		}
	    }

	});
	input.add(button);
	panel.add(input, BorderLayout.SOUTH);
	this.add(panel);
    }

    private void createSidebar() {
	JPanel panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
	JTextField text = new JTextField("I'm a sidebar");
	panel.add(text);
	textArea.getParent().getParent().getParent().add(panel, BorderLayout.EAST);
    }

    /***
     * Attempts to calculate the necessary dimensions for a potentially wrapped
     * string of text. This isn't perfect and some extra whitespace above or below
     * the text may occur
     * 
     * @param str
     * @return
     */
    int calcHeightForText(String str) {
	FontMetrics metrics = self.getGraphics().getFontMetrics(self.getFont());
	int hgt = metrics.getHeight();
	int adv = metrics.stringWidth(str);
	final int PIXEL_PADDING = 6;
	Dimension size = new Dimension(adv, hgt + PIXEL_PADDING);
	final float PADDING_PERCENT = 1.1f;
	// calculate modifier to line wrapping so we can display the wrapped message
	int mult = (int) Math.floor(size.width / (textArea.getSize().width * PADDING_PERCENT));
	// System.out.println(mult);
	mult++;
	return size.height * mult;
    }

    void addMessage(String str) {
	JEditorPane entry = new JEditorPane();
	entry.setEditable(false);
	entry.setLayout(null);
	entry.setText(str);
	Dimension d = new Dimension(textArea.getSize().width, calcHeightForText(str));
	// attempt to lock all dimensions
	entry.setMinimumSize(d);
	entry.setPreferredSize(d);
	entry.setMaximumSize(d);
	textArea.add(entry);
	pack();
	System.out.println(entry.getSize());
    }

    void next() {
	card.next(this.getContentPane());
    }

    void previous() {
	card.previous(this.getContentPane());
    }

    void showUI() {
	pack();
	Dimension lock = textArea.getSize();
	textArea.setMaximumSize(lock);
	setVisible(true);
    }

    public static void main(String[] args) {
	PracticalUI3 ui = new PracticalUI3("My UI");
    }
}