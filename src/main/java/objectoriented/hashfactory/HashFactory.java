package objectoriented.hashfactory;

public class HashFactory {

    public Hash createHash(int size) {
        return new Hash(size);
    }
}
