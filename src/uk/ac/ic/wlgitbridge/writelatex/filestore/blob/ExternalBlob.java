package uk.ac.ic.wlgitbridge.writelatex.filestore.blob;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.Response;
import uk.ac.ic.wlgitbridge.writelatex.api.request.exception.FailedConnectionException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Winston on 14/11/14.
 */
public class ExternalBlob extends Blob {

    private Future<byte[]> future;

    public ExternalBlob(String url) throws FailedConnectionException {
        super();
        fetchContents(url);
    }

    @Override
    public byte[] getContents() throws FailedConnectionException {
        try {
            return future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new FailedConnectionException();
        }
    }

    private void fetchContents(String url) throws FailedConnectionException {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        try {
            future = asyncHttpClient.prepareGet(url).execute(new AsyncCompletionHandler<byte[]>() {

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                @Override
                public STATE onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
                    bytes.write(bodyPart.getBodyPartBytes());
                    return STATE.CONTINUE;
                }

                @Override
                public byte[] onCompleted(Response response) throws Exception {
                    return bytes.toByteArray();
                }

            });
        } catch (IOException e) {
            throw new FailedConnectionException();
        }
    }

}
