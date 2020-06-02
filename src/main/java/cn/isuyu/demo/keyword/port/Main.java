package cn.isuyu.demo.keyword.port;

import cn.isuyu.demo.keyword.port.utils.PdfHelper;

import java.io.IOException;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @GitHub https://github.com/niezhiliang
 * @Date 2020/6/1 上午11:51
 */
public class Main {
    public static void main(String[] args) throws IOException {
        //每次读取的单个字符
        //PdfHelper.getWordPort("甲方签字","./data/test.pdf");
        //这个文档读取的是多个字符
        PdfHelper.getWordPort("message","./data/test2.pdf");
    }
}
