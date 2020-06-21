package com.eric.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 给定一个经过编码的字符串，返回它解码后的字符串。
 * <p>
 * 编码规则为: k[encoded_string]，表示其中方括号内部的 encoded_string 正好重复 k 次。注意 k 保证为正整数。
 * <p>
 * 你可以认为输入字符串总是有效的；输入字符串中没有额外的空格，且输入的方括号总是符合格式要求的。
 * <p>
 * 此外，你可以认为原始数据不包含数字，所有的数字只表示重复的次数 k ，例如不会出现像 3a 或 2[4] 的输入。
 * <p>
 * 示例:
 * <p>
 * s = "3[a]2[bc]", 返回 "aaabcbc".
 * s = "3[a2[c]]", 返回 "accaccacc".
 * s = "2[abc]3[cd]ef", 返回 "abcabccdcdcdef".
 * 通过次数41,174提交次数78,927
 * <p>
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
     * 3[[a]2[c]]
     * /    \
     * [a]   2[cc]
     */
    private static class Node {
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
        private int coefficient = 1;

        /**
         * 对当前节点解码
         *
         * @return
         */
        private String decodeSelf() {
            StringBuffer sb = new StringBuffer();
            if (childList != null) {
                //如果有子节点，递归解析子节点
                StringBuffer subSb = new StringBuffer();
                for (Node n : childList) {
                    if (n != null) {
                        subSb.append(n.decodeSelf());
                    }
                }
                //乘以系数
                if (coefficient > 0 && subSb.length() > 0) {
                    for (int i = 0; i < coefficient; i++) {
                        sb.append(subSb);
                    }
                }
            } else {
                //如果没有子节点，解析自己
                if (decodedStr != null && decodedStr.length() > 0) {
                    if (coefficient > 0) {
                        for (int i = 0; i < coefficient; i++) {
                            sb.append(decodedStr);
                        }
                    } else {
                        sb.append(decodedStr);
                    }
                } else {
                    sb.append("");
                }
            }
            return sb.toString();
        }
    }

    /**
     * 把字符串解析成一棵树
     *
     * @param undecodedStr
     * @return
     */
    private static Node parseString(String undecodedStr) {
        if (undecodedStr == null || undecodedStr.length() < 1) {
            return null;
        }
        //前后补一组方括号，保证最外层是一个完整的root节点
        if (!undecodedStr.startsWith("[") || !undecodedStr.endsWith("]")) {
            undecodedStr = "[" + undecodedStr + "]";
        }
        Stack<Object> charStack = new Stack<>();
        char[] charArray = undecodedStr.toCharArray();
        //将字符串写入栈
        //每次遇到 "]" 都往前出栈，知道找到 "["，将结果做为一个node写入栈
        //字符串遍历完成后，栈里应该只有一个root节点
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (c != ']') {
                //如果不是右括号，就一直入栈
                charStack.push(c);
            } else {
                StringBuilder sb = new StringBuilder();
                ArrayList<Node> brotherNodes = new ArrayList<>();
                //遇到右括号，就出栈，直到匹配到左括号
                //其中遇到的node节点互为兄弟节点
                while (!(charStack.peek() instanceof Character && (Character) charStack.peek() == '[')) {
                    Object previousObj = charStack.pop();
                    if (previousObj instanceof Character) {
                        //因为是倒序，所以要在最前面插入
                        sb.insert(0, previousObj);
                    } else {
                        //如果是节点的话
                        if (previousObj instanceof Node) {
                            //临时字符串里有值，把临时字符串保存成新的节点
                            Node n = null;
                            if (sb.length() > 0) {
                                n = new Node();
                                n.decodedStr = sb.toString();
                                //清空临时字符串
                                sb.delete(0, sb.length());
                            }
                            if (n != null) {
                                brotherNodes.add(0, n);
                            }
                            brotherNodes.add(0, (Node) previousObj);
                        } else {
                            //栈里处理Character就是Node，代码不可能走到这里
                        }
                    }
                }
                // "["出栈
                charStack.pop();
                //临时字符串写成Node
                if (sb.length() > 0) {
                    Node n = new Node();
                    n.decodedStr = sb.toString();
                    //清空临时字符串
                    sb.delete(0, sb.length());
                    //Node节点写入
                    brotherNodes.add(0, n);
                }
                //生成外层节点
                Node outerNode = new Node();
                outerNode.childList = brotherNodes;
                //解析外层节点系数
                StringBuilder sbNum = new StringBuilder();
                while (charStack.size() > 0 && charStack.peek() instanceof Character && Character.isDigit((Character) charStack.peek())) {
                    Character numChar = (Character) charStack.pop();
                    sbNum.insert(0, numChar);
                }
                if (sbNum.length() > 0) {
                    try {
                        Integer coefficient = Integer.valueOf(sbNum.toString());
                        outerNode.coefficient = coefficient;
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
                //将外层节点入栈
                charStack.push(outerNode);
            }
        }
        Object obj = charStack.pop();
        if (obj instanceof Node) {
            return (Node) obj;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
//        testNode();
        String s = "3[a2[c]]";
        s = "3[a]2[bc]";
        s = "2[abc]3[cd]ef";
        s = "3[z]2[2[y]pq4[2[jk]e1[f]]]ef";
        Node root = parseString(s);
        System.out.println("result: " + root.decodeSelf());
    }

    /**
     * 测试Node类
     */
    private static void testNode() {
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
