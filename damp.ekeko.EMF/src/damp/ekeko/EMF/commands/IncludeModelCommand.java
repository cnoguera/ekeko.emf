package damp.ekeko.EMF.commands;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import damp.ekeko.EMF.modelFactories.EMFModel;
import damp.ekeko.EMF.modelFactories.EMFModelFactory;

public class IncludeModelCommand extends AbstractHandler{

	

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// get workbench window
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		// set selection service
		ISelectionService service = window.getSelectionService();
		// set structured selection
		IStructuredSelection structured = (IStructuredSelection) service.getSelection();
		for (Iterator iterator = structured.iterator(); iterator.hasNext();) {
			Object item = (Object) iterator.next();
			if (item instanceof IFile) {
				IFile file = (IFile) item;
				EMFModel model  = EMFModelFactory.getModel(file.getProject());
				if(model != null)
					model.addFileToModel(file);
			}
		}
		return null;
	}



}
