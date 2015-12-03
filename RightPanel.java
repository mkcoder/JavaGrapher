import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.*;

public class RightPanel extends JPanel
{
	static int indexCount = 0; // keep count of the indexes in the table 
	
	public class OptionEditor implements TableCellEditor {
		private DrawManager dm; 		// an instance of draw manager 
		private boolean isEditable; 	// is the field editable
		private JFrame frame;			// the window frame for the dialog
		private JDialog dialog; 		// box that lets user update expression/functions properties
		private JPanel options_value;	// different options on the dialog box		
		private JPanel expression_options;	// function properties group
		private JPanel particle_options;	// particle properties group
		
		private JTextField function;		// current function value
		private Color userColor;			// what is users color
		private JButton color_button;		// change the color of the line 
		private JCheckBox checkVisible;		// is the function visibile on the graph
		private JCheckBox showParticle;		// show the particles along the function
		private JButton removeFunction;		// remove the function from the graph
		
		public OptionEditor()
		// POST: create a OptionEditor cell in the table that is editable
		{ 
			isEditable = true; 
		}
		
		public OptionEditor(DrawManager dm) 
		// PRE: dm is an instance of a DrawManager
		// POST: create an instance of a OptionEditor cell that is editable 
		// and set dm to dm
		{			
			this();
			this.dm = dm;
		}
		public OptionEditor(boolean isEditable)
		// PRE: isEditable is not null 
		// POST: creates an instanceof OptionEditor cell in the table that is editable 
		// and dm set to NULL
		{
			this.isEditable = isEditable;
		}
		@Override
		public void addCellEditorListener(CellEditorListener l)
		// PRE: l is set to an instance of CellEditorListener
		// POST: 
		{
		}
		
		@Override
		public void cancelCellEditing() 
		// POST: cell editing is canceled
		{
		}

		@Override
		public Object getCellEditorValue()
		// POST: fctval == return a null pointer
		// DOESN'T DO ANYTHING.		
		{
			return null;
		}

		@Override
		public boolean isCellEditable(EventObject anEvent) 
		// PRE: anEvent is an instance of EventObject
		// POST: fctval == return true if the table cell is editable,
		// false if it is not
		{
			// TODO Auto-generated method stub
			return isEditable;
		}

		@Override
		public void removeCellEditorListener(CellEditorListener l)
		// PRE: l is an instance of CellEditorListener
		// POST: DOESN'T DO ANYTHING
		{
		}

		@Override
		public boolean shouldSelectCell(EventObject anEvent) 
		// PRE: anEvent is an instance of EventObject
		// POST: fctval == always true becuause all field should be selectable
		{
			return true;
		}

		@Override
		public boolean stopCellEditing()
		// POST: fctval == always true because you should be able to stop editing a cell
		{
			return true;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
		// PRE: table is an instance of JTable, and has the following column headers: ["INDEX",  "EXPRESSION", "OPTION" ]
		// 	value is an instance of the value current editing, isSelected is true, 
		// 	row is the current row being edited, column is the current column being edit
		// fctval == return 
		{

			int index;		// the index of the current row being edited
			
			index = new Integer(table.getModel()
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
			color_button.addActionListener(new ActionListener()
			// POST: adds a event handler to color button to response on click
			{				
				@Override
				public void actionPerformed(ActionEvent arg0) 
				// PRE: arg0 is of instance of ActionEvent
				// POST: change's the line color
				{
					
					dm.getFunction(index)
					.setColor(JColorChooser
							.showDialog(dialog, "Choose a color", userColor));					
				}
			});
			
			showParticle.addActionListener(new ActionListener()
			// POST: adds an event handler to showParticle 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				// PRE: e is an instance of ActionEvent
				// POST: shows/hide the particle along the line function 
				{
					dm.getFunction(index).showParticles(showParticle.isSelected());
				}
			});
			
			checkVisible.addActionListener(new ActionListener()
			// POST: adds a event handler to checkVisibile
			{
				@Override
				public void actionPerformed(ActionEvent e)
				// PRE: e is an instance of ActionEvent
				// POST: hides the function from the graph
				{
					Function f = dm.getFunction(index);
					boolean visible = checkVisible.isSelected();
					f.setVisible(visible);
				}
			});
			
			function.addActionListener(new ActionListener()
			// POST: add an event handler to function
			{
				@Override
				public void actionPerformed(ActionEvent e)
				// PRE: e is an instance of ActionEvent
				// POST: updates the function express
				{
					String expression = function.getText(); 							
							table.getModel()
							.setValueAt(expression, row, column-1);
					Function f = dm.getFunction(index);
					f.setExpression(expression);
				}
			});
			
			removeFunction.addActionListener(new ActionListener() 
			// POST: adds a event handler to removeFunction
			{
				
				@Override
				public void actionPerformed(ActionEvent e) 
				// PRE: e is an instance of ActionEvent
				// POST: removes the function from the graph, 
				// and deletes is from the table, and removes, and deletes it from the functions
				{
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
	
	private DrawManager dm;                         // reference to DrawManager
	private JPanel functionInputPanel;				// groups the function input
	private JPanel tableHolderPanel;				// groups the table
	
	private DefaultTableModel dtm;					// a table model thats hold the data
	private JButton add_function;					// button that adds the function to the table and functions
	protected JTextField lineFunction;				// takes in the user function
	protected JTable table;							// the table to display the functions
	
	// grid optins
	private JPanel grid_options;					// groups grid options into one panel
	private JButton showGridColorOptions;			// button to the change the grid color
	private JButton showGridBGColorOptions;			// button to change background grid color
	private JCheckBox showGrid;						// show/hide the grid
	private JCheckBox showCursorCoords;				// show/hide the cursor info box
	private JCheckBox showNumbers;					// show/hide the numbers on the grid axis
	private JColorChooser gridColor;				// allows the user to chagne the grid color
	
	private JTextField tickHSize;					// change the ticks along the x axis, scales up
	private JTextField tickVSize;					// change the ticks along the y axis, scale wider
	private JTextField tickHScale;					// change the X scale, zoom in				
	private JTextField tickVScale;					// change the y scale, zooms in 
	
	private JLabel functionText;					// large font size function text at the top
	private JLabel graphText;						// large font size graph text in the center 
	
	
	public RightPanel(DrawManager _drawManager)
	// PRE: _drawmanager is of instance of DrawManager
	// POST: draw a RightPanel and places each component
	{		
		final DrawManager drawManager = _drawManager;
		dm = drawManager;
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
		functionText = new JLabel("FUNCTION");
		graphText = new JLabel("GRAPH OPTIONS");
		functionInputPanel = new JPanel();		
		tableHolderPanel = new JPanel();

		functionText.setFont(new Font("Serif", Font.BOLD, 18));
		graphText.setFont(new Font("Serif", Font.BOLD, 18));
		
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
		
		
		tickHSize.addActionListener(new ActionListener() 
		// POST: adds an eventhandler to the tickHSize
		{
			@Override
			public void actionPerformed(ActionEvent e)
			// PRE: e is an instance of ActionEvent
			// POST: sets the tickHValue
			{
				try 							// try to parse to user input 
				{
					drawManager.setTickH((new Double(tickHSize.getText())));
				} 
				catch (NumberFormatException e1) // the user input was not in the correct format
				{
					tickHSize.setText("");
					JOptionPane.showConfirmDialog(null, "Please enter a decimal value!", 
							"Wrong input",
							JOptionPane.OK_CANCEL_OPTION);
				}
			}
		});
		
		tickVSize.addActionListener(new ActionListener() 
		// POST: add an addActionListener handler to tickVSize   			
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			// PRE: e is an instance of ActionEvent
			// POST: changes the setTickV to user input
			{
				try 								// try and parse the user input
				{
					drawManager.setTickV((new Double(tickVSize.getText())));
				} 
				catch ( NumberFormatException e1 ) // user input was in the wrong format
				{
					tickVSize.setText("");
					JOptionPane.showConfirmDialog(null, "Please enter a decimal value!", 
							"Wrong input",
							JOptionPane.OK_CANCEL_OPTION);
				}
				
			}
		});
		
		add_function.addActionListener(new ActionListener()
		// POST: add ActionListener to the add_function
		{
			@Override
			public void actionPerformed(ActionEvent e)
			// PRE: e is an instanve of ActionEvent
			// POST: adds the function to the table, updates the functions, updates the graph
			{
				if ( lineFunction.getText().length() <= 0 )		// did the user input something greater than 0 
				{
					return;
				}
				
				try												// try and parse the user input	  
				{
					drawManager.addFunction(new Function(lineFunction.getText(), 
							new Color(0,0,0), drawManager));
					addRow(lineFunction.getText(), dtm);
					table.updateUI();
				} catch (Exception e1)							// the input was wrong, or something else when wrong 
				{
					// TODO Auto-generated catch block
					JOptionPane.showConfirmDialog(null, "The input for the function is wrong! "
							+ "Expected input is x*x or x, no white space and * for carrot symbol.");
				}
			}
		});
		
		showGrid.addActionListener(new ActionListener() 
		// POST: add ActionListener to the add_function
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			// PRE: arg0 is an instance of ActionEvent
			// POST:
			{
				drawManager.showGrid(showGrid.isSelected());
			}
		});
		
		showGridColorOptions.addActionListener(new ActionListener() 
		// POST: add ActionListener to the add_function
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			// PRE: arg0 is an instance of ActionEvent
			// POST:
			{
				// TODO Auto-generated method stub
				drawManager.setGridColor(JColorChooser
						.showDialog(grid_options, "Choose a color", new Color(0, 0, 0)));	
			}
		});
		
		showGridBGColorOptions.addActionListener(new ActionListener()
		// POST: add ActionListener to the add_function
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			// PRE: e is an instance of ActionEvent
			// POST: changes the background color of the grid
			{
				// TODO Auto-generated method stub
				drawManager.setBackground(JColorChooser
						.showDialog(grid_options, "Choose a color", drawManager.getBackground()));
			}
		});
		
		showNumbers.addActionListener(new ActionListener() 
		// POST: add ActionListener to the add_function
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			// PRE: e is an instance of ActionEvent
			// POST: shows/hide the number on the axis
			{
				// TODO Auto-generated method stub
				drawManager.showNumbers(showNumbers.isSelected());
			}
		});
		
		showCursorCoords.addActionListener(new ActionListener()
		// POST: add ActionListener to the add_function
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			// PRE: e is an instance of ActionEvent
			// POST: shows/hides the cursorCoords
			{
				drawManager.showCursorCoords(showCursorCoords.isSelected());
			}
		});
		
		tickHScale.addActionListener(new ActionListener()
		// POST: add ActionListener to the add_function
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			// PRE: e is an instance of ActionEvent
			// POST: zooms in horizontally into the graph/function
			{
				try 							// try and parse the user input	  
				{
					drawManager.setScaleH(new Double(tickHScale.getText()));
				} 
				catch (NumberFormatException e1) // user input was not in the correct format
				{
					tickHScale.setText("");
					JOptionPane.showConfirmDialog(null, "Please enter a decimal value!", 
							"Wrong input",
							JOptionPane.OK_CANCEL_OPTION);
				}
			}
		});
		
		tickVScale.addActionListener(new ActionListener()
		// POST: add ActionListener to the add_function
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			// PRE: e is an instance of ActionEvent
			// POST: zooms into to the function veritcally
			{
				try 							// try and parse the user input	  
				{
					drawManager.setScaleV(new Double(tickVScale.getText()));
				} catch (NumberFormatException e1) 	// user input was not in the correct format
				{			
					tickVScale.setText("");
					JOptionPane.showConfirmDialog(null, "Please enter a decimal value!", 
							"Wrong input",
							JOptionPane.OK_CANCEL_OPTION);
				}
			}
		});
		
		functionInputPanel.setLayout(new BorderLayout());
		functionInputPanel.add(functionText, BorderLayout.PAGE_START);	
		functionInputPanel.add(new JSeparator());
		functionInputPanel.add(new JLabel("f(x)="), BorderLayout.WEST);
		functionInputPanel.add(lineFunction, BorderLayout.CENTER);
		functionInputPanel.add(add_function, BorderLayout.EAST);
		
		table.getColumn("INDEX").setCellEditor(new OptionEditor(false));
		table.getColumn("EXPRESSION").setCellEditor(new OptionEditor(false));
		table.getColumn("OPTION").setCellEditor(new OptionEditor(drawManager));
		
		// create a table frame with the headers on top and the table on the center
		tableHolderPanel.setLayout(new BorderLayout());
		tableHolderPanel.add(table.getTableHeader(), BorderLayout.NORTH);
		tableHolderPanel.add(table, BorderLayout.CENTER);
		
		

		grid_options.add(new JSeparator(), BorderLayout.PAGE_START);
		grid_options.add(graphText, BorderLayout.WEST);
		grid_options.setLayout(new GridLayout(0, 1));
		grid_options.add(new JLabel("X Tick Value (0.00001, 10000000.0)"));
		grid_options.add(tickHSize);
		grid_options.add(new JLabel("Y Tick Value (0.00001, 10000000.0)"));
		grid_options.add(tickVSize);
		grid_options.add(new JLabel("Y Scale Value (0.00001, 10000000.0)"));
		grid_options.add(tickHScale);
		grid_options.add(new JLabel("X Scale Value (0.00001, 10000000.0)"));
		grid_options.add(tickVScale);
		grid_options.add(showCursorCoords);
		grid_options.add(showNumbers);
		
		grid_options.add(showGrid);
		grid_options.add(showGridColorOptions);
		grid_options.add(showGridBGColorOptions);
			
		add(functionInputPanel, BorderLayout.NORTH);		
		add(tableHolderPanel, BorderLayout.CENTER);
		add(grid_options, BorderLayout.PAGE_END);
		setPreferredSize(new Dimension(340,1000));
	}

	public static void addRow(String s, DefaultTableModel dtm) 
	// PRE: s is an instance of String, dtm is set to DefaultTableModel
	// POST: a helper function that adds a row to the dtm
	{
		dtm.addRow(new Object[] {
				++indexCount,
				s,
				"Edit"
		});
	}
	
	@Override
	protected void paintComponent(java.awt.Graphics g) 
	{
		DimensionF dim; // helper dimension
		
		super.paintComponent(g);
		
		dim = dm.getScale();
		
		if(!tickHScale.isFocusOwner()) // update if not focused
		{
			tickHScale.setText(dim.width+"");
		}
		
		if(!tickVScale.isFocusOwner()) // update if not focused
		{
			tickVScale.setText(dim.height+"");
		}
		
		dim = dm.getTick();
		
		if(!tickHSize.isFocusOwner()) // update if not focused
		{
			tickHSize.setText(dim.width+"");
		}
		
		if(!tickVSize.isFocusOwner()) // update if not focused
		{
			tickVSize.setText(dim.height+"");
		}
	}
	
	
}
