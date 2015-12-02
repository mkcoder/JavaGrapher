
public class Token {
	private double num;        // number stored in the node
	private char op;        // operator stored in the node
	private boolean isOp;   // flag that check if the node is a operator
	
	public Token(double num, boolean isOp)
	// PRE: num, next and isOp are initialized and numid
	// POST: creates a node object with the class members num set to num
	// and isOp set to isOp
	{
		this.num = num;
		this.isOp = isOp;
	}
	
	public void setOp(char op)
	// PRE:
	// POST:
	{
		this.op = op;
	}
	
	public double getNum()
	// POST: returns the class member num
	{
		return num;
	}
		
	public char getOp()
	// POST: returns the class member op
	{
		if(isOp)  // if an operator
			return op;
		return '!';     
	}
	
	public boolean isOp()
	// POST: returns the class member isOp
	{
		return isOp;
	}
	
	public String toString()
	// POST: returns the num/op of held in the node
	{
		if(isOp)
			return getOp()+"\n";
		return getNum()+"\n";
	}
}
