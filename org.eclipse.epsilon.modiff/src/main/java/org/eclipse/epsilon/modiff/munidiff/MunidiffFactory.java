/**
 */
package org.eclipse.epsilon.modiff.munidiff;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.epsilon.modiff.munidiff.MunidiffPackage
 * @generated
 */
public interface MunidiffFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MunidiffFactory eINSTANCE = org.eclipse.epsilon.modiff.munidiff.impl.MunidiffFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Munidiff</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Munidiff</em>'.
	 * @generated
	 */
	Munidiff createMunidiff();

	/**
	 * Returns a new object of class '<em>Difference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Difference</em>'.
	 * @generated
	 */
	Difference createDifference();

	/**
	 * Returns a new object of class '<em>Added Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Added Element</em>'.
	 * @generated
	 */
	AddedElement createAddedElement();

	/**
	 * Returns a new object of class '<em>Removed Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Removed Element</em>'.
	 * @generated
	 */
	RemovedElement createRemovedElement();

	/**
	 * Returns a new object of class '<em>Changed Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Changed Element</em>'.
	 * @generated
	 */
	ChangedElement createChangedElement();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	MunidiffPackage getMunidiffPackage();

} //MunidiffFactory
