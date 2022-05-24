package testsupport;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.Header;

import java.nio.charset.Charset;

import static com.github.tomakehurst.wiremock.common.HttpClientUtils.getEntityAsByteArrayAndCloseStream;
import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.collect.Iterables.getFirst;

public class WireMockResponse {

    private final ClassicHttpResponse httpResponse;
    private final byte[] content;

    public WireMockResponse(ClassicHttpResponse httpResponse) {
        this.httpResponse = httpResponse;
        content = getEntityAsByteArrayAndCloseStream(httpResponse);
    }

    public int statusCode() {
        return httpResponse.getCode();
    }

    public String content() {
        if (content == null) {
            return null;
        }
        return new String(content, Charset.forName(UTF_8.name()));
    }

    public byte[] binaryContent() {
        return content;
    }

    public String firstHeader(String key) {
        return getFirst(headers().get(key), null);
    }

    public Multimap<String, String> headers() {
        ImmutableListMultimap.Builder<String, String> builder = ImmutableListMultimap.builder();

        for (Header header : httpResponse.getHeaders()) {
            builder.put(header.getName(), header.getValue());
        }

        return builder.build();
    }

    public String statusMessage() {
        return httpResponse.getReasonPhrase();
    }
}
