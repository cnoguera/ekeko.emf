package damp.ekeko.EMF;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class EkekoEMFPlugin extends AbstractUIPlugin {
	
	public static final String PLUGIN_ID = "damp.ekeko.EMF";
	
	public static EkekoEMFPlugin plugin;

	public static EkekoEMFPlugin getDefault(){
		assert(plugin != null);
		return plugin;
	}
	
	public EkekoEMFPlugin() {
		// TODO Auto-generated constructor stub
	}
	
	
	public void start(BundleContext context){
		plugin = this;
		
	}

}
