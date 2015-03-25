package ch.usi.ict.dev.ois.gui;


import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jface.viewers.*;
import org.eclipse.jface.window.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

import ch.usi.ict.dev.AclOverSoapHttpMP.AgentIdentifier;
import ch.usi.ict.dev.ois.osinfrastructure.OISInfrastructureService;


public class OISGui extends ApplicationWindow implements Runnable {

	private StyledText logWindowViewver   								   = null;
	
	private TreeViewer treeView           								   = null;
	
	private ConcurrentHashMap<String, AgentIdentifier>  registeredAgentMap = null;
	
	final OISGui me;
	
	//Display disp = null;
	
	public OISGui(Shell parentShell, ConcurrentHashMap<String, AgentIdentifier>  aregisteredAgentMap) {
		super(parentShell);
		setBlockOnOpen(true);
		me = this;
		registeredAgentMap = aregisteredAgentMap;	
	}
	
	
	@Override
	public void run () {
		
		Display.getDefault().asyncExec(new Runnable() {
			
			public void run() {
				
				OISInfrastructureService ois 					    = new OISInfrastructureService("1480", registeredAgentMap, me);
				
				
				
				
				new Thread(ois).start();
				
			}
		});
		
		open();
		//Display.getCurrent().dispose();//be carefull here
		Display.getDefault().dispose();
		
	}
	
	@Override
	protected Control createContents(Composite parent) {
		
		parent.getShell().setToolTipText("");
		parent.getShell().setText("OpenSystemMsgLogView");
		parent.setLayout(new FillLayout());
		parent.setBounds(30, 60, 600, 400);
		
		
		logWindowViewver = new StyledText(parent, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
	
		logWindowViewver.setEditable(false);
		
		//logWindowViewver.setBounds(0, 0, 280, 380);
		
		treeView = new TreeViewer(parent);
		
		
		
		treeView.setLabelProvider(new LabelProvider() {
			
			@Override
			public String getText(Object element) {
				// TODO Auto-generated method stub
				return ((AgentIdentifier)element).getName();
			}
		});
		
		treeView.setContentProvider(new ITreeContentProvider() {
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean hasChildren(Object element) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Object getParent(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				// TODO Auto-generated method stub
				return ((ConcurrentHashMap<String, AgentIdentifier>)inputElement).values().toArray();
			}
			
			@Override
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof AgentIdentifier)
					return null;
				return getElements(parentElement);
			}
		});
		//participantList.
		
		treeView.setInput(registeredAgentMap);
		/*
		new Thread () {
			@Override
			public void run() {
				
				try {
					this.sleep(6000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for (int i = 0; i < 10; i++) {
					
					agtList.add("add:" + i);
					
					Display.getDefault().asyncExec(new Runnable() {
						
						public void run() {
							tree.refresh();
						}
					});
					
					try {
						this.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				
			}
		}.start();*/
		
		
		return parent;
	}
	
	public void updateLogView (final String message) {
		
		Display.getDefault().asyncExec(new Runnable() {
			
			public void run() {
				
				logWindowViewver.setText(new StringBuffer(message).append(logWindowViewver.getText()).toString());
				//logWindowViewver.append(message);
				//logWindowViewver.append(message + "\n");
				//logWindowViewver.insert(string)
				
				
			}
		});
	}
	
	public void updateAgtListView() {
		
		Display.getDefault().asyncExec(new Runnable() {

			public void run() {

				treeView.refresh();
				//logWindowViewver.append(message + "\n");
				//logWindowViewver.insert(string)


			}
		});
		
	}

	
}
