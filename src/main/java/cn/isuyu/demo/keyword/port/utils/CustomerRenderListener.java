package cn.isuyu.demo.keyword.port.utils;

import com.itextpdf.text.pdf.parser.*;
import vos.WordVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @GitHub https://github.com/niezhiliang
 * @Date 2020/6/1 上午11:55
 */
public class CustomerRenderListener implements RenderListener {

    //线程安全的list
    private List<WordVO> wordVOS = Collections.synchronizedList(new ArrayList(1000));

    private Integer pages;

    public CustomerRenderListener(Integer pages) {
        this.pages = pages;
    }

    public List<WordVO> getWordVOS() {
        return wordVOS;
    }

    public void beginTextBlock() {

    }

    public void renderText(TextRenderInfo textRenderInfo) {
        int index = wordVOS.size();
        String text = textRenderInfo.getText();
        //关键字的起始坐标
        Vector startPoint = textRenderInfo.getBaseline().getStartPoint();
        WordVO wordVO = new WordVO();
        wordVO.setWord(text);
        wordVO.setX(startPoint.get(0));
        wordVO.setY(startPoint.get(1));
        //获取传过来的合同页数
        wordVO.setPageNo(this.pages);
        //设置当前字符在当前页字符集的索引下标
        wordVO.setIndex(index);
        wordVOS.add(wordVO);
    }

    public void endTextBlock() {

    }

    public void renderImage(ImageRenderInfo imageRenderInfo) {

    }
}
