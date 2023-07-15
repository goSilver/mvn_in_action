import org.chensh.HelloWorld;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HelloWorldTest {

    /**
     * 一个典型的单元测试包含三个步骤：
     * ① 准备测试类及数据；
     * ② 执行要测试的行为；
     * ③ 检查结果。
     */
    @Test
    public void testSayHello() {
        HelloWorld hw = new HelloWorld();
        System.out.println(hw.sayHello());
        assertEquals("hello maven", hw.sayHello());
    }
}
