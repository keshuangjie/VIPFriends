package com.android.volley.Compatible.mutilpart.content;

/**
 * @since 4.0
 */
public abstract class AbstractContentBody implements ContentBody {

	private final String mimeType;
	private final String mediaType;
	private final String subType;

	public AbstractContentBody(final String mimeType) {
		super();
		if (mimeType == null) {
			throw new IllegalArgumentException("MIME type may not be null");
		}
		this.mimeType = mimeType;
		int i = mimeType.indexOf('/');
		if (i != -1) {
			this.mediaType = mimeType.substring(0, i);
			this.subType = mimeType.substring(i + 1);
		} else {
			this.mediaType = mimeType;
			this.subType = null;
		}
	}

	@Override
	public String getMimeType() {
		return this.mimeType;
	}

	@Override
	public String getMediaType() {
		return this.mediaType;
	}

	@Override
	public String getSubType() {
		return this.subType;
	}

}
