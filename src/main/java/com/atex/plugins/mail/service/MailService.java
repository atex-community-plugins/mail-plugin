package com.atex.plugins.mail.service;

/**
 * Mail service to be used to send emails server side.
 *
 * @author mnova
 */
public interface MailService {

    /**
     * Tell you if the mail service is enabled or not.
     * Never call the "send" methods if this return false otherwise
     * an exception may be thrown instead of the email being sent.
     *
     * @return true if the mail service has been enabled.
     */
    boolean isEnabled();

    /**
     * Validate, through a regexp, if a mail address contains invalid characters.
     * It cannot, in anyway, tell you if the email is really valid.
     *
     * @param mailAddress a not null mail address.
     * @return true if the mail address is valid.
     */
    boolean validateMailAddress(final String mailAddress);

    /**
     * Utility method to send mail.
     *
     * @param from
     *      from mail address
     * @param to
     *      comma separated email addresses
     * @param subject
     *      email subject
     * @param content
     *      email content
     * @param useHtml
     *      send in html or as plain text
     *
     * @return the id of the sent message
     *
     * @throws EmailException
     * if the mail could not be sent
     */
    String send(final String from, final String to, final String subject, final String content, final boolean useHtml)
            throws EmailException;

    /**
     * Utility method to send mail, the from mail address will automatically filled.
     *
     * @param to
     *      comma separated email addresses
     * @param subject
     *      email subject
     * @param content
     *      email content
     * @param useHtml
     *      send in html or as plain text
     *
     * @return the id of the sent message
     *
     * @throws EmailException
     * if the mail could not be sent
     */
    String send(final String to, final String subject, final String content, final boolean useHtml)
            throws EmailException;
}
