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
 * @param <E> The type of object of the model
 */
public class ComboModel<E extends Comparable> extends AbstractListModel<E> implements ComboBoxModel<E> {
	
	private List<E> items = new ArrayList<>();
	private Object selection = null;
	
	@Override
	public int getSize() {
		return items.size();
	}

	@Override
	public E getElementAt(int i) {
		return items.get(i);
	}
	
	@Override
	public Object getSelectedItem() {
		return selection;
	}
	
	@Override
	public void setSelectedItem(Object o) {
		selection = o;
		fireContentsChanged(this, 0, getSize() - 1);
	}
	
	public void setSelectedIndex(int i) {
		selection = items.get(i);
		fireContentsChanged(this, 0, getSize() - 1);
	}
	
	public void addAll(List<E> newItems, int selected) {
		items.clear();
		Collections.sort(newItems);
		items.addAll(newItems);
		setSelectedIndex(selected);
		fireContentsChanged(this, 0, getSize() - 1);
	}
	
}