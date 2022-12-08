package us.racem.guilds.util;

import java.lang.reflect.TypeVariable;

public class GuildUtils {
    public static long nextPowerOfTwo(long x) {
        if (x == 0) return 1;
        x--;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return ( x | x >> 32 ) + 1;
    }

    public static int arraySize(int expected, float f) {
        final long s = Math.max(2, nextPowerOfTwo((long) Math.ceil(expected / f)));
        if (s > (1 << 30)) throw new IllegalArgumentException();
        return (int) s;
    }

    private static final int INT_PHI = 0x9E3779B9;
    public static int phiMix(int x) {
        final int h = x * INT_PHI;
        return h ^ (h >> 16);
    }

    private static final class __<F> {
        private __() { }
    }

    public static <F> Class<F> to() {
        __<F> instance = new __<F>();
        TypeVariable<?>[] parameters = instance.getClass().getTypeParameters();

        return (Class<F>)parameters[0].getClass();
    }
}
