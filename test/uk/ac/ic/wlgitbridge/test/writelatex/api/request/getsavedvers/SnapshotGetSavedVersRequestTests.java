package uk.ac.ic.wlgitbridge.test.writelatex.api.request.getsavedvers;

import org.junit.Test;
import uk.ac.ic.wlgitbridge.writelatex.api.request.getsavedvers.SnapshotGetSavedVersRequest;

/**
 * Created by Winston on 06/11/14.
 */
public class SnapshotGetSavedVersRequestTests {

    @Test
    public void nothingToTest() {
        SnapshotGetSavedVersRequest request = new SnapshotGetSavedVersRequest("1826rqgsdb");
        request.request();
        try {
            System.out.println(request.getResult());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
