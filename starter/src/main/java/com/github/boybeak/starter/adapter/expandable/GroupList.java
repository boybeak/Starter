package com.github.boybeak.starter.adapter.expandable;

import com.github.boybeak.starter.adapter.LayoutImpl;
import com.github.boybeak.starter.adapter.expandable.Group;

import java.util.LinkedList;

public class GroupList extends LinkedList<Group> {

	public LayoutImpl getItem (int position) {

		int cursor = 0;

		for (Group group : this) {
			int cursorNext = cursor + group.size();
			if (position >= cursor && position < cursorNext) {
				return group.get(position - cursor);
			} else {
				cursor = cursorNext;
			}
		}
		return null;
	}

	public int getItemCount () {
		int size = 0;
		for (Group group : this) {
			size += group.size();
		}
		return size;
	}

	public boolean isGroupIndex (int index) {
	    if (index < 0 || index >= getItemCount()) {
	        return false;
        }
        int cursor = index;
        for (Group group : this) {
            cursor = cursor - group.size();
            if (cursor == 0) {
                return true;
            }
        }
        return false;
    }
}
