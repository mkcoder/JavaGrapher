import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import javafx.scene.layout.Border;

public class RightPanel extends JPanel
{
	static int indexCount = 0;
	public class OptionEditor implements TableCellEditor {
		private DrawManager dm; 
		private boolean isEditable; 
		private JButton update;			
		private JFrame frame;
		private JDialog dialog; 
		private JPanel options_value;			
		private JTextField function;
		private Color userColor;
		private JButton color_button;
		private GridLayout layout;
		private JCheckBox checkVisible;
		
		public OptionEditor(){ isEditable = true; }
		
		public OptionEditor(DrawManager dm)  
		{			
			this();
			this.dm = dm;
		}
		public OptionEditor(boolean isEditable)  
		{
			this.isEditable = isEditable;
		}
		@Override
		public void addCellEditorListener(CellEditorListener l) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void cancelCellEditing() {
			// TODO Auto-generated method stub
		}

		@Override
		public Object getCellEditorValue() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isCellEditable(EventObject anEvent) {
			// TODO Auto-generated method stub
			return isEditable;
		}

		@Override
		public void removeCellEditorListener(CellEditorListener l) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean shouldSelectCell(EventObject anEvent) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean stopCellEditing() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {

			int index = new Integer(table.getModel()
					.getValueAt(row, column-2).toString())-1;
			frame  = new JFrame();
			dialog = new JDialog(frame);		
			options_value = new JPanel();
			userColor = null;
			update = new JButton("Update");
			function = new JTextField(25);
			color_button = new JButton("Change color");
			layout = new GridLayout(4, 2);
			checkVisible = new JCheckBox("Visible");
			// set up size, location and title of the dialog box
			dialog.setLocation(100, 100);
			dialog.setPreferredSize(new Dimension(350, 150));
			dialog.setResizable(false);			
			dialog.setTitle("Options for " + table.getModel().getValueAt(row, column-1));					
			function.setText(table.getModel().getValueAt(row, column-1).toString());
			checkVisible.setSelected(dm.getFunction(index).getVisible());
			// event handlers
			update.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String expression = function.getText(); 
							
							table.getModel()
							.setValueAt(expression, row, column-1);
					boolean visible = checkVisible.isSelected();
					Function f = dm.getFunction(index);
					f.setExpression(expression);
					f.setVisible(visible);
				}
			});
			
			color_button.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					int index = new Integer((table.getModel()
							.getValueAt(row, column-2)).toString())-1;
					dm.getFunction(index)
					.setColor(JColorChooser
							.showDialog(dialog, "Choose a color", userColor));					
				}
			});
			

			options_value.setLayout(layout);
			options_value.add(new JLabel("Function: "));
			options_value.add(function);
			options_value.add(new JLabel("Color: "));		
			options_value.add(color_button);
			options_value.add(new JLabel("Visible: "));
			options_value.add(checkVisible);
			options_value.add(update);	
			
			
			dialog.add(options_value);			
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);	
			
			dialog.pack();
			dialog.setVisible(true);
			return null;
		}

	}
	
	private JPanel functionInputPanel;
	private JPanel tableHolderPanel;
	
	private DefaultTableModel dtm;
	private JButton add_function;
	protected JTextField lineFunction;
	protected JTable table;
	
	public RightPanel(DrawManager _drawManager)
	{		

		final DrawManager drawManager = _drawManager;
		JScrollPane tablePane = new JScrollPane(table);
		JFrame tableFrame = new JFrame();
		
		functionInputPanel = new JPanel();		
		tableHolderPanel = new JPanel();
		
		dtm = new DefaultTableModel(new Object[] {
				"Equation number", 
				"Equation",
				"Options"
			}, 0);				
		add_function = new JButton("Add");
		lineFunction = new JTextField(15);			
		lineFunction.setEnabled(true);
		table = new JTable(null, new String[] {
				"Equation number", 
				"Equation",
				"Options"
			});
		
		table.setModel(dtm);	
		dtm.setColumnIdentifiers(new Object[] {
				"INDEX", 
				"EXPRESSION",
				"OPTION"
			});
		
		// add event handlers here
		
		add_function.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( lineFunction.getText().length() <= 0 ) return;
				System.out.println(lineFunction.getText());
				drawManager.addFunction(new Function(lineFunction.getText(), new Color(0,0,0)));
				addRow(lineFunction.getText(), dtm);
				table.updateUI();
			}
		});
		
		
		functionInputPanel.add(new JLabel("Y="), BorderLayout.PAGE_START);
		functionInputPanel.add(lineFunction, BorderLayout.NORTH);
		functionInputPanel.add(add_function, BorderLayout.NORTH);

		table.getColumn("OPTION").setCellEditor(new OptionEditor(drawManager));
		table.getColumn("INDEX").setCellEditor(new OptionEditor(false));

		// create a table frame with the headers on top and the table on the center
		tableHolderPanel.setLayout(new BorderLayout());
		tableHolderPanel.add(table.getTableHeader(), BorderLayout.NORTH);
		tableHolderPanel.add(table, BorderLayout.CENTER);
		
		add(functionInputPanel);
		add(tableHolderPanel);
		setPreferredSize(new Dimension(340,1000));
	}
		
	public static void addRow(String s, DefaultTableModel dtm) 
	{
		dtm.addRow(new Object[] {
				++indexCount,
				s,
				"Edit"
		});
	}
	
}
