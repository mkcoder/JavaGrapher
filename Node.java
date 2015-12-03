// Class: Node
// Description: A node object that simulates a node in a linked list

public class Node {
    
	private Token token;    // the token stored in the node
	private Node next;      // the next node
	
	public Node(Token token, Node next)
	// PRE: token and next are initialized and valid
	// POST: creates a node object with the class members val set to val
	// next set to next and isOp set to isOp
	{
		this.token = token;
		this.next = next;
	}
	
	public void setNext(Node next)
	// PRE: next is initialized and valid
	// POST: set next to the class member next
	{
		this.next = next;
	}
	
	public Token getToken()
	// POST: returns the class member token
	{
		return this.token;
	}
	
	public Node getNext()
	// POST: returns the class member next
	{
		return this.next;
	}
}
