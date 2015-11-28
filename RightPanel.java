import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
		
		public OptionEditor(){}
		
		public OptionEditor(DrawManager dm)  
		{
			this.dm = dm;
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
			return true;
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
			// TODO Auto-generated method stub	
			JButton update = new JButton("Update");
			JFrame frame = new JFrame();
			JDialog dialog = new JDialog(frame);
			JPanel options_value = new JPanel();			
			JTextField function;
			JColorChooser color;
			Color userColor = null;
			JButton color_button;
			
			// set up size, location and title of the dialog box
			dialog.setLocation(100, 100);
			dialog.setPreferredSize(new Dimension(350, 350));
			dialog.setResizable(false);			
			dialog.setTitle("Options for " + table.getModel().getValueAt(row, column-1));
			
			options_value.add(new JLabel("Function: "));
			function = new JTextField(25);
			function.setText(table.getModel().getValueAt(row, column-1).toString());
			options_value.add(function);
			
			options_value.add(new JLabel("Color: "), BorderLayout.EAST);			
			color_button = new JButton("Change color");
			
			color_button.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					int index = new Integer(table.getModel()
							.getValueAt(row, column-2).toString());
					dm.setFunctionColor(index, 
					JColorChooser.showDialog(dialog, "Choose a color", userColor));
				}
			});
			options_value.add(color_button);

			options_value.add(update, BorderLayout.SOUTH);
			
			dialog.getContentPane().add(options_value);
			
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);	
			
			dialog.pack();
			dialog.setVisible(true);
			return null;
		}

	}

	String [] tableColumns = {
		"Equation number", 
		"Equation",
		"Options"
	};
	
	private JPanel function_panel;
	private JPanel panel;	
	private DefaultTableModel dtm;
	private JButton add_function;
	protected JTextField lineFunction;
	protected JTable table;
	
	public RightPanel(DrawManager _drawManager)
	{		

		final DrawManager drawManager = _drawManager;
		
		panel = new JPanel();
		function_panel = new JPanel();		
		dtm = new DefaultTableModel(new String[] {
				"Equation number", 
				"Equation",
				"Options"
			}, 0);		
		add_function = new JButton("Add");
		lineFunction = new JTextField(15);			
		lineFunction.setEnabled(true);
		table = new JTable();
		
		dtm.setColumnIdentifiers(tableColumns);
		table.setModel(dtm);	
		
		for (Function fun : drawManager.getFunctions()) {
			addRow(fun.getExpression(), dtm);			
		}		
		
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
		
		function_panel.add(new JLabel("Y="), BorderLayout.EAST);
		function_panel.add(lineFunction, BorderLayout.EAST);
		function_panel.add(add_function, BorderLayout.WEST);
		add(function_panel,BorderLayout.WEST);
		
		add(table, BorderLayout.WEST);
		table.getColumn("Options").setCellEditor(new OptionEditor(drawManager));
		
		setPreferredSize(new Dimension(340,1000));
	}
	
	public static void addRow(String s, DefaultTableModel dtm) 
	{
		dtm.addRow(new Object[] {
				indexCount++,
				s,
				"Edit"
		});
	}
	
}
