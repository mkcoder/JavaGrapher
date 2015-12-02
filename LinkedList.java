
public class LinkedList {
    
	private Node head;     // the head node of the list
	private int listSize;  // size of the linked list

	public LinkedList()
	// POST: creates a list with no nodes and listSize of 0
	{
		this.head = null;
		this.listSize = 0;
	}
	
	public boolean isEmpty(){
	// POST: return 1 if empty, 0 otherwise
		if (this.listSize == 0){ // empty
			return true;
		}
		return false;
	}
	
	public void push(Token x){
		Node temp = new Node(x, this.head);
		this.head = temp; //reassign head
		listSize++;
	}
	
	public double top()
	// PRE: the function is not called when it is empty
	// POST: returns the num/op at the top of the stack
	{
		if ((head.getToken()).isOp())// is a operator
			return (head.getToken()).getOp();// return operator
		else // is a number
			return (head.getToken()).getNum();//return number

	}
	
	public void pop(){
		Node temp = this.head;
		if (temp != null)// not empty
			this.head = temp.getNext(); //assign new head
		listSize--;
	}
	
	public Node getHead()
	// POST: return the class member head
	{
		return head;
	}
	
	
	
	
	
}
