package org.braun.digikam.web.component.common;

import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author mbraun
 */
public class XmlWriter {

    private final Writer writer;
    private boolean isOpenElement;
    
    public XmlWriter(Writer writer) {
        this.writer = writer;
    }
    
    public void startElement(String name) throws IOException {
        if (isOpenElement) {
            writer.write(">");
        }
        writer.write("<");
        writer.write(name);
        isOpenElement = true;
    }
    
    public void writeAttribute(String name, String value) throws IOException {
        writer.append(" ").append(name).append("=\"");
        escapeCharacters(value);
        writer.write("\"");
    }
    
    public void writeAttribute(String name, int value) throws IOException {
        writer.append(" ").append(name).append("=\"");
        escapeCharacters(String.valueOf(value));
        writer.write("\"");
    }
    
    public void writeAttribute(String name, double value) throws IOException {
        writer.append(" ").append(name).append("=\"");
        escapeCharacters(String.valueOf((int) Math.round(value)));
        writer.write("\"");
    }
    
    public void endElement(String name) throws IOException {
        if (isOpenElement) {
            writer.write("/>");
        } else {
            writer.write("</");
            writer.write(name);
            writer.write(">");
        }
        isOpenElement = false;
    }
    
    public void characters(String value) throws IOException {
        if (isOpenElement) {
            writer.write(">");
        }
        isOpenElement = false;
        if (null != value) {
            escapeCharacters(value);
        }
    }
    
    private void escapeCharacters(String value) throws IOException {
        for (char c : value.toCharArray()) {
            switch (c) {
                case '<' -> writer.write("&lt;");
                case '>' -> writer.write("&gt;");
                case '&' -> writer.write("&amp;");
                default -> writer.write(c);
            }
        }
    }

}
