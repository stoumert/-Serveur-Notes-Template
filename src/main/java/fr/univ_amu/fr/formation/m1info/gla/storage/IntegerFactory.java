package fr.univ_amu.fr.formation.m1info.gla.storage;

public class IntegerFactory implements IdentifierFactory<Integer> {
    private Integer currentId;

    public IntegerFactory() {
        this.currentId = 0;
    }

    public IntegerFactory(Integer currentId) {
        this.currentId = currentId;
    }

    @Override
    public Integer next() {
        this.currentId++;
        return this.currentId;
    }
}
