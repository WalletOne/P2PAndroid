package com.walletone.p2p.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anton on 13.09.2017.
 */

public enum UserTypeId {
    EMPLOYER(1),
    FREELANCER(2),
    UNDEFINED(0);

    private final Integer id;

    private static Map<Integer, UserTypeId> map = new HashMap<>();

    static {
        for (UserTypeId typeId : values()) {
            map.put(typeId.getId(), typeId);
        }
    }

    UserTypeId(Integer id) {
        this.id = id;
    }

    public static UserTypeId getUserTypeById(Integer id) {
        UserTypeId result = map.get(id);
        return result == null ? UNDEFINED : result;
    }

    public Integer getId() {
        return id;
    }
}
