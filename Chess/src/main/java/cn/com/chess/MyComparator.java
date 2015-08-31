package cn.com.chess;
import java.util.Comparator;

import static cn.com.chess.LoadUtil.nHistoryTable;

public class MyComparator implements Comparator<Integer>
{
	@Override
	public int compare(Integer arg0, Integer arg1) {
		Integer i1=(Integer)arg0;
		Integer i2=(Integer)arg1;		
		return nHistoryTable[i2.intValue()]-nHistoryTable[i1.intValue()];
	}
	
}