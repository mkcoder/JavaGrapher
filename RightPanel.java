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

public class RightPanel extends JPanel
{
	public class OptionEditor implements TableCellEditor {


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
			JFrame frame = new JFrame();
			JDialog dialog = new JDialog(frame);
			JPanel options_value = new JPanel();			
			JTextField function;
			JColorChooser color;
			final Color userColor = null;
			JButton color_button;
			
			// set up size, location and title of the dialog box
			dialog.setLocation(100, 100);
			dialog.setPreferredSize(new Dimension(350, 350));
			dialog.setResizable(false);			
			dialog.setTitle("Options for " + table.getModel().getValueAt(row, column-1));
			
			options_value.add(new JLabel("Function: "));
			function = new JTextField();
			function.setText(table.getModel().getValueAt(row, column-1).toString());
			options_value.add(function);
			
			options_value.add(new JLabel("Color: "));
			
			color_button = new JButton("Change color");
			color_button.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					JColorChooser.showDialog(dialog, "Choose a color", userColor);
				}
			});
			options_value.add(color_button);
			
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

		final DrawManager drawManager = _drawManager;;
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
		
		dtm.addRow(new Object[] {
				"1",
				"y=x^2",
				"Edit"
				
		});
		dtm.addRow(new Object[] {
				"1",
				"y=x-2",
				"Edit"
				
		});
		dtm.addRow(new Object[] {
				"1",
				"y=x*2",
				"Edit"
				
		});
		dtm.addRow(new Object[] {
				"1",
				"y=x+2",
				"Edit"
				
		});
		
		// add event handlers here
		add_function.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawManager.addFunction(new Function(lineFunction.getText(), new Color(0,0,0)));
			}
		});
		
		function_panel.add(new JLabel("Y="), BorderLayout.EAST);
		function_panel.add(lineFunction, BorderLayout.EAST);
		function_panel.add(add_function, BorderLayout.WEST);
		add(function_panel,BorderLayout.WEST);
		
		add(table, BorderLayout.WEST);
		table.getColumn("Options").setCellEditor(new OptionEditor());
		
		setPreferredSize(new Dimension(340,1000));
	}
}
