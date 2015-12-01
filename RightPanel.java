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
		
		private JPanel expression_options;
		
		private JPanel particle_options;
		
		private JTextField function;
		private Color userColor;
		private JButton color_button;
		private GridLayout layout;
		private JCheckBox checkVisible;
		
		// grid options
		private JPanel grid_options;
		private JButton showGridColorOptions;
		private JButton showGridBGColorOptions;
		private JCheckBox showGrid;
		private JColorChooser gridColor;
		private JTextField incrementSize;
		
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
			expression_options = new JPanel();
			grid_options = new JPanel();
			particle_options = new JPanel();
			showGrid = new JCheckBox("show grid");
			showGridColorOptions = new JButton("change grid color");
			showGridBGColorOptions = new JButton("change grid background color");
			incrementSize = new JTextField();			
			gridColor = new JColorChooser();
			userColor = null;
			update = new JButton("Update");
			function = new JTextField(25);
			color_button = new JButton("change line color");
			layout = new GridLayout(4, 2);
			checkVisible = new JCheckBox("Visible");
			
			// set up size, location and title of the dialog box
			dialog.setLocation(100, 100);
			dialog.setPreferredSize(new Dimension(500, 550));
			dialog.setResizable(false);			
			dialog.setTitle("Options for " + table.getModel().getValueAt(row, column-1));					
			function.setText(table.getModel().getValueAt(row, column-1).toString());
			checkVisible.setSelected(dm.getFunction(index).getVisible());
			update.setSize(100, 150);
//			incrementSize.setText(dm.);
			
			// event handlers
			update.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int index = new Integer((table.getModel()
							.getValueAt(row, column-2)).toString())-1;
					dm.showGrid(showGrid.isSelected());
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
			
			showGridColorOptions.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					int index = new Integer((table.getModel()
							.getValueAt(row, column-2)).toString())-1;
					dm.setGridColor(JColorChooser
							.showDialog(dialog, "Choose a color", userColor));	
				}
			});
			
			showGridBGColorOptions.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					int index = new Integer((table.getModel()
							.getValueAt(row, column-2)).toString())-1;
					dm.setBackground(JColorChooser
							.showDialog(dialog, "Choose a color", dm.getBackground()));
				}
			});
			options_value.setLayout(new GridLayout(3, 0));
			expression_options.setLayout(new GridLayout(0, 1));
			grid_options.setLayout(new GridLayout(0, 1));
			particle_options.setLayout(new GridLayout(0, 1));
			
			expression_options.add(new JLabel("Function: "));
			expression_options.add(function);	
			expression_options.add(color_button);
			expression_options.add(new JLabel("Visible: "));
			expression_options.add(checkVisible);
			
			options_value.add(new JLabel("Function options: " ));
			options_value.add(expression_options);
						
			options_value.add(new JLabel("Grid options: " ));
			grid_options.add(showGrid);
			grid_options.add(showGridColorOptions);
			grid_options.add(showGridBGColorOptions);
			options_value.add(grid_options);
			
			options_value.add(new JLabel("Particle options: " ));
			particle_options.add(update);	
			options_value.add(particle_options);
			
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
				drawManager.addFunction(new Function(lineFunction.getText(), 
						new Color(0,0,0), drawManager));
				addRow(lineFunction.getText(), dtm);
				table.updateUI();
			}
		});
		
		
		functionInputPanel.add(new JLabel("Y="), BorderLayout.PAGE_START);
		functionInputPanel.add(lineFunction, BorderLayout.NORTH);
		functionInputPanel.add(add_function, BorderLayout.NORTH);

		table.getColumn("INDEX").setCellEditor(new OptionEditor(false));
		table.getColumn("EXPRESSION").setCellEditor(new OptionEditor(false));
		table.getColumn("OPTION").setCellEditor(new OptionEditor(drawManager));

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
