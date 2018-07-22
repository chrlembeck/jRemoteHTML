package de.chrlembeck.jremotehtml.core.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class LoggingWriter extends Writer {

    private Writer out;

    private StringWriter logger;

    public LoggingWriter(Writer out) {
        this.out = out;
        this.logger = new StringWriter();
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        logger.write(cbuf, off, len);
        out.write(cbuf, off, len);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }

    @Override
    public String toString() {
        return logger.toString();
    }
}
