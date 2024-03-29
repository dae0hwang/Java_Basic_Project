package hashfactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import objectoriented.hashfactory.Hash;
import org.junit.jupiter.api.Test;

class HashTest {

    @Test
        //private getHashCode 테스트
    void testGetHashCode()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        Hash hash = new Hash(10);
        Method method = Hash.class.getDeclaredMethod("getHashCode", String.class);
        method.setAccessible(true);

        //when
        int result = (int) method.invoke(hash, "asdf");
        int actual = 414;
        //then
        assertEquals(result, actual);
    }

    @Test
        //private getindex테스트
    void testGetIndex()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        Hash hash = new Hash(10);
        Method method = Hash.class.getDeclaredMethod("getIndex", int.class);
        method.setAccessible(true);

        //when
        int actual = 4;
        int result = (int) method.invoke(hash, 414);
        //then
        assertEquals(result, actual);
    }

    @Test
    void testGet() {
        //given
        Hash hash = new Hash(10);
        //when
        hash.put("Thread/atomictest", "Thread/atomictest");
        String result = hash.get("Thread/atomictest");
        //then
        assertEquals(result, "Thread/atomictest");
    }

    @Test
        //해시에 값이 없을 때,
    void testGet2() {
        //given
        Hash hash = new Hash(10);
        //when
        hash.put("Thread/atomictest", "Thread/atomictest");
        String result = hash.get("bb");
        //then
        assertEquals(result, "null");
    }

    @Test
        //값을 다르지만, 같은 배열 인덱스에 존재할 때,
    void testGet3() {
        //given
        Hash hash = new Hash(10);
        //when
        //as와 tt getindex값이 갔다.
        hash.put("as", "as");
        hash.put("tt", "tt");
        String result = hash.get("tt");
        //then
        assertEquals(result, "tt");
    }

    @Test
        //동일한 키값을 받아서 덮어쓸 때,
    void testGet4() {
        //given
        Hash hash = new Hash(10);
        //when
        //as와 tt getindex값이 갔다.
        hash.put("Thread/atomictest", "Thread/atomictest");
        hash.put("Thread/atomictest", "ss");
        String result = hash.get("Thread/atomictest");
        //then
        assertEquals(result, "ss");
    }
}