/**
 */
package org.eclipse.epsilon.modiff.munidiff;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.epsilon.modiff.munidiff.MunidiffFactory
 * @model kind="package"
 * @generated
 */
public interface MunidiffPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "munidiff";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "munidiff";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "munidiff";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MunidiffPackage eINSTANCE = org.eclipse.epsilon.modiff.munidiff.impl.MunidiffPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.epsilon.modiff.munidiff.impl.MunidiffImpl <em>Munidiff</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.epsilon.modiff.munidiff.impl.MunidiffImpl
	 * @see org.eclipse.epsilon.modiff.munidiff.impl.MunidiffPackageImpl#getMunidiff()
	 * @generated
	 */
	int MUNIDIFF = 0;

	/**
	 * The feature id for the '<em><b>From Model File</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MUNIDIFF__FROM_MODEL_FILE = 0;

	/**
	 * The feature id for the '<em><b>To Model File</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MUNIDIFF__TO_MODEL_FILE = 1;

	/**
	 * The feature id for the '<em><b>Differences</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MUNIDIFF__DIFFERENCES = 2;

	/**
	 * The number of structural features of the '<em>Munidiff</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MUNIDIFF_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Munidiff</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MUNIDIFF_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.epsilon.modiff.munidiff.impl.DifferenceImpl <em>Difference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.epsilon.modiff.munidiff.impl.DifferenceImpl
	 * @see org.eclipse.epsilon.modiff.munidiff.impl.MunidiffPackageImpl#getDifference()
	 * @generated
	 */
	int DIFFERENCE = 1;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE__IDENTIFIER = 0;

	/**
	 * The number of structural features of the '<em>Difference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Difference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIFFERENCE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.epsilon.modiff.munidiff.impl.AddedElementImpl <em>Added Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.epsilon.modiff.munidiff.impl.AddedElementImpl
	 * @see org.eclipse.epsilon.modiff.munidiff.impl.MunidiffPackageImpl#getAddedElement()
	 * @generated
	 */
	int ADDED_ELEMENT = 2;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADDED_ELEMENT__IDENTIFIER = DIFFERENCE__IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADDED_ELEMENT__ELEMENT = DIFFERENCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Added Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADDED_ELEMENT_FEATURE_COUNT = DIFFERENCE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Added Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ADDED_ELEMENT_OPERATION_COUNT = DIFFERENCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.epsilon.modiff.munidiff.impl.RemovedElementImpl <em>Removed Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.epsilon.modiff.munidiff.impl.RemovedElementImpl
	 * @see org.eclipse.epsilon.modiff.munidiff.impl.MunidiffPackageImpl#getRemovedElement()
	 * @generated
	 */
	int REMOVED_ELEMENT = 3;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVED_ELEMENT__IDENTIFIER = DIFFERENCE__IDENTIFIER;

	/**
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVED_ELEMENT__ELEMENT = DIFFERENCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Removed Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVED_ELEMENT_FEATURE_COUNT = DIFFERENCE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Removed Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVED_ELEMENT_OPERATION_COUNT = DIFFERENCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.epsilon.modiff.munidiff.impl.ChangedElementImpl <em>Changed Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.epsilon.modiff.munidiff.impl.ChangedElementImpl
	 * @see org.eclipse.epsilon.modiff.munidiff.impl.MunidiffPackageImpl#getChangedElement()
	 * @generated
	 */
	int CHANGED_ELEMENT = 4;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGED_ELEMENT__IDENTIFIER = DIFFERENCE__IDENTIFIER;

	/**
	 * The feature id for the '<em><b>From Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGED_ELEMENT__FROM_ELEMENT = DIFFERENCE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>To Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGED_ELEMENT__TO_ELEMENT = DIFFERENCE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Changed Features</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGED_ELEMENT__CHANGED_FEATURES = DIFFERENCE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Changed Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGED_ELEMENT_FEATURE_COUNT = DIFFERENCE_FEATURE_COUNT + 3;

	/**
	 * The operation id for the '<em>Has Differences</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGED_ELEMENT___HAS_DIFFERENCES = DIFFERENCE_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Changed Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGED_ELEMENT_OPERATION_COUNT = DIFFERENCE_OPERATION_COUNT + 1;


	/**
	 * Returns the meta object for class '{@link org.eclipse.epsilon.modiff.munidiff.Munidiff <em>Munidiff</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Munidiff</em>'.
	 * @see org.eclipse.epsilon.modiff.munidiff.Munidiff
	 * @generated
	 */
	EClass getMunidiff();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.epsilon.modiff.munidiff.Munidiff#getFromModelFile <em>From Model File</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>From Model File</em>'.
	 * @see org.eclipse.epsilon.modiff.munidiff.Munidiff#getFromModelFile()
	 * @see #getMunidiff()
	 * @generated
	 */
	EAttribute getMunidiff_FromModelFile();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.epsilon.modiff.munidiff.Munidiff#getToModelFile <em>To Model File</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>To Model File</em>'.
	 * @see org.eclipse.epsilon.modiff.munidiff.Munidiff#getToModelFile()
	 * @see #getMunidiff()
	 * @generated
	 */
	EAttribute getMunidiff_ToModelFile();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.epsilon.modiff.munidiff.Munidiff#getDifferences <em>Differences</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Differences</em>'.
	 * @see org.eclipse.epsilon.modiff.munidiff.Munidiff#getDifferences()
	 * @see #getMunidiff()
	 * @generated
	 */
	EReference getMunidiff_Differences();

	/**
	 * Returns the meta object for class '{@link org.eclipse.epsilon.modiff.munidiff.Difference <em>Difference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Difference</em>'.
	 * @see org.eclipse.epsilon.modiff.munidiff.Difference
	 * @generated
	 */
	EClass getDifference();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.epsilon.modiff.munidiff.Difference#getIdentifier <em>Identifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Identifier</em>'.
	 * @see org.eclipse.epsilon.modiff.munidiff.Difference#getIdentifier()
	 * @see #getDifference()
	 * @generated
	 */
	EAttribute getDifference_Identifier();

	/**
	 * Returns the meta object for class '{@link org.eclipse.epsilon.modiff.munidiff.AddedElement <em>Added Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Added Element</em>'.
	 * @see org.eclipse.epsilon.modiff.munidiff.AddedElement
	 * @generated
	 */
	EClass getAddedElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.epsilon.modiff.munidiff.AddedElement#getElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Element</em>'.
	 * @see org.eclipse.epsilon.modiff.munidiff.AddedElement#getElement()
	 * @see #getAddedElement()
	 * @generated
	 */
	EReference getAddedElement_Element();

	/**
	 * Returns the meta object for class '{@link org.eclipse.epsilon.modiff.munidiff.RemovedElement <em>Removed Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Removed Element</em>'.
	 * @see org.eclipse.epsilon.modiff.munidiff.RemovedElement
	 * @generated
	 */
	EClass getRemovedElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.epsilon.modiff.munidiff.RemovedElement#getElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Element</em>'.
	 * @see org.eclipse.epsilon.modiff.munidiff.RemovedElement#getElement()
	 * @see #getRemovedElement()
	 * @generated
	 */
	EReference getRemovedElement_Element();

	/**
	 * Returns the meta object for class '{@link org.eclipse.epsilon.modiff.munidiff.ChangedElement <em>Changed Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Changed Element</em>'.
	 * @see org.eclipse.epsilon.modiff.munidiff.ChangedElement
	 * @generated
	 */
	EClass getChangedElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.epsilon.modiff.munidiff.ChangedElement#getFromElement <em>From Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>From Element</em>'.
	 * @see org.eclipse.epsilon.modiff.munidiff.ChangedElement#getFromElement()
	 * @see #getChangedElement()
	 * @generated
	 */
	EReference getChangedElement_FromElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.epsilon.modiff.munidiff.ChangedElement#getToElement <em>To Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>To Element</em>'.
	 * @see org.eclipse.epsilon.modiff.munidiff.ChangedElement#getToElement()
	 * @see #getChangedElement()
	 * @generated
	 */
	EReference getChangedElement_ToElement();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.epsilon.modiff.munidiff.ChangedElement#getChangedFeatures <em>Changed Features</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Changed Features</em>'.
	 * @see org.eclipse.epsilon.modiff.munidiff.ChangedElement#getChangedFeatures()
	 * @see #getChangedElement()
	 * @generated
	 */
	EReference getChangedElement_ChangedFeatures();

	/**
	 * Returns the meta object for the '{@link org.eclipse.epsilon.modiff.munidiff.ChangedElement#hasDifferences() <em>Has Differences</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Has Differences</em>' operation.
	 * @see org.eclipse.epsilon.modiff.munidiff.ChangedElement#hasDifferences()
	 * @generated
	 */
	EOperation getChangedElement__HasDifferences();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MunidiffFactory getMunidiffFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.epsilon.modiff.munidiff.impl.MunidiffImpl <em>Munidiff</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.epsilon.modiff.munidiff.impl.MunidiffImpl
		 * @see org.eclipse.epsilon.modiff.munidiff.impl.MunidiffPackageImpl#getMunidiff()
		 * @generated
		 */
		EClass MUNIDIFF = eINSTANCE.getMunidiff();

		/**
		 * The meta object literal for the '<em><b>From Model File</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MUNIDIFF__FROM_MODEL_FILE = eINSTANCE.getMunidiff_FromModelFile();

		/**
		 * The meta object literal for the '<em><b>To Model File</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MUNIDIFF__TO_MODEL_FILE = eINSTANCE.getMunidiff_ToModelFile();

		/**
		 * The meta object literal for the '<em><b>Differences</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MUNIDIFF__DIFFERENCES = eINSTANCE.getMunidiff_Differences();

		/**
		 * The meta object literal for the '{@link org.eclipse.epsilon.modiff.munidiff.impl.DifferenceImpl <em>Difference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.epsilon.modiff.munidiff.impl.DifferenceImpl
		 * @see org.eclipse.epsilon.modiff.munidiff.impl.MunidiffPackageImpl#getDifference()
		 * @generated
		 */
		EClass DIFFERENCE = eINSTANCE.getDifference();

		/**
		 * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIFFERENCE__IDENTIFIER = eINSTANCE.getDifference_Identifier();

		/**
		 * The meta object literal for the '{@link org.eclipse.epsilon.modiff.munidiff.impl.AddedElementImpl <em>Added Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.epsilon.modiff.munidiff.impl.AddedElementImpl
		 * @see org.eclipse.epsilon.modiff.munidiff.impl.MunidiffPackageImpl#getAddedElement()
		 * @generated
		 */
		EClass ADDED_ELEMENT = eINSTANCE.getAddedElement();

		/**
		 * The meta object literal for the '<em><b>Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ADDED_ELEMENT__ELEMENT = eINSTANCE.getAddedElement_Element();

		/**
		 * The meta object literal for the '{@link org.eclipse.epsilon.modiff.munidiff.impl.RemovedElementImpl <em>Removed Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.epsilon.modiff.munidiff.impl.RemovedElementImpl
		 * @see org.eclipse.epsilon.modiff.munidiff.impl.MunidiffPackageImpl#getRemovedElement()
		 * @generated
		 */
		EClass REMOVED_ELEMENT = eINSTANCE.getRemovedElement();

		/**
		 * The meta object literal for the '<em><b>Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REMOVED_ELEMENT__ELEMENT = eINSTANCE.getRemovedElement_Element();

		/**
		 * The meta object literal for the '{@link org.eclipse.epsilon.modiff.munidiff.impl.ChangedElementImpl <em>Changed Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.epsilon.modiff.munidiff.impl.ChangedElementImpl
		 * @see org.eclipse.epsilon.modiff.munidiff.impl.MunidiffPackageImpl#getChangedElement()
		 * @generated
		 */
		EClass CHANGED_ELEMENT = eINSTANCE.getChangedElement();

		/**
		 * The meta object literal for the '<em><b>From Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGED_ELEMENT__FROM_ELEMENT = eINSTANCE.getChangedElement_FromElement();

		/**
		 * The meta object literal for the '<em><b>To Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGED_ELEMENT__TO_ELEMENT = eINSTANCE.getChangedElement_ToElement();

		/**
		 * The meta object literal for the '<em><b>Changed Features</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGED_ELEMENT__CHANGED_FEATURES = eINSTANCE.getChangedElement_ChangedFeatures();

		/**
		 * The meta object literal for the '<em><b>Has Differences</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CHANGED_ELEMENT___HAS_DIFFERENCES = eINSTANCE.getChangedElement__HasDifferences();

	}

} //MunidiffPackage
