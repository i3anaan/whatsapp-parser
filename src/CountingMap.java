import java.util.HashMap;

import org.jfree.data.time.TimeSeries;


public class CountingMap<K> extends HashMap<K,Integer>{
	public void increaseOccurence(K key){
		if(this.get(key)==null){
			this.put(key, 0);
		}
		this.put(key, this.get(key)+1);
	}
	
	public String toString(){
		String output ="";
		for(K k : this.keySet()){
			output = output + k.toString() + " : "+this.get(k) + "\n";
		}
		return output;
	}
	
	public int totalCount(){
		int count = 0;
		for(K k : this.keySet()){
			count += this.get(k);
		}
		return count;
	}
}
