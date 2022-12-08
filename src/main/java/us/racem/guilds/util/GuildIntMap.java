package us.racem.guilds.util;

public class GuildIntMap{
    private static final int FREE_KEY = 0;
    public static final int NO_VALUE = 0;

    private int[] m_data;

    private boolean m_hasFreeKey;
    private int m_freeValue;

    private final float m_fillFactor;
    private int m_threshold;
    private int m_size;

    private int m_mask;
    private int m_mask2;

    public GuildIntMap(int size) {
        this(size, 0.5f);
    }

    public GuildIntMap(int size, float fillFactor) {
        if (fillFactor <= 0 || fillFactor >= 1)
            throw new IllegalArgumentException("FillFactor must be in (0, 1)");
        if (size <= 0)
            throw new IllegalArgumentException("Size must be positive!");
        int capacity = GuildUtils.arraySize(size, fillFactor);
        m_mask = capacity - 1;
        m_mask2 = capacity * 2 - 1;
        m_fillFactor = fillFactor;

        m_data = new int[capacity * 2];
        m_threshold = (int) (capacity * fillFactor);
    }

    public int get(int key) {
        int ptr = (GuildUtils.phiMix(key) & m_mask) << 1;

        if (key == FREE_KEY)
            return m_hasFreeKey ? m_freeValue : NO_VALUE;

        int k = m_data[ptr];
        if (k == FREE_KEY)
            return NO_VALUE;
        if (k == key)
            return m_data[ptr + 1];

        while (true) {
            ptr = (ptr + 2) & m_mask2;
            k = m_data[ptr];
            if (k == FREE_KEY)
                return NO_VALUE;
            if (k == key)
                return m_data[ptr + 1];
        }
    }

    public int put(int key, int value) {
        if (key == FREE_KEY) {
            int ret = m_freeValue;
            if (!m_hasFreeKey)
                ++m_size;
            m_hasFreeKey = true;
            m_freeValue = value;
            return ret;
        }

        int ptr = (GuildUtils.phiMix(key) & m_mask) << 1;
        int k = m_data[ptr];
        if (k == FREE_KEY) {
            m_data[ptr] = key;
            m_data[ptr + 1] = value;
            if (m_size >= m_threshold)
                rehash(m_data.length * 2);
            else
                ++m_size;
            return NO_VALUE;
        } else if (k == key) {
            int ret = m_data[ptr + 1];
            m_data[ptr + 1] = value;
            return ret;
        }

        while (true) {
            ptr = (ptr + 2) & m_mask2;
            k = m_data[ptr];
            if (k == FREE_KEY) {
                m_data[ptr] = key;
                m_data[ptr + 1] = value;
                if (m_size >= m_threshold)
                    rehash(m_data.length * 2 );
                else
                    ++m_size;
                return NO_VALUE;
            } else if (k == key) {
                int ret = m_data[ptr + 1];
                m_data[ptr + 1] = value;
                return ret;
            }
        }
    }

    public int remove(int key) {
        if (key == FREE_KEY) {
            if (!m_hasFreeKey)
                return NO_VALUE;
            m_hasFreeKey = false;
            --m_size;
            return m_freeValue;
        }

        int ptr = (GuildUtils.phiMix(key) & m_mask) << 1;
        int k = m_data[ptr];
        if (k == key) {
            int res = m_data[ptr + 1];
            shiftKeys(ptr);
            --m_size;
            return res;
        } else if (k == FREE_KEY)
            return NO_VALUE;

        while (true) {
            ptr = (ptr + 2) & m_mask2;
            k = m_data[ptr];
            if (k == key) {
                int res = m_data[ptr + 1];
                shiftKeys(ptr);
                --m_size;
                return res;
            } else if (k == FREE_KEY)
                return NO_VALUE;
        }
    }

    private int shiftKeys(int pos) {
        int last, slot;
        int k;
        int[] data = this.m_data;
        while (true) {
            pos = ((last = pos) + 2) & m_mask2;
            while (true) {
                if ((k = data[pos]) == FREE_KEY) {
                    data[last] = FREE_KEY;
                    return last;
                }
                slot = (GuildUtils.phiMix(k) & m_mask) << 1;
                if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) break;
                pos = (pos + 2) & m_mask2;
            }
            data[last] = k;
            data[last + 1] = data[pos + 1];
        }
    }


    public int size() {
        return m_size;
    }

    private void rehash(int newCapacity){
        m_threshold = (int) (newCapacity/2 * m_fillFactor);
        m_mask = newCapacity/2 - 1;
        m_mask2 = newCapacity - 1;

        int oldCapacity = m_data.length;
        int[] oldData = m_data;

        m_data = new int[newCapacity];
        m_size = m_hasFreeKey ? 1 : 0;

        for (int i = 0; i < oldCapacity; i += 2) {
            int oldKey = oldData[i];
            if(oldKey != FREE_KEY)
                put(oldKey, oldData[i + 1]);
        }
    }
}
