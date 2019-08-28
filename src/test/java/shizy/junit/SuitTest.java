package shizy.junit;

import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import shizy.junit.user.UserTest;


@RunWith(Suite.class)
@SuiteClasses(value = {
        UserTest.class
})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//按名称排序

/**
 * 批量运行junit
 */
public class SuitTest {

}
