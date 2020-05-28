package com.eric.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定一个经过编码的字符串，返回它解码后的字符串。
 *
 * 编码规则为: k[encoded_string]，表示其中方括号内部的 encoded_string 正好重复 k 次。注意 k 保证为正整数。
 *
 * 你可以认为输入字符串总是有效的；输入字符串中没有额外的空格，且输入的方括号总是符合格式要求的。
 *
 * 此外，你可以认为原始数据不包含数字，所有的数字只表示重复的次数 k ，例如不会出现像 3a 或 2[4] 的输入。
 *
 * 示例:
 *
 * s = "3[a]2[bc]", 返回 "aaabcbc".
 * s = "3[a2[c]]", 返回 "accaccacc".
 * s = "2[abc]3[cd]ef", 返回 "abcabccdcdcdef".
 * 通过次数41,174提交次数78,927
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/decode-string
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class DecodeString394 {

    /**
     * 一个方括号表示一个节点
     * 例如：3[a2[c]]
     * 可以写成：3[[a]2[c]]
     * 树形结构为：
     *           3[[a]2[c]]
     *              /    \
     *            [a]   2[cc]
     */
    private static class Node{
        /**
         * 树的子节点
         */
        private List<Node> childList;

        /**
         * 当前节点解码后的字符串
         */
        private String decodedStr = "";

        /**
         * 系数
         * 左括号前的数字
         */
        private int coefficient;

        /**
         * 对当前节点解码
         * @return
         */
        private String decodeSelf(){
            StringBuffer sb = new StringBuffer();
            if (childList != null){
                //如果有子节点，递归解析子节点
                StringBuffer subSb = new StringBuffer();
                for (Node n : childList){
                    if (n != null){
                        subSb.append(n.decodeSelf());
                    }
                }
                //乘以系数
                if (coefficient > 0 && subSb.length() > 0){
                    for (int i=0;i<coefficient;i++){
                        sb.append(subSb);
                    }
                }
            }else{
                //如果没有子节点，解析自己
                if (decodedStr != null && decodedStr.length() > 0){
                    if (coefficient > 0){
                        for (int i=0;i<coefficient;i++){
                            sb.append(decodedStr);
                        }
                    }else{
                        sb.append(decodedStr);
                    }
                }else{
                    sb.append("");
                }
            }
            return sb.toString();
        }
    }

    /**
     * 把字符串解析成一棵树
     * @param undecodedStr
     * @return
     */
    private static Node parseString(String undecodedStr){
        Node rootNode = new Node();

        return rootNode;
    }

    public static void main(String[] args){
//        testNode();
        String s = "3[a2[c]]";
        Node root = parseString(s);
        System.out.println("result: " + root.decodeSelf());
    }

    /**
     * 测试Node类
     */
    private static void testNode(){
        Node n = new Node();
        n.decodedStr = "a";
        n.coefficient = 3;

        Node n1 = new Node();
        n1.decodedStr = "b";
        n1.coefficient = 2;
        Node n12 = new Node();
        n12.decodedStr = "d";
        n12.coefficient = 2;
        n1.childList = new ArrayList<>();
        n1.childList.add(n12);

        Node n11 = new Node();
        n11.decodedStr = "c";
        n11.coefficient = 3;

        n.childList = new ArrayList<>();
        n.childList.add(n1);
        n.childList.add(n11);

        System.out.println("result: " + n.decodeSelf());
    }
}
