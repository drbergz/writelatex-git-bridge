package uk.ac.ic.wlgitbridge.writelatex.filestore;

import org.junit.Test;
import uk.ac.ic.wlgitbridge.writelatex.filestore.store.BlobHash;

public class BlobHashTest {

    private static final String toHash = "\\\\documentclass[a4paper]{article}\\n\\n\\\\usepackage[english]{babel}\\n\\\\usepackage[utf8]{inputenc}\\n\\\\usepackage{graphicx}\\n\\\\usepackage{fullpage}\\n\\\\usepackage{listings}\\n\\\\usepackage{courier}\\n\\\\usepackage{url}\\n\\n\\\\lstset{basicstyle=\\\\ttfamily,breaklines=true}\\n\\n\\\\begin{document}\\n\\\\title{API for the writeLaTeX-Git Bridge}\\n\\\\author{JLM}\\n\\\\date{\\\\today}\\n\\\\maketitle\\n\\n\\\\section{Fetching a Project from WriteLaTeX}\\n\\nThere are three API calls that will likely be of interest. You can run them against this server, \\\\url{radiant-wind-3058.herokuapp.com}, but they're not on the production server yet.\\n\\n\\\\subsection{Get Doc}\\n\\nA ``doc'' is our internal term for a ``project''. At present, this just returns the latest version number.\\n\\n\\\\begin{lstlisting}\\nGET https://.../api/v0/docs/1826rqgsdb\\n# => { latestVerId: 39 }\\n\\\\end{lstlisting}\\n\\n\\\\subsection{Get Saved Vers}\\n\\nA ``saved ver'' is a version of a doc, saved by via the versions menu. Note that this query is not currently paginated.\\n\\n\\\\begin{lstlisting}\\nGET https://.../api/v0/docs/1826rqgsdb/saved_vers\\n# => [\\n  {\\\"versionId\\\":39,\\n   \\\"comment\\\":\\\"with more files\\\",\\n   \\\"user\\\":{\\n     \\\"email\\\":\\\"jdleesmiller@gmail.com\\\",\\n     \\\"name\\\":\\\"John Lees-Miller\\\"},\\n   \\\"createdAt\\\":\\\"2014-11-05T18:02:19Z\\\"},\\n  {\\\"versionId\\\":24,\\n   \\\"comment\\\":\\\"first draft\\\",\\n   \\\"user\\\":{\\n     \\\"email\\\":\\\"jdleesmiller@gmail.com\\\",\\n     \\\"name\\\":\\\"John Lees-Miller\\\"},\\n   \\\"createdAt\\\":\\\"2014-11-05T17:56:58Z\\\"}]\\n\\\\end{lstlisting}\\n\\n\\\\subsection{Get Snapshot for Version}\\n\\nA snapshot contains the content of a project in the given version. You can safely request a snapshot of any version that is, or was at any point in the last 24 hours, (1) a saved version, or (2) the current version. (Older versions may or may not have been moved to cold storage.)\\n\\nThe srcs array contains (content, file name) pairs; the atts array contains (URL, file name) pairs.\\n\\n\\\\begin{lstlisting}\\nGET https://.../api/v0/docs/1826rqgsdb/snapshots/39\\n# => {\\n  \\\"srcs\\\":[\\n    [\\\"This text is from another file.\\\",\\\"foo/bar/test.tex\\\"],\\n    [\\\"\\\\\\\\documentclass[a4paper]{article}\\\\n...\\\",\\\"main.tex\\\"]],\\n  \\\"atts\\\":[\\n    [\\\"https://writelatex-staging.s3.amazonaws.com/filepicker/1ENnu6zJSGyslI3DuNZD_min_mean_wait_evm_7.eps.150dpi.png\\\",\\\"min_mean_wait_evm_7_eps_150dpi.png\\\"]]}\\n\\\\end{lstlisting}\\n\\n\\\\section{Pushing a Project to WriteLaTeX}\\n\\nTODO still working on this part\\n\\n\\\\section{Test Data}\\n\\nYou can use this project as one of your test projects. I've added an attachment and a file in a subfolder to make it a bit more interesting.\\n\\n\\\\input{foo/bar/test}\\n\\n\\\\includegraphics[width=\\\\linewidth]{min_mean_wait_evm_7_eps_150dpi}\\n\\n\\\\end{document}";

    @Test
    public void hashesTheBytesGivenInTheConstructorUsingSha256() {
        BlobHash blobHash = new BlobHash(toHash.getBytes());

    }

}