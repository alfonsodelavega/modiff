/**
 */
package org.eclipse.epsilon.modiff.munidiff;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Added Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.epsilon.modiff.munidiff.AddedElement#getElement <em>Element</em>}</li>
 * </ul>
 *
 * @see org.eclipse.epsilon.modiff.munidiff.MunidiffPackage#getAddedElement()
 * @model
 * @generated
 */
public interface AddedElement extends Difference {
	/**
	 * Returns the value of the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Element</em>' reference.
	 * @see #setElement(EObject)
	 * @see org.eclipse.epsilon.modiff.munidiff.MunidiffPackage#getAddedElement_Element()
	 * @model required="true"
	 * @generated
	 */
	EObject getElement();

	/**
	 * Sets the value of the '{@link org.eclipse.epsilon.modiff.munidiff.AddedElement#getElement <em>Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Element</em>' reference.
	 * @see #getElement()
	 * @generated
	 */
	void setElement(EObject value);

} // AddedElement
