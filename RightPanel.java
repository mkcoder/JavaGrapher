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

import javax.print.attribute.standard.JobHoldUntil;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
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
		private JFrame frame;
		private JDialog dialog; 
		private JPanel options_value;
		
		private JPanel expression_options;
		private JPanel particle_options;
		
		private JTextField function;
		private Color userColor;
		private JButton color_button;
		private JCheckBox checkVisible;
		private JCheckBox showParticle;
		private JButton removeFunction;
		
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
		}
		
		@Override
		public void cancelCellEditing() {
		}

		@Override
		public Object getCellEditorValue() {
			return null;
		}

		@Override
		public boolean isCellEditable(EventObject anEvent) {
			// TODO Auto-generated method stub
			return isEditable;
		}

		@Override
		public void removeCellEditorListener(CellEditorListener l) {

		}

		@Override
		public boolean shouldSelectCell(EventObject anEvent) {
			return true;
		}

		@Override
		public boolean stopCellEditing() {
			return true;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {

			int index = new Integer(table.getModel()
					.getValueAt(row, column-2).toString())-1;
			frame  = new JFrame();
			dialog = new JDialog(frame, true);		
			options_value = new JPanel();
			expression_options = new JPanel();
			grid_options = new JPanel();
			particle_options = new JPanel();
			userColor = null;
			function = new JTextField(25);
			color_button = new JButton("change line color");
			checkVisible = new JCheckBox("Visible", true);
			showParticle = new JCheckBox("show particles", true);		
			removeFunction = new JButton("remove function");
			// set up size, location and title of the dialog box
			dialog.setLocation(100, 100);
			dialog.setPreferredSize(new Dimension(500, 550));
			dialog.setResizable(false);			
			dialog.setTitle("Options for " + table.getModel().getValueAt(row, column-1));					
			function.setText(table.getModel().getValueAt(row, column-1).toString());
			checkVisible.setSelected(dm.getFunction(index).getVisible());
		
//			incrementSize.setText(dm.);
			
			// event handlers
			color_button.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int index = new Integer((table.getModel()
							.getValueAt(row, column-2)).toString())-1;
					dm.getFunction(index)
					.setColor(JColorChooser
							.showDialog(dialog, "Choose a color", userColor));					
				}
			});
			
			showParticle.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					int index = new Integer((table.getModel()
							.getValueAt(row, column-2)).toString())-1;
					dm.getFunction(index).showParticles(showParticle.isSelected());
				}
			});
			checkVisible.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub

					int index = new Integer((table.getModel()
							.getValueAt(row, column-2)).toString())-1;
					Function f = dm.getFunction(index);
					boolean visible = checkVisible.isSelected();
					f.setVisible(visible);
				}
			});
			function.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String expression = function.getText(); 							
							table.getModel()
							.setValueAt(expression, row, column-1);
					int index = new Integer((table.getModel()
							.getValueAt(row, column-2)).toString())-1;
					Function f = dm.getFunction(index);
					f.setExpression(expression);
				}
			});
			
			removeFunction.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					int index = new Integer((table.getModel()
							.getValueAt(row, column-2)).toString())-1;
					dm.removeFunction(index);
					((DefaultTableModel)table.getModel()).removeRow(index);
					indexCount--;
					dialog.setVisible(false);
				}
			});
			
			options_value.setLayout(new GridLayout(2, 0));
			expression_options.setLayout(new GridLayout(0, 1));
			particle_options.setLayout(new GridLayout(0, 1));
			
			expression_options.add(new JLabel("Function: "));
			expression_options.add(function);	
			expression_options.add(color_button);
			expression_options.add(checkVisible);
			expression_options.add(removeFunction);
			particle_options.add(showParticle);
			
			options_value.add(new JLabel("Function options: " ));
			options_value.add(expression_options);
			expression_options.add(new JSeparator());
			
			options_value.add(new JLabel("Particle options: " ));			
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
	
	// grid optins
	private JPanel grid_options;
	private JButton showGridColorOptions;
	private JButton showGridBGColorOptions;
	private JCheckBox showGrid;
	private JCheckBox showCursorCoords;
	private JCheckBox showNumbers;
	private JColorChooser gridColor;
	
	private JTextField tickHSize;
	private JTextField tickVSize;
	private JTextField tickHScale;
	private JTextField tickVScale;
	
	
	public RightPanel(DrawManager _drawManager)
	{		
		final DrawManager drawManager = _drawManager;
		JScrollPane tablePane = new JScrollPane(table);
		JFrame tableFrame = new JFrame();
		grid_options = new JPanel();
		showGrid = new JCheckBox("show grid", true);
		showCursorCoords = new JCheckBox("show cursor coords", true);
		showNumbers = new JCheckBox("show numbers", true);
		showGridColorOptions = new JButton("change grid color");
		showGridBGColorOptions = new JButton("change grid background color");
		tickHSize = new JTextField();
		tickVSize = new JTextField();
		tickHScale = new JTextField();
		tickVScale = new JTextField();
		gridColor = new JColorChooser();
		
		functionInputPanel = new JPanel();		
		tableHolderPanel = new JPanel();
		
		tickHSize.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					drawManager.setTickH((new Double(tickHSize.getText())));
				} catch (NumberFormatException e1) {
					tickHSize.setText("");
					JOptionPane.showConfirmDialog(null, "Please enter a decimal value!", 
							"Wrong input",
							JOptionPane.OK_CANCEL_OPTION);
				}
			}
		});
		
		tickVSize.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					drawManager.setTickV((new Double(tickVSize.getText())));
				} catch ( NumberFormatException e1 ) {
					tickVSize.setText("");
					JOptionPane.showConfirmDialog(null, "Please enter a decimal value!", 
							"Wrong input",
							JOptionPane.OK_CANCEL_OPTION);
				}
				
			}
		});
		
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
				
				try {
					drawManager.addFunction(new Function(lineFunction.getText(), 
							new Color(0,0,0), drawManager));
					addRow(lineFunction.getText(), dtm);
					table.updateUI();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					JOptionPane.showConfirmDialog(null, "The input for the function is wrong! "
							+ "Expected input is x*x or x, no white space and * for carrot symbol.");
				}
			}
		});
		
		showGrid.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				drawManager.showGrid(showGrid.isSelected());
			}
		});
		
		showGridColorOptions.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				drawManager.setGridColor(JColorChooser
						.showDialog(grid_options, "Choose a color", new Color(0, 0, 0)));	
			}
		});
		
		showGridBGColorOptions.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				drawManager.setBackground(JColorChooser
						.showDialog(grid_options, "Choose a color", drawManager.getBackground()));
			}
		});
		
		showNumbers.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				drawManager.showNumbers(showNumbers.isSelected());
			}
		});
		
		showCursorCoords.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				drawManager.showCursorCoords(showCursorCoords.isSelected());
			}
		});
		
		tickHScale.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try 
				{
					drawManager.setScaleH(new Double(tickHScale.getText()));
				} catch (NumberFormatException e1) 
				{
					tickHScale.setText("");
					JOptionPane.showConfirmDialog(null, "Please enter a decimal value!", 
							"Wrong input",
							JOptionPane.OK_CANCEL_OPTION);
				}
			}
		});
		
		tickVScale.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try 
				{
					drawManager.setScaleV(new Double(tickVScale.getText()));
				} catch (NumberFormatException e1) 
				{			
					tickVScale.setText("");
					JOptionPane.showConfirmDialog(null, "Please enter a decimal value!", 
							"Wrong input",
							JOptionPane.OK_CANCEL_OPTION);
				}
			}
		});
		
		functionInputPanel.add(new JLabel("f(x)="), BorderLayout.PAGE_START);
		functionInputPanel.add(lineFunction, BorderLayout.NORTH);
		functionInputPanel.add(add_function, BorderLayout.NORTH);

		table.getColumn("INDEX").setCellEditor(new OptionEditor(false));
		table.getColumn("EXPRESSION").setCellEditor(new OptionEditor(false));
		table.getColumn("OPTION").setCellEditor(new OptionEditor(drawManager));
		
		// create a table frame with the headers on top and the table on the center
		tableHolderPanel.setLayout(new BorderLayout());
		tableHolderPanel.add(table.getTableHeader(), BorderLayout.NORTH);
		tableHolderPanel.add(table, BorderLayout.CENTER);
		
		grid_options.setLayout(new GridLayout(0, 1));
		grid_options.add(new JLabel("H Tick Value"));
		grid_options.add(tickHSize);
		grid_options.add(new JLabel("V Tick Value"));
		grid_options.add(tickVSize);
		grid_options.add(new JLabel("V Scale Value"));
		grid_options.add(tickHScale);
		grid_options.add(new JLabel("H Scale Value"));
		grid_options.add(tickVScale);
		grid_options.add(showCursorCoords);
		grid_options.add(showNumbers);
		
		grid_options.add(showGrid);
		grid_options.add(showGridColorOptions);
		grid_options.add(showGridBGColorOptions);
		
		add(grid_options);
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
