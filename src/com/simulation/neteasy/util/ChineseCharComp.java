package com.simulation.neteasy.util;

import java.text.Collator;
import java.util.Comparator;
/**
 * 
 * @author 紫枫
 *
 */
@SuppressWarnings("rawtypes")
public class ChineseCharComp implements Comparator {
	@Override
	public int compare(Object lhs, Object rhs) {
		Collator myCollator = Collator.getInstance(java.util.Locale.CHINA);
		if (myCollator.compare(lhs, rhs) < 0) {
			return -1;
		} else if (myCollator.compare(lhs, rhs) > 0) {
			return 1;
		} else {
			return 0;
		}
	}

}
