/**
 */
package org.eclipse.epsilon.modiff.munidiff.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.epsilon.modiff.munidiff.AddedElement;
import org.eclipse.epsilon.modiff.munidiff.ChangedElement;
import org.eclipse.epsilon.modiff.munidiff.Difference;
import org.eclipse.epsilon.modiff.munidiff.Munidiff;
import org.eclipse.epsilon.modiff.munidiff.MunidiffFactory;
import org.eclipse.epsilon.modiff.munidiff.MunidiffPackage;
import org.eclipse.epsilon.modiff.munidiff.RemovedElement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MunidiffPackageImpl extends EPackageImpl implements MunidiffPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass munidiffEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass differenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass addedElementEClass = null;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass removedElementEClass = null;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass changedElementEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.epsilon.modiff.munidiff.MunidiffPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private MunidiffPackageImpl() {
		super(eNS_URI, MunidiffFactory.eINSTANCE);
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link MunidiffPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static MunidiffPackage init() {
		if (isInited) return (MunidiffPackage)EPackage.Registry.INSTANCE.getEPackage(MunidiffPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredMunidiffPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		MunidiffPackageImpl theMunidiffPackage = registeredMunidiffPackage instanceof MunidiffPackageImpl ? (MunidiffPackageImpl)registeredMunidiffPackage : new MunidiffPackageImpl();

		isInited = true;

		// Create package meta-data objects
		theMunidiffPackage.createPackageContents();

		// Initialize created meta-data
		theMunidiffPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theMunidiffPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(MunidiffPackage.eNS_URI, theMunidiffPackage);
		return theMunidiffPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMunidiff() {
		return munidiffEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMunidiff_FromModelFile() {
		return (EAttribute)munidiffEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMunidiff_ToModelFile() {
		return (EAttribute)munidiffEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMunidiff_Differences() {
		return (EReference)munidiffEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDifference() {
		return differenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDifference_Identifier() {
		return (EAttribute)differenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAddedElement() {
		return addedElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getAddedElement_Element() {
		return (EReference)addedElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getRemovedElement() {
		return removedElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRemovedElement_Element() {
		return (EReference)removedElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getChangedElement() {
		return changedElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangedElement_FromElement() {
		return (EReference)changedElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangedElement_ToElement() {
		return (EReference)changedElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChangedElement_ChangedFeatures() {
		return (EReference)changedElementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getChangedElement__HasDifferences() {
		return changedElementEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MunidiffFactory getMunidiffFactory() {
		return (MunidiffFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		munidiffEClass = createEClass(MUNIDIFF);
		createEAttribute(munidiffEClass, MUNIDIFF__FROM_MODEL_FILE);
		createEAttribute(munidiffEClass, MUNIDIFF__TO_MODEL_FILE);
		createEReference(munidiffEClass, MUNIDIFF__DIFFERENCES);

		differenceEClass = createEClass(DIFFERENCE);
		createEAttribute(differenceEClass, DIFFERENCE__IDENTIFIER);

		addedElementEClass = createEClass(ADDED_ELEMENT);
		createEReference(addedElementEClass, ADDED_ELEMENT__ELEMENT);

		removedElementEClass = createEClass(REMOVED_ELEMENT);
		createEReference(removedElementEClass, REMOVED_ELEMENT__ELEMENT);

		changedElementEClass = createEClass(CHANGED_ELEMENT);
		createEReference(changedElementEClass, CHANGED_ELEMENT__FROM_ELEMENT);
		createEReference(changedElementEClass, CHANGED_ELEMENT__TO_ELEMENT);
		createEReference(changedElementEClass, CHANGED_ELEMENT__CHANGED_FEATURES);
		createEOperation(changedElementEClass, CHANGED_ELEMENT___HAS_DIFFERENCES);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		addedElementEClass.getESuperTypes().add(this.getDifference());
		removedElementEClass.getESuperTypes().add(this.getDifference());
		changedElementEClass.getESuperTypes().add(this.getDifference());

		// Initialize classes, features, and operations; add parameters
		initEClass(munidiffEClass, Munidiff.class, "Munidiff", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMunidiff_FromModelFile(), ecorePackage.getEString(), "fromModelFile", null, 1, 1, Munidiff.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMunidiff_ToModelFile(), ecorePackage.getEString(), "toModelFile", null, 1, 1, Munidiff.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMunidiff_Differences(), this.getDifference(), null, "differences", null, 0, -1, Munidiff.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(differenceEClass, Difference.class, "Difference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDifference_Identifier(), ecorePackage.getEString(), "identifier", null, 0, 1, Difference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(addedElementEClass, AddedElement.class, "AddedElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAddedElement_Element(), ecorePackage.getEObject(), null, "element", null, 1, 1, AddedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(removedElementEClass, RemovedElement.class, "RemovedElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRemovedElement_Element(), ecorePackage.getEObject(), null, "element", null, 1, 1, RemovedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changedElementEClass, ChangedElement.class, "ChangedElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChangedElement_FromElement(), ecorePackage.getEObject(), null, "fromElement", null, 1, 1, ChangedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangedElement_ToElement(), ecorePackage.getEObject(), null, "toElement", null, 1, 1, ChangedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChangedElement_ChangedFeatures(), ecorePackage.getEStructuralFeature(), null, "changedFeatures", null, 0, -1, ChangedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getChangedElement__HasDifferences(), ecorePackage.getEBoolean(), "hasDifferences", 0, 1, IS_UNIQUE, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //MunidiffPackageImpl
