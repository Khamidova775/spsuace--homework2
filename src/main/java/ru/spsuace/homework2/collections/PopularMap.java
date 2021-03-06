package ru.spsuace.homework2.collections;


import java.util.*;


/**
 * Написать структуру данных, реализующую интерфейс мапы + набор дополнительных методов.
 * 2 дополнительных метода должны вовзращать самый популярный ключ и его популярность.
 * Популярность - это количество раз, который этот ключ (или значение) учавствовал в методах мапы containsKey,
 * containsValue, get, put, remove в качестве аргумента или возвращаемого значения.
 * Читайте документацию к методам (ctrl + Q) для понимания, что где возвращается.
 * Считаем, что null я вам не передю ни в качестве ключа, ни в качестве значения
 * <p>
 * Важный момент, вам не надо реализовывать мапу, вы должны использовать композицию.
 * Вы можете использовать любые коллекции, которые есть в java. Перечислю реализации основных типов коллекций:
 * List -> {@link java.util.ArrayList}
 * Set -> {@link java.util.HashSet}
 * Map -> {@link java.util.HashMap}
 * Stack -> {@link java.util.Stack}
 * Queue -> {@link java.util.LinkedList}
 * Deque -> {@link java.util.ArrayDeque}
 * Для быстрого перехода в нужный класс или метод, просто зажмите ctrl и щелкните по нему мышкой, или просто щелкните
 * колесиком. Бывает удобно, когда нужно переходить из одной точки кода в другую
 * <p>
 * Помните, что по мапе тоже можно итерироваться
 * <p>
 * for (Map.Entry<K, V> entry : map.entrySet()) {
 * entry.getKey();
 * entry.getValue();
 * }
 * <p>
 * <p>
 * Полный балл за все: 7
 *
 * @param <K> - тип ключа
 * @param <V> - тип значения
 */
public class PopularMap<K, V> implements Map<K, V> {

    private final Map<K, V> map;
    private Map<K, Integer> keyPopularityMap = new HashMap<>();
    private Map<V, Integer> valuePopularityMap = new HashMap<>();

    public PopularMap() {
        this.map = new HashMap<>();
    }

    public PopularMap(Map<K, V> map) {
        this.map = map;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    private <T> void increasePopularity(Object element, Map<T, Integer> map) {
        if (map.containsKey(element)) {
            map.put((T) element, (int) map.get((T) element) + 1);
        } else {
            map.put((T) element, 1);
        }
    }

    @Override
    public boolean containsKey(Object key) {
        increasePopularity(key, keyPopularityMap);
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        count(value, valuePopularityMap);
        return map.containsValue(value);

    }

    @Override
    public V get(Object key) {
        V value = map.get(key);
        count(key, keyPopularityMap);
        count(value, valuePopularityMap);

        return value;
    }

    @Override
    public V put(K key, V value) {
        V valuePut = map.put(key, value);

        count(value, valuePopularityMap);

        count(valuePut, valuePopularityMap);
        count(key, keyPopularityMap);

        return valuePut;
    }

    @Override
    public V remove(Object key) {
        V value = map.remove(key);
        count(key, keyPopularityMap);
        count(value, valuePopularityMap);
        return value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    private void count(Object obj, Map map) {
        if (obj == null) {
            return;
        }
        if (map.containsKey(obj)) {
            int temp = (int) map.get(obj);
            temp++;
            map.put(obj, temp);
        } else {
            map.put(obj, 1);
        }
    }

    /**
     * Возвращает самый популярный, на данный момент, ключ
     * 1 балл
     */
    private <T> T countMostPopular(Map<T, Integer> map) {
        int maxPopularity = 0;
        T mostPopularKey = null;
        for (Map.Entry<T, Integer> entry : map.entrySet()) {
            if (entry.getValue() >= maxPopularity) {
                mostPopularKey = entry.getKey();
                maxPopularity = entry.getValue();
            }
        }
        return mostPopularKey;
    }

    private <T> int getPopularity(T key, Map<T, Integer> map) {
        Integer keyPopularity = map.get(key);
        if (keyPopularity == null) {
            keyPopularity = 0;
        }
        return keyPopularity;
    }

    public K getPopularKey() {
        return countMostPopular(keyPopularityMap);
    }


    /**
     * Возвращает количество использование ключа
     * 1 балла
     */
    public int getKeyPopularity(K key) {
        return getPopularity(key, keyPopularityMap);
    }

    /**
     * Возвращает самое популярное, на данный момент, значение. Надо учесть что значени может быть более одного
     * 1 балл
     */
    public V getPopularValue() {
        return countMostPopular(valuePopularityMap);
    }

    /**
     * Возвращает количество использований значений в методах: containsValue, get, put (учитывается 2 раза, если
     * старое значение и новое - одно и тоже), remove (считаем по старому значению).
     * 1 балл
     */
    public int getValuePopularity(V value) {
        if (valuePopularityMap.get(value) != null) {
            return valuePopularityMap.get(value);
        }
        return 0;
    }

    /**
     * Вернуть итератор, который итерируется по значениям (от самых НЕ популярных, к самым популярным)
     * 2 балла (Сортировать можно через метод Collections.sort() с использованием Comparator (как с фильтрами)
     */
    public Iterator<V> popularIterator() {
        LinkedHashMap<V, Integer> sortedValuePopularityMap = new LinkedHashMap<>();

        valuePopularityMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> sortedValuePopularityMap.put(x.getKey(), x.getValue()));

        return sortedValuePopularityMap.keySet().iterator();
    }
}