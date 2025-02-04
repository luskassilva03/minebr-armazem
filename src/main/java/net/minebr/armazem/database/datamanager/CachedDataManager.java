package net.minebr.armazem.database.datamanager;


import net.minebr.armazem.database.Keyable;
import net.minebr.armazem.database.datasource.AbstractDataSource;
import net.minebr.armazem.database.util.Utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CachedDataManager<K, V extends Keyable<K>> extends GenericDataManager<K, V> {

    private final Map<K, V> cache;

    public CachedDataManager(AbstractDataSource abstractDataSource, String tableName, Class<V> vClass) {
        super(abstractDataSource, tableName, vClass);
        this.cache = new HashMap<>();
    }

    public CachedDataManager(AbstractDataSource abstractDataSource, String tableName, Class<V> vClass, Function<K, String> keyAdapter) {
        super(abstractDataSource, tableName, vClass, keyAdapter);
        this.cache = new HashMap<>();
    }

    public void cache(V value) {
        this.cache.put(value.getKey(), value);
    }

    public void uncache(K key) {
        this.cache.remove(key);
    }

    public V getCached(K key) {
        return this.cache.get(key);
    }

    public boolean isCached(K key) {
        return this.cache.containsKey(key);
    }

    public void saveCached(boolean async) {
        this.cache.forEach((key, value) -> super.insert(value, async));
    }

    public Collection<V> getCached() {
        return cache.values();
    }

    public Collection<V> getNonCached() {
        List<V> values = super.findAll();
        values.removeIf(value -> cache.containsKey(value.getKey()));
        return values;
    }

    public Collection<V> getAll() {
        Collection<V> cached = getCached();
        Collection<V> nonCached = getNonCached();
        return Stream.of(cached, nonCached).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public V load(K key, Supplier<V> supplier) {
        if (isCached(key)) return getCached(key);
        else if (exists(key)) {
            V v = find(key);
            cache(v);
            return v;
        } else {
            V v = supplier.get();
            cache(v);
            return v;
        }
    }

    public void loadAll() {
        Utils.measureTime(() -> {
            Collection<V> all = getNonCached();
            all.forEach(this::cache);
            return "Carregado " + all.size() + " objetos em {time}";
        });
    }
}
