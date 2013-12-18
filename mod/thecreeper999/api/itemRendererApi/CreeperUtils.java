package mod.thecreeper999.api.itemRendererApi;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class CreeperUtils {

	public static void registerItemRendererEventFirst(float frame, Minecraft mc, ItemRenderer itemRenderer, ItemStack itemToRender2)
	{
		if(MinecraftForge.EVENT_BUS.post(new ItemRendererEvent.RenderInFirstPerson(frame, mc, itemRenderer, itemToRender2))){return;}
	}

}
