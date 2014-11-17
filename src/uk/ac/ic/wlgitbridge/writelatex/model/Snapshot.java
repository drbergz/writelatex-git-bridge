package uk.ac.ic.wlgitbridge.writelatex.model;

import uk.ac.ic.wlgitbridge.writelatex.api.request.getforversion.SnapshotAttachment;
import uk.ac.ic.wlgitbridge.writelatex.api.request.getforversion.SnapshotData;
import uk.ac.ic.wlgitbridge.writelatex.api.request.getforversion.SnapshotFile;
import uk.ac.ic.wlgitbridge.writelatex.api.request.getsavedvers.SnapshotInfo;
import uk.ac.ic.wlgitbridge.writelatex.api.request.getsavedvers.WLUser;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Winston on 03/11/14.
 */
public class Snapshot implements Comparable<Snapshot> {

    private final int versionID;
    private final String comment;
    private final String userName;
    private final String userEmail;

    private final List<SnapshotFile> srcs;
    private final List<SnapshotAttachment> atts;

    public Snapshot(SnapshotInfo info, SnapshotData data) {
        versionID = info.getVersionId();
        comment = info.getComment();
        WLUser user = info.getUser();
        userName = user.getName();
        userEmail = user.getEmail();

        srcs = data.getSrcs();
        atts = data.getAtts();
    }

    public Snapshot(int versionID) {
        this.versionID = versionID;
        comment = "Pushed update";
        userName = "Anonymous";
        userEmail = "anonymous@writelatex.com";
        srcs = new LinkedList<SnapshotFile>();
        atts = new LinkedList<SnapshotAttachment>();
    }

    public int getVersionID() {
        return versionID;
    }

    public String getComment() {
        return comment;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public List<SnapshotFile> getSrcs() {
        return srcs;
    }

    public List<SnapshotAttachment> getAtts() {
        return atts;
    }

    @Override
    public int compareTo(Snapshot snapshot) {
        return Integer.compare(versionID, snapshot.versionID);
    }

    @Override
    public String toString() {
        return String.valueOf(versionID);
    }

}
