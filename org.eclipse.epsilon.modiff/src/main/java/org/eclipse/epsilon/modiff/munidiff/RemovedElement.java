/**
 */
package org.eclipse.epsilon.modiff.munidiff;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Removed Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.modiff.munidiff.RemovedElement#getElement <em>Element</em>}</li>
 * </ul>
 *
 * @see org.eclipse.epsilon.modiff.munidiff.MunidiffPackage#getRemovedElement()
 * @model
 * @generated
 */
public interface RemovedElement extends Difference {
	/**
	 * Returns the value of the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Element</em>' reference.
	 * @see #setElement(EObject)
	 * @see org.eclipse.epsilon.modiff.munidiff.MunidiffPackage#getRemovedElement_Element()
	 * @model required="true"
	 * @generated
	 */
	EObject getElement();

	/**
	 * Sets the value of the '{@link org.eclipse.epsilon.modiff.munidiff.RemovedElement#getElement <em>Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Element</em>' reference.
	 * @see #getElement()
	 * @generated
	 */
	void setElement(EObject value);

} // RemovedElement
