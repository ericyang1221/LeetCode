package com.eric.leetcode;

import java.util.ArrayList;

/**
 * 42. 接雨水
 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
 *
 *  4|
 *  3|              a
 *  2|      a b b b a a b a
 *  1|  a b a a b a a a a a a
 *  0|----------------------------
 *
 *  a表示数组值，b表示水
 *
 * 上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，在这种情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）。 感谢 Marcos 贡献此图。
 *
 * 示例:
 *
 * 输入: [0,1,0,2,1,0,1,3,2,1,2,1]
 * 输出: 6
 * 通过次数111,758提交次数217,698
 */
public class TrappingRainWater42 {

    public static void main(String[] args) {
        int[] x = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        int ret = 0;
        ArrayList<RetBean> retBeans = new ArrayList<>();
        RetBean retBean = new RetBean();
        retBean.retArrayList = new ArrayList<>();
        retBean.retArrayList.add(x);
        retBeans.add(retBean);

        do {
            retBeans = calculate(retBeans);
            if (retBeans != null) {
                for (RetBean rb : retBeans) {
                    if (rb != null) {
                        ret = ret + rb.retWater;
                    }
                }
            }
        } while (retBeans != null);

        System.out.println(ret);
    }

    /**
     * 递归计算储水量
     * @param retBeans 输入数组
     * @return RetBean 储存递归结果
     */
    public static ArrayList<RetBean> calculate(ArrayList<RetBean> retBeans) {
        if (retBeans == null || retBeans.size() <= 0) {
            return null;
        }
        ArrayList<RetBean> retArrayList = new ArrayList<>();
        for (RetBean b : retBeans) {
            if (b != null && b.retArrayList.size() > 0) {
                for (int[] argArray : b.retArrayList) {
                    BlockBean bean = findOneBlock(argArray);
                    //无法计算
                    if (bean == null) {
                        return null;
                    }
                    //计算左边新数组
                    int[] leftArray = null;
                    if (bean.leftIndex > 0) {
                        int[] tmpArray = new int[bean.leftIndex + 1];
                        System.arraycopy(argArray, 0, tmpArray, 0, tmpArray.length);
                        leftArray = tmpArray;
                    }
                    //计算右边新数组
                    int[] rightArray = null;
                    if (bean.rightIndex < argArray.length - 1) {
                        int[] tmpArray = new int[argArray.length - bean.rightIndex];
                        System.arraycopy(argArray, bean.rightIndex, tmpArray, 0, tmpArray.length);
                        rightArray = tmpArray;
                    }
                    //赋值RetBean
                    RetBean retBean = null;
                    if (leftArray != null || rightArray != null || bean.waters > 0) {
                        retBean = new RetBean();
                        retBean.retArrayList = new ArrayList<>();
                        if (leftArray != null) {
                            retBean.retArrayList.add(leftArray);
                        }
                        if (rightArray != null) {
                            retBean.retArrayList.add(rightArray);
                        }
                        retBean.retWater = bean.waters;
                        retArrayList.add(retBean);
                    }
                }
            }

        }
        return retArrayList;
    }

    /**
     * 储存递归结果
     */
    public static class RetBean {
        //block计算后还剩余的数组
        public ArrayList<int[]> retArrayList;
        //block计算后的储水量
        public int retWater;
    }

    /**
     * 找出数组中砖头高度的最大与次大的值，记录index
     * 这两个值index中间夹的就是可储水区域，
     * 储水值等于储水区域总值减去砖头数量
     * 本函数，输出一个区域的储水值与砖头墙的index
     *
     * @param inputIntArray 砖头描述数组
     * @return BlockBean 描述一个区域的储水值与砖头墙的index
     */
    public static BlockBean findOneBlock(int[] inputIntArray) {
        //输入参数非法
        if (inputIntArray == null || inputIntArray.length < 2) {
            return null;
        }
        //如果砖头墙相邻，无法储水
        if (inputIntArray.length == 2) {
            return new BlockBean(0, inputIntArray[0], 1, inputIntArray[1], 0);
        }
        //寻找数组中的最大与次大值
        int max = Integer.MIN_VALUE;
        int maxIndex = -1;
        int secondMax = Integer.MIN_VALUE;
        int secondMaxIndex = -1;
        for (int i = 0 ; i < inputIntArray.length; i++) {
            if (inputIntArray[i] > max) {
                secondMax = max;
                secondMaxIndex = maxIndex;
                max = inputIntArray[i];
                maxIndex = i;
            } else if (inputIntArray[i] > secondMax) {
                secondMax = inputIntArray[i];
                secondMaxIndex = i;
            }
        }
        //校验计算值，没有寻找到最大与次大值，返回空
        if (maxIndex < 0 || secondMaxIndex < 0) {
            return null;
        }
        //赋值左右墙index与高度
        BlockBean blockBean = new BlockBean();
        if (maxIndex > secondMaxIndex) {
            blockBean.leftIndex = secondMaxIndex;
            blockBean.leftValue = secondMax;
            blockBean.rightIndex = maxIndex;
            blockBean.rightValue = max;
        } else {
            blockBean.leftIndex = maxIndex;
            blockBean.leftValue = max;
            blockBean.rightIndex = secondMaxIndex;
            blockBean.rightValue = secondMax;
        }
        //计算储水量
        //1. 计算总储水量
        int totalWater = secondMax * (blockBean.rightIndex - blockBean.leftIndex - 1);
        //2. 储水量 = 总储水量 - 中间的砖头墙
        for (int i = blockBean.leftIndex + 1 ; i < blockBean.rightIndex ; i++) {
            totalWater = totalWater - inputIntArray[i];
        }
        blockBean.waters = totalWater;
        return blockBean;
    }

    /**
     * 描述一个区域的储水值与砖头墙的index
     */
    public static class BlockBean {
        //左边砖头墙的inex
        public int leftIndex;
        //左边砖头墙的高度
        public int leftValue;
        //右边砖头墙的index
        public int rightIndex;
        //右边砖头墙的高度
        public int rightValue;
        //block区域的储水量
        public int waters;

        public BlockBean() {
        }

        public BlockBean(int leftIndex, int leftValue, int rightIndex, int rightValue, int waters) {
            this.leftIndex = leftIndex;
            this.leftValue = leftValue;
            this.rightIndex = rightIndex;
            this.rightValue = rightValue;
            this.waters = waters;
        }
    }
}
