package com.camdocscanner.pdfscanner.utils;

import java.io.FileOutputStream;

public interface FileWritingCallback {

    public void write(FileOutputStream out);
}
