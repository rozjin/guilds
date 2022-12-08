package us.racem.guilds.util;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.list.SetUniqueList;

import java.util.List;
import java.util.Objects;

public class GuildOTM<K, V>{
    public final SetUniqueList<V> backingList;
    private final GuildIntMap map;
    // Key = Objects.hash(K, V)
    // Value = (int) Index into backingList

    public GuildOTM(List<V> backingList) {
        this.backingList = SetUniqueList.setUniqueList(backingList);
        this.map = new GuildIntMap(backingList.size());
    }

    public void set(K key, V val) {
        backingList.add(val);

        int numKey = Objects.hash(key, val);
        int numIdx = backingList.indexOf(val);

        map.put(numKey, numIdx);
    }

    public void del(K key, V val) {
        int numKey = Objects.hash(key, val);

        backingList.remove(val);
        map.remove(numKey);
    }

    public int indexOf(V val) {
        return backingList.indexOf(val);
    }

    public boolean contains(V val) {
        return backingList.contains(val);
    }

    public class GuildSlice<V> {
        private final GuildOTM<K, V> backingOtm;
        private final K key;
        private final List<V> vals;

        public GuildSlice(GuildOTM<K, V> backingOtm, K key, List<V> vals) {
            this.backingOtm = backingOtm;
            this.key = key;
            this.vals = vals;
        }

        public void set(V val) {
            backingOtm.set(key, val);
        }

        public void del(V val) {
            backingOtm.del(key, val);
        }

        public int indexOf(V val) {
            return backingOtm.indexOf(val);
        }

        public boolean contains(V val) {
            return backingOtm.contains(val);
        }

        public void clear() {
            for (V val: vals) {
                backingOtm.del(key, val);
            }
        }
    }

    public GuildSlice<V> of(K key) {
        int numDivisor = Objects.hashCode(key);
        if (numDivisor == 0) return new GuildSlice<>(this, key, Lists.newArrayList());

        List<V> subList = Lists.newArrayList();
        for (V val: backingList) {
            if ((Objects.hash(key, val)/ numDivisor) == 0)
                subList.add(val);
        }

        return new GuildSlice<>(this, key, subList);
    }
}

