package us.racem.guilds.sponge.persist;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class AccessTransaction implements AutoCloseable {
    private Transaction tx;
    private Session ctx;

    private boolean isFinished;
    public AccessTransaction(Session ctx) {
        this.ctx = ctx;
        this.tx = ctx.beginTransaction();
        this.isFinished = false;
    }

    public void commit() {
        this.isFinished = true;
    }

    @Override
    public void close() {
        if (isFinished) {
            tx.commit();
        } else {
            tx.rollback();
        }
    }
}
