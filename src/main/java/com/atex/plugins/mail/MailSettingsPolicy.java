package com.atex.plugins.mail;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.atex.plugins.baseline.policy.BaselinePolicy;
import com.polopoly.cm.app.policy.BooleanValuePolicy;
import com.polopoly.cm.client.CMException;

/**
 * MailSettingsPolicy
 *
 * @author mnova
 */
public class MailSettingsPolicy extends BaselinePolicy {

    private static final Logger LOGGER = Logger.getLogger(MailSettingsPolicy.class.getName());

    private static final String ENABLED = "enabled";
    private static final String DEFAULT_SOCKET_TIMEOUT = "30000";
    private static final String DEFAULT_CONNECT_TIMEOUT = "5000";
    private static final String DEFAULT_SMTP_PORT = "25";

    public boolean isEnabled() {
        return getCheckboxValue(ENABLED, false);
    }

    public String getSMTPServerHostname() {
        return getChildValue("smtpServerHostname", null);
    }

    public int getSMTPServerPort() {
        return Integer.parseInt(getChildValue("smtpServerPort", DEFAULT_SMTP_PORT));
    }

    public String getSMTPServerUsername() {
        return getChildValue("smtpServerUsername", null);
    }

    public String getSMTPServerPassword() {
        return getChildValue("smtpServerPassword", null);
    }

    public int getSMTPServerSocketTimeout() {
        return Integer.parseInt(getChildValue("smtpServerSocketTimeout", DEFAULT_SOCKET_TIMEOUT));
    }

    public int getSMTPServerConnectionTimeout() {
        return Integer.parseInt(getChildValue("smtpServerConnectionTimeout", DEFAULT_CONNECT_TIMEOUT));
    }

    public boolean useTLS() {
        return getCheckboxValue("smtpServerTLS", false);
    }

    public boolean useSSL() {
        return getCheckboxValue("smtpServerSSL", false);
    }

    public boolean isDebugEnabled() {
        return getCheckboxValue("smtpServerDebug", false);
    }

    public String getDefaultFromAddress() {
        return getChildValue("defaultFromAddress", null);
    }

    public String getValidateRe() {
        return getChildValue("validateRe", null);
    }

    private boolean getCheckboxValue(final String name, final boolean defaultValue) {
        try {
            return ((BooleanValuePolicy) getChildPolicy(name)).getBooleanValue();
        } catch (CMException exception) {
            logger.log(Level.SEVERE, "Couldn't read child policy with name '" + name + "'", exception);
        }
        return defaultValue;
    }

}
