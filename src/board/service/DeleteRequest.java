package board.service;

/**
 * Created by choiz on 2017-03-04.
 */
public class DeleteRequest {

    private int articleId;
    private String password;

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
