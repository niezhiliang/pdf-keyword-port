package vos;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @GitHub https://github.com/niezhiliang
 * @Date 2020/6/1 下午1:58
 */
public class WordVO {

    private String word;

    private Float x;

    private Float y;

    private Integer pageNo;

    private Integer index;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    @Override
    public String toString() {
        return "WordVO{" +
                "word='" + word + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", pageNo=" + pageNo +
                ", index=" + index +
                '}';
    }
}
