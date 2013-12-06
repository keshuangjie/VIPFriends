package com.android.volley.Compatible.mutilpart.content;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @since 4.0
 */
public interface ContentBody extends ContentDescriptor {

	String getFilename();

	void writeTo(OutputStream out) throws IOException;

}
