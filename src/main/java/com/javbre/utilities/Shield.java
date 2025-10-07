package com.javbre.utilities;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class Shield {

    private Shield() {
    }

    public static String blindStr(String value) {
        if (Objects.isNull(value)) return null;
        return Jsoup.clean(value, Safelist.none());
    }


}
