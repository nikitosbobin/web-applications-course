package server;

public class TemplateModel {
    private String title;
    private String indexActiveClass;
    private String individualActiveClass;
    private String tablesActiveClass;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIndexActiveClass() {
        return indexActiveClass;
    }

    public void setIndexActiveClass(String indexActiveClass) {
        this.indexActiveClass = indexActiveClass;
    }

    public String getIndividualActiveClass() {
        return individualActiveClass;
    }

    public void setIndividualActiveClass(String individualActiveClass) {
        this.individualActiveClass = individualActiveClass;
    }

    public String getTablesActiveClass() {
        return tablesActiveClass;
    }

    public void setTablesActiveClass(String tablesActiveClass) {
        this.tablesActiveClass = tablesActiveClass;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
