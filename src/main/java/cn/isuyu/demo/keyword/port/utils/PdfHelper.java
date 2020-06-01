package cn.isuyu.demo.keyword.port.utils;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import vos.KeyVO;
import vos.WordVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @GitHub https://github.com/niezhiliang
 * @Date 2020/6/1 上午11:54
 */
public class PdfHelper {


    /**
     * 获取符合关键字首字符的坐标
     * @param key
     * @param path
     * @return
     * @throws IOException
     */
    public static List<KeyVO> getWordPort(String key, String path) throws IOException {
        //用来获取pdf中每一页的字符
        List<List<WordVO>> allWordsList = Collections.synchronizedList(new ArrayList());
        //获取符合关键字的结果
        List<KeyVO> keyVOS = new ArrayList<>();

        PdfReader pdfReader = new PdfReader(path);
        PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(pdfReader);
        //将字符串分割为单个字符数组
        String [] keys = key.trim().split("");

        for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
            //获取每一页的字符集
            CustomerRenderListener customerRenderListener = new CustomerRenderListener(i);
            pdfReaderContentParser.processContent(i,customerRenderListener);
            //每一页的字符以及字符坐标
            List<WordVO> wordVOS = customerRenderListener.getWordVOS();
            allWordsList.add(wordVOS);
        }

        List<WordVO> filterList = new ArrayList<>();
        //筛选出每页符合当前关键词首个字符的元素
        for (int i = 0; i < allWordsList.size(); i++) {
            filterList.addAll(allWordsList.get(i).stream().filter(word -> word.getWord().equals(keys[0])).collect(Collectors.toList()));
        }

        for (WordVO wordVO : filterList) {
            for (int i = 1; i< keys.length; i++) {
                List<WordVO> pageWordVO =  allWordsList.get(wordVO.getPageNo()-1);
                //如果第二个字符不是我们想要的 直接跳过
                if (!keys[i].equals(pageWordVO.get(wordVO.getIndex()+i).getWord())) {
                    break;
                }
                //表示最后一个字符都符合了，表示已经符合我我们给出的关键字标准
                if (i == keys.length -1) {
                    //打印出第一个字符
                    System.out.println(wordVO);
                    //打印出剩余字符
                    for (int j = 1; j < keys.length; j++) {
                        System.out.println(pageWordVO.get(wordVO.getIndex()+j));
                    }

                    KeyVO keyVO = new KeyVO();
                    keyVO.setPageNo(wordVO.getPageNo());
                    keyVO.setX(wordVO.getX());
                    keyVO.setY(wordVO.getY());

                    keyVOS.add(keyVO);
                }
            }
        }
        //filterList.forEach(wordVO -> System.out.println(wordVO));
        return keyVOS;
    }
}
