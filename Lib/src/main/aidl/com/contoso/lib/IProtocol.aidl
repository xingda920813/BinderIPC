// IProtocol.aidl
package com.contoso.lib;

// Declare any non-default types here with import statements
import android.os.Bundle;

import com.contoso.lib.Person;
import com.contoso.lib.IGetPackageSizeCallback;

interface IProtocol {

    String basicTypes(int i, String s, CharSequence cs, in int[] a, inout List<String> l, in Map m);

    // Not allowed
    // void basicType(List<int> listA, List<Integer> listB, Map<String, String> m, ArrayList<String> listC);

    Person parcelables(in Bundle b, in Person p);

    // Binders / Callbacks
    void getPackageSize(String pkgName, IGetPackageSizeCallback callback);

    void throwIllegalArgumentException();

    void throwRuntimeException();

    void throwIOException();
}
