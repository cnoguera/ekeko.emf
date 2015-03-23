package damp.ekeko.EMF.modelFactories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreAdapterFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;

import damp.ekeko.EkekoModel;
import damp.ekeko.IProjectModel;

public class EMFModel implements IProjectModel {
	
	IProject theproject;
	Collection<IFile> includedModels = new ArrayList<>();
	Collection<EObject> eobjects = new HashSet<>();
	
	public EMFModel(IProject project) {
		theproject = project;
	}

	@Override
	public void clean() {
		System.out.println("clean");
		
	}

	public Collection<EObject> getEObjects(){
		return eobjects;
	}
	
	public void addFileToModel(IFile file){
		includedModels.add(file);
		try {
			populate(null); //XXX UGH!
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void populate(IProgressMonitor monitor) throws CoreException {
		eobjects.clear();
		try {
//			loadIncludedModelsWithEditing();
//			loadIncludedModelsWithFactoryLookup();
			loadIncludedModelsWithResourceSet();
		} catch (IOException e) {
			throw new RuntimeException("couldn't load file", e);
		}
	}
	
	
	
	
	private void loadIncludedModelsWithResourceSet() throws IOException, CoreException {
		ResourceSet rs = new ResourceSetImpl();
		for (IFile iFile : includedModels) {
			Resource resource = rs.getResource(URI.createPlatformResourceURI(
							iFile.getFullPath().toString(), true), true);
			resource.load(null);
			EcoreUtil.resolveAll(resource);
			eobjects.addAll(getAllContents(resource));
			
		}
	}

	

	private void loadIncludedModelsWithFactoryLookup() throws IOException {
		// ResourceSet rs = new ResourceSetImpl();
		for (IFile iFile : includedModels) {
			String fileExtension = iFile.getFileExtension();
			Object x = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().get(fileExtension);
			if (x == null)
				x = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().get("*");
			if (x instanceof Resource.Factory.Descriptor) {
				Resource.Factory.Descriptor desc = (Resource.Factory.Descriptor) x;
				x = desc.createFactory();
			}

			if (x instanceof Resource.Factory) {
				Resource.Factory factory = (Resource.Factory) x;
				Resource resource = factory.createResource(URI.createPlatformResourceURI(
						iFile.getFullPath().toString(), true));
				resource.load(null);
				eobjects.addAll(getAllContents(resource));
			}
		}

		// Resource resource =
		// domain.createResource(iFile.getFullPath().toString());
		// resource.load(null);
	}

	private void loadIncludedModelsWithEditing() throws IOException {
		AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(new EcoreAdapterFactory(),
				new BasicCommandStack());
		for (IFile iFile : includedModels) {
			Resource resource = domain.createResource(iFile.getFullPath().toString());
			resource.load(null);
			eobjects.addAll(getAllContents(resource));
		}

	}

	public static Collection<? extends EObject> getAllContents(Resource resource) {
		Collection<EObject> contents = new HashSet<>();
		for (Iterator<EObject> iterator = resource.getAllContents(); iterator.hasNext();) {
			EObject next = iterator.next();
			contents.add(next);
		}
		return contents;
	}

	@Override
	public void processDelta(IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
		//XXX should do something smart with the delta :/
		populate(monitor);
	}

	@Override
	public void addedToEkekoModel(EkekoModel m, Collection<IProjectModel> otherModelsForProject) {
		// TODO Auto-generated method stub

	}

}
