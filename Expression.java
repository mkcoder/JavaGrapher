// Class: Expression
// Description: takes a valid algebraic expression in the form of a string 
//               then returns the values as a double

public class Expression{
	String input; 	     // input that is going to be evaluated
	String singleTest;   // a string that tests if a single number is entered
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
		this.input = input.replaceAll(" ","") + "="; // removes all spaces and appends =
		this.singleTest = input.replaceAll(" ","");  // remove all spaces
		this.valStack = new LinkedList();            // initialize lists
		this.opStack = new LinkedList();
		this.index = 0;                              // set class members
		this.sign = 1;
		this.inputLength = this.input.length();
		this.solution = 0;
	}
	
	public Token getToken()
	// POST: parses a section of the input and return a token with either an operator or number
	{
		Token retToken = new Token(0, true); // initialize token
		char ch;                             // character that holds the char at current index
		String num;                          // string that holds a number
		double val;                          // the double representation of num
				
		ch  = input.charAt(index);	
			
		if(ch == '='){ // end of equation
			retToken.setOp('=');
			return retToken;
		}
		
		else if(index == 0 && ch == '-'){ // start with a - 
			sign = -1;               // must be a negative number
			index++;
			ch = input.charAt(index);
		}else if(ch == '-' && !Character.isDigit(input.charAt(index-1))){ // previous char 
			sign = -1;                           //isn't a digit either  it's a negative number
			index++;	
			ch = input.charAt(index);
		}else{ // operator for not negatives
			if ( ('(' == ch)){ // if open parenthesis
				retToken.setOp(ch);
				index++;
				retToken.setNum(1);
				return retToken;
			}else if(('s' == ch)){ // if start with s 
				if(input.charAt(index+1) == 'i'          // check for sin
						&& input.charAt(index+2) == 'n'){
					retToken.setOp(ch);
					index+=3;
					retToken.setNum(1);
					return retToken;
				}else if (input.charAt(index+1) == 'q'&& input.charAt(index+2) == 'r' 
						 && input.charAt(index+3) == 't'){  // check for sqrt
					retToken.setOp('q');
					index+=4;
					retToken.setNum(1);
					return retToken;						
				}else
					throw new RuntimeException("ILLEGAL EXPRESSION\n");
			}else if(('c' == ch)){	// if start with c
				if(input.charAt(index+1) == 'o'          // check for cos
						&& input.charAt(index+2) == 's'){
					retToken.setOp(ch);
					index+=3;
					retToken.setNum(1);
					return retToken;
				}else
					throw new RuntimeException("ILLEGAL EXPRESSION\n");
			}else if(('t' == ch)){	// if starts with t
				if(input.charAt(index+1) == 'a'          // check for tan
						&& input.charAt(index+2) == 'n'){
					retToken.setOp(ch);
					index+=3;
					retToken.setNum(1);
					return retToken;
				}else
					throw new RuntimeException("ILLEGAL EXPRESSION\n");
			}else if(('l' == ch)){	// if starts with l
				if(input.charAt(index+1) == 'o'          // check for log
						&& input.charAt(index+2) == 'g'){
					retToken.setOp(ch);
					index+=3;
					retToken.setNum(1);
					return retToken;
				}else
					throw new RuntimeException("ILLEGAL EXPRESSION\n");								
			}else if( ('+' == ch) || ch == '-' || ('*' == ch) || ('/' == ch) || 
						('(' == ch) || (')' == ch) || ('^' == ch)){
				sign=1; 
				retToken.setOp(ch); // set the operator
				index++;
				retToken.setNum(1);
				return retToken; 
			}
		}

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
	
	public double Eval(double a, char b)
	// PRE: a b and c are initialized and valid
	// POST: returns the double sin/cos/tan/log/sqrt
	//       of a depending on b
	{
		if( b == 's')      // sin
			return Math.sin(a);
		else if( b == 'c') // cos
			return Math.cos(a);
		else if (b == 't') // tan  
			return Math.tan(a);
		else if (b == 'l') // log
			return Math.log(a);
		else{			   // sqrt
			return Math.sqrt(a);
		}
	}
	
	public double Eval(double a, char b, double c)
	// PRE: a b and c are initialized and valid
	// POST: returns the double a op c depending on b
	{
		if( b == '^'){     // exponents
			double val=1;
			
			if(a == 0)   // if base is 0
				return 0;
			if(c == 0)   // if exponent is 0
				return 1;
			
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
		char op;     // the operator 
		double v1=0; // first number 
		double v2=0; // the second number
		double v3=0; // the solution
		
		op = (char) opStack.top();

		if ((op != 'l' && op != 's' && op != 'c' &&op != 't' && op != '^' && op != '*' 
				&& op != '/' && op != '+' && op != '-' && op != 'q')||opStack.isEmpty()){ 
			throw new RuntimeException("ILLEGAL EXPRESSION\n");            // if not valid operator
		}else{                      // if valid operator
			opStack.pop();
		}
		
		if (op == '^' || op == '*' || op == '/' || op == '+' || op == '-'){ // if ^, *, /, +, -
			if(valStack.isEmpty()){ // check if v2 is empty
				throw new RuntimeException("ILLEGAL EXPRESSION\n");
			}
		
			v2 = valStack.top();
			valStack.pop();
		
			if(valStack.isEmpty()){ // check if v1 is empty
				throw new RuntimeException("ILLEGAL EXPRESSION\n");
			}

			v1 = valStack.top();
			valStack.pop();

			v3 = Eval(v1, op, v2);
			Token temp = new Token(v3,false);
			valStack.push(temp);
		}else if (op == 's' || op == 'c' || op == 't'|| op == 'l' || op == 'q'){  // if sin, cos, tan, log
			if(valStack.isEmpty()){ // check if v1 is empty
				throw new RuntimeException("ILLEGAL EXPRESSION\n");
			}
			v1 = valStack.top();
			valStack.pop();
			
			v3 = Eval(v1, op);
			Token temp = new Token(v3,false);
			valStack.push(temp);
		}	
		return v3;
	}

	public double processExpression() 
	// POST: processes the parsed input and return the solution
	{
		if(isNumeric(singleTest)){ // if a single number is entered
			solution = Double.parseDouble(singleTest);
			return solution;
		}
		Token inputToken = getToken();	
		if(inputToken.getNum() == 0 && inputToken.isOp()){
			throw new RuntimeException("ILLEGAL EXPRESSION\n");	
		}
		
		while(inputToken.getOp() != '='){ // loop til end of string
			if(inputToken.getNum() == 0 && inputToken.isOp()){
				throw new RuntimeException("ILLEGAL EXPRESSION\n");	
			}
			if (!inputToken.isOp()){ // is a value	 		
				valStack.push(inputToken); // push the value onto the valStack
			}

			if(inputToken.isOp()){ // is a op
				if(inputToken.getOp() =='('){//If the current operator is a Open Parenthesis
					opStack.push(inputToken); //push ( on the OperatorStack
				}
				if(inputToken.getOp()=='+'||inputToken.getOp()=='-'){//if op is + or - 
					while((!opStack.isEmpty())&&(opStack.top()=='+'||opStack.top()=='-'     
							||opStack.top()=='*'||opStack.top()=='/'||opStack.top()=='^' 
							||opStack.top()=='s'||opStack.top()=='c' || opStack.top()=='q'
							||opStack.top()=='t'|| opStack.top()=='l')){
						solution=popAndEval();
					}
					opStack.push(inputToken); //push the current op on the opStack 
				}
				if(inputToken.getOp()=='*'||inputToken.getOp()=='/'){//if op is * or /
					while((!opStack.isEmpty())&&(opStack.top()=='*'||opStack.top()=='/'
							||opStack.top()=='^'||opStack.top()=='s'||opStack.top()=='c' 
							|| opStack.top()=='t'|| opStack.top()=='l'|| opStack.top()=='q')){
						solution=popAndEval();
					}				
					opStack.push(inputToken);  // push the current op on the opStack 
				}
				if(inputToken.getOp()=='^')
				{ // if op is ^
					while((!opStack.isEmpty())&&(opStack.top()=='^'||opStack.top()=='s'
							||opStack.top()=='c' || opStack.top()=='t' || opStack.top()=='l' 
							|| opStack.top()=='l')){ 
						solution = popAndEval();
					}				
					opStack.push(inputToken); // push the current op on the opStack 
				}
				if(inputToken.getOp()=='s'||inputToken.getOp()=='c' ||
						inputToken.getOp()=='q' // if op is sin,cos,tan, log
						||inputToken.getOp()=='t'||inputToken.getOp() =='l'){
					while((!opStack.isEmpty())&&(opStack.top()=='s'||opStack.top()=='c' 
							|| opStack.top()=='t'|| opStack.top()=='l' || opStack.top()=='q')){
						solution = popAndEval();
					}				
					opStack.push(inputToken); // push the current op on the opStack 
				}
				if(inputToken.getOp()==')'){ // the current operator is a closing parenthesis
					while((!opStack.isEmpty())&&(opStack.top()!='(')){ // the top of the opStack 
						solution=popAndEval();                         // is not an open parenthesis 
					}
					
					if(opStack.isEmpty()){//the OperatorStack is Empty
						throw new RuntimeException("ILLEGAL EXPRESSION\n");
					}else{
						opStack.pop(); //pop the Open Parenthesis from the OperatorStack
					}
				}
			}
			inputToken = getToken(); // get the next token
		} 

		while(!opStack.isEmpty()){ // expression is done
			solution = popAndEval();
		}
			
		valStack.pop(); // popping the ValueStack
		if(!valStack.isEmpty()){
			throw new RuntimeException("ILLEGAL EXPRESSION\n"); //print error if not empty
		}else{
			return solution; // return the solution
		}	
	}
	
	public static boolean isNumeric(String str)
	//PRE: str is initialized and valid
	//POST: returns true if the str is a string reprensation of a number and false otherwise
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}	
	
	public static double evaluate(String expr)
	//PRE: expr is initialized and valid
	//POST: returns the solution to expre
	{
	  Expression e = new Expression(expr);
	  return e.processExpression();
	}
}
