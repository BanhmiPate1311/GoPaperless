package vn.mobileid.paperless.entity;

public class SignPosition {
    private String pageNumber;
    private Position position;
    private String pos;
    private int x1;
    private int y1;
    private int height = 39;
    private int width = 135;
    private int distance = 3;

    public SignPosition(String page, int count, int pageHeight) {
        this.pageNumber = page;
//        position = new Position(count, pageHeight);
        this.x1 = (width + distance) * (count % 4) + distance;
        this.y1 = (pageHeight - distance - height) - (height + distance) * (count / 4);
        this.pos = x1 + "," + y1 + "," + (x1 + width) + "," + (y1 + height);
    }

    public SignPosition(String page, int top, int left, int width, int height, int pageHeight) {
        this.pageNumber = page;
        this.pos = left + "," + (pageHeight - top - height) + "," + (left + width) + "," + (pageHeight - top);

    }

    public SignPosition(String page, int top, int left, int pageHeight) {
        this.pageNumber = page;
        this.x1 = left;
        this.y1 = (pageHeight - top - height);
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
