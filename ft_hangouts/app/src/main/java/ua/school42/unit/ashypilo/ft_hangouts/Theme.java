package ua.school42.unit.ashypilo.ft_hangouts;

public enum Theme {
    THEME, FON;

    private boolean theme;
    private boolean fon;

    public void setTheme(boolean theme) {
        this.theme = theme;
    }

    public boolean isTheme() {
        return theme;
    }

    public boolean isFon() {
        return fon;
    }

    public void setFon(boolean fon) {
        this.fon = fon;
    }
}
