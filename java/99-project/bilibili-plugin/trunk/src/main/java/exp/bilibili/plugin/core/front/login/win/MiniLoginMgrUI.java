package exp.bilibili.plugin.core.front.login.win;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import exp.libs.warp.ui.cpt.pnl.ADPanel;
import exp.libs.warp.ui.cpt.win.PopChildWindow;

public class MiniLoginMgrUI extends PopChildWindow {

	private static final long serialVersionUID = 4379374798564622516L;

	private final static int WIDTH = 500;
	
	private final static int HEIGHT = 600;
	
	private ADPanel<UserLine> adPanel;
	
	public MiniLoginMgrUI() {
		super("哔哩哔哩-小号管理列表", WIDTH, HEIGHT);
	}
	
	@Override
	protected void initComponents(Object... args) {
		this.adPanel = new ADPanel<UserLine>(UserLine.class);
	}

	@Override
	protected void setComponentsLayout(JPanel rootPanel) {
		rootPanel.add(adPanel.getJScrollPanel(), BorderLayout.CENTER);
	}

	@Override
	protected void setComponentsListener(JPanel rootPanel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void AfterView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void beforeHide() {
		// TODO Auto-generated method stub
		
	}

}