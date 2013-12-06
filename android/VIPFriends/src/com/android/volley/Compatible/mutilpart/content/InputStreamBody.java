package com.android.volley.Compatible.mutilpart.content;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;

import com.android.volley.Compatible.mutilpart.MIME;

/**
 * @since 4.0
 */
public class InputStreamBody extends AbstractContentBody {

	private final InputStream in;
	private final String filename;
	private final long length;

	public InputStreamBody(final InputStream in, long length,
			final String mimeType, final String filename) {
		super(mimeType);
		if (in == null) {
			throw new IllegalArgumentException("Input stream may not be null");
		}
		this.in = in;
		this.filename = filename;
		this.length = length;
	}

	public InputStreamBody(final InputStream in, long length,
			final String filename) {
		this(in, length, "application/octet-stream", filename);
	}

	public InputStreamBody(final InputStream in, long length) {
		this(in, length, "application/octet-stream", "no_name");
	}

	public InputStream getInputStream() {
		return this.in;
	}

	@Override
	public void writeTo(final OutputStream out) throws IOException {
		if (out == null) {
			throw new IllegalArgumentException("Output stream may not be null");
		}
		try {
			byte[] tmp = new byte[4096];
			int l;
			while ((l = this.in.read(tmp)) != -1) {
				out.write(tmp, 0, l);

			}
			out.flush();
		} finally {
			if (in != null) {

				in.close();

			}

		}
	}

	@Override
	public String getTransferEncoding() {
		return MIME.ENC_BINARY;
	}

	@Override
	public String getCharset() {
		return null;
	}

	@Override
	public long getContentLength() {
		return this.length;
	}

	@Override
	public String getFilename() {
		return this.filename;
	}

}
