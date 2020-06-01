### Pdf定位关键词坐标

之前就做过关键词获取坐标的demo,但是有时候会发现有些关键字，文档上面明明存在的，但是通过demo中的代码来获取，
就是获取不大，后面查资料，发现大家都有这个情况，后面在一篇博客中看到个比较好的解决方案，因为
itext 5.x 获取文本的代码`textRenderInfo.getText();`这个只能获取到单个字符的坐标，比如，甲方，如果你去匹配
`甲`这个关键词，是能定位到坐标的，但是如果你同时去获取`甲方`这个关键词就会获取不到，后面在这个博客上面看到既然
能获取到单个字，于是我有了下面这个思路：

**1.** 获取pdf每一页的所有字符，并放到集合中，放入格式为，x（左下角x坐标）: y:（左下角y坐标） pageNo:（合同页数） key（关键字）： index（在当前集合中的索引） 

**2.** 然后将要定位的关键字分割为单个字符的数组，通过第一个字符去获取所有字符集合中，符合key=第一个字符的集合元素，并将其放到一个
新的数组中。

**3.** 然后去遍历第二步得到的集合，然后编辑，再判断需要匹配的第二个字符，如果当前元素的下一个元素的key值不等于我们要匹配的
第二个字符，直接跳过，如果相等，则进行第三个字符匹配，如果都相等则，我们就找到了其中符合的元素，然后再放到一个新的集合当中，
这个集合中的元素就是我们要定位的关键字的坐标的首字符的左下角坐标。

```java
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
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/2020060116253592.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4MDgyMzA0,size_16,color_FFFFFF,t_70)


[源码地址：https://github.com/niezhiliang/pdf-keyword-port](https://github.com/niezhiliang/pdf-keyword-port)

