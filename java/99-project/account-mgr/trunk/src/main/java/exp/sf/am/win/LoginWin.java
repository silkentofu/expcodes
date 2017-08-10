package exp.sf.am.win;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.FrameBorderStyle;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI.NormalColor;

import exp.libs.utils.StrUtils;
import exp.libs.warp.ui.BeautyEyeUtils;
import exp.libs.warp.ui.SwingUtils;
import exp.libs.warp.ui.cpt.win.MainWindow;

class LoginWin extends MainWindow {

	public static void main(String[] args) {
		BeautyEyeUtils.init(FrameBorderStyle.translucencySmallShadow);
		new LoginWin();
	}
	
	/** serialVersionUID */
	private static final long serialVersionUID = -1752327112586227761L;

	protected final static int HIGH = 210;
	
	protected final static int WIDTH = 330;
	
	private final static String FOLD = "︽", OPEN = "︾";
	
	private JButton helpBtn;
	
	private HelpWin helpWin;
	
	private JTextField usernameTXT;
	
	private JPasswordField passwordTXT;
	
	private JButton viewBtn;
	
	private JButton loginBtn;
	
	private JButton registBtn;
	
	protected LoginWin() {
		super("登陆", WIDTH, HIGH);
	}
	
	@Override
	protected void initCloseWindowMode() {
		_view();	// 默认显示窗口
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				if(SwingUtils.confirm("Exit ?")) {
					_hide();
					System.exit(0);
				}
			}
			
			// 窗口最小化时隐藏帮助面板
			public void windowIconified(WindowEvent e) { 
				helpWin._hide();
			}
			
			// 窗口还原时, 若帮助面板此前已展开则重新显示
			public void windowDeiconified(WindowEvent e) {
				if(FOLD.equals(helpBtn.getText())) {
					helpWin._view();
				}
			}
			
		});
	}
	
	@Override
	protected void initComponents(Object... args) {
		this.helpWin = new HelpWin();
		this.helpBtn = new JButton(OPEN);
		helpBtn.setPreferredSize(new Dimension(WIDTH, 15));	// 设置按钮高度
		BeautyEyeUtils.setButtonStyle(NormalColor.lightBlue, helpBtn);
		
		this.usernameTXT = new JTextField();
		this.passwordTXT = new JPasswordField();
		SwingUtils.hide(passwordTXT);
		
		this.viewBtn = new JButton(new ImageIcon(
				LoginWin.class.getResource("/exp/libs/warp/upm/eye.png")));
		BeautyEyeUtils.setButtonStyle(NormalColor.lightBlue, viewBtn);
		
		this.loginBtn = new JButton("登陆");
		this.registBtn = new JButton("注册");
		BeautyEyeUtils.setButtonStyle(NormalColor.normal, loginBtn, registBtn);
	}

	@Override
	protected void setComponentsLayout(JPanel rootPanel) {
		rootPanel.add(toAccountPanel(), BorderLayout.CENTER);
		rootPanel.add(helpBtn, BorderLayout.SOUTH);
	}

	/**
	 * 获取帐密面板
	 * @return
	 */
	private JPanel toAccountPanel() {
		JPanel panel = new JPanel(new GridLayout(6, 1)); {
			panel.add(new JLabel(), 0);
			panel.add(SwingUtils.getWEBorderPanel(
					new JLabel("  [账号] :  "), usernameTXT, 
					new JLabel("   ")), 1);
			panel.add(new JLabel(), 2);
			panel.add(SwingUtils.getWEBorderPanel(
					new JLabel("  [密码] :  "), passwordTXT, 
					SwingUtils.getEBorderPanel(viewBtn, new JLabel("   "))), 3);
			panel.add(new JLabel(), 4);
			panel.add(SwingUtils.getHGridPanel(
					new JLabel(), loginBtn, 
					new JLabel(), registBtn, new JLabel()), 5);
		} SwingUtils.addBorder(panel);
		return panel;
	}
	
	@Override
	protected void setComponentsListener(JPanel rootPanel) {
		
		// 设置帮助按钮的监听
		helpBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(OPEN.equals(helpBtn.getText())) {
					helpWin._view();
					helpBtn.setText(FOLD);
					
				} else {
					helpWin._hide();
					helpBtn.setText(OPEN);
				}
			}
		});

		// 设置帮助窗口的黏着效果
		this.addComponentListener(new ComponentAdapter() {
			
			//当登陆窗口移动时，帮助面板跟随移动
			@Override
			public void componentMoved(ComponentEvent e) {
				Point point = getLocation();
				helpWin.setLocation((int) point.getX(),(int) (point.getY() + HIGH));
			}

			//当展开帮助面板时，必显示在登陆窗口下方
			@Override
			public void componentShown(ComponentEvent e) {
				Point point = getLocation();
				helpWin.setLocation((int) point.getX(),(int) (point.getY() + HIGH));
			}
		});
		
		// 设置注册按钮监听
		registBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameTXT.getText();
				String password = new String(passwordTXT.getPassword());
				
				if(StrUtils.isEmpty(username)) {
					SwingUtils.warn("账号不能为空");
					
				} else if(StrUtils.isEmpty(password)) {
					SwingUtils.warn("密码不能为空");
					
				} else {
					
					// TODO 注册到库
					// 检查账号是否唯一, 密码不用检查
					SwingUtils.info("注册成功");
				}
			}
		});
		
		// 设置密码可视按钮监听
		viewBtn.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				SwingUtils.hide(passwordTXT);	// 鼠标释放时隐藏明文
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				SwingUtils.view(passwordTXT);	// 鼠标按下时显示明文
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
			
		});
	}
	
}