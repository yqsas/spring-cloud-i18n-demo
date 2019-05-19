package com.yqsas.demo.common.i18n;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 自定义语言区域解析，使用Header中自定义属性
 * @author yqsas
 */
public class LocaleHeaderLocaleResolver implements LocaleContextResolver {

    public static final String LOCALE_REQUEST_ATTRIBUTE_NAME = LocaleHeaderLocaleResolver.class.getName() + ".LOCALE";

    public static final String TIME_ZONE_REQUEST_ATTRIBUTE_NAME = LocaleHeaderLocaleResolver.class.getName() + ".TIME_ZONE";

    @Nullable
    private Locale defaultLocale;
    @Nullable
    private TimeZone defaultTimeZone;

    @Nullable
    private String localeHeadName;

    public void setLocaleHeadName(@Nullable String localeHeadName) {
        this.localeHeadName = localeHeadName;
    }

    @Nullable
    public String getLocaleHeadName() {
        return this.localeHeadName;
    }

    public void setDefaultLocale(@Nullable Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    @Nullable
    public Locale getDefaultLocale() {
        return this.defaultLocale;
    }

    public void setDefaultTimeZone(@Nullable TimeZone defaultTimeZone) {
        this.defaultTimeZone = defaultTimeZone;
    }

    @Nullable
    protected TimeZone getDefaultTimeZone() {
        return this.defaultTimeZone;
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        parseLocaleHeaderIfNecessary(request);
        return (Locale) request.getAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME);
    }

    @Override
    public LocaleContext resolveLocaleContext(final HttpServletRequest request) {
        parseLocaleHeaderIfNecessary(request);
        return new TimeZoneAwareLocaleContext() {
            @Override
            @Nullable
            public Locale getLocale() {
                return (Locale) request.getAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME);
            }

            @Override
            @Nullable
            public TimeZone getTimeZone() {
                return (TimeZone) request.getAttribute(TIME_ZONE_REQUEST_ATTRIBUTE_NAME);
            }
        };
    }

    @Override
    public void setLocaleContext(HttpServletRequest request, @Nullable HttpServletResponse response,
                                 @Nullable LocaleContext localeContext) {
        Assert.notNull(response, "HttpServletResponse is required for LocaleHeaderLocaleResolver");

        Locale locale = null;
        TimeZone timeZone = null;
        if (localeContext != null) {
            locale = localeContext.getLocale();
            if (localeContext instanceof TimeZoneAwareLocaleContext) {
                timeZone = ((TimeZoneAwareLocaleContext) localeContext).getTimeZone();
            }
            response.setHeader(getLocaleHeadName(),
                (locale != null ? locale.toString() : "-") + (timeZone != null ? ' ' + timeZone.getID() : ""));
        }
        request.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME,
            (locale != null ? locale : determineDefaultLocale(request)));
        request.setAttribute(TIME_ZONE_REQUEST_ATTRIBUTE_NAME,
            (timeZone != null ? timeZone : determineDefaultTimeZone(request)));
    }

    private void parseLocaleHeaderIfNecessary(HttpServletRequest request) {
        if (request.getAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME) == null) {
            Locale locale = null;
            TimeZone timeZone = null;

            String headName = getLocaleHeadName();
            if (headName != null) {
                String localeStr = request.getHeader(headName);
                if (localeStr != null) {
                    String localePart = localeStr;
                    String timeZonePart = null;
                    int spaceIndex = localePart.indexOf(' ');
                    if (spaceIndex != -1) {
                        localePart = localeStr.substring(0, spaceIndex);
                        timeZonePart = localeStr.substring(spaceIndex + 1);
                    }
                    try {
                        locale = (!"-".equals(localePart) ? parseLocaleValue(localePart) : null);
                        if (timeZonePart != null) {
                            timeZone = StringUtils.parseTimeZoneString(timeZonePart);
                        }
                    } catch (IllegalArgumentException ex) {
                        if (request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE) != null) {
                            // Error dispatch: ignore locale/timezone parse exceptions
                        } else {
                            throw new IllegalStateException("Invalid locale Header '" + getLocaleHeadName() +
                                "' with value [" + localeStr + "]: " + ex.getMessage());
                        }
                    }
                }
            }

            request.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME,
                (locale != null ? locale : determineDefaultLocale(request)));
            request.setAttribute(TIME_ZONE_REQUEST_ATTRIBUTE_NAME,
                (timeZone != null ? timeZone : determineDefaultTimeZone(request)));
        }
    }

    @Nullable
    protected Locale determineDefaultLocale(HttpServletRequest request) {
        Locale defaultLocale = getDefaultLocale();
        if (defaultLocale == null) {
            defaultLocale = request.getLocale();
        }
        return defaultLocale;
    }

    @Nullable
    protected TimeZone determineDefaultTimeZone(HttpServletRequest request) {
        return getDefaultTimeZone();
    }

    @Nullable
    protected Locale parseLocaleValue(String locale) {
        return StringUtils.parseLocaleString(locale);
    }

    @Override
    public void setLocale(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Locale locale) {
        setLocaleContext(request, response, (locale != null ? new SimpleLocaleContext(locale) : null));
    }

}
