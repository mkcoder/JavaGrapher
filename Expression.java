
public class Expression{
	String input; 	     // input that is going to be evaluated
	int index;           // the current index of the input string\
	int sign;            // the sign of a parsed number
	int inputLength;     // the length of the input string
	double solution;     // the solution of the equation
	LinkedList valStack; // linked list that holds all the numbers 
	LinkedList opStack;  // linked list that holds all the operators
	
	public Expression(String input)
	// PRE:  input is initialized and valid (a string with only numbers and ops)
	// POST: sets the class member input to input and initialize the class members
	//         valStack, opStack and solution
	{
		this.input = input.replaceAll(" ","") + "="; // removes all space and appends =
		this.valStack = new LinkedList();
		this.opStack = new LinkedList();
		this.index = 0;
		this.sign = 1;
		this.inputLength = this.input.length();
		this.solution = 0;
	}
	
	public Token getToken()
	// POST: parses a section of the input and return a token with either an operator or number
	{
		Token retToken = new Token(0, true);
		char ch;
		String num; 
		double val;
				
		ch  = input.charAt(index);
		
		if(ch == '='){ // end of equation
			retToken.setOp('=');
			return retToken;
		}
		
		if(index == 0 && ch == '-'){ // start with a - 
			sign = -1;               // must be a negative number
			index++;
		}else if(ch == '-' && !Character.isDigit(input.charAt(index-1))){ // previous char isn't a digit either
			sign = -1;                                                    // it's a negative number
			index++;	
		}else{
			if ( ('(' == ch)){ // if open parenthesis, sign doesn't change
				retToken.setOp(ch);
				index++;
				return retToken;
			}else if( ('+' == ch) || ('*' == ch) || ('/' == ch) || 
						('(' == ch) || (')' == ch) || ('^' == ch)){
				sign=1; 
				retToken.setOp(ch); // set the operator
				index++;
				return retToken; 
			}
		}
		ch  = input.charAt(index);

		if(Character.isDigit(ch)){  // if the char is a character
			int start = index;      // store start and end
			int end = start;
			char next = input.charAt(start+1); 
			while(Character.isDigit(next)|| next == '.'){ // if next is a digit or .
				end++;                       
				next = input.charAt(end);
			}
			if(start != end){ // if the number is more than 1 character
				num = input.substring(start,end);
				val = Double.parseDouble(num);
				index=end;
			}else{ // if the number is only 1 character 
				val = Character.getNumericValue(ch);
				index++;
			}
			retToken = new Token(sign*val,false); // set the token 
		}
		return retToken;
	}
	
	public double Eval(double a, char b, double c)
	// PRE: a b and c are initialized and valid
	// POST: returns the double a op c depending on b
	{
		if( b == '^'){     // exponents
			double val=1;
			
			for(int i=0;i < Math.abs(c);i++){
				val *= a;
			}
			if(c > 0)   // if positive exponent
				return val;
			else       // if negative exponent
				return 1/val;
			
		}else if (b == '+') // addition
			return a + c;
		else if (b == '-')  // subtraction
			return a - c;    
		else if (b == '*')  // multiplication
			return a * c;
		else 			    // division
			return a / c;
	}
	
	public double popAndEval()
	// POST: evaluate two numbers v1 and v2 from the stack and stores it in v3
	{
		double v1,v2,v3;
		char op;
		
		op = (char) opStack.top();

		if ((op != '^' && op != '*' && op != '/' && op != '+' && op != '-')||opStack.isEmpty()){
			throw new RuntimeException("ILLEGAL EXPRESSION\n");
		}else{
			opStack.pop();
		}
		
		if(valStack.isEmpty()){//check if v2 is empty
			throw new RuntimeException("ILLEGAL EXPRESSION\n");
		}
		
		v2 = valStack.top();
		valStack.pop();
		
		if(valStack.isEmpty()){//check if v1 is empty
			throw new RuntimeException("ILLEGAL EXPRESSION\n");
		}

		v1 = valStack.top();
		valStack.pop();

		v3 = Eval(v1, op, v2);
		Token temp = new Token(v3,false);
		valStack.push(temp);
		
		return v3;
	}

	public double processExpression() 
	// POST: processes the parsed input and return the solution
	{
		Token inputToken = getToken();	
		
			/* Loop until the expression reaches its End */
			while(inputToken.getOp() != '='){
			    /* The expression contain a VALUE */
				if (!inputToken.isOp()){		 		
					valStack.push(inputToken); // push the value onto the valStack
				}
				/* The expression contains an OPERATOR */
				if(inputToken.isOp()){

					if(inputToken.getOp() =='('){//If the current operator is a Open Parenthesis
						opStack.push(inputToken); //push ( on the OperatorStack
					}
					if(inputToken.getOp()=='+'||inputToken.getOp()=='-'){//if operator is + or - 
						while((!opStack.isEmpty())&&(opStack.top()=='+'||opStack.top()=='-'||opStack.top()=='*'||opStack.top()=='/'||opStack.top()=='^')){//the top of the OperatorStack is +, -, *, or /
							solution=popAndEval();
						}
						opStack.push(inputToken);  //push the current operator on the OperatorStack
					}
					if(inputToken.getOp()=='*'||inputToken.getOp()=='/'){//if operator is * or /
						while((!opStack.isEmpty())&&(opStack.top()=='*'||opStack.top()=='/'||opStack.top()=='^')){//the top of the OperatorStack is +, -, * or /
							solution=popAndEval();
						}				
						opStack.push(inputToken);  //push the current operator on the OperatorStack
					}
					if(inputToken.getOp()=='^'){//if operator is * or /
						while((!opStack.isEmpty())&&(opStack.top()=='^')){//the top of the OperatorStack is +, -, * or /
							solution = popAndEval();
						}				
						opStack.push(inputToken); //push the current operator on the OperatorStack
					}
					if(inputToken.getOp()==')'){//the current operator is a Closing Parenthesis
						while((!opStack.isEmpty())&&(opStack.top()!='(')){//the top of the OperatorStack is not an Open Parenthesis
							solution=popAndEval();
						}
					
						if(opStack.isEmpty()){//the OperatorStack is Empty
							throw new RuntimeException("ILLEGAL EXPRESSION\n");
						}else{
							opStack.pop(); //pop the Open Parenthesis from the OperatorStack
						}
					}
				}
				/* get next token from input */
				inputToken = getToken();
			} 

			/* The expression has reached its end */
			while(!opStack.isEmpty()){
				solution = popAndEval();
			}
			
			valStack.pop(); // popping the ValueStack
			if(!valStack.isEmpty()){
				throw new RuntimeException("ILLEGAL EXPRESSION\n"); //print error if not empty
			}else{
				return solution; // return the solution
			}	
		}
	
	public static double evaluate(String expr)
	{
		Expression e = new Expression(expr);
		return e.processExpression();
	}
		
	public String toString(){
		return "\n" + input  + solution +"\nThe length was: " + inputLength;
	}
}
