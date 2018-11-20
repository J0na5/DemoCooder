package com.processbyte.landing.i18n;

import com.google.common.net.InternetDomainName;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Locale;
import java.util.Optional;

public class TLDLocaleResolver extends AbstractLocaleResolver {

    /**
     * - DO NOT USE -
     * A method to set the locale to a given locale.
     * The current locale resolver (based on TLD) does not support locales to be set, an
     * UnsupportedOperationException will be thrown.
     *
     * @param httpServletRequest HttpServletRequest, the servlet request
     * @param httpServletResponse HttpServletResponse, the servlet response
     * @param locale Locale, the new locale
     */
    @Override
    public void setLocale(@NotNull HttpServletRequest httpServletRequest,
                          HttpServletResponse httpServletResponse,
                          Locale locale) {
        throw new UnsupportedOperationException("Cannot change tld locale - use a different locale resolution strategy");
    }

    /**
     * Determines the locale that needs to be set depending on the top level domain in the server request.
     * If no locale could be determined the default value (English - US) will be used.
     *
     * @param httpServletRequest HttpServletRequest, the servlet request
     *
     * @return Locale the locale based on the tld
     */
    @NotNull
    @Override
    public Locale resolveLocale(@NotNull HttpServletRequest httpServletRequest) {
        final String serverName = httpServletRequest.getServerName();
        String localeString;

        try {
            final String tld = InternetDomainName.from(serverName).topPrivateDomain().registrySuffix().toString();
            localeString = getLanguageByTld(tld);
        } catch (IllegalStateException e) {
            localeString = "en_US";
        }

        return Optional.ofNullable(StringUtils.parseLocaleString(localeString))
                .orElse(Locale.ENGLISH);
    }

    /**
     * Maps the top level domain to a String specifying the corresponding locale.
     *
     * @param tld String, the top level domain
     *
     * @return String the string representing the locale that corresponds to the given tld.
     */
    @NotNull
    private String getLanguageByTld(String tld) {
        switch (tld) {
            case "be":
                return "nl_BE";
            case "eu":
                return "en_GB";
            default:
                return "en_US";
        }
    }
}