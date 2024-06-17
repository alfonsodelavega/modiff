/**
 */
package org.eclipse.epsilon.modiff.munidiff.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.epsilon.modiff.munidiff.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MunidiffFactoryImpl extends EFactoryImpl implements MunidiffFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MunidiffFactory init() {
		try {
			MunidiffFactory theMunidiffFactory = (MunidiffFactory)EPackage.Registry.INSTANCE.getEFactory(MunidiffPackage.eNS_URI);
			if (theMunidiffFactory != null) {
				return theMunidiffFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new MunidiffFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MunidiffFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case MunidiffPackage.MUNIDIFF: return createMunidiff();
			case MunidiffPackage.DIFFERENCE: return createDifference();
			case MunidiffPackage.ADDED_ELEMENT: return createAddedElement();
			case MunidiffPackage.REMOVED_ELEMENT: return createRemovedElement();
			case MunidiffPackage.CHANGED_ELEMENT: return createChangedElement();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Munidiff createMunidiff() {
		MunidiffImpl munidiff = new MunidiffImpl();
		return munidiff;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Difference createDifference() {
		DifferenceImpl difference = new DifferenceImpl();
		return difference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AddedElement createAddedElement() {
		AddedElementImpl addedElement = new AddedElementImpl();
		return addedElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RemovedElement createRemovedElement() {
		RemovedElementImpl removedElement = new RemovedElementImpl();
		return removedElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangedElement createChangedElement() {
		ChangedElementImpl changedElement = new ChangedElementImpl();
		return changedElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MunidiffPackage getMunidiffPackage() {
		return (MunidiffPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static MunidiffPackage getPackage() {
		return MunidiffPackage.eINSTANCE;
	}

} //MunidiffFactoryImpl
