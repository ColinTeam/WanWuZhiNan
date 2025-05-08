// IWebProcessInterface.aidl
package com.colin.library.android.widget;

// Declare any non-default types here with import statements
import com.colin.library.android.widget.ICallbackFromWebProcessInterface;

interface IWebProcessInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void handleWebCommand(String commandName, String params, in ICallbackFromWebProcessInterface callback);
}