package com.kob.botrunningsystem.service.impl.utils;

import com.kob.botrunningsystem.utils.BotInterface;
import org.joor.Reflect;

import java.util.UUID;

public class Consumer extends Thread {
    private Bot bot;
    public void startTimeout(long timeout,Bot bot){
        this.bot=bot;
        this.start();//启动当前线程

        try {
            this.join(timeout);//run函数执行完了或者最多等待timeout秒后会继续执行这步之后的操作。
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            this.interrupt();//中断当前线程
        }
    }

//    辅助函数:在code中的Bot类名后添加uid
    private String addUid(String code,String uid){
        int k=code.indexOf(" implements com.kob.botrunningsystem.utils.BotInterface");
        return code.substring(0, k)+uid+code.substring(k);
    }

    @Override
    public void run() {//动态编译代码
        UUID uuid = UUID.randomUUID();
        String uid=uuid.toString().substring(0,8);

        BotInterface botInterface= Reflect.compile(//动态编译前端传来的代码
                "com.kob.botrunningsystem.utils.Bot" + uid,
//                "package com.kob.botrunningsystem.utils;\n" +
//                        "\n" +
//                        "public class Bot implements com.kob.botrunningsystem.utils.BotInterface{\n" +
//                        "\n" +
//                        "    @Override\n" +
//                        "    public Integer nextMove(String input) {\n" +
//                        "        return 0;\n" +
//                        "    }\n" +
//                        "}\n"
                addUid(bot.getBotCode(),uid)
        ).create().get();

        Integer direction=botInterface.nextMove(bot.getInput());

        System.out.println("move-direction: "+bot.getUserId()+" "+direction);//动态的调用编译后的结果
    }
}
