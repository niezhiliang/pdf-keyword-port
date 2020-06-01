package cn.isuyu.demo.keyword.port;

import cn.isuyu.demo.keyword.port.utils.PdfHelper;

import java.io.IOException;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @GitHub https://github.com/niezhiliang
 * @Date 2020/6/1 上午11:51
 */
public class Application {
    public static void main(String[] args) throws IOException {
        PdfHelper.getWordPort("甲方签字","./data/test.pdf");
    }
}
