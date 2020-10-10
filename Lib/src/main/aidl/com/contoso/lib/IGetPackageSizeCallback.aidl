// IGetPackageSizeCallback.aidl
package com.contoso.lib;

// Declare any non-default types here with import statements

oneway interface IGetPackageSizeCallback {

    void onPackageSizeAcquired(String pkgName, long size);

    // The same effect
    // oneway void onPackageSizeAcquired(String pkgName, long size);
}
