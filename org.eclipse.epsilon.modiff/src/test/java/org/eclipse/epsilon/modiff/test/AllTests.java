package org.eclipse.epsilon.modiff.test;

import org.eclipse.epsilon.modiff.test.emfcompare.req.ReqComputingTest;
import org.eclipse.epsilon.modiff.test.repairshop.RepairShopTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		ReqComputingTest.class,
		RepairShopTest.class
})
public class AllTests {
}
