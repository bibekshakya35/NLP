package scrape.bike.domain;

/**
 * @author bibek on 12/5/17
 * @project compare&search
 */
public class Brake {
    private BrakeType front;
    private BrakeType rear;

    public BrakeType getFront() {
        return front;
    }

    public void setFront(BrakeType front) {
        this.front = front;
    }

    public BrakeType getRear() {
        return rear;
    }

    public void setRear(BrakeType rear) {
        this.rear = rear;
    }
}
