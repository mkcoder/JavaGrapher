
public class Expression{
	String input; 	     // input that is going to be evaluated
	int index;           // the current index of the input string
	int inputLength;     // the length of the input string
	double solution;     // the solution of the equation
	LinkedList valStack; // linked list that holds all the numbers 
	LinkedList opStack;  // linked list that holds all the operators
	
	public Expression(String input)
	// PRE:  input is initialized and valid (a string with only numbers and ops)
	// POST: sets the class member input to input and initialize the class members
	//         valStack, opStack and solution
	{
		this.input = input.replaceAll(" ","") + "=";
		//this.input = input;
		this.valStack = new LinkedList();
		this.opStack = new LinkedList();
		this.index = 0;
		this.inputLength = this.input.length();
		this.solution = 0;
	}
	
	public Token getToken()
	// POST: return a token with either an operator or number
	{
		Token retToken = new Token(0, true);
		char ch;
		String num;
		double val;
				
		ch  = input.charAt(index);
		if(ch == '='){
			retToken.setOp('=');
			return retToken;
		}
		
		if ( ('+' == ch) || ('-' == ch) || ('*' == ch) ||
				('/' == ch) || ('(' == ch) || (')' == ch) || ('^' == ch)){
			retToken = new Token(0,true);
			retToken.setOp((char) ch);
			index++;
			return retToken;
		}
	   
		if(Character.isDigit(ch)){
			int start = index;
			int end = start;
			char next = input.charAt(start+1);
			while(Character.isDigit(next)|| next == '.'){
				end++;
				next = input.charAt(end);
			}
			if(start != end){
				num = input.substring(start,end);
				val = Double.parseDouble(num);
				index=end;
			}else{
				val = Character.getNumericValue(ch);
				index++;
			}
			System.out.println("Start string: " + start);
			System.out.println("End string: " + end);
			retToken = new Token(val,false);
		}
		return retToken;
	}
	
	public double Eval(double a, char b, double c)
	// PRE: a b and c are initialized and valid
	// POST: returns the double a op c depending on b
	{
		if( b == '^'){
			double val=1;
			
			for(int i=0;i < c;i++){
				val *= val;
			}
			return val;
			
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
			System.out.println("ILLEGAL EXPRESSION\n");
			opStack.pop();
			//return -999;
		}else{
			opStack.pop();
		}
		
		if(valStack.isEmpty()){//check if v2 is empty
			System.out.println("ILLEGAL EXPRESSION\n");
			//return -999;
		}
		
		v2 = valStack.top();
		valStack.pop();
		
		if(valStack.isEmpty()){//check if v1 is empty
			System.out.println("ILLEGAL EXPRESSION\n");
			//return -999;
		}

		v1 = valStack.top();
		valStack.pop();

		v3 = Eval(v1, op, v2);
		Token temp = new Token(v3,false);
		valStack.push(temp);
		
		System.out.println("Expression: " + v1 + op + v2);
		System.out.println("v3: " +v3);
		return v3;
	}
	
	void processExpression ()
	{
		int pf=0;
		Token inputToken;
		do{
			inputToken = getToken();
			if(inputToken.getOp() == '=') break;
			
			if(inputToken.isOp())
				System.out.println("Token " + index + ": " + inputToken.getOp());
			else
				System.out.println("Token " + index + ": " + inputToken.getNum());
			/*
			if(valStack.isEmpty())
				System.out.println("The number stack is empty");
			else
				System.out.println("The number stack is not empty");
			
			if(opStack.isEmpty())
				System.out.println("The operation stack is empty");
			else
				System.out.println("The operation stack is not empty");
			*/
			// loop til the end of the string
			if (!inputToken.isOp()){ // contains a number
				valStack.push(inputToken); // push the value onto the valStack
			}
			if(inputToken.isOp()){ // contains an operator
				if(inputToken.getOp()=='('){ // if the current operator is a open parenthesis
					opStack.push(inputToken); //push ( on the OperatorStack
				}
				
				if(inputToken.getOp()=='+'||inputToken.getOp()=='-'){ //if operator is + or - 
					while((!opStack.isEmpty())&&(opStack.top()=='^'||opStack.top()=='+'||opStack.top()=='-'||opStack.top()=='*'||opStack.top()=='/')){//the top of the OperatorStack is +, -, *, or /
						this.solution = popAndEval();
						if(solution == -999){break;}//Invalid expression
					}
					opStack.push(inputToken); //push the current operator on the OperatorStack
				}
				if(inputToken.getOp()=='*' || inputToken.getOp() == '/'){//if operator is * or /
					while((!opStack.isEmpty())&&(opStack.top()=='^' || opStack.top() == '*' || opStack.top() == '/')){//the top of the OperatorStack is +, -, * or /
						solution = popAndEval();
						if(solution == -999){break;}//Invalid expression
					}				
					opStack.push(inputToken); //push the current operator on the OperatorStack
				}
				
				if(inputToken.getOp()=='^'){//if operator is * or /
					while((!opStack.isEmpty())&&(opStack.top()=='^')){//the top of the OperatorStack is +, -, * or /
						solution = popAndEval();
						if(solution == -999){break;}//Invalid expression
					}				
					opStack.push(inputToken); //push the current operator on the OperatorStack
				}
				
				if(inputToken.getOp()==')'){//the current operator is a Closing Parenthesis
					while((!opStack.isEmpty())&&(opStack.top()!='(')){//the top of the OperatorStack is not an Open Parenthesis
						solution=popAndEval();
					}
				
					if((!opStack.isEmpty())){//the OperatorStack is Empty
						System.out.println("ILLEGAL EXPRESSION 1\n");
						 pf = 1; //flag for )
					}else{
						opStack.pop(); //pop the Open Parenthesis from the OperatorStack
					}
				}
			}
		}while(inputToken.getOp() != '=');// end of while loop 

		/* The expression has reached its end */
		while((!opStack.isEmpty())){
			solution = popAndEval();
		}
		
		valStack.pop();//popping the ValueStack
		if((!valStack.isEmpty()) || pf == 1){
			System.out.println("ILLEGAL EXPRESSION 2\n");
			//solution = -999;
		}
	}
	
	public String toString(){
		return "\n" + input  + solution +"\nThe length was: " + inputLength;
	}

	public int getInputLength() 
	//DEBUG ONLY
	{
		return inputLength;
	}

	public char getCharAt(int x) {
		// TODO Auto-generated method stub
		return input.charAt(x);
	}
	

	
	
}
