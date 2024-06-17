package org.eclipse.epsilon.modiff.test;

import org.eclipse.epsilon.modiff.test.ecore.ModiffEcoreTest;
import org.eclipse.epsilon.modiff.test.emfcompare.req.ModiffReqComputingTest;
import org.eclipse.epsilon.modiff.test.repairshop.ModiffRepairShopTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		ModiffReqComputingTest.class,
		ModiffRepairShopTest.class,
		ModiffEcoreTest.class
})
public class ModiffAllTests {
}
