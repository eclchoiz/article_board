package board.service;

import board.mode.Article;

public class WritingRequest {
    private String writeName;
    private String password;
    private String title;
    private String content;

    public String getWriteName() {
        return writeName;
    }

    public void setWriteName(String writeName) {
        this.writeName = writeName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public Article toArticle() {
        Article article = new Article();
        article.setWriteName(writeName);
        article.setPassword(password);
        article.setTitle(title);
        article.setContent(content);

        return article;
    }
}
