package nij.ikovsky.bn.model;

public class Category implements MusicItem {

    private final String name;
    private final String id;

    public Category(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String description() {
        return this.name + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (!name.equals(category.name)) return false;
        return id.equals(category.id);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Category{" +
               "name='" + name + '\'' +
               ", id='" + id + '\'' +
               '}';
    }
}
