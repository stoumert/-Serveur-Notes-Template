package fr.univ_amu.fr.formation.m1info.gla.storage;

import fr.univ_amu.fr.formation.m1info.gla.data.ClonableWithNewID;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Generic version of the "In Memory" DAO
 * @param <T> a DAO class (usually a record)
 * @param <F> the identifier factory
 * @param <K> the class of the index element
 */
public class InMemoryDAO<K, F extends IdentifierFactory<K>, T extends Identifiable<K> & ClonableWithNewID<K, T>>
        implements DAO<K,T> {

    private final List<T> elements;

    private final F identifierFactory;

    public InMemoryDAO(F identifierFactory) {
        this.elements = new ArrayList<>();
        this.identifierFactory = identifierFactory;
    }

    @Override
    public Optional<T> get(K key) {
        return elements.stream().filter(s -> s.id() == key).findFirst();
    }

    @Override
    public List<T> getAll() {
        return List.copyOf(elements);
    }

    @Override
    public K create(T element) {
        K id = identifierFactory.next();
        T newElement = element.clone(id);
        elements.add(newElement);
        return id;
    }

    @Override
    public void update(T element) {
        elements.removeIf(s -> s.id() == element.id());
        elements.add(element);
    }

    @Override
    public void delete(T element) {
        elements.removeIf(s -> s.id() == element.id());
    }

}
