package databaseApp;
/*****************************************
 * 		This class is used to set maximum characters in a textfield
 ****************************************/
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class FixedSizeDoc extends PlainDocument{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9053071492458723955L;
	private int max=10;
			public FixedSizeDoc(int max)
			{
				this.max=max;
		
			}
			public void insertString(int offs,String str,AttributeSet a) throws BadLocationException{
				if(getLength()+str.length()>max){
					str=str.substring(0,max-getLength());
				}
				super.insertString(offs, str, a);
			}

}
