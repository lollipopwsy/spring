package com.kob.backend.leetcode;

import java.util.*;

public class Orange {
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // 四方向

    public int orangesRotting(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int fresh = 0;
        List<int[]> q = new ArrayList<>();

        for(int i = 0; i < m; i ++ ){
            for(int j = 0; j < n; j ++){
                if(grid[i][j] == 1){
                    fresh++;
                }else if(grid[i][j] == 2){
                    q.add(new int[]{i, j});
                }
            }
        }

        if(fresh == 0){
            return 0;
        }
        if(q.isEmpty()){
            return -1;
        }
        int ans = 0;
        while(fresh > 0 && !q.isEmpty()) {
            ans ++;
            List<int[]> tmp = q;
            q = new ArrayList<>();
            for(int[] pos : tmp){
                for(int[] dir : DIRECTIONS){
                    int x = pos[0] + dir[0];
                    int y = pos[1] + dir[1];
                    if(0 <= x && x < m && 0 <= y && y < n && grid[x][y] == 1){
                        fresh --;
                        grid[x][y] = 2;
                        q.add(new int[]{x, y});
                    }
                }
            }
        }
        return fresh > 0 ? -1 : ans;
    }

    public static void main(String[] args) {
        Orange solution = new Orange();

        // 测试用例1：正常情况
        int[][] grid1 = {
                {2, 1, 1},
                {1, 1, 0},
                {0, 1, 1}
        };
        System.out.println("测试用例1 - 预期输出4: " + solution.orangesRotting(grid1));

        // 测试用例2：已经没有新鲜橘子
        int[][] grid2 = {
                {2, 2, 2},
                {2, 2, 0},
                {0, 2, 2}
        };
        System.out.println("测试用例2 - 预期输出0: " + solution.orangesRotting(grid2));

        // 测试用例3：无法使所有橘子腐烂
        int[][] grid3 = {
                {2, 1, 1},
                {0, 1, 1},
                {1, 0, 1}
        };
        System.out.println("测试用例3 - 预期输出-1: " + solution.orangesRotting(grid3));

        // 测试用例5：只有一个新鲜橘子
        int[][] grid5 = {{1}};
        System.out.println("测试用例5 - 预期输出-1: " + solution.orangesRotting(grid5));
    }
}