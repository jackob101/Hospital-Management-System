package com.jackob101.hms.integrationstests.api.data;

import java.util.List;

public interface DataGenerator<T> {

    List<T> generate(int amount);

    T generateSingle();
}
