package org.eclipse.epsilon.modiff.emfcompare.munidiff.tests;

import org.eclipse.epsilon.modiff.emfcompare.munidiff.tests.emfCompare.req.MunidiffReqTest;
import org.eclipse.epsilon.modiff.emfcompare.munidiff.tests.repairShop.MunidiffRepairShopTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		MunidiffReqTest.class,
		MunidiffRepairShopTest.class
})
public class MunidiffAllTests {

}
