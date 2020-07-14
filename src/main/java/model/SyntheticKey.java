package model;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;

public class SyntheticKey {
    @AffinityKeyMapped
    String countryCode;
    int id;

    public SyntheticKey(String countryCode, int id) {
        this.countryCode = countryCode;
        this.id = id;
    }
}
