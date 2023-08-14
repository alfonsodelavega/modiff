package org.eclipse.epsilon.modiff.emfcompare.munidiff.tests;

import org.eclipse.epsilon.modiff.emfcompare.munidiff.tests.emfCompare.req.MunidiffReqTest;
import org.eclipse.epsilon.modiff.emfcompare.munidiff.tests.repairShop.RepairShopTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		MunidiffReqTest.class,
		RepairShopTest.class
})
public class MunidiffTests {

}
