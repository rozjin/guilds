package us.racem.guilds.sponge.persist.types;

import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2i;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

public class Vector2IType implements UserType {
    public static final Vector2IType INSTANCE = new Vector2IType();

    @Override
    public int[] sqlTypes() {
        return new int[] { Types.INTEGER, Types.INTEGER };
    }

    @Override
    public Class returnedClass() {
        return Vector2i.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names,
                              SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        int x = rs.getInt(names[0]);
        if (rs.wasNull()) return null;

        int z = rs.getInt(names[1]);

        return new Vector2i(x, z);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value,
                            int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (Objects.isNull(value)) {
            st.setNull(index, Types.INTEGER);
            st.setNull(index + 1, Types.INTEGER);
        } else {
            Vector2i vector = (Vector2i) value;
            st.setInt(index, vector.getX());
            st.setInt(index + 1, vector.getY());
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}
