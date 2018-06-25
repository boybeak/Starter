package com.github.boybeak.starter.adapter.expandable;

import com.github.boybeak.starter.adapter.LayoutImpl;

import java.util.ArrayList;
import java.util.List;

public class Group {
	
	private LayoutImpl header;
	private List<LayoutImpl> data;
	
	private boolean isOpen = true;
	
	public Group (LayoutImpl header, List<? extends LayoutImpl> data, boolean isOpen) {
		this.header = header;
		ensureData();
		addDataList(data);
		this.isOpen = isOpen;
	}

	public LayoutImpl getHeader() {
		return header;
	}

	public LayoutImpl get (int position) {
		if (position == 0) {
			return header;
		}
		return data.get(position - 1);
	}

	/**
	 * @param position it not works if 0, because 0 is header position
	 */
	public void remove (int position) {
		if (position == 0) {
			return;
		}
		data.remove(position - 1);
	}

	public void remove (LayoutImpl layout) {
		data.remove(layout);
	}

	public LayoutImpl getLastData () {
	    if (data == null || data.isEmpty()) {
	        return null;
        }
	    return data.get(data.size() - 1);
    }

	public int size() {
		return sizeOfData() + 1;
	}
	
	public int sizeOfData () {
		if (data == null) {
			return 0;
		}
		return data.size();
	}

	public boolean isDataEmpty () {
		return  sizeOfData() == 0;
	}

	private void ensureData() {
		if (data == null) {
			data = new ArrayList<LayoutImpl>();
		}
	}

	public void addDataList (List<? extends LayoutImpl> layouts) {
	    if (layouts == null) {
	        return;
        }
		ensureData();
		data.addAll(layouts);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Group && header.equals(((Group) obj).header);
	}
}
