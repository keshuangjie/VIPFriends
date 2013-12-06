/**
 * BunnyRene 
 *com.android.volley.Compatible
 ***
 *下午9:01:21
 */
package com.android.volley.Compatible;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.android.volley.Compatible.mutilpart.HttpMultipartMode;
import com.android.volley.Compatible.mutilpart.MultipartEntity;

public class ProgressMultipartEntity extends MultipartEntity {

	private final ProgressListener listener;

	public ProgressMultipartEntity(final ProgressListener listener) {
		// super();
		this.listener = listener;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.apache.http.entity.mime.MultipartEntity#getContentType()
	 */
	/*
	 * （非 Javadoc）
	 * 
	 * @see org.apache.http.entity.mime.MultipartEntity#getContent()
	 */
	@Override
	public InputStream getContent() throws IOException,
			UnsupportedOperationException {
		// TODO 自动生成的方法存根
		return null;

	}

	public ProgressMultipartEntity(final HttpMultipartMode mode,
			final ProgressListener listener) {

		super(mode);
		this.listener = listener;
	}

	public ProgressMultipartEntity(HttpMultipartMode mode,
			final String boundary, final Charset charset,
			final ProgressListener listener) {
		super(mode, boundary, charset);
		this.listener = listener;
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		super.writeTo(new CountingOutputStream(outstream, this.listener));
	}

	public static interface ProgressListener {
		void transferred(long num);
	}

	public static class CountingOutputStream extends FilterOutputStream {

		private final ProgressListener listener;
		private long transferred;

		public CountingOutputStream(final OutputStream out,
				final ProgressListener listener) {
			super(out);
			this.listener = listener;
			this.transferred = 0;
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			out.write(b, off, len);
			this.transferred += len;
			this.listener.transferred(this.transferred);
		}

		@Override
		public void write(int b) throws IOException {
			out.write(b);
			this.transferred++;
			this.listener.transferred(this.transferred);
		}
	}

}
