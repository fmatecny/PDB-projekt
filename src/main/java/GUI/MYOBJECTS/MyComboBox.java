/**
 * Projekt do předmětu PDB (2016/2017) - Prostorové, multimediální a temporální 
 * databáze: Metro Accident Databse
 * Autoři: Petr Staněk (xstane34), 
           František Matečný (xmatec00),
           Jakub Stejskal (xstejs24)
 * Datum:  12.12.2016
 * Verze:  1.0
 */
package GUI.MYOBJECTS;

import javax.swing.DefaultComboBoxModel;
import java.util.Vector;
import javax.swing.JComboBox;

/**
 * Třída umožňující použít combobox v režimu key-value.
 */
public class MyComboBox {
	String key;
	int value;
	
	public MyComboBox(String key, int value) {
		this.key= key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key= key;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	@Override
	public String toString(){
		return key;
	}
	
	public static DefaultComboBoxModel getCombo(Vector items)
	{
		return new DefaultComboBoxModel(items);
	}
	
	public static void setSelectedKey(JComboBox comboBox, int value)
    {
        MyComboBox item;
        for (int i = 0; i < comboBox.getItemCount(); i++)
        {
            item = (MyComboBox)comboBox.getItemAt(i);
            if (item.getValue() == value)
            {
                comboBox.setSelectedIndex(i);
                break;
            }
        }
    }
}