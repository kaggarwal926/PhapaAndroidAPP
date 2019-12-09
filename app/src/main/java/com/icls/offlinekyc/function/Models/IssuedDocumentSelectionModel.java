package com.icls.offlinekyc.function.Models;

public class IssuedDocumentSelectionModel {
    private String DocName;
    private String DocUri;
    private String Doctype;
    private boolean isSelected = false;


    public void setDocName(String docName) {
        DocName = docName;
    }

    public void setDocUri(String docUri) {
        DocUri = docUri;
    }

    public void setDoctype(String doctype) {
        Doctype = doctype;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDocName() {
        return DocName;
    }

    public String getDocUri() {
        return DocUri;
    }

    public String getDoctype() {
        return Doctype;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
