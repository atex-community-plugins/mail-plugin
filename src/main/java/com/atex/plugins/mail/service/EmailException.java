package com.atex.plugins.mail.service;

/**
 * Exception throwed by {@link MailService}.
 *
 * @author mnova
 */
public class EmailException extends Exception {

    private static final long serialVersionUID = 1L;
    /**
     * Constructs a new <code>EmailException</code> with no specific message.
     */
    public EmailException()
    {
        super();
    }
    /**
     * Constructs a new <code>EmailException</code> with specified detail
     * message.
     *
     * @param msg the error message.
     */
    public EmailException(final String msg)
    {
        super(msg);
    }
    /**
     * Constructs a new <code>EmailException</code> with specified nested
     * <code>Throwable</code> root cause.
     *
     * @param rootCause the exception or error that caused this exception
     * to be thrown.
     */
    public EmailException(final Throwable rootCause)
    {
        super(rootCause);
    }
    /**
     * Constructs a new <code>EmailException</code> with specified detail
     * message and nested <code>Throwable</code> root cause.
     *
     * @param msg the error message.
     * @param rootCause the exception or error that caused this exception
     * to be thrown.
     */
    public EmailException(final String msg, final Throwable rootCause)
    {
        super(msg, rootCause);
    }
}
