package swing.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 * The class representing a graph
 * @author VAILLON Albert
 * @version JDK 11.0.13
 * @param <E> The type of object the ComboBox will contain (the toString method will automatically be called to display text in the UI)
 */
public class ComboModel<E extends Comparable> extends AbstractListModel<E> implements ComboBoxModel<E> {
	
	private List<E> items = new ArrayList<>();
	private Object selection = null;
	
	/**
	 * @return Returns the number of rows
	 */
	@Override
	public int getSize() {
		return items.size();
	}
	
	/**
	 * @param i The row index
	 * @return Returns the element at the row <code>i</code>
	 */
	@Override
	public E getElementAt(int i) {
		return items.get(i);
	}
	
	/**
	 * @return Returns the currently selected item
	 */
	@Override
	public Object getSelectedItem() {
		return selection;
	}
	
	/**
	 * @param o The element that's going to become selected
	 */
	@Override
	public void setSelectedItem(Object o) {
		selection = o;
		fireContentsChanged(this, 0, getSize() - 1);
	}
	
	/**
	 * @param i The index of the element that's going to be selected
	 */
	public void setSelectedIndex(int i) {
		selection = items.get(i);
		fireContentsChanged(this, 0, getSize() - 1);
	}
	
	/**
	 * Replaces the current rows by the ones in <code>newItems</code> and sorts them by alphabetical order
	 * @param newItems The list of new rows
	 */
	public void addAll(List<E> newItems) {
		items.clear();
		Collections.sort(newItems);
		items.addAll(newItems);
		fireContentsChanged(this, 0, getSize() - 1);
	}
	
	/**
	 * Resets the ComboBox by clearing all the rows and selecting nothing
	 */
	public void reset() {
		selection = null;
		items.clear();
	}
	
}