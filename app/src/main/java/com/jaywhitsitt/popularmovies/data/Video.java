package com.jaywhitsitt.popularmovies.data;

import java.util.HashMap;
import java.util.Map;

public class Video {

    public final String id;
    public final String title;
    public final Site site;
    public final String key;

    public Video(String id, String title, Site site, String key) {
        this.id = id;
        this.title = title;
        this.site = site;
        this.key = key;
    }

    public enum Site {
        UNKNOWN(null),
        YOUTUBE("YouTube");

        private String id;
        Site(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }
        private static final Map<String, Site> lookup = new HashMap<>();
        static {
            for (Site site : Site.values()) {
                lookup.put(site.getId(), site);
            }
        }
        public static Site get(String id) {
            Site site = lookup.get(id);
            return site == null ? UNKNOWN : site;
        }
    }
}
