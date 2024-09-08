package net.minebr.armazem.settings;

import lombok.Getter;

@Getter
public class BonusesConfig {
    private final String display;
    private final String permission;
    private final double bonus;

    public BonusesConfig(String display, String permission, double bonus) {
        this.display = display;
        this.permission = permission;
        this.bonus = bonus;
    }

}
