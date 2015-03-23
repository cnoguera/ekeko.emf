package damp.ekeko.EMF.modelFactories;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;

import damp.ekeko.EkekoNature;
import damp.ekeko.IProjectModel;
import damp.ekeko.IProjectModelFactory;

public class EMFModelFactory implements IProjectModelFactory {
	
	static Map<IProject, EMFModel> project2model = new HashMap<>();
	

	public EMFModelFactory() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IProjectModel createModel(IProject project) {
		project2model.put(project, new EMFModel(project));
		return getModel(project);
	}

	static public EMFModel getModel(IProject project){
		return project2model.get(project);
	}
	
	
	@Override
	public Collection<String> applicableNatures() {
		return Arrays.asList(EkekoNature.NATURE_ID);
	}

	@Override
	public Collection<IProjectModelFactory> conflictingFactories(IProject p,
			Collection<IProjectModelFactory> applicableFactories) {
		return Collections.emptyList();
	}

}
