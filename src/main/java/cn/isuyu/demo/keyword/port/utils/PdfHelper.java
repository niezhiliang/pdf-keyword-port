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
        PdfReader pdfReader = new PdfReader(path);
        PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(pdfReader);

        for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
            //获取每一页的字符集
            CustomerRenderListener customerRenderListener = new CustomerRenderListener(i);
            pdfReaderContentParser.processContent(i,customerRenderListener);
            //每一页的字符以及字符坐标
            List<WordVO> wordVOS = customerRenderListener.getWordVOS();
            allWordsList.add(wordVOS);
        }
        //将字符串分割为单个字符数组
        String [] keys = new String[]{key};
        //直接将关键词去匹配

        //获取符合关键字的结果
        List<KeyVO> keyVOS = getPort(keys,allWordsList);

        //没匹配到分割字符去匹配
        if(keyVOS.size() < 1) {
            keyVOS = getPort(key.split(""),allWordsList);
        }
        return keyVOS;

    }

    /**
     * 因为test2.pdf读到的不是单个字符 可能会读到多个字符
     * 所以将这一块剥离出来，如果不将关键词分割成单个字符
     * 就能找到关键字位置，就直接返回，如果找不到 就分割成单个
     * 字符来找
     * @param keys
     * @param allWordsList
     * @return
     */
    private static List getPort(String [] keys,List<List<WordVO>> allWordsList) {
        List<WordVO> filterList = new ArrayList<>();
        //筛选出每页符合当前关键词首个字符的元素
        for (int i = 0; i < allWordsList.size(); i++) {
            //本来这里判读使用equase方法的，但是由于可能读出多个字符其中可能包含了关键字，所以就用了contains
            //如果有空格 直接复制内容去匹配，匹配会有问题
            filterList.addAll(allWordsList.get(i).stream().filter(word -> word.getWord().replaceAll("\\s*", "")
                    .contains(keys[0].replaceAll("\\s*", ""))).collect(Collectors.toList()));

        }
        for (int i = 0; i < allWordsList.size(); i++) {
            //从文档里面复制带有空格的直接来匹配 匹配不到，所以我们把空格去掉
        }

        //获取符合关键字的结果
        List<KeyVO> keyVOS = new ArrayList<>();

        for (WordVO wordVO : filterList) {
            //如果关键字长度只有1，表示未分割就去匹配，如果匹配到了直接返回结果就行
            if  (keys.length == 1) {
                KeyVO keyVO = new KeyVO();
                keyVO.setPageNo(wordVO.getPageNo());
                keyVO.setX(wordVO.getX());
                keyVO.setY(wordVO.getY());
                keyVOS.add(keyVO);
                System.out.println(wordVO);
            } else {
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
        }
        return keyVOS;
    }
}
