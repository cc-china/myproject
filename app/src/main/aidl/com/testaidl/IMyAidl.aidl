// IMyAidl.aidl
package com.testaidl;

// 这个引用类型必须是全类名的才能使用
import com.testaidl.model.Person;

interface IMyAidl {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    void addPerson(in Person person);
    List<Person> getPersonList();
}
