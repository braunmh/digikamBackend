package org.braun.digikam.backend.ejb;

import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

/**
 *
 * @author mbraun
 */
public class CommonTransaction {

    private final boolean isUserTransaction;
    
    private final UserTransaction userTransaction;
    private final EntityTransaction entityTransaction;
    
    public CommonTransaction(UserTransaction transaction) {
        isUserTransaction = true;
        this.userTransaction = transaction;
        entityTransaction = null;
    }
    public CommonTransaction(EntityTransaction transaction) {
        isUserTransaction = false;
        this.entityTransaction = transaction; 
        userTransaction = null;
    }
    
    /**
     * Create a new transaction and associate it with the current thread.
     *
     * @exception NotSupportedException Thrown if the thread is already
     *    associated with a transaction and the Transaction Manager
     *    implementation does not support nested transactions.
     *
     * @exception SystemException Thrown if the transaction manager
     *    encounters an unexpected error condition.
     *
     */
    public void begin() throws NotSupportedException, SystemException {
        if (isUserTransaction) 
            userTransaction.begin();
        else 
            entityTransaction.begin();
    }

    /**
     * Complete the transaction associated with the current thread. When this
     * method completes, the thread is no longer associated with a transaction.
     *
     * @exception RollbackException Thrown to indicate that
     *    the transaction has been rolled back rather than committed.
     *
     * @exception HeuristicMixedException Thrown to indicate that a heuristic
     *    decision was made and that some relevant updates have been committed
     *    while others have been rolled back.
     *
     * @exception HeuristicRollbackException Thrown to indicate that a
     *    heuristic decision was made and that all relevant updates have been
     *    rolled back.
     *
     * @exception SecurityException Thrown to indicate that the thread is
     *    not allowed to commit the transaction.
     *
     * @exception IllegalStateException Thrown if the current thread is
     *    not associated with a transaction.
     *
     * @exception SystemException Thrown if the transaction manager
     *    encounters an unexpected error condition.
    */
    public void commit() throws RollbackException,
	HeuristicMixedException, HeuristicRollbackException, SecurityException,
	IllegalStateException, SystemException {
        if (isUserTransaction) 
            userTransaction.commit();
        else
            entityTransaction.commit();
    }

    /**
     * Roll back the transaction associated with the current thread. When this
     * method completes, the thread is no longer associated with a transaction.
     *
     * @exception SecurityException Thrown to indicate that the thread is
     *    not allowed to roll back the transaction.
     *
     * @exception IllegalStateException Thrown if the current thread is
     *    not associated with a transaction.
     *
     * @exception SystemException Thrown if the transaction manager
     *    encounters an unexpected error condition.
     *
     */
    public void rollback() throws IllegalStateException, SecurityException,
        SystemException {
        if (isUserTransaction) 
            userTransaction.rollback();
        else 
            entityTransaction.rollback();
    }

    /**
     * Modify the transaction associated with the current thread such that
     * the only possible outcome of the transaction is to roll back the
     * transaction.
     *
     * @exception IllegalStateException Thrown if the current thread is
     *    not associated with a transaction.
     *
     * @exception SystemException Thrown if the transaction manager
     *    encounters an unexpected error condition.
     *
     */
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        if (isUserTransaction) 
            userTransaction.setRollbackOnly();
        else 
            entityTransaction.setRollbackOnly();
    }

    /**
     * Obtain the status of the transaction associated with the current thread.
     *
     * @return The transaction status. If no transaction is associated with
     *    the current thread, this method returns the Status.NoTransaction
     *    value.
     *
     * @exception SystemException Thrown if the transaction manager
     *    encounters an unexpected error condition.
     *
     */
    public int getStatus() throws SystemException {
        return (isUserTransaction) ? userTransaction.getStatus() : 0;
    }

    /**
     * Modify the timeout value that is associated with transactions started
     * by the current thread with the begin method.
     *
     * <p> If an application has not called this method, the transaction
     * service uses some default value for the transaction timeout.
     *
     * @param seconds The value of the timeout in seconds. If the value is zero,
     *        the transaction service restores the default value. If the value
     *        is negative a SystemException is thrown.
     *
     * @exception SystemException Thrown if the transaction manager
     *    encounters an unexpected error condition.
     *
     */
    public void setTransactionTimeout(int seconds) throws SystemException {
        if (isUserTransaction) {
            userTransaction.setTransactionTimeout(seconds);
        }
    }
     /**
      * Indicate whether a resource transaction is in progress.
      * @return boolean indicating whether transaction is
      *         in progress
      * @throws PersistenceException if an unexpected error 
      *         condition is encountered
      */
     public boolean isActive() throws PersistenceException {
         return (isUserTransaction) ? true : entityTransaction.isActive();
     }
}
