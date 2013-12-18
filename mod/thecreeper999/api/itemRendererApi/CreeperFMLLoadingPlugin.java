package mod.thecreeper999.api.itemRendererApi;
import java.io.File;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;

@MCVersion(value = "1.5.2")
public class CreeperFMLLoadingPlugin implements cpw.mods.fml.relauncher.IFMLLoadingPlugin {

	@Override
	public String[] getLibraryRequestClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{ItemRendererTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return CreeperDummyContainer.class.getName();
	}

	@Override
	public String getSetupClass() {
		// TODO Auto-generated method stub
		return "mod.thecreeper999.api.itemRendererApi.CreeperTranslator";
	}

	@Override
	public void injectData(Map<String, Object> data) {
		
	}

}