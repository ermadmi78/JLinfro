package com.github.linfro.core.reflection;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-20
 * @since 1.0.0
 */
public class TestBean {
    private int readOnlyInt = 5;

    public int getReadOnlyInt() {
        return readOnlyInt;
    }

    //**************************************************************************

    public int preferGetter = 7;
    public boolean getterInvoked = false;

    public int getPreferGetter() {
        getterInvoked = true;
        return preferGetter;
    }

    //**************************************************************************

    public int preferSetter = 9;
    public boolean setterInvoked = false;

    public void setPreferSetter(int preferSetter) {
        setterInvoked = true;
        this.preferSetter = preferSetter;
    }

    //**************************************************************************

    public String preferMethods = "pm";
    public boolean preferMethodsGetterInvoked = false;
    public boolean preferMethodsSetterInvoked = false;

    public String getPreferMethods() {
        preferMethodsGetterInvoked = true;
        return preferMethods;
    }

    public void setPreferMethods(String preferMethods) {
        preferMethodsSetterInvoked = true;
        this.preferMethods = preferMethods;
    }

    //**************************************************************************

    public int invalidGetter = 15;
    public boolean invalidGetterInvoked = false;

    public Integer getInvalidGetter() {
        invalidGetterInvoked = true;
        return invalidGetter;
    }

    //**************************************************************************

    public int invalidSetter = 17;
    public boolean invalidSetterInvoked = false;

    public int setInvalidSetter(int invalidSetter) {
        invalidSetterInvoked = true;
        this.invalidSetter = invalidSetter;
        return this.invalidSetter;
    }
}
