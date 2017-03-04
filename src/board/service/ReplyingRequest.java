package board.service;

public class ReplyingRequest extends WritingRequest{
    private int parentArticledId;

    public int getParentArticledId() {
        return parentArticledId;
    }

    public void setParentArticledId(int parentArticledId) {
        this.parentArticledId = parentArticledId;
    }
}
